package com.moldavets.SpringTelegramChannelManager.dao;

import com.moldavets.SpringTelegramChannelManager.entity.LinkedGroup;
import com.moldavets.SpringTelegramChannelManager.entity.Role;
import com.moldavets.SpringTelegramChannelManager.entity.Subscription;
import com.moldavets.SpringTelegramChannelManager.entity.User;

public interface AppDAO {

    //user
    void save(User user);
    void update(User user);
    User findById(long id);

    //subscription
    void updateSubscription(Subscription subscription);

    //role
    void updateRole(Role role);

    //linked groups
    void deleteLinkedGroupById(long id);
    LinkedGroup findLinkedGroupById(long id);
}
