package com.moldavets.SpringTelegramChannelManager.service.message.Impl;

import com.moldavets.SpringTelegramChannelManager.bot.TelegramBot;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.utils.log.LogType;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@Service
public class MessageSenderImpl implements MessageSender {

    @Value("${bot.log.chat.id}")
    private long LOG_CHAT_ID;

    private final TelegramBot TELEGRAM_BOT;

    @Autowired
    public MessageSenderImpl(TelegramBot telegramBot) {
        this.TELEGRAM_BOT = telegramBot;
    }

    @Override
    public void sendMessage(String chatId, String message) {
        SendMessage answer = new SendMessage(chatId,message);
        try {
            TELEGRAM_BOT.execute(answer);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void executeCustomMessage(SendMessage message) {
        try {
            TELEGRAM_BOT.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage().substring(e.getMessage().indexOf(":")+1));
        }
    }

    @Override
    public void executeEditMessage(EditMessageText message) {
        try {
            TELEGRAM_BOT.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void executeDeleteMessage(DeleteMessage message) {
        try {
            TELEGRAM_BOT.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void sendLog(String chatId, String username, String message, LogType logType) {
        String logMessage = username + "[" + chatId + "] " + message;
        switch (logType) {
            case INFO:
                log.info(logMessage);
                sendLogToChat("[" + LogType.INFO + "] " + username + "[" + chatId + "] " + message);
                break;

            case ERROR:
                log.error(logMessage);
                sendLogToChat("[" + LogType.ERROR + "] " + username + "[" + chatId + "] " + message);
                break;
        }
    }

    private void sendLogToChat(String message) {
        try {
            TELEGRAM_BOT.execute(
                    new SendMessage(
                            String.valueOf(LOG_CHAT_ID),
                            message)
            );
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
