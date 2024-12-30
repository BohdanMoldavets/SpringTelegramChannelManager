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


@Component("COMMAND_ADD_LINKED_GROUP")
public class CommandAddLinkedGroup implements Command {

    @Override
    public void execute(Message message,
                        MessageSender messageSender,
                        AppDAO appDAO, Keyboard keyboard) {

        long groupId;
        boolean isPresent = false;
        User currentUser = null;
        List<LinkedGroup> linkedGroups = null;

        SendMessage answer = new SendMessage();

        if(MessageUtils.isValidGroupId(message.getText().strip())) {
            groupId = Long.parseLong(message.getText().strip());

            try {
                currentUser = appDAO.findById(message.getChatId());
                linkedGroups = currentUser.getLinkedGroups();
            } catch (Exception e) {
                messageSender.sendLog(String.valueOf(message.getChatId()),
                                      message.getFrom().getUserName(),
                                      e.getMessage(),
                                      LogType.ERROR
                );
            }


            if( linkedGroups != null && !linkedGroups.isEmpty()) {
                for(LinkedGroup linkedGroup : linkedGroups) {
                    if(linkedGroup.getGroupId() == groupId) {
                        isPresent = true;
                        break;
                    }
                }
            }

            if(!isPresent) {
                if(appDAO.findLinkedGroupById(groupId) == null) {
                    LinkedGroup linkedGroup = new LinkedGroup(groupId);

                    currentUser.addLinkedGroup(linkedGroup);
                    try {
                        appDAO.update(currentUser);
                    } catch (Exception e) {
                        messageSender.sendLog(String.valueOf(message.getChatId()),
                                              message.getFrom().getUserName(),
                                              e.getMessage(),
                                              LogType.ERROR
                        );
                    }

                    answer.setChatId(message.getChatId());
                    answer.setText("✅You have successfully linked group " + groupId);

                    messageSender.executeCustomMessage(answer);

                    messageSender.sendLog(String.valueOf(message.getChatId()),
                            message.getFrom().getUserName(),
                            "<- Bot: You have successfully linked group " + groupId,
                            LogType.INFO );
                } else {
                    answer.setChatId(message.getChatId());
                    answer.setText("❌This group already linked to other user ");
                    messageSender.executeCustomMessage(answer);

                    messageSender.sendLog(String.valueOf(message.getChatId()),
                            message.getFrom().getUserName(),
                            "<- Bot: This group already linked to other user " + groupId,
                            LogType.ERROR);
                }
            } else {
                answer.setChatId(message.getChatId());
                answer.setText("❌Selected group already linked to you " + groupId);

                messageSender.executeCustomMessage(answer);

                messageSender.sendLog(String.valueOf(message.getChatId()),
                        message.getFrom().getUserName(),
                        "<- Bot: Selected group already linked to you " + groupId,
                        LogType.ERROR);
            }

        } else {
            answer.setChatId(message.getChatId());
            answer.setText("⚠ Only numbers from 0 to 9 are allowed and " +
                           "the first character of the Id group must be '-'.  Try again.");

            messageSender.executeCustomMessage(answer);


            messageSender.sendLog(String.valueOf(message.getChatId()),
                                  message.getFrom().getUserName(),
                         "<- Bot: Only numbers from 0 to 9 are allowed and " +
                                  "the first character of the Id group must be '-'.  Try again.",
                                  LogType.ERROR);
        }
        messageSender.executeCustomMessage(keyboard.getMainMenu(message.getChatId()));

        try {
            User tempUser = appDAO.findById(message.getChatId());
            tempUser.setLastAction("MENU");
            appDAO.update(tempUser);
        } catch (Exception e) {
            messageSender.sendLog(String.valueOf(message.getChatId()),
                    message.getFrom().getUserName(),
                    e.getMessage(),
                    LogType.ERROR
            );
        }
    }
}
