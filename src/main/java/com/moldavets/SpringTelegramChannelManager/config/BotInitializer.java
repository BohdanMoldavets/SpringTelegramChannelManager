package com.moldavets.SpringTelegramChannelManager.config;

import com.moldavets.SpringTelegramChannelManager.service.TelegramBotService;
import com.moldavets.SpringTelegramChannelManager.service.message.Impl.MessageSenderImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotInitializer {

    private final TelegramBotService TELEGRAM_BOT_SERVICE;

    @Autowired
    public BotInitializer(TelegramBotService telegramBotService) {
        this.TELEGRAM_BOT_SERVICE = telegramBotService;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            telegramBotsApi.registerBot(TELEGRAM_BOT_SERVICE);
        } catch (TelegramApiException e) {
            //log.error("Error occurred: {}", e.getMessage());
        }

    }
}
