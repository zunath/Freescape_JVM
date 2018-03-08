package GameSystems.Models;

import org.nwnx.nwnx2.jvm.NWObject;

public class CasterSpellModel {

    private NWObject caster;

    private NWObject target;

    private String effectName;

    private int customEffectID;

    public NWObject getCaster() {
        return caster;
    }

    public void setCaster(NWObject caster) {
        this.caster = caster;
    }

    public NWObject getTarget() {
        return target;
    }

    public void setTarget(NWObject target) {
        this.target = target;
    }

    public String getEffectName() {
        return effectName;
    }

    public void setEffectName(String effectName) {
        this.effectName = effectName;
    }

    public int getCustomEffectID() {
        return customEffectID;
    }

    public void setCustomEffectID(int customEffectID) {
        this.customEffectID = customEffectID;
    }
}
