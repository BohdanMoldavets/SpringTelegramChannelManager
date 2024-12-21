package com.moldavets.SpringTelegramChannelManager.dao;

import com.moldavets.SpringTelegramChannelManager.entity.User;

public interface AppDAO {

    void save(User user);
    User findById(long id);

}
