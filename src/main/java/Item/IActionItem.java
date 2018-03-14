package Item;

import org.nwnx.nwnx2.jvm.NWObject;

public interface IActionItem {
    Object StartUseItem(NWObject user, NWObject item, NWObject target);
    void ApplyEffects(NWObject user, NWObject item, NWObject target, Object customData);
    float Seconds(NWObject user, NWObject item, NWObject target, Object customData);
    boolean FaceTarget();
    int AnimationID();
    float MaxDistance();
    boolean ReducesItemCharge(NWObject user, NWObject item, NWObject target, Object customData);
    String IsValidTarget(NWObject user, NWObject item, NWObject target);
}
