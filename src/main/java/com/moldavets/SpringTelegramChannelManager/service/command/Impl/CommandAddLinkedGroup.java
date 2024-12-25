package com.moldavets.SpringTelegramChannelManager.service.command.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.entity.LinkedGroup;
import com.moldavets.SpringTelegramChannelManager.entity.User;
import com.moldavets.SpringTelegramChannelManager.service.command.Command;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.utils.message.MessageUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class CommandAddLinkedGroup implements Command {

    @Override
    public void execute(Message message,
                        MessageSender messageSender,
                        AppDAO appDAO, Keyboard keyboard) {

        long groupId;
        if(MessageUtils.isDigitMessage(message.getText().strip())) {
            groupId = Long.parseLong(message.getText().strip());
            User currentUser = appDAO.findById(message.getChatId());

            LinkedGroup linkedGroup = new LinkedGroup(groupId);

            currentUser.addLinkedGroup(linkedGroup);
            appDAO.update(currentUser);

            SendMessage answer = new SendMessage();

            answer.setChatId(message.getChatId());
            answer.setText("You have successfully linked group - " + groupId);

            messageSender.executeCustomMessage(answer);
            messageSender.executeCustomMessage(keyboard.getMainMenu(message.getChatId()));

        } else {
            SendMessage answer = new SendMessage();
            answer.setChatId(message.getChatId());
            answer.setText("Only numbers from 0 to 9 are allowed. Try again.");
            messageSender.executeCustomMessage(answer);
            messageSender.executeCustomMessage(keyboard.getMainMenu(message.getChatId()));
        }
    }
}
