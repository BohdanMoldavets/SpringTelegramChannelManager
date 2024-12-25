package com.moldavets.SpringTelegramChannelManager.service.action.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.entity.Subscription;
import com.moldavets.SpringTelegramChannelManager.service.action.Action;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.utils.message.MessageUtils;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.ArrayList;
import java.util.List;

public class ActionMyProfile implements Action {
    @Override
    public void execute(CallbackQuery callbackQuery,
                        MessageSender messageSender,
                        AppDAO appDAO, Keyboard keyboard) {

        StringBuilder MyProfileText = new StringBuilder();
        Subscription subscription = appDAO.findById(callbackQuery.getMessage().getChatId()).getSubscription();
        MyProfileText.append("My Profile:\n")
                .append("\t Username: ")
                .append(appDAO.findById(callbackQuery.getMessage().getChatId()).getUsername())
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

        EditMessageText answerForMyProfileMenu = MessageUtils.buildAnswer(MyProfileText.toString(),callbackQuery);

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

        answerForMyProfileMenu.setReplyMarkup(keyboard.createButtonMenu(buttonsMenuForMyProfileMenu));
        messageSender.executeEditMessage(answerForMyProfileMenu);

    }
}