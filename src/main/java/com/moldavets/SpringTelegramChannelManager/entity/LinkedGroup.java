package com.moldavets.SpringTelegramChannelManager.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "linked_groups")
public class LinkedGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade = CascadeType.ALL,
               fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "group_id")
    private long groupId;


    public LinkedGroup() {
    }

    public LinkedGroup(long groupId) {
        this.groupId = groupId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

}
