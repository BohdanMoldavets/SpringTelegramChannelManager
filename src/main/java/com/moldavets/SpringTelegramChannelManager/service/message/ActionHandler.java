package com.moldavets.SpringTelegramChannelManager.service.message;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ActionHandler {

    void handleAction(Update update);

}
