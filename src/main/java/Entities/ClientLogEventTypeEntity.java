package Entities;


import javax.persistence.*;

@Entity
@Table(name = "ChatLog")
public class ClientLogEventTypeEntity {

    @Id
    @Column(name = "ClientLogEventTypeID")
    private int clientLogEventTypeID;

    @Column(name = "Name")
    private String name;

    public int getClientLogEventTypeID() {
        return clientLogEventTypeID;
    }

    public void setClientLogEventTypeID(int clientLogEventTypeID) {
        this.clientLogEventTypeID = clientLogEventTypeID;
    }
}
