package Entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PCTerritoryFlagsStructuresResearchQueues")
public class PCTerritoryFlagsStructuresResearchQueueEntity {

    @Id
    @Column(name = "PCStructureResearchID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pcStructureResearchID;

    @Column(name = "PCStructureID")
    private int pcStructureID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ResearchBlueprintID")
    private ResearchBlueprintEntity blueprint;

    @Column(name = "ResearchSlot")
    private int researchSlot;

    @Column(name = "StartDateTime")
    private Date startDate;

    @Column(name = "CompletedDateTime")
    private Date completedDateTime;

    @Column(name = "IsCanceled")
    private boolean isCanceled;

    @Column(name = "DeliverDateTime")
    private Date deliverDateTime;

    public int getPcStructureResearchID() {
        return pcStructureResearchID;
    }

    public void setPcStructureResearchID(int pcStructureResearchID) {
        this.pcStructureResearchID = pcStructureResearchID;
    }

    public int getPcStructureID() {
        return pcStructureID;
    }

    public void setPcStructureID(int pcStructureID) {
        this.pcStructureID = pcStructureID;
    }

    public ResearchBlueprintEntity getBlueprint() {
        return blueprint;
    }

    public void setBlueprint(ResearchBlueprintEntity blueprint) {
        this.blueprint = blueprint;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getCompletedDateTime() {
        return completedDateTime;
    }

    public void setCompletedDateTime(Date completedDateTime) {
        this.completedDateTime = completedDateTime;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public Date getDeliverDateTime() {
        return deliverDateTime;
    }

    public void setDeliverDateTime(Date deliverDateTime) {
        this.deliverDateTime = deliverDateTime;
    }

    public int getResearchSlot() {
        return researchSlot;
    }

    public void setResearchSlot(int researchSlot) {
        this.researchSlot = researchSlot;
    }
}
