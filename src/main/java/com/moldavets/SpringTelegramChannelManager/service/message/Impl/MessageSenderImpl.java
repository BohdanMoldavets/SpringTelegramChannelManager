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
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
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
    public void sendMessage(Update update, String message) {
        long chatId = update.getMessage().getChatId();
        SendMessage answer = new SendMessage(String.valueOf(chatId),message);
        try {
            TELEGRAM_BOT.execute(answer);
            sendLog(update, update.getMessage().getText(), LogType.INFO);
            sendLog(update, "Response for previous message: " + message, LogType.INFO);
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
    public void sendLog(Update update, String message, LogType logType) {
        long chatId = update.getMessage().getChatId();
        String username = update.getMessage().getFrom().getUserName();

        switch (logType) {
            case INFO:
                log.info(message);
                sendLogToChat("[" + LogType.INFO + "] " + username + "[" + chatId + "] " + message);
                break;

            case ERROR:
                log.error(message);
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
