package com.moldavets.SpringTelegramChannelManager.service.action.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.service.action.Action;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.utils.message.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.ArrayList;
import java.util.List;

@Component("SEND_POSTS")
public class ActionSendPosts implements Action {

    @Override
    public void execute(CallbackQuery callbackQuery,
                        MessageSender messageSender,
                        AppDAO appDAO, Keyboard keyboard) {

        String SendPostsText = """
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
        EditMessageText answerForSendPostsMenu = MessageUtils.buildAnswer(SendPostsText,callbackQuery);

        //creating buttons menu
        List<List<String>> buttonsMenuForSendPostsMenu = new ArrayList<>();

        List<String> buttonRowForSendPostsMenu = new ArrayList<>();
        buttonRowForSendPostsMenu.add("Menu");

        buttonsMenuForSendPostsMenu.add(buttonRowForSendPostsMenu);

        answerForSendPostsMenu.setReplyMarkup(keyboard.createButtonMenu(buttonsMenuForSendPostsMenu));
        messageSender.executeEditMessage(answerForSendPostsMenu);

    }

}
