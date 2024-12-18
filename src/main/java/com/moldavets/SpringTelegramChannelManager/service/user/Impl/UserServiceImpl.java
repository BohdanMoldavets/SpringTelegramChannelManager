package com.moldavets.SpringTelegramChannelManager.service.user.Impl;

import com.moldavets.SpringTelegramChannelManager.model.User;
import com.moldavets.SpringTelegramChannelManager.model.UserRepository;
import com.moldavets.SpringTelegramChannelManager.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User findById(Long chatId) {
        return userRepository.findById(chatId).orElse(null);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteById(Long chatId) {
        if(userRepository.findById(chatId).isPresent()) {
            userRepository.deleteById(chatId);
        }
    }
}
