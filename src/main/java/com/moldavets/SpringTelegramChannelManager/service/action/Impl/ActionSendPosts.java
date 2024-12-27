package com.moldavets.SpringTelegramChannelManager.service.action.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.service.action.Action;
import com.moldavets.SpringTelegramChannelManager.service.message.Impl.ActionHandlerImpl;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.service.security.Security;
import com.moldavets.SpringTelegramChannelManager.utils.log.LogType;
import com.moldavets.SpringTelegramChannelManager.utils.message.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.ArrayList;
import java.util.List;

@Component("SEND_POSTS")
public class ActionSendPosts implements Action {

    private final Security SECURITY;

    @Autowired
    public ActionSendPosts(Security security) {
        this.SECURITY = security;
    }

    @Override
    public void execute(CallbackQuery callbackQuery,
                        MessageSender messageSender,
                        AppDAO appDAO, Keyboard keyboard) {

        EditMessageText answer;
        String SendPostsText;

        if(SECURITY.hasRole(appDAO,callbackQuery.getMessage().getChatId(),"ROLE_SUBSCRIBER")) {
            if (appDAO.findById(callbackQuery.getMessage().getChatId()).getLinkedGroups() != null
                    && !appDAO.findById(callbackQuery.getMessage().getChatId()).getLinkedGroups().isEmpty()) {
                SendPostsText = """
                    ⬇️Use these styles to decorate your text⬇️
                    <b>bold</b>
                    <i>Italics</i>
                    <code>code</code>
                    <s>crossed-out</s>
                    <u>underlined</u>
                    <pre language="c++">code</pre>
                    <a href="your_link">Link</a>
                    
                    Enter the text that will be sent to your all groups
                    """;
            } else {
                SendPostsText = "You don't have any linked group.";
                ActionHandlerImpl.lastAction = "MENU";
            }
        } else {
            SendPostsText = """
                    Access denied
                    In order to use this feature you need to buy a subscription
                    Menu->My Profile->Buy subscription
                    """;

            ActionHandlerImpl.lastAction = "MENU";

        }

        answer = MessageUtils.buildAnswer(SendPostsText,callbackQuery);

        //creating buttons menu
        List<List<String>> buttons = new ArrayList<>();

        List<String> buttonRow = new ArrayList<>();
        buttonRow.add("Menu");

        buttons.add(buttonRow);

        answer.setReplyMarkup(keyboard.createButtonMenu(buttons));
        messageSender.executeEditMessage(answer);

        messageSender.sendLog(String.valueOf(callbackQuery.getMessage().getChatId()),
                              callbackQuery.getFrom().getUserName(),
                              "<- Bot: " + SendPostsText,
                              LogType.INFO
        );
    }

}
