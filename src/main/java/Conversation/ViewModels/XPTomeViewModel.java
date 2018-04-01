package Conversation.ViewModels;

import org.nwnx.nwnx2.jvm.NWObject;

public class XPTomeViewModel {

    private NWObject item;

    private int skillID;

    public NWObject getItem() {
        return item;
    }

    public void setItem(NWObject item) {
        this.item = item;
    }

    public int getSkillID() {
        return skillID;
    }

    public void setSkillID(int skillID) {
        this.skillID = skillID;
    }
}
