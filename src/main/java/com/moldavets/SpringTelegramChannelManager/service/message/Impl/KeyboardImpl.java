package com.moldavets.SpringTelegramChannelManager.service.message.Impl;

import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
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
        setDetailsForChat(chatId, """
                <b>✨Welcome to main menu✨</b>
                
                <u><b>How to use this bot:</b></u>
                    <b>📌 Link one or more groups</b>
                    <i>Menu->Linked Groups->Add linked group</i>
                    <b>📌 Buy subscription</b>
                    <i>Menu->My Profile->Buy Subscription</i>
                    <b>📌 Send posts to your channels</b>
                    <i>Menu->Send posts</i>
                """);

        List<List<String>> buttons = new ArrayList<>();
        List<String> buttonRow = new ArrayList<>();

        buttonRow.add("Send Posts");
        buttonRow.add("Linked Groups");
        buttons.add(buttonRow);

        buttonRow = new ArrayList<>();
        buttonRow.add("My Profile");

        buttons.add(buttonRow);

        MESSAGE_TO_BE_SENT.setParseMode(ParseMode.HTML);
        MESSAGE_TO_BE_SENT.setReplyMarkup(createButtonMenu(buttons));

        return MESSAGE_TO_BE_SENT;
    }

    @Override
    public InlineKeyboardMarkup getOnlyMenuButton() {
        List<List<String>> buttons = new ArrayList<>();

        List<String> buttonRow = new ArrayList<>();
        buttonRow.add("Menu");

        buttons.add(buttonRow);
        return createButtonMenu(buttons);
    }

    @Override
    public InlineKeyboardMarkup createButtonMenu(List<List<String>> buttons) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();

        for (List<String> row : buttons) {
            rowsLine.add(createButtonRow(row));
        }

        inlineKeyboardMarkup.setKeyboard(rowsLine);

        return inlineKeyboardMarkup;
    }


    private List<InlineKeyboardButton> createButtonRow(List<String> buttons) {
        List<InlineKeyboardButton> row = new ArrayList<>();

        for (String button : buttons) {
            row.add(createButton(button, buildCallbackData(button)));
        }

        return row;
    }


    private InlineKeyboardButton createButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }

    private String buildCallbackData(String text) {
        return text.toUpperCase().replace(' ', '_');
    }


    private void setDetailsForChat(long chatId, String textForMessage) {
        MESSAGE_TO_BE_SENT.setChatId(String.valueOf(chatId));
        MESSAGE_TO_BE_SENT.setText(textForMessage);
    }
}
