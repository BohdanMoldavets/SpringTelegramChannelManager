package com.moldavets.SpringTelegramChannelManager.service.message.Impl;

import com.moldavets.SpringTelegramChannelManager.model.User;
import com.moldavets.SpringTelegramChannelManager.service.message.ActionHandler;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.service.user.UserService;
import com.moldavets.SpringTelegramChannelManager.utils.LogType;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;


@Component
public class ActionHandlerImpl implements ActionHandler {

    private final MessageSender MESSAGE_SENDER;
    private final Keyboard KEYBOARD;
    private final UserService USER_SERVICE;
    private String lastAction;

    public ActionHandlerImpl(@Lazy MessageSender messageSender, Keyboard keyboard,
                             UserService userService) {
        this.MESSAGE_SENDER = messageSender;
        this.KEYBOARD = keyboard;
        this.USER_SERVICE = userService;
    }

    @Override
    public void handleAction(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        MESSAGE_SENDER.sendLog(update,"Inside block " + callbackQuery.getData(), LogType.INFO);
        switch (callbackQuery.getData()) {
            case "MENU":
                MESSAGE_SENDER.executeDeleteMessage(DeleteMessage
                        .builder()
                        .chatId(callbackQuery.getMessage().getChatId())
                        .messageId(callbackQuery.getMessage().getMessageId())
                        .build());
                MESSAGE_SENDER.executeCustomMessage(KEYBOARD.getMainMenu(callbackQuery.getMessage().getChatId()));
                break;

            case "MY_PROFILE":
                String MyProfileText = "My Profile";
                EditMessageText answerForMyProfileMenu = buildAnswer(MyProfileText,callbackQuery);

                List<List<String>> buttonsMenuForMyProfileMenu = new ArrayList<>();

                List<String> buttonRowForMyProfileMenu = new ArrayList<>();
                buttonRowForMyProfileMenu.add("Menu");

                buttonsMenuForMyProfileMenu.add(buttonRowForMyProfileMenu);

                answerForMyProfileMenu.setReplyMarkup(KEYBOARD.createButtonMenu(buttonsMenuForMyProfileMenu));
                MESSAGE_SENDER.executeEditMessage(answerForMyProfileMenu);
                break;

            case "LINKED_GROUPS":
                String LinkedGroupsText = "Linked Groups";
                EditMessageText answerForLinkedGroupsMenu = buildAnswer(LinkedGroupsText,callbackQuery);

                List<List<String>> buttonsMenuForLinkedGroupsMenu = new ArrayList<>();

                List<String> buttonRowForLinkedGroupsMenu = new ArrayList<>();
                buttonRowForLinkedGroupsMenu.add("Menu");

                buttonsMenuForLinkedGroupsMenu.add(buttonRowForLinkedGroupsMenu);

                answerForLinkedGroupsMenu.setReplyMarkup(KEYBOARD.createButtonMenu(buttonsMenuForLinkedGroupsMenu));
                MESSAGE_SENDER.executeEditMessage(answerForLinkedGroupsMenu);
                break;

            case "SEND_POSTS":
                String SendPostsText = "Send Posts";
                EditMessageText answerForSendPostsMenu = buildAnswer(SendPostsText,callbackQuery);

                List<List<String>> buttonsMenuForSendPostsMenu = new ArrayList<>();

                List<String> buttonRowForSendPostsMenu = new ArrayList<>();
                buttonRowForSendPostsMenu.add("Menu");

                buttonsMenuForSendPostsMenu.add(buttonRowForSendPostsMenu);

                answerForSendPostsMenu.setReplyMarkup(KEYBOARD.createButtonMenu(buttonsMenuForSendPostsMenu));
                MESSAGE_SENDER.executeEditMessage(answerForSendPostsMenu);
                break;
        }
    }

    @Override
    public void handleCommand(Update update) {
        String message = update.getMessage()
                               .getText();

        if (message.equals("/start")) {
            registerUser(update);
        } else {
            MESSAGE_SENDER.sendMessage(update, "Command does not exist");
        }
    }

    private void registerUser(Update update) {
        long chatId = update.getMessage().getChatId();
        Chat chat = update.getMessage().getChat();
        if(USER_SERVICE.findById(chatId) == null) {
            User tempUser = new User();

            tempUser.setId(chatId);
            tempUser.setUsername(chat.getUserName());

            USER_SERVICE.save(tempUser);
            MESSAGE_SENDER.sendLog(update, "has been registered", LogType.INFO);
        }
        MESSAGE_SENDER.executeCustomMessage(KEYBOARD.getMainMenu(chatId));
    }

    private EditMessageText buildAnswer(String text, CallbackQuery callbackQuery) {

        return EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(text)
                .build();
    }
}
