package com.moldavets.SpringTelegramChannelManager.service.message;


import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface ActionHandler {
    void handleAction(CallbackQuery callbackQuery);
    void handleCommand(Message message);
}
