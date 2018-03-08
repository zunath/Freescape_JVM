package Entities;

import javax.persistence.*;
import java.sql.Timestamp;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="PCKeyItems")
public class PCKeyItemEntity {

    @Id
    @Column(name="PCKeyItemID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pcKeyItemID;

    @Column(name="PlayerID")
    private String playerID;

    @Column(name="KeyItemID")
    private int keyItemID;

    @Column(name="AcquiredDate")
    private Timestamp acquiredDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "KeyItemID", updatable = false, insertable = false)
    private KeyItemEntity keyItem;

    public int getPcKeyItemID() {
        return pcKeyItemID;
    }

    public void setPcKeyItemID(int pcKeyItemID) {
        this.pcKeyItemID = pcKeyItemID;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public int getKeyItemID() {
        return keyItemID;
    }

    public void setKeyItemID(int keyItemID) {
        this.keyItemID = keyItemID;
    }

    public KeyItemEntity getKeyItem() {
        return keyItem;
    }

    public Timestamp getAcquiredDate() {
        return acquiredDate;
    }

    public void setAcquiredDate(Timestamp acquiredDate) {
        this.acquiredDate = acquiredDate;
    }
}
