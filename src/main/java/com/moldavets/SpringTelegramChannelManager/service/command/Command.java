package com.moldavets.SpringTelegramChannelManager.service.command;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface Command {

    void execute(Message message,
                 MessageSender messageSender,
                 AppDAO appDAO, Keyboard keyboard);

}
