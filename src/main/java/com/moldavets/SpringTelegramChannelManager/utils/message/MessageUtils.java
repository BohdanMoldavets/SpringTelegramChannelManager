package com.moldavets.SpringTelegramChannelManager.utils.message;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public class MessageUtils {

    public static EditMessageText buildAnswer(String text, CallbackQuery callbackQuery) {
        return EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(text)
                .build();
    }

    public static boolean isDigitMessage(String message) {
        for (int i = 0; i < message.length(); i++) {
            if(!Character.isDigit(message.charAt(i))){
                return false;
            }
        }
        return true;
    }

}
