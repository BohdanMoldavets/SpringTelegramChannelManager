package com.moldavets.SpringTelegramChannelManager.service.command.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.entity.Role;
import com.moldavets.SpringTelegramChannelManager.entity.User;
import com.moldavets.SpringTelegramChannelManager.service.command.Command;
import com.moldavets.SpringTelegramChannelManager.service.message.Impl.ActionHandlerImpl;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.service.security.Security;
import com.moldavets.SpringTelegramChannelManager.utils.log.LogType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component("/start")
public class CommandStart implements Command {

    private final Security SECURITY;

    @Autowired
    public CommandStart(Security security) {
        this.SECURITY = security;
    }

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
                                  LogType.INFO
            );

        } else if(!SECURITY.isValidSubscription(appDAO,message.getChatId())) {

            SendMessage security = new SendMessage();
            security.setChatId(message.getChatId());
            security.enableHtml(true);

            security.setText("<b>Your subscription is expired!</b>");

            messageSender.executeCustomMessage(security);
        }
        messageSender.executeCustomMessage(keyboard.getMainMenu(chatId));

        ActionHandlerImpl.lastAction = "MENU";
    }
}
