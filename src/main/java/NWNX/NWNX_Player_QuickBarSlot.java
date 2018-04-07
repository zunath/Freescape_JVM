package NWNX;

import org.nwnx.nwnx2.jvm.NWObject;

public class NWNX_Player_QuickBarSlot {

    // Create an empty QBS of given type
    public static QuickBarSlot Empty(int type)
    {
        QuickBarSlot qbs = new QuickBarSlot();

        qbs.nObjectType    = type;

        qbs.oItem          = NWObject.INVALID;
        qbs.oSecondaryItem = NWObject.INVALID;
        qbs.nMultiClass    = 0;
        qbs.sResRef        = "";
        qbs.sCommandLabel  = "";
        qbs.sCommandLine   = "";
        qbs.sToolTip       = "";
        qbs.nINTParam1     = 0;
        qbs.nMetaType      = 0;
        qbs.nDomainLevel   = 0;
        qbs.nAssociateType = 0;
        qbs.oAssociate     = NWObject.INVALID;

        return qbs;
    }

    // Create a QBS for using an item
    public static QuickBarSlot UseItem(NWObject oItem, int nPropertyID)
    {
        QuickBarSlot qbs = Empty(QuickBarSlotType.ITEM);

        qbs.oItem          = oItem;
        qbs.nINTParam1     = nPropertyID;

        return qbs;
    }

    // Create a QBS for equipping an item
    public static QuickBarSlot EquipItem(NWObject oItem, NWObject oSecondaryItem)
    {
        QuickBarSlot qbs = Empty(QuickBarSlotType.ITEM);

        qbs.oItem          = oItem;
        qbs.oSecondaryItem = oSecondaryItem;

        return qbs;
    }

    // Create a QBS for casting a spell
    public static QuickBarSlot CastSpell(int nSpell, int nClassIndex, int nMetamagic, int nDomainLevel)
    {
        QuickBarSlot qbs = Empty(QuickBarSlotType.SPELL);

        qbs.nINTParam1     = nSpell;
        qbs.nMultiClass    = nClassIndex;
        qbs.nMetaType      = nMetamagic;
        qbs.nDomainLevel   = nDomainLevel;

        return qbs;
    }

    // Create a QBS for using a skill
    public static QuickBarSlot UseSkill(int nSkill)
    {
        QuickBarSlot qbs = Empty(QuickBarSlotType.SKILL);

        qbs.nINTParam1     = nSkill;

        return qbs;
    }

    // Create a QBS for using a feat
    public static QuickBarSlot UseFeat(int nFeat)
    {
        QuickBarSlot qbs = Empty(QuickBarSlotType.FEAT);

        qbs.nINTParam1     = nFeat;

        return qbs;
    }

    // Create a QBS for starting a dialog
    public static QuickBarSlot StartDialog()
    {
        return Empty(QuickBarSlotType.DIALOG);
    }

    // Create a QBS for attacking
    public static QuickBarSlot Attack()
    {
        return Empty(QuickBarSlotType.ATTACK);
    }

    // Create a QBS for emoting
    public static QuickBarSlot Emote(int nEmote)
    {
        QuickBarSlot qbs = Empty(QuickBarSlotType.EMOTE);

        qbs.nINTParam1     = nEmote;

        return qbs;
    }

    // Create a QBS for toggling a mode
    public static QuickBarSlot ToggleMode(int nMode)
    {
        QuickBarSlot qbs = Empty(QuickBarSlotType.MODE_TOGGLE);

        qbs.nINTParam1     = nMode;

        return qbs;
    }

    // Create a QBS for examining
    public static QuickBarSlot Examine()
    {
        return Empty(QuickBarSlotType.EXAMINE);
    }

    // Create a QBS for bartering
    public static QuickBarSlot Barter()
    {
        return Empty(QuickBarSlotType.BARTER);
    }

    // Create a QBS for quickchat command
    public static QuickBarSlot QuickChat(int nCommand)
    {
        QuickBarSlot qbs = Empty(QuickBarSlotType.QUICK_CHAT);

        qbs.nINTParam1     = nCommand;

        return qbs;
    }

    // Create a QBS for possessing a familiar
    public static QuickBarSlot PossessFamiliar()
    {
        return Empty(QuickBarSlotType.POSSESS_FAMILIAR);
    }

    // Create a QBS for casting a spell
    public static QuickBarSlot UseSpecialAbility(int nSpell, int nCasterLevel)
    {
        QuickBarSlot qbs = Empty(QuickBarSlotType.SPELL);

        qbs.nINTParam1     = nSpell;
        qbs.nDomainLevel   = nCasterLevel;

        return qbs;
    }

}
