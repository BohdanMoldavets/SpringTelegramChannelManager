package com.moldavets.SpringTelegramChannelManager.service.factory;

import com.moldavets.SpringTelegramChannelManager.service.action.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ActionFactory {

    private final ApplicationContext APPLICATION_CONTEXT;

    @Autowired
    public ActionFactory(ApplicationContext applicationContext) {
        this.APPLICATION_CONTEXT = applicationContext;
    }

    public Action getAction(String actionName) {
        return APPLICATION_CONTEXT.getBean(actionName, Action.class);
    }

}
