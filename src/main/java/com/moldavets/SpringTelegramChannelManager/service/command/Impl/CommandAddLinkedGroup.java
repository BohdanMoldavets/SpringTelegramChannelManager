package com.moldavets.SpringTelegramChannelManager.service.command.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.entity.LinkedGroup;
import com.moldavets.SpringTelegramChannelManager.entity.User;
import com.moldavets.SpringTelegramChannelManager.service.command.Command;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.utils.log.LogType;
import com.moldavets.SpringTelegramChannelManager.utils.message.MessageUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public class CommandAddLinkedGroup implements Command {

    @Override
    public void execute(Message message,
                        MessageSender messageSender,
                        AppDAO appDAO, Keyboard keyboard) {

        long groupId;
        boolean isPresent = false;
        SendMessage answer = new SendMessage();

        if(MessageUtils.isDigitMessage(message.getText().strip())) {
            groupId = Long.parseLong(message.getText().strip());

            User currentUser = appDAO.findById(message.getChatId());

            List<LinkedGroup> linkedGroups = currentUser.getLinkedGroups();

            if(!linkedGroups.isEmpty()) {
                for(LinkedGroup linkedGroup : linkedGroups) {
                    if(linkedGroup.getGroupId() == groupId) {
                        isPresent = true;
                        break;
                    }
                }
            }

            if(!isPresent) {
                LinkedGroup linkedGroup = new LinkedGroup(groupId);

                currentUser.addLinkedGroup(linkedGroup);
                appDAO.update(currentUser);


                answer.setChatId(message.getChatId());
                answer.setText("You have successfully linked group - " + groupId);

                messageSender.executeCustomMessage(answer);
                messageSender.executeCustomMessage(keyboard.getMainMenu(message.getChatId()));

                messageSender.sendLog(String.valueOf(message.getChatId()),
                        message.getFrom().getUserName(),
                        "<- Bot: You have successfully linked group - " + groupId,
                        LogType.INFO );
            } else {
                answer.setChatId(message.getChatId());
                answer.setText("Selected group already linked to you - " + groupId);
                messageSender.executeCustomMessage(answer);
                messageSender.executeCustomMessage(keyboard.getMainMenu(message.getChatId()));
                messageSender.sendLog(String.valueOf(message.getChatId()),
                        message.getFrom().getUserName(),
                        "<- Bot: Selected group already linked to you - " + groupId,
                        LogType.ERROR);
            }

        } else {
            answer.setChatId(message.getChatId());
            answer.setText("Only numbers from 0 to 9 are allowed. Try again.");
            messageSender.executeCustomMessage(answer);
            messageSender.executeCustomMessage(keyboard.getMainMenu(message.getChatId()));
            messageSender.sendLog(String.valueOf(message.getChatId()),
                                  message.getFrom().getUserName(),
                                  "<- Bot: Only numbers from 0 to 9 are allowed. Try again.",
                                  LogType.ERROR);
        }
    }
}
