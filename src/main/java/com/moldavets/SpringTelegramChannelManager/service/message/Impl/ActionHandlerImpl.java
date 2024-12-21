package com.moldavets.SpringTelegramChannelManager.service.message.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.entity.LinkedGroup;
import com.moldavets.SpringTelegramChannelManager.entity.Role;
import com.moldavets.SpringTelegramChannelManager.entity.Subscription;
import com.moldavets.SpringTelegramChannelManager.entity.User;
import com.moldavets.SpringTelegramChannelManager.service.message.ActionHandler;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.utils.LogType;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;


@Component
public class ActionHandlerImpl implements ActionHandler {

    private final MessageSender MESSAGE_SENDER;
    private final Keyboard KEYBOARD;
    private final AppDAO APP_DAO;
    private String lastAction;

    public ActionHandlerImpl(@Lazy MessageSender messageSender, Keyboard keyboard,
                             AppDAO appDAO) {
        this.MESSAGE_SENDER = messageSender;
        this.KEYBOARD = keyboard;
        this.APP_DAO = appDAO;
    }

    @Override
    public void handleAction(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        lastAction = callbackQuery.getData();
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
                StringBuilder MyProfileText = new StringBuilder();
                long chatId = callbackQuery.getMessage().getChatId();
                Subscription subscription = APP_DAO.findById(chatId).getSubscription();
                MyProfileText.append("My Profile:\n")
                        .append("\t Username: ")
                        .append(APP_DAO.findById(callbackQuery.getMessage().getChatId()).getUsername())
                        .append("\n")
                        .append("\t User ID: ")
                        .append(callbackQuery.getMessage().getChatId())
                        .append("\n")
                        .append("\t Subscription: ");

                if(subscription != null) {
                    if(subscription.getStatus()) {
                        MyProfileText.append("Expires in - ")
                                .append(subscription.getEndDate().getYear())
                                .append("-")
                                .append(subscription.getEndDate().getMonthValue())
                                .append("-")
                                .append(subscription.getEndDate().getDayOfMonth())
                                .append("\n")
                                .append("\t Subscription purchases: ")
                                .append(subscription.getPurchaseCount());
                    } else {
                        MyProfileText.append("Not subscribed");
                    }
                }

                EditMessageText answerForMyProfileMenu = buildAnswer(MyProfileText.toString(),callbackQuery);

                //creating buttons menu

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
            lastAction = "MENU";
        } else {
            MESSAGE_SENDER.sendMessage(update, "Command does not exist");
        }
    }

    private void registerUser(Update update) {
        long chatId = update.getMessage().getChatId();
        Chat chat = update.getMessage().getChat();
        if(APP_DAO.findById(chatId) == null) {
            User tempUser = new User();

            tempUser.setId(chatId);
            tempUser.setUsername(chat.getUserName());
            tempUser.setRole(new Role("ROLE_USER"));
            tempUser.addLinkedGroup(new LinkedGroup(123));
            tempUser.addMonthlySubscription();

            APP_DAO.save(tempUser);
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
