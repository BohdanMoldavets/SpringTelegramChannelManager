package com.moldavets.SpringTelegramChannelManager.service;


import com.moldavets.SpringTelegramChannelManager.model.User;

public interface UserService {

    User findById(Long chatId);

    void save(User user);

    void deleteById(Long chatId);
}
