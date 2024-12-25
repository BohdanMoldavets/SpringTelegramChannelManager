package com.moldavets.SpringTelegramChannelManager.service.action;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface Action {
    void execute(CallbackQuery callbackQuery,
                 MessageSender messageSender,
                 AppDAO appDAO, Keyboard keyboard);
}
