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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
        long chatId = callbackQuery.getMessage().getChatId();
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
                if (subscription != null) {
                    if (!subscription.getStatus()) {
                        buttonRowForMyProfileMenu.add("Buy subscription");
                        buttonsMenuForMyProfileMenu.add(buttonRowForMyProfileMenu);
                        buttonRowForMyProfileMenu = new ArrayList<>();
                    }
                }

                buttonRowForMyProfileMenu.add("Menu");
                buttonsMenuForMyProfileMenu.add(buttonRowForMyProfileMenu);

                answerForMyProfileMenu.setReplyMarkup(KEYBOARD.createButtonMenu(buttonsMenuForMyProfileMenu));
                MESSAGE_SENDER.executeEditMessage(answerForMyProfileMenu);
                break;

            case "LINKED_GROUPS":
                StringBuilder stringBuilder = new StringBuilder();

                List<LinkedGroup> linkedGroups = APP_DAO.findById(callbackQuery.getMessage().getChatId()).getLinkedGroups();

                if (linkedGroups != null && !linkedGroups.isEmpty()) {
                    stringBuilder.append("Your linked groups:\n");
                    for(LinkedGroup tempLinkedGroup : linkedGroups) {
                        stringBuilder
                                .append("\t")
                                .append(tempLinkedGroup.getGroupId())
                                .append("\n");
                    }
                } else {
                    stringBuilder
                            .append("You don't have any linked group.");
                }

                EditMessageText answerForLinkedGroupsMenu = buildAnswer(stringBuilder.toString(),callbackQuery);

                //creating buttons menu
                List<List<String>> buttonsMenuForLinkedGroupsMenu = new ArrayList<>();

                List<String> buttonRowForLinkedGroupsMenu = new ArrayList<>();
                buttonRowForLinkedGroupsMenu.add("Add linked group");
                buttonRowForLinkedGroupsMenu.add("Delete linked group");
                buttonsMenuForLinkedGroupsMenu.add(buttonRowForLinkedGroupsMenu);

                buttonRowForLinkedGroupsMenu = new ArrayList<>();
                buttonRowForLinkedGroupsMenu.add("Menu");
                buttonsMenuForLinkedGroupsMenu.add(buttonRowForLinkedGroupsMenu);

                answerForLinkedGroupsMenu.setReplyMarkup(KEYBOARD.createButtonMenu(buttonsMenuForLinkedGroupsMenu));
                MESSAGE_SENDER.executeEditMessage(answerForLinkedGroupsMenu);
                break;

            case "SEND_POSTS":
                String SendPostsText = "Send Posts";
                EditMessageText answerForSendPostsMenu = buildAnswer(SendPostsText,callbackQuery);

                //creating buttons menu
                List<List<String>> buttonsMenuForSendPostsMenu = new ArrayList<>();

                List<String> buttonRowForSendPostsMenu = new ArrayList<>();
                buttonRowForSendPostsMenu.add("Menu");

                buttonsMenuForSendPostsMenu.add(buttonRowForSendPostsMenu);

                answerForSendPostsMenu.setReplyMarkup(KEYBOARD.createButtonMenu(buttonsMenuForSendPostsMenu));
                MESSAGE_SENDER.executeEditMessage(answerForSendPostsMenu);
                break;

            case "BUY_SUBSCRIPTION":
                String BuySubscriptionText = "Buying a subscription is not available right now as the bot is not commercial." +
                                             " Congratulations you just got a monthly subscription for free!";
                EditMessageText answerForBuySubscriptionMenu = buildAnswer(BuySubscriptionText,callbackQuery);

                User currentUser = APP_DAO.findById(chatId);
                currentUser.getSubscription().addMonthlySubscription();
                currentUser.getSubscription().setUser(currentUser);
                APP_DAO.updateSubscription(currentUser.getSubscription());

                Role tempRole = new Role();
                tempRole.setRole("ROLE_SUBSCRIBER");
                currentUser.setRole(tempRole);
                APP_DAO.updateRole(currentUser.getRole());


                //creating buttons menu
                List<List<String>> buttonsForBuySubscriptionMenu = new ArrayList<>();

                List<String> buttonRowForBuySubscriptionMenu = new ArrayList<>();
                buttonRowForBuySubscriptionMenu.add("Menu");
                buttonsForBuySubscriptionMenu.add(buttonRowForBuySubscriptionMenu);

                answerForBuySubscriptionMenu.setReplyMarkup(KEYBOARD.createButtonMenu(buttonsForBuySubscriptionMenu));
                MESSAGE_SENDER.executeEditMessage(answerForBuySubscriptionMenu);
                break;

            case "ADD_LINKED_GROUP":
                String AddLinkedGroupText = "Enter the group ID that will be linked to you, using this format: " +
                                            "1234567890";
                EditMessageText answerForAddLinkedGroupMenu = buildAnswer(AddLinkedGroupText,callbackQuery);

                List<List<String>> buttonsForAddLinkedGroupMenu = new ArrayList<>();

                List<String> buttonRowForAddLinkedGroupMenu = new ArrayList<>();
                buttonRowForAddLinkedGroupMenu.add("Menu");
                buttonsForAddLinkedGroupMenu.add(buttonRowForAddLinkedGroupMenu);

                answerForAddLinkedGroupMenu.setReplyMarkup(KEYBOARD.createButtonMenu(buttonsForAddLinkedGroupMenu));
                MESSAGE_SENDER.executeEditMessage(answerForAddLinkedGroupMenu);
                break;

            case "DELETE_LINKED_GROUP":
                String DeleteLinkedGroupText = "Enter the group ID from previous page, " +
                                               "that will be deleted, " +
                                               "using this format:\n" +
                                               "1234567890";
                EditMessageText answerForDeleteLinkedGroupMenu = buildAnswer(DeleteLinkedGroupText,callbackQuery);

                List<List<String>> buttonsForDeleteLinkedGroupMenu = new ArrayList<>();

                List<String> buttonRowForDeleteLinkedGroupMenu = new ArrayList<>();
                buttonRowForDeleteLinkedGroupMenu.add("Menu");
                buttonsForDeleteLinkedGroupMenu.add(buttonRowForDeleteLinkedGroupMenu);

                answerForDeleteLinkedGroupMenu.setReplyMarkup(KEYBOARD.createButtonMenu(buttonsForDeleteLinkedGroupMenu));
                MESSAGE_SENDER.executeEditMessage(answerForDeleteLinkedGroupMenu);
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
        } else if (lastAction.equals("ADD_LINKED_GROUP")) {
            long groupId;
            if(isDigitMessage(message.strip())) {
                groupId = Long.parseLong(message.strip());
                User currentUser = APP_DAO.findById(update.getMessage().getChatId());

                LinkedGroup linkedGroup = new LinkedGroup(groupId);

                currentUser.addLinkedGroup(linkedGroup);
                APP_DAO.update(currentUser);

                SendMessage answer = new SendMessage();

                answer.setChatId(update.getMessage().getChatId());
                answer.setText("You have successfully linked group - " + groupId);

                MESSAGE_SENDER.executeCustomMessage(answer);
                MESSAGE_SENDER.executeCustomMessage(KEYBOARD.getMainMenu(update.getMessage().getChatId()));
            } else {
                SendMessage answer = new SendMessage();
                answer.setChatId(update.getMessage().getChatId());
                answer.setText("Only numbers from 0 to 9 are allowed. Try again.");
                MESSAGE_SENDER.executeCustomMessage(answer);
                MESSAGE_SENDER.executeCustomMessage(KEYBOARD.getMainMenu(update.getMessage().getChatId()));
            }

        } else if (lastAction.equals("DELETE_LINKED_GROUP")) {
            long groupId;
            boolean isDeleted = false;

            if(isDigitMessage(message.strip())) {
                groupId = Long.parseLong(message.strip());

                User currentUser = APP_DAO.findById(update.getMessage().getChatId());
                List<LinkedGroup> linkedGroups = currentUser.getLinkedGroups();

                for(LinkedGroup linkedGroup : linkedGroups) {
                    if(linkedGroup.getGroupId() == groupId) {
                        currentUser.deleteLinkedGroup(linkedGroup);
                        APP_DAO.update(currentUser);
                        APP_DAO.deleteLinkedGroupById(groupId);
                        isDeleted = true;
                    }
                }

                if(isDeleted) {
                    SendMessage answer = new SendMessage();

                    answer.setChatId(update.getMessage().getChatId());
                    answer.setText("You have successfully deleted group - " + groupId);

                    MESSAGE_SENDER.executeCustomMessage(answer);
                    MESSAGE_SENDER.executeCustomMessage(KEYBOARD.getMainMenu(update.getMessage().getChatId()));
                } else {
                    SendMessage answer = new SendMessage();

                    answer.setChatId(update.getMessage().getChatId());
                    answer.setText("The selected group is not linked to you - " + groupId);

                    MESSAGE_SENDER.executeCustomMessage(answer);
                    MESSAGE_SENDER.executeCustomMessage(KEYBOARD.getMainMenu(update.getMessage().getChatId()));
                }

            } else {
                SendMessage answer = new SendMessage();

                answer.setChatId(update.getMessage().getChatId());
                answer.setText("Only numbers from 0 to 9 are allowed. Try again.");

                MESSAGE_SENDER.executeCustomMessage(answer);
                MESSAGE_SENDER.executeCustomMessage(KEYBOARD.getMainMenu(update.getMessage().getChatId()));
            }
        }
        else {
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

    private boolean isDigitMessage(String message) {
        for (int i = 0; i < message.length(); i++) {
            if(!Character.isDigit(message.charAt(i))){
                return false;
            }
        }
        return true;
    }
}
