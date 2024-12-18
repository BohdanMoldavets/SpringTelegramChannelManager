package com.moldavets.SpringTelegramChannelManager.keyboard;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface Keyboard {

    SendMessage getMainMenu(long chatId);
    SendMessage getLinkedGroupsMenu(long chatId);
    SendMessage getProfileMenu(long chatId);
    SendMessage getSendPostsMenu(long chatId);
}
