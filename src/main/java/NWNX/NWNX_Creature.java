package NWNX;

import org.nwnx.nwnx2.jvm.NWObject;

import static NWNX.NWNX_Core.*;

@SuppressWarnings("unused")
public class NWNX_Creature {

    private static final String NWNX_Creature = "NWNX_Creature";

    public static void AddFeat(NWObject creature, int feat)
    {
        String sFunc = "AddFeat";
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, feat);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    public static void AddFeatByLevel(NWObject creature, int feat, int level)
    {
        String sFunc = "AddFeatByLevel";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, feat);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    public static void RemoveFeat(NWObject creature, int feat)
    {
        String sFunc = "RemoveFeat";
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, feat);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    public static int GetKnowsFeat(NWObject creature, int feat)
    {
        String sFunc = "GetKnowsFeat";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, feat);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    public static int GetFeatCountByLevel(NWObject creature, int level)
    {
        String sFunc = "GetFeatCountByLevel";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    public static int GetFeatByLevel(NWObject creature, int level, int index)
    {
        String sFunc = "GetFeatByLevel";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, index);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    public static int GetFeatCount(NWObject creature)
    {
        String sFunc = "GetFeatCount";

        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    public static int GetFeatByIndex(NWObject creature, int index)
    {
        String sFunc = "GetFeatByIndex";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, index);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    public static int GetMeetsFeatRequirements(NWObject creature, int feat)
    {
        String sFunc = "GetMeetsFeatRequirements";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, feat);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    public static SpecialAbilitySlot GetSpecialAbility(NWObject creature, int index)
    {
        String sFunc = "GetSpecialAbility";

        SpecialAbilitySlot ability = new SpecialAbilitySlot();

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, index);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);

        ability.level  = NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
        ability.ready  = NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
        ability.id     = NWNX_GetReturnValueInt(NWNX_Creature, sFunc);

        return ability;
    }

    public static int GetSpecialAbilityCount(NWObject creature)
    {
        String sFunc = "GetSpecialAbilityCount";

        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);
        NWNX_CallFunction(NWNX_Creature, sFunc);

        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    public static void AddSpecialAbility(NWObject creature, SpecialAbilitySlot ability)
    {
        String sFunc = "AddSpecialAbility";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, ability.id);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, ability.ready);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, ability.level);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    public static void RemoveSpecialAbility(NWObject creature, int index)
    {
        String sFunc = "RemoveSpecialAbility";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, index);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    public static void SetSpecialAbility(NWObject creature, int index, SpecialAbilitySlot ability)
    {
        String sFunc = "SetSpecialAbility";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, ability.id);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, ability.ready);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, ability.level);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    public static int GetClassByLevel(NWObject creature, int level)
    {
        String sFunc = "GetClassByLevel";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    public static void SetBaseAC(NWObject creature, int ac)
    {
        String sFunc = "SetBaseAC";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, ac);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    public static int GetBaseAC(NWObject creature)
    {
        String sFunc = "GetBaseAC";

        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    public static void SetRawAbilityScore(NWObject creature, int ability, int value)
    {
        String sFunc = "SetRawAbilityScore";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, value);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, ability);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }


    public static int GetRawAbilityScore(NWObject creature, int ability)
    {
        String sFunc = "GetRawAbilityScore";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, ability);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    public static void NWNX_Creature_ModifyRawAbilityScore(NWObject creature, int ability, int modifier)
    {
        String sFunc = "ModifyRawAbilityScore";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, modifier);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, ability);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    public static MemorizedSpellSlot GetMemorizedSpell(NWObject creature, int characterClass, int level, int index)
    {
        String sFunc = "GetMemorisedSpell";
        MemorizedSpellSlot spell = new MemorizedSpellSlot();

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, index);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, characterClass);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);

        spell.domain = NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
        spell.meta   = NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
        spell.ready  = NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
        spell.id     = NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
        return spell;
    }

    public static int GetMemorizedSpellCountByLevel(NWObject creature, int characterClass, int level)
    {
        String sFunc = "GetMemorisedSpellCountByLevel";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, characterClass);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    public static void SetMemorizedSpell(NWObject creature, int characterClass, int level, int index, MemorizedSpellSlot spell)
    {
        String sFunc = "SetMemorisedSpell";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, spell.id);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, spell.ready);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, spell.meta);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, spell.domain);

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, index);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, characterClass);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    public static int GetRemainingSpellSlots(NWObject creature, int characterClass, int level)
    {
        String sFunc = "GetRemainingSpellSlots";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, characterClass);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    public static void SetRemainingSpellSlots(NWObject creature, int characterClass, int level, int slots)
    {
        String sFunc = "SetRemainingSpellSlots";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, slots);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, characterClass);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    public static int GetKnownSpell(NWObject creature, int characterClass, int level, int index)
    {
        String sFunc = "GetKnownSpell";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, index);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, characterClass);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    public static int GetKnownSpellCount(NWObject creature, int characterClass, int level)
    {
        String sFunc = "GetKnownSpellCount";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, characterClass);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    public static void RemoveKnownSpell(NWObject creature, int characterClass, int level, int spellId)
    {
        String sFunc = "RemoveKnownSpell";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, spellId);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, characterClass);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    public static void AddKnownSpell(NWObject creature, int characterClass, int level, int spellId)
    {
        String sFunc = "AddKnownSpell";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, spellId);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, characterClass);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    public static int GetMaxSpellSlots(NWObject creature, int characterClass, int level)
    {
        String sFunc = "GetMaxSpellSlots";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, characterClass);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }


    public static int GetMaxHitPointsByLevel(NWObject creature, int level)
    {
        String sFunc = "GetMaxHitPointsByLevel";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    public static void SetMaxHitPointsByLevel(NWObject creature, int level, int value)
    {
        String sFunc = "SetMaxHitPointsByLevel";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, value);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    public static void SetMovementRate(NWObject creature, int rate)
    {
        String sFunc = "SetMovementRate";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, rate);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }
    public static void SetAlignmentGoodEvil(NWObject creature, int value)
    {
        String sFunc = "SetAlignmentGoodEvil";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, value);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    public static void SetAlignmentLawChaos(NWObject creature, int value)
    {
        String sFunc = "SetAlignmentLawChaos";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, value);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    public static int GetWizardSpecialization(NWObject creature)
    {
        String sFunc = "GetWizardSpecialization";

        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    public static void SetWizardSpecialization(NWObject creature, int school)
    {
        String sFunc = "SetWizardSpecialization";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, school);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    public static int GetSoundset(NWObject creature)
    {
        String sFunc = "GetSoundset";

        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    public static void SetSoundset(NWObject creature, int soundset)
    {
        String sFunc = "SetSoundset";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, soundset);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    public static void SetSkillRank(NWObject creature, int skill, int rank)
    {
        String sFunc = "SetSkillRank";
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, rank);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, skill);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    public static void SetClassByPosition(NWObject creature, int position, int classID)
    {
        String sFunc = "SetClassByPosition";
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, classID);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, position);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

}
