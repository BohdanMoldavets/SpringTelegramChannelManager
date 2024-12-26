package com.moldavets.SpringTelegramChannelManager.service.action.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.entity.LinkedGroup;
import com.moldavets.SpringTelegramChannelManager.service.action.Action;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.utils.message.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Component("SEND_THIS_POST✅")
public class ActionSendThisPost implements Action {


    @Override
    public void execute(CallbackQuery callbackQuery,
                        MessageSender messageSender,
                        AppDAO appDAO, Keyboard keyboard) {

        messageSender.executeDeleteMessage(MessageUtils.buildDeleteMessage(callbackQuery));

        List<String> linkedGroupsId =
            appDAO.findById(callbackQuery.getMessage().getChatId()).getLinkedGroups().stream()
            .map(LinkedGroup::getGroupId)
            .map(Object::toString)
            .toList();

        for(String linkedGroupId : linkedGroupsId) {

            boolean isSent = true;

            SendMessage response = new SendMessage();
            SendMessage post = new SendMessage();

            post.setChatId(linkedGroupId);
            post.setText(MessageUtils.message.getText());

            try {
                messageSender.executeCustomMessage(post);
            } catch (Exception e) {
                isSent = false;
            }

            response.setChatId(callbackQuery.getMessage().getChatId());

            if(isSent) {
            response.setText("<b>✅ Sent to group with id " + linkedGroupId + "</b>");
            response.setParseMode("HTML");

            messageSender.executeCustomMessage(response);
            } else {
            response.setText("❌ Error sending to group with id " + linkedGroupId);

            messageSender.executeCustomMessage(response);
            }

        }
        messageSender.executeCustomMessage(keyboard.getMainMenu(callbackQuery.getMessage().getChatId()));
    }
}
