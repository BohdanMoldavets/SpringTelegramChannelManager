package com.moldavets.SpringTelegramChannelManager.keyboard.Impl;

import com.moldavets.SpringTelegramChannelManager.keyboard.Keyboard;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyboardImpl implements Keyboard {

    private final SendMessage MESSAGE = new SendMessage();

    @Override
    public SendMessage getMainMenu(long chatId) {
        setDetailsForChat(chatId, EmojiParser.parseToUnicode("Welcome to main menu :blush:"));

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add(EmojiParser.parseToUnicode("Linked Groups"));
        keyboardRow.add("My Profile");
        keyboardRows.add(keyboardRow);

        keyboardRow = new KeyboardRow();

        keyboardRow.add("Send Posts");
        keyboardRows.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        MESSAGE.setReplyMarkup(replyKeyboardMarkup);

        return MESSAGE;
    }

    @Override
    public SendMessage getLinkedGroupsMenu(long chatId) {
        setDetailsForChat(chatId, "Linked Groups");

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        keyboardRows.add(getMenuButtonRow());

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        MESSAGE.setReplyMarkup(replyKeyboardMarkup);

        return MESSAGE;
    }

    @Override
    public SendMessage getProfileMenu(long chatId) {
        setDetailsForChat(chatId, "Your profile");

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        keyboardRows.add(getMenuButtonRow());

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        MESSAGE.setReplyMarkup(replyKeyboardMarkup);

        return MESSAGE;
    }

    @Override
    public SendMessage getSendPostsMenu(long chatId) {
        setDetailsForChat(chatId, "Post builder");

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        keyboardRows.add(getMenuButtonRow());

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        MESSAGE.setReplyMarkup(replyKeyboardMarkup);

        return MESSAGE;
    }

    private KeyboardRow getMenuButtonRow() {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("Menu");
        return keyboardRow;
    }

    private void setDetailsForChat(long chatId, String textForMessage) {
        MESSAGE.setChatId(String.valueOf(chatId));
        MESSAGE.setText(textForMessage);
    }
}
