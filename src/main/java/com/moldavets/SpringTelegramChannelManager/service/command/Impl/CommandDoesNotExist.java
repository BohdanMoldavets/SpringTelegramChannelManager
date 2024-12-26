package com.moldavets.SpringTelegramChannelManager.service.command.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.service.command.Command;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.utils.log.LogType;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component("COMMAND_DOES_NOT_EXIST")
public class CommandDoesNotExist implements Command {

    @Override
    public void execute(Message message,
                        MessageSender messageSender,
                        AppDAO appDAO, Keyboard keyboard) {

        messageSender.sendMessage(String.valueOf(message.getChatId()), "Command does not exist");

        messageSender.sendLog(String.valueOf(message.getChatId()),
                              message.getFrom().getUserName(),
                              "<- Bot: Command does not exist",
                              LogType.ERROR);
    }
}
