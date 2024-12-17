package com.moldavets.SpringTelegramChannelManager.service;

import com.moldavets.SpringTelegramChannelManager.config.BotConfig;
import com.moldavets.SpringTelegramChannelManager.model.User;
import com.moldavets.SpringTelegramChannelManager.model.UserRepository;
import com.moldavets.SpringTelegramChannelManager.service.message.Impl.MessageSenderImpl;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.utils.LogType;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
@Service
public class TelegramBotService extends TelegramLongPollingBot {

    private final BotConfig CONFIG;
    private final MessageSender MESSAGE_SENDER;
    private UserService userService;

    @Autowired
    @Deprecated
    public TelegramBotService(BotConfig botConfig, @Lazy MessageSender messageSender , UserService userService)  {
        this.CONFIG = botConfig;
        this.userService = userService;
        this.MESSAGE_SENDER = messageSender;
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
            String message = update.getMessage()
                                   .getText();
            long chatId = update.getMessage()
                                .getChatId();

            MESSAGE_SENDER.sendLog(update.getMessage().getChat().getUserName() + "[" + update.getMessage().getChatId() + "]" + ":" + update.getMessage().getText(), LogType.INFO);

            switch (message) {

                case "/start":
                    registerUser(update.getMessage());
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;

                case "Linked Groups":
                    keyBoardLinkedGroupsMenu(chatId);
                    break;

                case "Menu":
                    keyBoardMainMenu(chatId);
                    break;

                case "My Profile":
                    keyBoardProfileMenu(chatId);
                    break;

                default:
                    MESSAGE_SENDER.sendMessage(chatId, "Command does not exist");
            }

        }
    }

    private void registerUser(Message message) {

        if(userService.findById(message.getChatId()) == null) {
            long chatId = message.getChatId();
            Chat chat = message.getChat();

            User tempUser = new User();

            tempUser.setId(chatId);
            tempUser.setUsername(chat.getUserName());

            userService.save(tempUser);
            MESSAGE_SENDER.sendLog("User " + chat.getUserName() + "[" + chatId +"]" + " has been registered", LogType.INFO);
        }

    }

    private void startCommandReceived(long chatId, String username) {

        //String response = EmojiParser.parseToUnicode("Hello, " + username + "! :blush:");
        //MESSAGE_SENDER.sendMessage(chatId,response);
        keyBoardMainMenu(chatId);
    }

    private void keyBoardLinkedGroupsMenu(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add("Menu");
        keyboardRows.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        sendMessage.setText("Linked Groups");
        MESSAGE_SENDER.executeScreenKeyboard(sendMessage);
    }

    private void keyBoardProfileMenu(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add("Menu");
        keyboardRows.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        sendMessage.setText("Your profile");
        MESSAGE_SENDER.executeScreenKeyboard(sendMessage);
    }

    private void keyBoardMainMenu(long chatId) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));

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

        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setText(EmojiParser.parseToUnicode("Welcome to main menu :blush:"));
        MESSAGE_SENDER.executeScreenKeyboard(sendMessage);
    }

}
