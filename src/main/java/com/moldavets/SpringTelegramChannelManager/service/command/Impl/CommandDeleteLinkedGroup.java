package com.moldavets.SpringTelegramChannelManager.service.command.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.entity.LinkedGroup;
import com.moldavets.SpringTelegramChannelManager.entity.User;
import com.moldavets.SpringTelegramChannelManager.service.command.Command;
import com.moldavets.SpringTelegramChannelManager.service.message.Impl.ActionHandlerImpl;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.utils.log.LogType;
import com.moldavets.SpringTelegramChannelManager.utils.message.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Component("COMMAND_DELETE_LINKED_GROUP")
public class CommandDeleteLinkedGroup implements Command {

    @Override
    public void execute(Message message,
                        MessageSender messageSender,
                        AppDAO appDAO, Keyboard keyboard) {

        long groupId;
        boolean isDeleted = false;
        SendMessage answer = new SendMessage();

        if(MessageUtils.isValidGroupId(message.getText().strip())) {
            groupId = Long.parseLong(message.getText().strip());

            User currentUser = appDAO.findById(message.getChatId());
            List<LinkedGroup> linkedGroups = currentUser.getLinkedGroups();

            for(LinkedGroup linkedGroup : linkedGroups) {
                if(linkedGroup.getGroupId() == groupId) {
                    currentUser.deleteLinkedGroup(linkedGroup);
                    appDAO.update(currentUser);
                    appDAO.deleteLinkedGroupById(groupId);
                    isDeleted = true;
                }
            }

            if(isDeleted) {
                answer.setChatId(message.getChatId());
                answer.setText("✅You have successfully deleted group " + groupId);

                messageSender.executeCustomMessage(answer);
                messageSender.executeCustomMessage(keyboard.getMainMenu(message.getChatId()));

                messageSender.sendLog(String.valueOf(message.getChatId()),
                        message.getFrom().getUserName(),
                        "<- Bot: You have successfully deleted group " + groupId,
                        LogType.INFO);
            } else {
                answer.setChatId(message.getChatId());
                answer.setText("❌The selected group is not linked to you " + groupId);

                messageSender.executeCustomMessage(answer);
                messageSender.executeCustomMessage(keyboard.getMainMenu(message.getChatId()));

                messageSender.sendLog(String.valueOf(message.getChatId()),
                        message.getFrom().getUserName(),
                        "<- Bot: The selected group is not linked to you "+ groupId,
                        LogType.ERROR);
            }

        } else {
            answer.setChatId(message.getChatId());
            answer.setText("⚠ Only numbers from 0 to 9 are allowed and " +
                           "the first character of the Id group must be '-'.  Try again.");

            messageSender.executeCustomMessage(answer);
            messageSender.executeCustomMessage(keyboard.getMainMenu(message.getChatId()));

            messageSender.sendLog(String.valueOf(message.getChatId()),
                    message.getFrom().getUserName(),
                    "<- Bot: Only numbers from 0 to 9 are allowed and " +
                             "the first character of the Id group must be '-'.  Try again.",
                             LogType.ERROR);
        }
        ActionHandlerImpl.lastAction = "MENU";
    }
}
