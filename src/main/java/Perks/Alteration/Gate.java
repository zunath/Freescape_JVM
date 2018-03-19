package Perks.Alteration;

import Bioware.AddItemPropertyPolicy;
import Bioware.XP2;
import Enumerations.SkillID;
import GameObject.ItemGO;
import GameSystems.DeathSystem;
import GameSystems.SkillSystem;
import Perks.IPerk;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.*;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class Gate implements IPerk {
    @Override
    public boolean CanCastSpell(NWObject oPC, NWObject oTarget) {
        if(oPC.equals(oTarget)) return true;

        NWObject[] members = getFactionMembers(oPC, true);
        for(NWObject member: members)
        {
            if(oTarget.equals(member))
                return true;
        }
        return false;
    }

    @Override
    public String CannotCastSpellMessage() {
        return "Only party members may be targeted with that ability.";
    }

    @Override
    public int ManaCost(NWObject oPC, int baseManaCost) {
        return baseManaCost;
    }

    @Override
    public float CastingTime(NWObject oPC, float baseCastingTime) {
        int wisdom = NWScript.getAbilityModifier(Ability.WISDOM, oPC);
        int intelligence = NWScript.getAbilityModifier(Ability.INTELLIGENCE, oPC);
        float castingTime = baseCastingTime - ((wisdom * 1.5f) + (intelligence));

        if(castingTime <= 2.0f) castingTime = 2.0f;
        return castingTime;
    }

    @Override
    public float CooldownTime(NWObject oPC, float baseCooldownTime) {
        return baseCooldownTime;
    }

    @Override
    public void OnImpact(NWObject oPC, NWObject oTarget) {
        NWLocation location = getLocation(oTarget);
        NWEffect effect = effectVisualEffect(Vfx.IMP_UNSUMMON, false);

        DeathSystem.TeleportPlayerToBindPoint(oTarget);
        applyEffectAtLocation(DurationType.INSTANT, effect, location, 0.0f);

        SkillSystem.GiveSkillXP(oPC, SkillID.AlterationMagic, 100);
    }

    @Override
    public void OnPurchased(NWObject oPC, int newLevel) {

        // Party members become targetable at level 2.
        if(newLevel >= 2)
        {
            NWObject token = getItemPossessedBy(oPC, "perk_gate");
            ItemGO itemGO = new ItemGO(token);
            itemGO.stripAllItemProperties();
            NWItemProperty ip = itemPropertyCastSpell(IpConst.CASTSPELL_UNIQUE_POWER, IpConstCastspell.NUMUSES_UNLIMITED_USE);
            XP2.IPSafeAddItemProperty(token, ip, 0.0f, AddItemPropertyPolicy.ReplaceExisting, false, false);
        }
    }

    @Override
    public void OnRemoved(NWObject oPC) {

    }

    @Override
    public void OnItemEquipped(NWObject oPC, NWObject oItem) {

    }

    @Override
    public void OnItemUnequipped(NWObject oPC, NWObject oItem) {

    }

    @Override
    public boolean IsHostile() {
        return false;
    }
}
