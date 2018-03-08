package Entities;

import javax.persistence.*;


@Entity
@Table(name="PCEquippedAbilities")
public class PCEquippedAbilityEntity {

    @Id
    @Column(name = "PlayerID")
    private String playerID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="Slot1")
    private AbilityEntity slot1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="Slot2")
    private AbilityEntity slot2;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="Slot3")
    private AbilityEntity slot3;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="Slot4")
    private AbilityEntity slot4;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="Slot5")
    private AbilityEntity slot5;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="Slot6")
    private AbilityEntity slot6;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="Slot7")
    private AbilityEntity slot7;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="Slot8")
    private AbilityEntity slot8;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="Slot9")
    private AbilityEntity slot9;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="Slot10")
    private AbilityEntity slot10;

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public AbilityEntity getSlot1() {
        return slot1;
    }

    public void setSlot1(AbilityEntity slot1) {
        this.slot1 = slot1;
    }

    public AbilityEntity getSlot2() {
        return slot2;
    }

    public void setSlot2(AbilityEntity slot2) {
        this.slot2 = slot2;
    }

    public AbilityEntity getSlot3() {
        return slot3;
    }

    public void setSlot3(AbilityEntity slot3) {
        this.slot3 = slot3;
    }

    public AbilityEntity getSlot4() {
        return slot4;
    }

    public void setSlot4(AbilityEntity slot4) {
        this.slot4 = slot4;
    }

    public AbilityEntity getSlot5() {
        return slot5;
    }

    public void setSlot5(AbilityEntity slot5) {
        this.slot5 = slot5;
    }

    public AbilityEntity getSlot6() {
        return slot6;
    }

    public void setSlot6(AbilityEntity slot6) {
        this.slot6 = slot6;
    }

    public AbilityEntity getSlot7() {
        return slot7;
    }

    public void setSlot7(AbilityEntity slot7) {
        this.slot7 = slot7;
    }

    public AbilityEntity getSlot8() {
        return slot8;
    }

    public void setSlot8(AbilityEntity slot8) {
        this.slot8 = slot8;
    }

    public AbilityEntity getSlot9() {
        return slot9;
    }

    public void setSlot9(AbilityEntity slot9) {
        this.slot9 = slot9;
    }

    public AbilityEntity getSlot10() {
        return slot10;
    }

    public void setSlot10(AbilityEntity slot10) {
        this.slot10 = slot10;
    }
}
