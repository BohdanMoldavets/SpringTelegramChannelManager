package com.moldavets.SpringTelegramChannelManager.service.action.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.entity.Role;
import com.moldavets.SpringTelegramChannelManager.entity.Subscription;
import com.moldavets.SpringTelegramChannelManager.entity.User;
import com.moldavets.SpringTelegramChannelManager.service.action.Action;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.service.security.Security;
import com.moldavets.SpringTelegramChannelManager.utils.log.LogType;
import com.moldavets.SpringTelegramChannelManager.utils.message.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.ArrayList;
import java.util.List;

@Component("MY_PROFILE")
public class ActionMyProfile implements Action {

    private final Security SECURITY;

    @Autowired
    public ActionMyProfile(Security security) {
        this.SECURITY = security;
    }

    @Override
    public void execute(CallbackQuery callbackQuery,
                        MessageSender messageSender,
                        AppDAO appDAO, Keyboard keyboard) {

        Subscription subscription = null;
        String username = null;

        if(!SECURITY.isValidSubscription(appDAO,callbackQuery.getMessage().getChatId())) {
            SendMessage security = new SendMessage();
            security.setChatId(callbackQuery.getMessage().getChatId());
            security.enableHtml(true);

            security.setText("<b>\uD83D\uDD34Your subscription is expired!\uD83D\uDD34</b>");

            messageSender.executeCustomMessage(security);
        }

        StringBuilder MyProfileText = new StringBuilder();

        //get subscription and username
        try {
            subscription = appDAO.findById(callbackQuery.getMessage().getChatId()).getSubscription();
            username = appDAO.findById(callbackQuery.getMessage().getChatId()).getUsername();
        } catch (Exception e) {
            messageSender.sendLog(String.valueOf(callbackQuery.getMessage().getChatId()),
                                  callbackQuery.getFrom().getUserName(),
                                  e.getMessage(),
                                  LogType.ERROR
            );
        }

        //build the answer
        MyProfileText.append("My Profile:\n")
                .append("\t ╭Username: ")
                .append(username)
                .append("\n")
                .append("\t ├User ID: ")
                .append(callbackQuery.getMessage().getChatId())
                .append("\n")
                .append("\t ╰Subscription: ");

        if(subscription != null) {
            if(subscription.getStatus()) {
                MyProfileText.append("Expires in - ")
                        .append(subscription.getEndDate().getYear())
                        .append("-")
                        .append(subscription.getEndDate().getMonthValue())
                        .append("-")
                        .append(subscription.getEndDate().getDayOfMonth())
                        .append("\n")
                        .append("\t \uD83D\uDCCDSubscription purchases: ")
                        .append(subscription.getPurchaseCount());
            } else {
                MyProfileText.append("Not subscribed");
            }
        }

        EditMessageText answer = MessageUtils.buildAnswer(MyProfileText.toString(),callbackQuery);

        //creating buttons menu
        List<List<String>> buttons = new ArrayList<>();

        List<String> buttonRow = new ArrayList<>();
        if (subscription != null) {
            if (!subscription.getStatus()) {
                buttonRow.add("Buy subscription");
                buttons.add(buttonRow);
                buttonRow = new ArrayList<>();
            }
        }

        buttonRow.add("Menu");
        buttons.add(buttonRow);

        answer.setReplyMarkup(keyboard.createButtonMenu(buttons));
        messageSender.executeEditMessage(answer);

    }
}
