package com.moldavets.SpringTelegramChannelManager.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "linked_groups")
public class LinkedGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "group_id")
    private long group_id;

    public LinkedGroup() {
    }

    public LinkedGroup(long group_id) {
        this.group_id = group_id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }
}
