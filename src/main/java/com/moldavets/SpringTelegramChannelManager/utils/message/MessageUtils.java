package com.moldavets.SpringTelegramChannelManager.utils.message;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

public class MessageUtils {

    public static Message message;

    public static EditMessageText buildAnswer(String text, CallbackQuery callbackQuery) {
        return EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(text)
                .build();
    }

    public static DeleteMessage buildDeleteMessage(CallbackQuery callbackQuery) {
        return DeleteMessage.builder()
                .chatId(
                        callbackQuery
                                .getMessage()
                                .getChatId()
                )
                .messageId(
                        callbackQuery
                                .getMessage()
                                .getMessageId()
                )
                .build();
    }

    public static boolean isValidGroupId(String message) {
        return message.matches("-[0-9]+");
    }
}