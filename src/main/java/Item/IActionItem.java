package Item;

import org.nwnx.nwnx2.jvm.NWObject;

public interface IActionItem {
    Object StartUseItem(NWObject user, NWObject item, NWObject target);
    void ApplyEffects(NWObject user, NWObject item, NWObject target, Object customData);
    float Seconds(NWObject user, NWObject item, NWObject target);
    boolean FaceTarget();
    int AnimationID();
    float MaxDistance();
    String IsValidTarget(NWObject user, NWObject item, NWObject target);
}
