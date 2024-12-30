package com.moldavets.SpringTelegramChannelManager.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name="last_message", length = 4096)
    private String lastMessage;

    @Column(name="last_action")
    private String lastAction;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,
               fetch = FetchType.EAGER)
    private List<LinkedGroup> linkedGroups;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Role role;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Subscription subscription;


    public User() {
        subscription = new Subscription();
        subscription.setUser(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastAction() {
        return lastAction;
    }

    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }

    public List<LinkedGroup> getLinkedGroups() {
        return linkedGroups;
    }

    public void addLinkedGroup(LinkedGroup linkedGroup) {
        if(linkedGroups == null) {
            linkedGroups = new ArrayList<>();
        }

        linkedGroup.setUser(this);
        linkedGroups.add(linkedGroup);
    }

    public void deleteLinkedGroup(LinkedGroup linkedGroup) {}

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        role.setUser(this);
        this.role = role;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        subscription.setUser(this);
        this.subscription = subscription;
    }
}
