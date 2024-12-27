package com.moldavets.SpringTelegramChannelManager.service.security;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;

import java.time.LocalDateTime;

public interface Security {

    boolean hasRole(AppDAO appDAO, long userId, String role);
    boolean isValidSubscription(AppDAO appDAO, long userId);

}
