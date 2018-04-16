package Entities;

import javax.persistence.*;

@Entity
@Table(name="PCMapPins")
public class PCMapPinEntity {

    @Id
    @Column(name = "PCMapPinID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pcMapPinID;

    @Column(name = "PlayerID")
    private String playerID;

    @Column(name = "AreaTag")
    private String areaTag;

    @Column(name = "PositionX")
    private float positionX;

    @Column(name = "PositionY")
    private float positionY;

    @Column(name = "NoteText")
    private String noteText;

    public int getPcMapPinID() {
        return pcMapPinID;
    }

    public void setPcMapPinID(int pcMapPinID) {
        this.pcMapPinID = pcMapPinID;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public String getAreaTag() {
        return areaTag;
    }

    public void setAreaTag(String areaTag) {
        this.areaTag = areaTag;
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }
}
