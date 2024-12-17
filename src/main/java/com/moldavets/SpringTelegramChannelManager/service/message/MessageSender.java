package com.moldavets.SpringTelegramChannelManager.service.message;

import com.moldavets.SpringTelegramChannelManager.utils.LogType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface MessageSender {

    void sendMessage(long chatId, String textToBeSent);
    void sendLog(String message, LogType logType);
    void executeScreenKeyboard(SendMessage message);

}
