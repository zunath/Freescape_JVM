package GameSystems;

import Helper.ColorToken;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.BaseItem;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class DurabilitySystem {
    private static final String MaxDurabilityVariable = "DURABILITY_MAX";
    private static final String CurrentDurabilityVariable = "DURABILITY_CURRENT";
    private static final String InitializedDurabilityVariable = "DURABILITY_INITIALIZED";

    public static void OnModuleEquip() {
        NWObject oPC = NWScript.getPCItemLastEquippedBy();
        final NWObject oItem = NWScript.getPCItemLastEquipped();
        float durability = GetItemDurability(oItem);

        if (durability <= 0 && durability != -1) {
            Scheduler.assign(oPC, () -> {
                NWScript.clearAllActions(false);
                NWScript.actionUnequipItem(oItem);
            });

            NWScript.floatingTextStringOnCreature(ColorToken.Red() + "That item is broken and must be repaired before you can use it." + ColorToken.End(), oPC, false);
        }
    }

    public static String OnModuleExamine(String existingDescription, NWObject examinedObject) {
        if(!GetValidDurabilityTypes().contains(NWScript.getBaseItemType(examinedObject))) return existingDescription;

        String description = ColorToken.Orange() + "Durability: " + ColorToken.End();
        float durability = GetItemDurability(examinedObject);
        if(durability <= 0.0f) description += ColorToken.Red() + durability + ColorToken.End();
        else description += ColorToken.White() + FormatDurability(durability) + ColorToken.End();

        description += ColorToken.White() + " / " + GetMaxItemDurability(examinedObject) + ColorToken.End();

        return existingDescription + "\n\n" + description;
    }

    public static void RunItemDecay(NWObject oPC, NWObject oItem) {
        // Item decay doesn't run for any items if Invincible is in effect
        // Or if the item is unbreakable (e.g profession items)
        // Or if the item is not part of the valid list of base item types
        if (NWScript.getPlotFlag(oPC) ||
            NWScript.getLocalInt(oItem, "UNBREAKABLE") == 1 ||
            !GetValidDurabilityTypes().contains(NWScript.getBaseItemType(oItem)))
            return;

        float durability = GetItemDurability(oItem);
        String sItemName = NWScript.getName(oItem, true);

        // Reduce by 0.001 each time it's run. Player only receives notifications when it drops a full point.
        // I.E: Dropping from 29.001 to 29.
        // Note that players only see two decimal places in-game on purpose.
        durability -= 0.001f;
        boolean displayMessage = durability % 1 == 0;

        if(displayMessage)
        {
            NWScript.sendMessageToPC(oPC, ColorToken.Red() + "Your " + sItemName + " has been damaged. (" + FormatDurability(durability) + " / " + GetMaxItemDurability(oItem) + ColorToken.End());
        }

        if(durability <= 0.00f)
        {
            NWObject oCopy = NWScript.copyItem(oItem, oPC, true);
            NWScript.destroyObject(oItem, 0.0f);
            SetItemDurability(oCopy, 0);
            NWScript.sendMessageToPC(oPC, ColorToken.Red() + "Your " + sItemName + " has broken!" + ColorToken.End());
        }
        else {
            SetItemDurability(oItem, durability);
        }
    }

    private static String FormatDurability(float durability)
    {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        return df.format(durability);
    }

    public static void RunItemRepair(NWObject oPC, NWObject oItem, float amount) {
        // Prevent repairing for less than 0.01
        if (amount < 0.01f) return;

        // Item has no durability - inform player they can't repair it
        if (GetItemDurability(oItem) == -1) {
            NWScript.sendMessageToPC(oPC, ColorToken.Red() + "You cannot repair that item." + ColorToken.End());
            return;
        }

        SetItemDurability(oItem, GetItemDurability(oItem) + amount);
        String durMessage = FormatDurability(GetItemDurability(oItem)) + " / " + GetMaxItemDurability(oItem);
        NWScript.sendMessageToPC(oPC, ColorToken.Green() + "You repaired your " + NWScript.getName(oItem, true) + ". (" + durMessage + ")" + ColorToken.End());
    }

    public static int GetMaxItemDurability(NWObject item)
    {
        int itemType = NWScript.getBaseItemType(item);
        if(!DurabilitySystem.GetValidDurabilityTypes().contains(itemType))
            return -1;

        return NWScript.getLocalInt(item, MaxDurabilityVariable) <= 0 ? 30 : NWScript.getLocalInt(item, MaxDurabilityVariable);
    }

    public static float GetItemDurability(NWObject item)
    {
        int itemType = NWScript.getBaseItemType(item);
        if(!DurabilitySystem.GetValidDurabilityTypes().contains(itemType))
            return -1;
        InitializeDurability(item);

        return NWScript.getLocalFloat(item, CurrentDurabilityVariable);
    }

    public static void SetItemDurability(NWObject item, float value)
    {
        int itemType = NWScript.getBaseItemType(item);
        if(!DurabilitySystem.GetValidDurabilityTypes().contains(itemType))
            return;
        InitializeDurability(item);

        int maxDurability = GetMaxItemDurability(item);
        if(maxDurability == -1) maxDurability = 30;

        if(value > maxDurability) value = maxDurability;
        else if(value < 0) value = 0;

        NWScript.setLocalFloat(item, CurrentDurabilityVariable, value);
    }

    private static void InitializeDurability(NWObject item)
    {
        if(!DurabilitySystem.GetValidDurabilityTypes().contains(NWScript.getBaseItemType(item))) return;

        // If item hasn't been initialized and doesn't have a current durability value,
        // set its current value to the maximum amount. Then mark it as initialized so this doesn't fire again.
        if(NWScript.getLocalInt(item, InitializedDurabilityVariable) <= 0 &&
                NWScript.getLocalFloat(item, CurrentDurabilityVariable) <= 0.0f)
        {
            int maxDurability = GetMaxItemDurability(item);
            maxDurability = maxDurability <= 0 ? 30 : maxDurability;
            NWScript.setLocalFloat(item, CurrentDurabilityVariable, maxDurability);
        }
        NWScript.setLocalInt(item, InitializedDurabilityVariable, 1);
    }

    private static List<Integer> GetValidDurabilityTypes() {
        Integer[] result = {
                BaseItem.ARMOR,
                BaseItem.BASTARDSWORD,
                BaseItem.BATTLEAXE,
                BaseItem.BELT,
                BaseItem.BOOTS,
                BaseItem.BRACER,
                BaseItem.CLOAK,
                BaseItem.CLUB,
                BaseItem.DAGGER,
                BaseItem.DIREMACE,
                BaseItem.DOUBLEAXE,
                BaseItem.DWARVENWARAXE,
                BaseItem.GLOVES,
                BaseItem.GREATAXE,
                BaseItem.GREATSWORD,
                BaseItem.HALBERD,
                BaseItem.HANDAXE,
                BaseItem.HEAVYCROSSBOW,
                BaseItem.HEAVYFLAIL,
                BaseItem.HELMET,
                BaseItem.KAMA,
                BaseItem.KATANA,
                BaseItem.KUKRI,
                BaseItem.LARGESHIELD,
                BaseItem.LIGHTCROSSBOW,
                BaseItem.LIGHTFLAIL,
                BaseItem.LIGHTHAMMER,
                BaseItem.LIGHTMACE,
                BaseItem.LONGBOW,
                BaseItem.LONGSWORD,
                BaseItem.MORNINGSTAR,
                BaseItem.QUARTERSTAFF,
                BaseItem.RAPIER,
                BaseItem.SCIMITAR,
                BaseItem.SCYTHE,
                BaseItem.SHORTBOW,
                BaseItem.SHORTSPEAR,
                BaseItem.SHORTSWORD,
                BaseItem.SHURIKEN,
                BaseItem.SICKLE,
                BaseItem.SLING,
                BaseItem.SMALLSHIELD,
                BaseItem.TOWERSHIELD,
                BaseItem.TRIDENT,
                BaseItem.TWOBLADEDSWORD,
                BaseItem.WARHAMMER,
                BaseItem.WHIP
        };

        return Arrays.asList(result);
    }

    public static void OnHitCastSpell(NWObject oTarget)
    {
        NWObject oSpellOrigin = NWScript.getSpellCastItem();
        int itemType = NWScript.getBaseItemType(oSpellOrigin);
        // Durability system
        if(DurabilitySystem.GetValidDurabilityTypes().contains(itemType))
        {
            DurabilitySystem.RunItemDecay(oTarget, oSpellOrigin);
        }
    }

}
