package com.moldavets.SpringTelegramChannelManager.service.action.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.entity.LinkedGroup;
import com.moldavets.SpringTelegramChannelManager.service.action.Action;
import com.moldavets.SpringTelegramChannelManager.service.message.Keyboard;
import com.moldavets.SpringTelegramChannelManager.service.message.MessageSender;
import com.moldavets.SpringTelegramChannelManager.utils.message.MessageUtils;
import org.springframework.stereotype.Component;
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
            stringBuilder.append("Your linked groups:\n");
            for(LinkedGroup tempLinkedGroup : linkedGroups) {
                stringBuilder
                        .append("\t")
                        .append(tempLinkedGroup.getGroupId())
                        .append("\n");
            }
        } else {
            stringBuilder
                    .append("You don't have any linked group.");
        }

        EditMessageText answerForLinkedGroupsMenu = MessageUtils.buildAnswer(stringBuilder.toString(),callbackQuery);

        //creating buttons menu
        List<List<String>> buttonsMenuForLinkedGroupsMenu = new ArrayList<>();

        List<String> buttonRowForLinkedGroupsMenu = new ArrayList<>();
        buttonRowForLinkedGroupsMenu.add("Add linked group");

        if(isHaveLinkedGroups) {
            buttonRowForLinkedGroupsMenu.add("Delete linked group");
        }

        buttonsMenuForLinkedGroupsMenu.add(buttonRowForLinkedGroupsMenu);

        buttonRowForLinkedGroupsMenu = new ArrayList<>();
        buttonRowForLinkedGroupsMenu.add("Menu");
        buttonsMenuForLinkedGroupsMenu.add(buttonRowForLinkedGroupsMenu);

        answerForLinkedGroupsMenu.setReplyMarkup(keyboard.createButtonMenu(buttonsMenuForLinkedGroupsMenu));
        messageSender.executeEditMessage(answerForLinkedGroupsMenu);

    }
}
