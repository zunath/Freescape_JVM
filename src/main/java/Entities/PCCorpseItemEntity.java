package Entities;

import javax.persistence.*;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="PCCorpseItems")
public class PCCorpseItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PCCorpseItemID")
    private int pcCorpseItemID;

    @Lob
    @Column(name="NWItemObject")
    private byte[] item;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PCCorpseID")
    private PCCorpseEntity corpse;

    public int getPcCorpseItemID() {
        return pcCorpseItemID;
    }

    public void setPcCorpseItemID(int pcCorpseItemID) {
        this.pcCorpseItemID = pcCorpseItemID;
    }

    public byte[] getItem() {
        return item;
    }

    public void setItem(byte[] item) {
        this.item = item;
    }

    public PCCorpseEntity getCorpse() {
        return corpse;
    }

    public void setCorpse(PCCorpseEntity corpse) {
        this.corpse = corpse;
    }
}
