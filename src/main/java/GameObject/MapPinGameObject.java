package GameObject;

import org.nwnx.nwnx2.jvm.NWObject;

public class MapPinGameObject {

    private int playerIndex;

    private String text;

    private float positionX;

    private float positionY;

    private NWObject area;

    private String tag;

    private NWObject oPC;

    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTag() {
        return tag;
    }

    public NWObject getPC() {
        return oPC;
    }

    public void setPC(NWObject oPC) {
        this.oPC = oPC;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public NWObject getArea() {
        return area;
    }

    public void setArea(NWObject area) {
        this.area = area;
    }

}
