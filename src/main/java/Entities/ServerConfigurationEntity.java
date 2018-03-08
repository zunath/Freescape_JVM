package Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ServerConfiguration")
public class ServerConfigurationEntity {

    @Id
    @Column(name = "ServerConfigurationID")
    private int serverConfigurationID;

    @Column(name = "ServerName")
    private String serverName;

    @Column(name = "MessageOfTheDay")
    private String messageOfTheDay;

    public int getServerConfigurationID() {
        return serverConfigurationID;
    }

    public void setServerConfigurationID(int serverConfigurationID) {
        this.serverConfigurationID = serverConfigurationID;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getMessageOfTheDay() {
        return messageOfTheDay;
    }

    public void setMessageOfTheDay(String messageOfTheDay) {
        this.messageOfTheDay = messageOfTheDay;
    }
}