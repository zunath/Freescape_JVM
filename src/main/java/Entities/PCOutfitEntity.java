package Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="PCOutfits")
public class PCOutfitEntity {

    @Id
    @Column(name = "PlayerID")
    private String playerID;
    @Column(name="Outfit1")
    private byte[] outfit1;
    @Column(name="Outfit2")
    private byte[] outfit2;
    @Column(name="Outfit3")
    private byte[] outfit3;
    @Column(name="Outfit4")
    private byte[] outfit4;
    @Column(name="Outfit5")
    private byte[] outfit5;
    @Column(name="Outfit6")
    private byte[] outfit6;
    @Column(name="Outfit7")
    private byte[] outfit7;
    @Column(name="Outfit8")
    private byte[] outfit8;
    @Column(name="Outfit9")
    private byte[] outfit9;
    @Column(name="Outfit10")
    private byte[] outfit10;


    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public byte[] getOutfit1() {
        return outfit1;
    }

    public void setOutfit1(byte[] outfit1) {
        this.outfit1 = outfit1;
    }

    public byte[] getOutfit2() {
        return outfit2;
    }

    public void setOutfit2(byte[] outfit2) {
        this.outfit2 = outfit2;
    }

    public byte[] getOutfit3() {
        return outfit3;
    }

    public void setOutfit3(byte[] outfit3) {
        this.outfit3 = outfit3;
    }

    public byte[] getOutfit4() {
        return outfit4;
    }

    public void setOutfit4(byte[] outfit4) {
        this.outfit4 = outfit4;
    }

    public byte[] getOutfit5() {
        return outfit5;
    }

    public void setOutfit5(byte[] outfit5) {
        this.outfit5 = outfit5;
    }

    public byte[] getOutfit6() {
        return outfit6;
    }

    public void setOutfit6(byte[] outfit6) {
        this.outfit6 = outfit6;
    }

    public byte[] getOutfit7() {
        return outfit7;
    }

    public void setOutfit7(byte[] outfit7) {
        this.outfit7 = outfit7;
    }

    public byte[] getOutfit8() {
        return outfit8;
    }

    public void setOutfit8(byte[] outfit8) {
        this.outfit8 = outfit8;
    }

    public byte[] getOutfit9() {
        return outfit9;
    }

    public void setOutfit9(byte[] outfit9) {
        this.outfit9 = outfit9;
    }

    public byte[] getOutfit10() {
        return outfit10;
    }

    public void setOutfit10(byte[] outfit10) {
        this.outfit10 = outfit10;
    }
}
