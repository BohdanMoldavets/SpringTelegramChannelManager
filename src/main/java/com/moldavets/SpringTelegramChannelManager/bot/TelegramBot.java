package com.moldavets.SpringTelegramChannelManager.bot;

import com.moldavets.SpringTelegramChannelManager.bot.config.BotConfig;
import com.moldavets.SpringTelegramChannelManager.keyboard.Keyboard;
import com.moldavets.SpringTelegramChannelManager.model.User;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.service.user.UserService;
import com.moldavets.SpringTelegramChannelManager.utils.LogType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;


@Slf4j
@Component
@Service
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig CONFIG;
    private final MessageSender MESSAGE_SENDER;
    private final Keyboard KEYBOARD;
    private final UserService USER_SERVICE;

    @Autowired
    @Deprecated
    public TelegramBot(BotConfig botConfig, @Lazy MessageSender messageSender,
                       Keyboard keyboard, UserService userService)  {
        this.CONFIG = botConfig;
        this.USER_SERVICE = userService;
        this.MESSAGE_SENDER = messageSender;
        this.KEYBOARD = keyboard;
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

                case "/start" -> registerUser(update.getMessage());

                case "Linked Groups" -> MESSAGE_SENDER.executeScreenKeyboard(KEYBOARD.getLinkedGroupsMenu(chatId));

                case "Menu" -> MESSAGE_SENDER.executeScreenKeyboard(KEYBOARD.getMainMenu(chatId));

                case "My Profile" -> MESSAGE_SENDER.executeScreenKeyboard(KEYBOARD.getProfileMenu(chatId));

                case "Send Posts" -> MESSAGE_SENDER.executeScreenKeyboard(KEYBOARD.getSendPostsMenu(chatId));

                default -> MESSAGE_SENDER.sendMessage(chatId, "Command does not exist");
            }

        }
    }

    private void registerUser(Message message) {
        long chatId = message.getChatId();
        Chat chat = message.getChat();
        if(USER_SERVICE.findById(chatId) == null) {
            User tempUser = new User();

            tempUser.setId(chatId);
            tempUser.setUsername(chat.getUserName());

            USER_SERVICE.save(tempUser);
            MESSAGE_SENDER.sendLog("User " + chat.getUserName() + "[" + chatId +"]" + " has been registered", LogType.INFO);
        }
        MESSAGE_SENDER.executeScreenKeyboard(KEYBOARD.getMainMenu(chatId));
    }
}
