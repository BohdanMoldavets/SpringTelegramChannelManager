package com.moldavets.SpringTelegramChannelManager.dao.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.entity.User;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class AppDAOImpl implements AppDAO {

    private final EntityManager ENTITY_MANAGER;

    @Autowired
    public AppDAOImpl(EntityManager ENTITY_MANAGER) {
        this.ENTITY_MANAGER = ENTITY_MANAGER;
    }


    @Override
    @Transactional
    public void save(User user) {
        ENTITY_MANAGER.persist(user);
    }

    @Override
    public User findById(long id) {
        return ENTITY_MANAGER.find(User.class, id);
    }
}
