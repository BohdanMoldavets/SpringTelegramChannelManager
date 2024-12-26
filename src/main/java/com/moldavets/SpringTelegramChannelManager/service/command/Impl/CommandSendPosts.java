package com.moldavets.SpringTelegramChannelManager.service.command.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.service.command.Command;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.utils.message.MessageUtils;
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

//        List<String> linkedGroupsId =
//                appDAO.findById(message.getChatId()).getLinkedGroups().stream()
//                .map(LinkedGroup::getGroupId)
//                .map(Object::toString)
//                .toList();

        SendMessage response = new SendMessage();
        response.setChatId(message.getChatId());
        response.setParseMode("HTML");

        response.setText(message.getText());
        messageSender.executeCustomMessage(response);
        MessageUtils.message = message;

        response.setText("<b>Are you sure you want to send a post with this text?</b>");

        List<List<String>> buttons = new ArrayList<>();

        List<String> buttonRow = new ArrayList<>();
        buttonRow.add("Send this post✅");
        buttonRow.add("Do not send post❌");

        buttons.add(buttonRow);

        response.setReplyMarkup(keyboard.createButtonMenu(buttons));

        messageSender.executeCustomMessage(response);

        //sending
//        for(String linkedGroupId : linkedGroupsId) {
//
//            boolean isSent = true;
//
//            SendMessage answer = new SendMessage();
//            SendMessage post = new SendMessage();
//
//            post.setChatId(linkedGroupId);
//            post.setText(message.getText());
//
//            try {
//                messageSender.executeCustomMessage(post);
//            } catch (Exception e) {
//                isSent = false;
//            }
//
//            answer.setChatId(message.getChatId());
//            if(isSent) {
//                answer.setText("<b>✅ Sent to group with id " + linkedGroupId + "</b>");
//                answer.setParseMode("HTML");
//
//                messageSender.executeCustomMessage(answer);
//            } else {
//                answer.setText("❌ Error sending to group with id " + linkedGroupId);
//
//                messageSender.executeCustomMessage(answer);
//            }
//
//        }
    }

}
