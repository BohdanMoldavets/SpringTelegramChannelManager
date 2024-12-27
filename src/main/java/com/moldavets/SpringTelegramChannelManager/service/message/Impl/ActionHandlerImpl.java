package com.moldavets.SpringTelegramChannelManager.service.message.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.service.factory.ActionFactory;
import com.moldavets.SpringTelegramChannelManager.service.factory.CommandFactory;
import com.moldavets.SpringTelegramChannelManager.service.message.ActionHandler;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.utils.log.LogType;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;



@Component
public class ActionHandlerImpl implements ActionHandler {

    private final MessageSender MESSAGE_SENDER;
    private final Keyboard KEYBOARD;
    private final AppDAO APP_DAO;
    private final ActionFactory ACTION_FACTORY;
    private final CommandFactory COMMAND_FACTORY;

    public static String lastAction;


    public ActionHandlerImpl(@Lazy MessageSender messageSender, Keyboard keyboard,
                             AppDAO appDAO, ActionFactory actionFactory,
                             CommandFactory commandFactory) {
        this.MESSAGE_SENDER = messageSender;
        this.KEYBOARD = keyboard;
        this.APP_DAO = appDAO;
        this.ACTION_FACTORY = actionFactory;
        this.COMMAND_FACTORY = commandFactory;
    }

    @Override
    public void handleAction(CallbackQuery callbackQuery) {
        lastAction = callbackQuery.getData();
        MESSAGE_SENDER.sendLog(String.valueOf(callbackQuery.getMessage().getChatId()),
                               callbackQuery.getFrom().getUserName(),
                               "Inside block " + callbackQuery.getData(),
                               LogType.INFO);

        ACTION_FACTORY.getAction(callbackQuery.getData())
                .execute(callbackQuery,MESSAGE_SENDER,APP_DAO,KEYBOARD);
    }

    @Override
    public void handleCommand(Message message) {
        MESSAGE_SENDER.sendLog(String.valueOf(message.getChatId()),
                               message.getFrom().getUserName(),
                               message.getText(),
                               LogType.INFO);


        if(COMMAND_FACTORY.containsCommand(message.getText())) {
            COMMAND_FACTORY.getCommand(message.getText())
                           .execute(message,MESSAGE_SENDER,APP_DAO,KEYBOARD);
        } else if (COMMAND_FACTORY.containsCommand("COMMAND_"+lastAction)) {
            COMMAND_FACTORY.getCommand("COMMAND_"+lastAction)
                           .execute(message,MESSAGE_SENDER,APP_DAO,KEYBOARD);
        } else {
            COMMAND_FACTORY.getCommand("COMMAND_DOES_NOT_EXIST")
                           .execute(message,MESSAGE_SENDER,APP_DAO,KEYBOARD);
        }
    }
}
