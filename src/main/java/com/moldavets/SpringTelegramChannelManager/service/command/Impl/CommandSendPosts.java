package com.moldavets.SpringTelegramChannelManager.service.command.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.entity.User;
import com.moldavets.SpringTelegramChannelManager.service.command.Command;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.utils.log.LogType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component("COMMAND_SEND_POSTS")
public class CommandSendPosts implements Command {

    @Override
    public void execute(Message message, MessageSender messageSender,
                        AppDAO appDAO, Keyboard keyboard) {

        SendMessage response = new SendMessage();
        response.setChatId(message.getChatId());
        response.setParseMode("HTML");

        response.setText(message.getText());
        messageSender.executeCustomMessage(response);

        //save last message to db
        try {
            User tempUser = appDAO.findById(message.getChatId());
            tempUser.setLastMessage(message.getText());
            appDAO.update(tempUser);
        } catch (Exception e) {
            messageSender.sendLog(String.valueOf(message.getChatId()),
                                  message.getFrom().getUserName(),
                                  e.getMessage(),
                                  LogType.ERROR
            );
        }

        response.setText("<b>\uD83D\uDCCCAre you sure you want to send a post with this text?\uD83D\uDCCC</b>");

        List<List<String>> buttons = new ArrayList<>();

        List<String> buttonRow = new ArrayList<>();
        buttonRow.add("Send this post✅");
        buttonRow.add("Do not send post❌");

        buttons.add(buttonRow);

        response.setReplyMarkup(keyboard.createButtonMenu(buttons));

        messageSender.executeCustomMessage(response);

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

        messageSender.sendLog(String.valueOf(message.getChatId()),
                              message.getFrom().getUserName(),
                              response.getText(),
                              LogType.INFO
        );
    }

}
