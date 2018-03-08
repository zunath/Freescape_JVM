package Entities;

import javax.persistence.*;

@Entity
@Table(name = "PCRegionalFame")
public class PCRegionalFameEntity {

    @Id
    @Column(name = "PCRegionalFameID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pcRegionalFameID;

    @Column(name = "PlayerID")
    private String playerID;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FameRegionID")
    private FameRegionEntity fameRegion;

    @Column(name = "Amount")
    private int amount;

    public int getPcRegionalFameID() {
        return pcRegionalFameID;
    }

    public void setPcRegionalFameID(int pcRegionalFameID) {
        this.pcRegionalFameID = pcRegionalFameID;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public FameRegionEntity getFameRegion() {
        return fameRegion;
    }

    public void setFameRegion(FameRegionEntity fameRegion) {
        this.fameRegion = fameRegion;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
