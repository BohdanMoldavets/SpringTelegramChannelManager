package com.moldavets.SpringTelegramChannelManager.bot;

import com.moldavets.SpringTelegramChannelManager.bot.config.BotConfig;
import com.moldavets.SpringTelegramChannelManager.service.message.ActionHandler;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.utils.LogType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


@Slf4j
@Component
@Service
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig CONFIG;
    private final MessageSender MESSAGE_SENDER;
    private final ActionHandler ACTION_HANDLER;

    @Autowired
    @Deprecated
    public TelegramBot(BotConfig botConfig, @Lazy MessageSender messageSender,
                       ActionHandler actionHandler)  {
        this.CONFIG = botConfig;
        this.MESSAGE_SENDER = messageSender;
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

            MESSAGE_SENDER.sendLog(update.getMessage().getChat().getUserName() + "[" + update.getMessage().getChatId() + "]" + ":" + update.getMessage().getText(), LogType.INFO);

            ACTION_HANDLER.handleCommand(update);
        } else if(update.hasCallbackQuery()) {
            MESSAGE_SENDER.sendLog("Inside block " + update.getCallbackQuery().getData(), LogType.INFO);

            ACTION_HANDLER.handleAction(update);
        }
    }
}
