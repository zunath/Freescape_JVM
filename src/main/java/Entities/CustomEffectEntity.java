package Entities;

import javax.persistence.*;

@Entity
@Table(name = "CustomEffects")
public class CustomEffectEntity {

    @Id
    @Column(name = "CustomEffectID")
    private int customEffectID;

    @Column(name = "Name")
    private String name;

    @Column(name = "IconID")
    private int iconID;

    @Column(name = "ScriptHandler")
    private String scriptHandler;

    @Column(name = "StartMessage")
    private String startMessage;

    @Column(name = "ContinueMessage")
    private String continueMessage;

    @Column(name = "WornOffMessage")
    private String wornOffMessage;


    public int getCustomEffectID() {
        return customEffectID;
    }

    public void setCustomEffectID(int customEffectID) {
        this.customEffectID = customEffectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    public String getScriptHandler() {
        return scriptHandler;
    }

    public void setScriptHandler(String scriptHandler) {
        this.scriptHandler = scriptHandler;
    }

    public String getWornOffMessage() {
        return wornOffMessage;
    }

    public void setWornOffMessage(String wornOffMessage) {
        this.wornOffMessage = wornOffMessage;
    }

    public String getStartMessage() {
        return startMessage;
    }

    public void setStartMessage(String startMessage) {
        this.startMessage = startMessage;
    }

    public String getContinueMessage() {
        return continueMessage;
    }

    public void setContinueMessage(String continueMessage) {
        this.continueMessage = continueMessage;
    }
}
