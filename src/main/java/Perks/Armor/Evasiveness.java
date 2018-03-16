package Perks.Armor;

import Enumerations.CustomItemType;
import Enumerations.PerkID;
import GameObject.ItemGO;
import GameSystems.PerkSystem;
import Perks.IPerk;
import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.*;

public class Evasiveness implements IPerk {
    @Override
    public boolean CanCastSpell(NWObject oPC, NWObject oTarget) {
        NWObject armor = NWScript.getItemInSlot(InventorySlot.CHEST, oPC);
        ItemGO itemGO = new ItemGO(armor);
        return itemGO.getCustomItemType() == CustomItemType.LightArmor;
    }

    @Override
    public String CannotCastSpellMessage() {
        return "You must be equipped with light armor to use that combat ability.";
    }

    @Override
    public int ManaCost(NWObject oPC, int baseManaCost) {
        return baseManaCost;
    }

    @Override
    public float CastingTime(NWObject oPC, float baseCastingTime) {
        return baseCastingTime;
    }

    @Override
    public float CooldownTime(NWObject oPC, float baseCooldownTime) {
        return baseCooldownTime;
    }

    @Override
    public void OnImpact(NWObject oPC, NWObject oTarget) {
        int perkLevel = PerkSystem.GetPCPerkLevel(oPC, PerkID.Evasiveness);
        int concealment;
        float length;

        switch(perkLevel)
        {
            case 1:
                concealment = 10;
                length = 12.0f;
                break;
            case 2:
                concealment = 15;
                length = 12.0f;
                break;
            case 3:
                concealment = 20;
                length = 12.0f;
                break;
            case 4:
                concealment = 25;
                length = 12.0f;
                break;
            case 5:
                concealment = 30;
                length = 18.0f;
                break;
            default:
                return;
        }

        NWEffect effect = NWScript.effectConcealment(concealment, MissChanceType.NORMAL);
        NWScript.applyEffectToObject(DurationType.TEMPORARY, effect, oPC, length);

        effect = NWScript.effectVisualEffect(VfxDurAura.CYAN, false);
        NWScript.applyEffectToObject(DurationType.TEMPORARY, effect, oPC, length);

        effect = NWScript.effectVisualEffect(VfxImp.AC_BONUS, false);
        NWScript.applyEffectToObject(DurationType.INSTANT, effect, oPC, 0.0f);
    }

    @Override
    public void OnPurchased(NWObject oPC, int newLevel) {

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
