package com.moldavets.SpringTelegramChannelManager.service.command.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.service.command.Command;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import org.telegram.telegrambots.meta.api.objects.Message;

public class CommandDoesNotExist implements Command {

    @Override
    public void execute(Message message,
                        MessageSender messageSender,
                        AppDAO appDAO, Keyboard keyboard) {

        messageSender.sendMessage(String.valueOf(message.getChatId()), "Command does not exist");
    }
}
