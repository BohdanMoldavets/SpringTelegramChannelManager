package com.moldavets.SpringTelegramChannelManager.service.message;

import com.moldavets.SpringTelegramChannelManager.utils.log.LogType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageSender {

    void sendMessage(String chatId, String message);

    void executeCustomMessage(SendMessage message);
    void executeEditMessage(EditMessageText message);
    void executeDeleteMessage(DeleteMessage message);

    void sendLog(String chatId, String username, String message, LogType logType);

}
