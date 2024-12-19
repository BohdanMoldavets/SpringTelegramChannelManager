package com.moldavets.SpringTelegramChannelManager.service.message;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface Keyboard {

    SendMessage getMainMenu(long chatId);

}
