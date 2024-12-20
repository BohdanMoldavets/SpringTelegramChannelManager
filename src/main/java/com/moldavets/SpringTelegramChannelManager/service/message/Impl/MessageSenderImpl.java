package com.moldavets.SpringTelegramChannelManager.service.message.Impl;

import com.moldavets.SpringTelegramChannelManager.bot.TelegramBot;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.utils.LogType;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@Service
public class MessageSenderImpl implements MessageSender {

    //@Value("${bot.log.chat.id}")
    private final long LOG_CHAT_ID =-1002422084878L;

    private final TelegramBot TELEGRAM_BOT;

    @Autowired
    public MessageSenderImpl(TelegramBot telegramBot) {
        this.TELEGRAM_BOT = telegramBot;
    }

    @Override
    public void sendMessage(long chatId, String textToBeSent) {
        SendMessage message = new SendMessage(String.valueOf(chatId),textToBeSent);
        try {
            TELEGRAM_BOT.execute(message);
            sendLog("Response message for chat [" + chatId + "]:" + textToBeSent, LogType.INFO);
        } catch (TelegramApiException e) {
            try {
                sendLog(e.getMessage(), LogType.ERROR);
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
    }

    @Override
    public void executeCustomMessage(SendMessage message) {
        try {
            TELEGRAM_BOT.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            sendLog(e.getMessage(), LogType.ERROR);
        }
    }

    @Override
    public void executeEditMessage(EditMessageText message) {
        try {
            TELEGRAM_BOT.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            sendLog(e.getMessage(), LogType.ERROR);
        }
    }

    @Override
    public void executeDeleteMessage(DeleteMessage message) {
        try {
            TELEGRAM_BOT.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            sendLog(e.getMessage(), LogType.ERROR);
        }
    }

    @Override
    public void sendLog(String message, LogType logType) {
        switch (logType) {
            case INFO:
                log.info(message);
                sendLogToChat("[" + LogType.INFO + "] " + message);
                break;

            case ERROR:
                log.error(message);
                sendLogToChat("[" + LogType.ERROR + "] " + message);
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
            sendLog(e.getMessage(), LogType.ERROR);
        }
    }
}
