package com.moldavets.SpringTelegramChannelManager.service.message.Impl;

import com.moldavets.SpringTelegramChannelManager.service.message.ActionHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ActionHandlerImpl implements ActionHandler {

    @Override
    public void handleAction(Update update) {
        String callBackData = update.getCallbackQuery().getData();
//        long chatId = update.getMessage().getChatId();
//        long messageId = update.getMessage().getMessageId();

        switch (callBackData) {
            case "MY_PROFILE":
                String text = "My Profile";
                EditMessageText message = new EditMessageText();
                message.setChatId(String.valueOf(update.getMessage().getChatId()));
                message.setText(text);
                message.setMessageId(update.getMessage().getMessageId());
                break;

            case "LINKED_GROUPS":

                break;
        }



    }

}
