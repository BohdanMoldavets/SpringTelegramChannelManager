package com.moldavets.SpringTelegramChannelManager.service.message.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.entity.User;
import com.moldavets.SpringTelegramChannelManager.service.factory.ActionFactory;
import com.moldavets.SpringTelegramChannelManager.service.factory.CommandFactory;
import com.moldavets.SpringTelegramChannelManager.service.message.ActionHandler;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.utils.log.LogType;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;



@Component
public class ActionHandlerImpl implements ActionHandler {

    private final MessageSender MESSAGE_SENDER;
    private final Keyboard KEYBOARD;
    private final AppDAO APP_DAO;
    private final ActionFactory ACTION_FACTORY;
    private final CommandFactory COMMAND_FACTORY;


    public ActionHandlerImpl(@Lazy MessageSender messageSender, Keyboard keyboard,
                             AppDAO appDAO, ActionFactory actionFactory,
                             CommandFactory commandFactory) {
        this.MESSAGE_SENDER = messageSender;
        this.KEYBOARD = keyboard;
        this.APP_DAO = appDAO;
        this.ACTION_FACTORY = actionFactory;
        this.COMMAND_FACTORY = commandFactory;
    }

    @Override
    public void handleAction(CallbackQuery callbackQuery) {

        //saving the last action
        try {
            User tempUser = APP_DAO.findById(callbackQuery.getMessage().getChatId());
            tempUser.setLastAction(callbackQuery.getData());
            APP_DAO.update(tempUser);
        } catch (Exception e) {
            MESSAGE_SENDER.sendLog(String.valueOf(callbackQuery.getMessage().getChatId()),
                                   callbackQuery.getFrom().getUserName(),
                                   e.getMessage(),
                                   LogType.ERROR
            );
        }

        MESSAGE_SENDER.sendLog(String.valueOf(callbackQuery.getMessage().getChatId()),
                               callbackQuery.getFrom().getUserName(),
                               "Inside block " + callbackQuery.getData(),
                               LogType.INFO);

        ACTION_FACTORY.getAction(callbackQuery.getData())
                .execute(callbackQuery,MESSAGE_SENDER,APP_DAO,KEYBOARD);
    }

    @Override
    public void handleCommand(Message message) {
        MESSAGE_SENDER.sendLog(String.valueOf(message.getChatId()),
                               message.getFrom().getUserName(),
                               message.getText(),
                               LogType.INFO);


        if(COMMAND_FACTORY.containsCommand(message.getText())) {
            COMMAND_FACTORY.getCommand(message.getText())
                           .execute(message,MESSAGE_SENDER,APP_DAO,KEYBOARD);
        } else if (COMMAND_FACTORY.containsCommand("COMMAND_"+getLastActionById(message.getChatId()))) {
            COMMAND_FACTORY.getCommand("COMMAND_"+getLastActionById(message.getChatId()))
                           .execute(message,MESSAGE_SENDER,APP_DAO,KEYBOARD);
        } else {
            COMMAND_FACTORY.getCommand("COMMAND_DOES_NOT_EXIST")
                           .execute(message,MESSAGE_SENDER,APP_DAO,KEYBOARD);
        }
    }

    @Override
    public void handleOther(Message message) {
        SendMessage doNotSupport = new SendMessage();

        doNotSupport.setChatId(message.getChatId());
        doNotSupport.setReplyToMessageId(message.getMessageId());
        doNotSupport.setText("❌This message is not supported now❌");

        MESSAGE_SENDER.executeCustomMessage(doNotSupport);

        MESSAGE_SENDER.sendLog(String.valueOf(message.getChatId()),
                message.getFrom().getUserName(),
                "<- Bot: ❌This message is not supported now❌",
                LogType.ERROR
        );
    }

    private String getLastActionById(long id) {
        return APP_DAO.findById(id).getLastAction();
    }
}
