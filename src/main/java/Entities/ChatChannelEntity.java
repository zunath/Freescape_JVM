package Entities;

import javax.persistence.*;

@Entity
@Table(name = "ChatChannelsDomain")
public class ChatChannelEntity {

    @Id
    @Column(name = "ChatChannelID")
    private int chatChannelID;

    @Column(name = "Name")
    private String name;

    public int getChatChannelID() {
        return chatChannelID;
    }

    public void setChatChannelID(int chatChannelID) {
        this.chatChannelID = chatChannelID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
