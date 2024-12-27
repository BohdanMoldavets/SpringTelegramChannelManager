package com.moldavets.SpringTelegramChannelManager.service.action.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.service.action.Action;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.utils.message.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component("DO_NOT_SEND_POST❌")
public class ActionDoNotSendPost implements Action {

    @Override
    public void execute(CallbackQuery callbackQuery,
                        MessageSender messageSender,
                        AppDAO appDAO, Keyboard keyboard) {


        messageSender.executeDeleteMessage(MessageUtils.buildDeleteMessage(callbackQuery));
        messageSender.executeCustomMessage(keyboard.getMainMenu(callbackQuery.getMessage().getChatId()));
    }

}
