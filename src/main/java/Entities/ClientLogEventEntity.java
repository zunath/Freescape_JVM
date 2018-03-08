package Entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ClientLogEvents")
public class ClientLogEventEntity {

    @Id
    @Column(name = "ClientLogEventID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int clientLogEventID;

    @Column(name = "ClientLogEventTypeID")
    private int clientLogEventTypeID;

    @Column(name = "PlayerID")
    private String playerID;

    @Column(name = "CDKey")
    private String cdKey;

    @Column(name = "AccountName")
    private String accountName;

    @Column(name = "DateOfEvent")
    private Timestamp dateofEvent;

    public int getClientLogEventID() {
        return clientLogEventID;
    }

    public void setClientLogEventID(int clientLogEventID) {
        this.clientLogEventID = clientLogEventID;
    }

    public int getClientLogEventTypeID() {
        return clientLogEventTypeID;
    }

    public void setClientLogEventTypeID(int clientLogEventTypeID) {
        this.clientLogEventTypeID = clientLogEventTypeID;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public String getCdKey() {
        return cdKey;
    }

    public void setCdKey(String cdKey) {
        this.cdKey = cdKey;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Timestamp getDateofEvent() {
        return dateofEvent;
    }

    public void setDateofEvent(Timestamp dateofEvent) {
        this.dateofEvent = dateofEvent;
    }
}
