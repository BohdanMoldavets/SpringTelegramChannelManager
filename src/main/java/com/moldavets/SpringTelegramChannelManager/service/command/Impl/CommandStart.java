package com.moldavets.SpringTelegramChannelManager.service.command.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.entity.Role;
import com.moldavets.SpringTelegramChannelManager.entity.User;
import com.moldavets.SpringTelegramChannelManager.service.command.Command;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.utils.log.LogType;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

public class CommandStart implements Command {

    @Override
    public void execute(Message message,
                        MessageSender messageSender,
                        AppDAO appDAO, Keyboard keyboard) {

        long chatId = message.getChatId();
        Chat chat = message.getChat();
        if(appDAO.findById(chatId) == null) {
            User tempUser = new User();

            tempUser.setId(chatId);
            tempUser.setUsername(chat.getUserName());
            tempUser.setRole(new Role("ROLE_USER"));

            appDAO.save(tempUser);
            messageSender.sendLog(String.valueOf(message.getChatId()),
                                  message.getFrom().getUserName(),
                                  "has been registered",
                                  LogType.INFO);
        }
        messageSender.executeCustomMessage(keyboard.getMainMenu(chatId));

    }

}
