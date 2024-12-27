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

@Component("ADD_LINKED_GROUP")
public class ActionAddLinkedGroup implements Action {

    @Override
    public void execute(CallbackQuery callbackQuery,
                        MessageSender messageSender,
                        AppDAO appDAO, Keyboard keyboard) {

        String AddLinkedGroupText = "⬇️Enter the group ID that will be linked to you, using this format: " +
                "-1234567890⬇️";

        EditMessageText answerForAddLinkedGroupMenu = MessageUtils.buildAnswer(AddLinkedGroupText,callbackQuery);

        List<List<String>> buttonsForAddLinkedGroupMenu = new ArrayList<>();

        List<String> buttonRowForAddLinkedGroupMenu = new ArrayList<>();
        buttonRowForAddLinkedGroupMenu.add("Menu");
        buttonsForAddLinkedGroupMenu.add(buttonRowForAddLinkedGroupMenu);

        answerForAddLinkedGroupMenu.setReplyMarkup(keyboard.createButtonMenu(buttonsForAddLinkedGroupMenu));
        messageSender.executeEditMessage(answerForAddLinkedGroupMenu);

    }

}
