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

@Component("DELETE_LINKED_GROUP")
public class ActionDeleteLinkedGroup implements Action {

    @Override
    public void execute(CallbackQuery callbackQuery,
                        MessageSender messageSender,
                        AppDAO appDAO, Keyboard keyboard) {

        String DeleteLinkedGroupText = "Enter the group ID from previous page, " +
                "that will be deleted, " +
                "using this format:\n" +
                "1234567890";
        EditMessageText answerForDeleteLinkedGroupMenu = MessageUtils.buildAnswer(DeleteLinkedGroupText,callbackQuery);

        List<List<String>> buttonsForDeleteLinkedGroupMenu = new ArrayList<>();

        List<String> buttonRowForDeleteLinkedGroupMenu = new ArrayList<>();
        buttonRowForDeleteLinkedGroupMenu.add("Menu");
        buttonsForDeleteLinkedGroupMenu.add(buttonRowForDeleteLinkedGroupMenu);

        answerForDeleteLinkedGroupMenu.setReplyMarkup(keyboard.createButtonMenu(buttonsForDeleteLinkedGroupMenu));
        messageSender.executeEditMessage(answerForDeleteLinkedGroupMenu);

    }

}
