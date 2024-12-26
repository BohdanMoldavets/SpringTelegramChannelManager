package com.moldavets.SpringTelegramChannelManager.service.factory;

import com.moldavets.SpringTelegramChannelManager.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class CommandFactory {

    private final ApplicationContext APPLICATION_CONTEXT;

    @Autowired
    public CommandFactory(ApplicationContext applicationContext) {
        this.APPLICATION_CONTEXT = applicationContext;
    }

    public Command getCommand(String commandName) {
        return APPLICATION_CONTEXT.getBean(commandName, Command.class);
    }

    public boolean containsCommand(String commandName) {
        return APPLICATION_CONTEXT.containsBean(commandName);
    }
}
