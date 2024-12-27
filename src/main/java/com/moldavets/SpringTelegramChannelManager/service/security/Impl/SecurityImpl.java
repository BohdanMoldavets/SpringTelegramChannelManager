package com.moldavets.SpringTelegramChannelManager.service.security.Impl;

import com.moldavets.SpringTelegramChannelManager.dao.AppDAO;
import com.moldavets.SpringTelegramChannelManager.entity.Role;
import com.moldavets.SpringTelegramChannelManager.entity.Subscription;
import com.moldavets.SpringTelegramChannelManager.entity.User;
import com.moldavets.SpringTelegramChannelManager.service.security.Security;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SecurityImpl implements Security {

    @Override
    public boolean hasRole(AppDAO appDAO, long userId, String role) {
        return appDAO.findById(userId).getRole().getRole().equals(role);
    }

    @Override
    public boolean isValidSubscription(AppDAO appDAO, long userId) {

        boolean isValid = false;

        if(hasRole(appDAO,userId,"ROLE_SUBSCRIBER")) {
            isValid = LocalDateTime.now().isBefore(appDAO.findById(userId).getSubscription().getEndDate());
            if(!isValid) {
                User tempUser = appDAO.findById(userId);
                Role tempRole = new Role("ROLE_USER");
                tempRole.setUser(tempUser);

                appDAO.updateRole(tempRole);

                Subscription tempSubscription = new Subscription();
                tempSubscription.setStatus(false);
                tempSubscription.setStartDate(null);
                tempSubscription.setEndDate(null);
                tempSubscription.setUser(tempUser);
                tempSubscription.setPurchaseCount(tempUser.getSubscription().getPurchaseCount());

                appDAO.updateSubscription(tempSubscription);
            }
        } else {
            isValid = true;
        }
        return isValid;
    }

}
