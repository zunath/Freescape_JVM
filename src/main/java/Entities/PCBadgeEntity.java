package Entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "PCBadges")
public class PCBadgeEntity {

    @Id
    @Column(name = "PCBadgeID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pcBadgeID;

    @Column(name ="PlayerID")
    private String playerID;

    @Column(name = "BadgeID")
    private int badgeID;

    @Column(name = "AcquiredDate")
    private Timestamp acquiredDate;

    @Column(name = "AcquiredAreaName")
    private String acquiredAreaName;

    @Column(name = "AcquiredAreaTag")
    private String acquiredAreaTag;

    @Column(name = "AcquiredAreaResref")
    private String acquiredAreaResref;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BadgeID", updatable = false, insertable = false)
    private BadgeEntity badge;

    public Long getPcBadgeID() {
        return pcBadgeID;
    }

    public void setPcBadgeID(Long pcBadgeID) {
        this.pcBadgeID = pcBadgeID;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public int getBadgeID() {
        return badgeID;
    }

    public void setBadgeID(int badgeID) {
        this.badgeID = badgeID;
    }

    public Timestamp getAcquiredDate() {
        return acquiredDate;
    }

    public void setAcquiredDate(Timestamp acquiredDate) {
        this.acquiredDate = acquiredDate;
    }

    public String getAcquiredAreaName() {
        return acquiredAreaName;
    }

    public void setAcquiredAreaName(String acquiredAreaName) {
        this.acquiredAreaName = acquiredAreaName;
    }

    public String getAcquiredAreaTag() {
        return acquiredAreaTag;
    }

    public void setAcquiredAreaTag(String acquiredAreaTag) {
        this.acquiredAreaTag = acquiredAreaTag;
    }

    public String getAcquiredAreaResref() {
        return acquiredAreaResref;
    }

    public void setAcquiredAreaResref(String acquiredAreaResref) {
        this.acquiredAreaResref = acquiredAreaResref;
    }

    public BadgeEntity getBadge() {
        return badge;
    }

    public void setBadge(BadgeEntity badge) {
        this.badge = badge;
    }
}
