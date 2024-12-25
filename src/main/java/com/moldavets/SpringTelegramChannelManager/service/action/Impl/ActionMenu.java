package com.moldavets.SpringTelegramChannelManager.service.action.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.service.action.Action;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public class ActionMenu implements Action {

    @Override
    public void execute(CallbackQuery callbackQuery,
                        MessageSender messageSender,
                        AppDAO appDAO, Keyboard keyboard) {

        messageSender.executeDeleteMessage(DeleteMessage
                .builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .build());
        messageSender.executeCustomMessage(keyboard.getMainMenu(callbackQuery.getMessage().getChatId()));

    }
}
