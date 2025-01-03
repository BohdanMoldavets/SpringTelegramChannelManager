package com.moldavets.SpringTelegramChannelManager.service.action.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.entity.Role;
import com.moldavets.SpringTelegramChannelManager.entity.User;
import com.moldavets.SpringTelegramChannelManager.service.action.Action;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.utils.log.LogType;
import com.moldavets.SpringTelegramChannelManager.utils.message.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component("BUY_SUBSCRIPTION")
public class ActionBuySubscription implements Action {

    @Override
    public void execute(CallbackQuery callbackQuery,
                        MessageSender messageSender,
                        AppDAO appDAO, Keyboard keyboard) {

        String BuySubscriptionText = "Buying a subscription is not available right now as the bot is not commercial." +
                " Congratulations you just got a monthly subscription for free!";
        EditMessageText answer = MessageUtils.buildAnswer(BuySubscriptionText,callbackQuery);

        //add a monthly subscription and change the role
        try {
            User currentUser = appDAO.findById(callbackQuery.getMessage().getChatId());
            currentUser.getSubscription().addMonthlySubscription();
            currentUser.getSubscription().setUser(currentUser);
            appDAO.updateSubscription(currentUser.getSubscription());

            Role tempRole = new Role();
            tempRole.setRole("ROLE_SUBSCRIBER");
            currentUser.setRole(tempRole);
            appDAO.updateRole(currentUser.getRole());
        } catch (Exception e) {
            messageSender.sendLog(String.valueOf(callbackQuery.getMessage().getChatId()),
                                  callbackQuery.getFrom().getUserName(),
                                  e.getMessage(),
                                  LogType.ERROR
            );
        }


        //creating buttons menu
        answer.setReplyMarkup(keyboard.getOnlyMenuButton());
        messageSender.executeEditMessage(answer);

    }

}
