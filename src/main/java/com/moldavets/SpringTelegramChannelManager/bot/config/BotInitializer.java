package com.moldavets.SpringTelegramChannelManager.bot.config;

import com.moldavets.SpringTelegramChannelManager.bot.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@Slf4j
public class BotInitializer {

    private final TelegramBot TELEGRAM_BOT_SERVICE;

    @Autowired
    public BotInitializer(TelegramBot telegramBot) {
        this.TELEGRAM_BOT_SERVICE = telegramBot;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            telegramBotsApi.registerBot(TELEGRAM_BOT_SERVICE);
        } catch (TelegramApiException e) {
            log.error("Error occurred: {}", e.getMessage());
        }

    }
}
