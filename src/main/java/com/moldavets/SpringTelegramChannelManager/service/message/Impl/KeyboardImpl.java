package com.moldavets.SpringTelegramChannelManager.service.message.Impl;

import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.utils.LogType;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyboardImpl implements Keyboard {

    private final SendMessage MESSAGE_TO_BE_SENT = new SendMessage();

    @Override
    public SendMessage getMainMenu(long chatId) {
        setDetailsForChat(chatId, EmojiParser.parseToUnicode("Welcome to main menu :blush:"));

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        row.add(createButton("Send Posts", "SEND_POSTS"));
        row.add(createButton("Liked Groups", "LINKED_GROUPS"));
        rowsLine.add(row);

        row = new ArrayList<>();
        row.add(createButton("My Profile", "MY_PROFILE"));
        rowsLine.add(row);

        inlineKeyboardMarkup.setKeyboard(rowsLine);

        MESSAGE_TO_BE_SENT.setReplyMarkup(inlineKeyboardMarkup);

        return MESSAGE_TO_BE_SENT;
    }

    @Override
    public InlineKeyboardButton createButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }


    private void setDetailsForChat(long chatId, String textForMessage) {
        MESSAGE_TO_BE_SENT.setChatId(String.valueOf(chatId));
        MESSAGE_TO_BE_SENT.setText(textForMessage);
    }
}
