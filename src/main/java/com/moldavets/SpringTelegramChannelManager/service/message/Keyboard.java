package com.moldavets.SpringTelegramChannelManager.service.message;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public interface Keyboard {

    SendMessage getMainMenu(long chatId);
    InlineKeyboardButton createButton(String text, String callbackData);
}