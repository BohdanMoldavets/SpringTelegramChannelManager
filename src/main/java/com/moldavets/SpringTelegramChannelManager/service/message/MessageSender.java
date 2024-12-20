package com.moldavets.SpringTelegramChannelManager.service.message;

import com.moldavets.SpringTelegramChannelManager.utils.LogType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface MessageSender {

    void sendMessage(long chatId, String textToBeSent);
    void executeCustomMessage(SendMessage message);
    void executeEditMessage(EditMessageText message);
    void executeDeleteMessage(DeleteMessage message);
    void sendLog(String message, LogType logType);

}
