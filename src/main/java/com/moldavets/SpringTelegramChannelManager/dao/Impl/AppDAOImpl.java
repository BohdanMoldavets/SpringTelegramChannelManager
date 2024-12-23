package com.moldavets.SpringTelegramChannelManager.dao.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.entity.LinkedGroup;
import com.moldavets.SpringTelegramChannelManager.entity.Role;
import com.moldavets.SpringTelegramChannelManager.entity.Subscription;
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
    @Transactional
    public void update(User user) {
        ENTITY_MANAGER.merge(user);
    }

    @Override
    public User findById(long id) {
        return ENTITY_MANAGER.find(User.class, id);
    }

    @Override
    @Transactional
    public void updateSubscription(Subscription subscription) {
        ENTITY_MANAGER.createQuery("UPDATE Subscription " +
                                      "SET startDate=:startDate, endDate=:endDate,status =:status,purchaseCount=:purchaseCount " +
                                      "WHERE user=:user")
                .setParameter("startDate",subscription.getStartDate())
                .setParameter("endDate",subscription.getEndDate())
                .setParameter("status",subscription.getStatus())
                .setParameter("purchaseCount",subscription.getPurchaseCount())
                .setParameter("user",subscription.getUser())
                .executeUpdate();
    }

    @Override
    @Transactional
    public void updateRole(Role role) {
        ENTITY_MANAGER.createQuery("UPDATE Role SET role=:role WHERE user=:user")
                .setParameter("role",role.getRole())
                .setParameter("user",role.getUser())
                .executeUpdate();
    }

    @Override
    @Transactional
    public void deleteLinkedGroupById(long id) {
        ENTITY_MANAGER.createQuery("DELETE FROM LinkedGroup WHERE groupId=:groupId")
                .setParameter("groupId",id)
                .executeUpdate();
    }


}
