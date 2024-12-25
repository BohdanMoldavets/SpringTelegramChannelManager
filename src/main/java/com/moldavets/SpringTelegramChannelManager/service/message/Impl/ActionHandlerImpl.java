package com.moldavets.SpringTelegramChannelManager.service.message.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.service.action.Action;
import com.moldavets.SpringTelegramChannelManager.service.action.Impl.*;
import com.moldavets.SpringTelegramChannelManager.service.command.Command;
import com.moldavets.SpringTelegramChannelManager.service.command.Impl.CommandAddLinkedGroup;
import com.moldavets.SpringTelegramChannelManager.service.command.Impl.CommandDeleteLinkedGroup;
import com.moldavets.SpringTelegramChannelManager.service.command.Impl.CommandDoesNotExist;
import com.moldavets.SpringTelegramChannelManager.service.command.Impl.CommandStart;
import com.moldavets.SpringTelegramChannelManager.service.message.ActionHandler;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.utils.log.LogType;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;


@Component
public class ActionHandlerImpl implements ActionHandler {

    private final MessageSender MESSAGE_SENDER;
    private final Keyboard KEYBOARD;
    private final AppDAO APP_DAO;

    private final Map<String,Action> actions = new HashMap<>();
    private final Map<String, Command> commands = new HashMap<>();

    private String lastAction;


    @PostConstruct
    public void InitActions() {
        actions.put("MENU", new ActionMenu());
        actions.put("MY_PROFILE", new ActionMyProfile());
        actions.put("LINKED_GROUPS", new ActionLinkedGroups());
        actions.put("SEND_POSTS", new ActionSendPosts());
        actions.put("BUY_SUBSCRIPTION", new ActionBuySubscription());
        actions.put("ADD_LINKED_GROUP", new ActionAddLinkedGroup());
        actions.put("DELETE_LINKED_GROUP", new ActionDeleteLinkedGroup());
    }

    @PostConstruct
    public void InitCommands() {
        commands.put("/start", new CommandStart());
        commands.put("ADD_LINKED_GROUP", new CommandAddLinkedGroup());
        commands.put("DELETE_LINKED_GROUP", new CommandDeleteLinkedGroup());

        commands.put("COMMAND_DOES_NOT_EXIST", new CommandDoesNotExist());
    }

    public ActionHandlerImpl(@Lazy MessageSender messageSender, Keyboard keyboard,
                             AppDAO appDAO) {
        this.MESSAGE_SENDER = messageSender;
        this.KEYBOARD = keyboard;
        this.APP_DAO = appDAO;
    }

    @Override
    public void handleAction(CallbackQuery callbackQuery) {
        lastAction = callbackQuery.getData();
        //MESSAGE_SENDER.sendLog(update,"Inside block " + callbackQuery.getData(), LogType.INFO);

        actions.get(callbackQuery.getData()).execute(callbackQuery,MESSAGE_SENDER,APP_DAO,KEYBOARD);
    }

    @Override
    public void handleCommand(Message message) {

        if(commands.containsKey(message.getText())) {
            commands.get(message.getText()).execute(message,MESSAGE_SENDER,APP_DAO,KEYBOARD);
        } else if (commands.containsKey(lastAction)) {
            commands.get(lastAction).execute(message,MESSAGE_SENDER,APP_DAO,KEYBOARD);
        } else {
            commands.get("COMMAND_DOES_NOT_EXIST").execute(message,MESSAGE_SENDER,APP_DAO,KEYBOARD);
        }
    }
}
