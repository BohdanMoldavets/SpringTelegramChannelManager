package com.moldavets.SpringTelegramChannelManager.service.action.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.service.action.Action;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.utils.message.MessageUtils;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.ArrayList;
import java.util.List;

public class ActionSendPosts implements Action {

    @Override
    public void execute(CallbackQuery callbackQuery,
                        MessageSender messageSender,
                        AppDAO appDAO, Keyboard keyboard) {

        String SendPostsText = "Send Posts";
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
