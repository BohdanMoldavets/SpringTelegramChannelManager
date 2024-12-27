package com.moldavets.SpringTelegramChannelManager.service.action.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.service.action.Action;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.service.security.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component("MENU")
public class ActionMenu implements Action {

    private final Security SECURITY;

    @Autowired
    public ActionMenu(Security security) {
        this.SECURITY = security;
    }

    @Override
    public void execute(CallbackQuery callbackQuery,
                        MessageSender messageSender,
                        AppDAO appDAO, Keyboard keyboard) {

        if(!SECURITY.isValidSubscription(appDAO,callbackQuery.getMessage().getChatId())) {
            SendMessage security = new SendMessage();
            security.setChatId(callbackQuery.getMessage().getChatId());
            security.enableHtml(true);

            security.setText("<b>Your subscription is expired!</b>");

            messageSender.executeCustomMessage(security);
        }

        messageSender.executeDeleteMessage(DeleteMessage
                .builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .build());
        messageSender.executeCustomMessage(keyboard.getMainMenu(callbackQuery.getMessage().getChatId()));

    }
}
