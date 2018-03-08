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

    @Column(name = "MaxLevel")
    private int maxLevel;

    @Column(name = "MaxExpAcquirable")
    private int maxExpAcquirable;

    @Column(name = "MaxExpGainDistance")
    private float maxExpGainDistance;

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

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getMaxExpAcquirable() {
        return maxExpAcquirable;
    }

    public void setMaxExpAcquirable(int maxExpAcquirable) {
        this.maxExpAcquirable = maxExpAcquirable;
    }

    public float getMaxExpGainDistance() {
        return maxExpGainDistance;
    }

    public void setMaxExpGainDistance(float maxExpGainDistance) {
        this.maxExpGainDistance = maxExpGainDistance;
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