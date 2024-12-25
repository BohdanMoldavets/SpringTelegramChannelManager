package com.moldavets.SpringTelegramChannelManager.bot;

import com.moldavets.SpringTelegramChannelManager.bot.config.BotConfig;
import com.moldavets.SpringTelegramChannelManager.service.message.ActionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


@Slf4j
@Component
@Service
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig CONFIG;
    private final ActionHandler ACTION_HANDLER;

    @Autowired
    @Deprecated
    public TelegramBot(BotConfig botConfig, ActionHandler actionHandler)  {
        this.CONFIG = botConfig;
        this.ACTION_HANDLER = actionHandler;
    }

    @Override
    public String getBotUsername() {
        return CONFIG.getBotName();
    }

    @Override
    public String getBotToken() {
        return CONFIG.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            ACTION_HANDLER.handleCommand(update.getMessage());
        } else if(update.hasCallbackQuery()) {
            ACTION_HANDLER.handleAction(update.getCallbackQuery());
        }
    }
}
