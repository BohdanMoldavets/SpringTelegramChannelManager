package com.moldavets.SpringTelegramChannelManager.service.action.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.entity.LinkedGroup;
import com.moldavets.SpringTelegramChannelManager.service.action.Action;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.utils.message.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.ArrayList;
import java.util.List;

@Component("LINKED_GROUPS")
public class ActionLinkedGroups implements Action {

    @Override
    public void execute(CallbackQuery callbackQuery,
                        MessageSender messageSender,
                        AppDAO appDAO, Keyboard keyboard) {

        boolean isHaveLinkedGroups = false;
        StringBuilder stringBuilder = new StringBuilder();

        List<LinkedGroup> linkedGroups = appDAO.findById(callbackQuery.getMessage().getChatId()).getLinkedGroups();

        if (linkedGroups != null && !linkedGroups.isEmpty()) {
            isHaveLinkedGroups = true;
            stringBuilder.append("☰Your linked groups:\n");
            for(LinkedGroup tempLinkedGroup : linkedGroups) {
                stringBuilder
                        .append("\t")
                        .append(String.format("\uD83D\uDCE9<code>%s</code>",tempLinkedGroup.getGroupId()))
                        .append("\n");
            }
        } else {
            stringBuilder
                    .append("⚠You don't have any linked group.⚠");
        }

        EditMessageText answer = MessageUtils.buildAnswer(stringBuilder.toString(),callbackQuery);
        answer.setParseMode(ParseMode.HTML);

        //creating buttons menu
        List<List<String>> buttons = new ArrayList<>();

        List<String> buttonRow = new ArrayList<>();
        buttonRow.add("Add linked group");

        if(isHaveLinkedGroups) {
            buttonRow.add("Delete linked group");
        }

        buttons.add(buttonRow);

        buttonRow = new ArrayList<>();
        buttonRow.add("Menu");
        buttons.add(buttonRow);

        answer.setReplyMarkup(keyboard.createButtonMenu(buttons));
        messageSender.executeEditMessage(answer);

    }
}
