package NWNX;

import org.nwnx.nwnx2.jvm.NWObject;

import static NWNX.NWNX_Core.*;
import static org.nwnx.nwnx2.jvm.NWScript.writeTimestampedLogEntry;

@SuppressWarnings("unused")
public class NWNX_Creature {
    private static final String NWNX_Creature = "NWNX_Creature";

    // Gives the provided creature the provided feat.
    public static void AddFeat(NWObject creature, int feat) {
        String sFunc = "AddFeat";
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, feat);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Gives the provided creature the provided feat.
// Adds the feat to the stat list at the provided level.
    public static void AddFeatByLevel(NWObject creature, int feat, int level) {
        String sFunc = "AddFeatByLevel";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, feat);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Removes from the provided creature the provided feat.
    public static void RemoveFeat(NWObject creature, int feat) {
        String sFunc = "RemoveFeat";
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, feat);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    public static int GetKnowsFeat(NWObject creature, int feat) {
        String sFunc = "GetKnowsFeat";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, feat);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    // Returns the count of feats learned at the provided level.
    public static int GetFeatCountByLevel(NWObject creature, int level) {
        String sFunc = "GetFeatCountByLevel";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    // Returns the feat learned at the provided level at the provided index.
// Index bounds: 0 <= index < GetFeatCountByLevel(creature, level).
    public static int GetFeatByLevel(NWObject creature, int level, int index) {
        String sFunc = "GetFeatByLevel";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, index);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    // Returns the total number of feats known by creature
    public static int GetFeatCount(NWObject creature) {
        String sFunc = "GetFeatCount";

        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    // Returns the creature's feat at a given index
// Index bounds: 0 <= index < GetFeatCount(creature);
    public static int GetFeatByIndex(NWObject creature, int index) {
        String sFunc = "GetFeatByIndex";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, index);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    // Returns TRUE if creature meets all requirements to take given feat
    public static int GetMeetsFeatRequirements(NWObject creature, int feat) {
        String sFunc = "GetMeetsFeatRequirements";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, feat);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    // Returns the special ability of the provided creature at the provided index.
// Index bounds: 0 <= index < GetSpecialAbilityCount(creature).
    SpecialAbilitySlot GetSpecialAbility(NWObject creature, int index) {
        String sFunc = "GetSpecialAbility";

        SpecialAbilitySlot ability = new SpecialAbilitySlot();

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, index);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);

        ability.level = NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
        ability.ready = NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
        ability.id = NWNX_GetReturnValueInt(NWNX_Creature, sFunc);

        return ability;
    }

    // Returns the count of special ability count of the provided creature.
    public static int GetSpecialAbilityCount(NWObject creature) {
        String sFunc = "GetSpecialAbilityCount";

        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);
        NWNX_CallFunction(NWNX_Creature, sFunc);

        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    // Adds the provided special ability to the provided creature.
    public static void AddSpecialAbility(NWObject creature, SpecialAbilitySlot ability) {
        String sFunc = "AddSpecialAbility";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, ability.id);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, ability.ready);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, ability.level);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Removes the provided special ability from the provided creature.
// Index bounds: 0 <= index < GetSpecialAbilityCount(creature).
    public static void RemoveSpecialAbility(NWObject creature, int index) {
        String sFunc = "RemoveSpecialAbility";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, index);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Sets the special ability at the provided index for the provided creature to the provided ability.
// Index bounds: 0 <= index < GetSpecialAbilityCount(creature).
    public static void SetSpecialAbility(NWObject creature, int index, SpecialAbilitySlot ability) {
        String sFunc = "SetSpecialAbility";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, ability.id);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, ability.ready);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, ability.level);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Returns the classId taken by the provided creature at the provided level.
    public static int GetClassByLevel(NWObject creature, int level) {
        String sFunc = "GetClassByLevel";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    // Sets the base AC for the provided creature.
    public static void SetBaseAC(NWObject creature, int ac) {
        String sFunc = "SetBaseAC";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, ac);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Returns the base AC for the provided creature.
    public static int GetBaseAC(NWObject creature) {
        String sFunc = "GetBaseAC";

        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    // DEPRECATED. Please use SetRawAbilityScore now. This will be removed in future NWNX releases.
// Sets the provided ability score of provided creature to the provided value.
    public static void SetAbilityScore(NWObject creature, int ability, int value) {
        writeTimestampedLogEntry("NWNX_Creature: SetAbilityScore() is deprecated. Use native SetRawAbilityScore() instead");
        SetRawAbilityScore(creature, ability, value);
    }

    // Sets the provided ability score of provided creature to the provided value. Does not apply racial bonuses/penalties.
    public static void SetRawAbilityScore(NWObject creature, int ability, int value) {
        String sFunc = "SetRawAbilityScore";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, value);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, ability);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Gets the provided ability score of provided creature. Does not apply racial bonuses/penalties.
    public static int GetRawAbilityScore(NWObject creature, int ability) {
        String sFunc = "GetRawAbilityScore";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, ability);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    // Adjusts the provided ability score of a provided creature. Does not apply racial bonuses/penalties.
    public static void ModifyRawAbilityScore(NWObject creature, int ability, int modifier) {
        String sFunc = "ModifyRawAbilityScore";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, modifier);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, ability);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Gets the memorized spell of the provided creature for the provided class, level, and index.
// Index bounds: 0 <= index < GetMemorizedSpellCountByLevel(creature, class, level).
    MemorizedSpellSlot GetMemorizedSpell(NWObject creature, int classId, int level, int index) {
        String sFunc = "GetMemorisedSpell";
        MemorizedSpellSlot spell = new MemorizedSpellSlot();

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, index);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, classId);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);

        spell.domain = NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
        spell.meta = NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
        spell.ready = NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
        spell.id = NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
        return spell;
    }

    // Gets the count of memorized spells of the provided classId and level belonging to the provided creature.
    public static int GetMemorizedSpellCountByLevel(NWObject creature, int classId, int level) {
        String sFunc = "GetMemorisedSpellCountByLevel";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, classId);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    // Sets the memorized spell of the provided creature for the provided class, level, and index.
    // Index bounds: 0 <= index < GetMemorizedSpellCountByLevel(creature, class, level).
    public static void SetMemorizedSpell(NWObject creature, int classId, int level, int index, MemorizedSpellSlot spell) {
        String sFunc = "SetMemorisedSpell";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, spell.id);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, spell.ready);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, spell.meta);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, spell.domain);

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, index);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, classId);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Gets the remaining spell slots (innate casting) for the provided creature for the provided classId and level.
    public static int GetRemainingSpellSlots(NWObject creature, int classId, int level) {
        String sFunc = "GetRemainingSpellSlots";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, classId);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    // Sets the remaining spell slots (innate casting) for the provided creature for the provided classId and level.
    public static void SetRemainingSpellSlots(NWObject creature, int classId, int level, int slots) {
        String sFunc = "SetRemainingSpellSlots";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, slots);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, classId);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Get the spell at index in level in creature's spellbook from class.
    public static int GetKnownSpell(NWObject creature, int classId, int level, int index) {
        String sFunc = "GetKnownSpell";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, index);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, classId);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    public static int GetKnownSpellCount(NWObject creature, int classId, int level) {
        String sFunc = "GetKnownSpellCount";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, classId);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    // Remove a spell from creature's spellbook for class.
    public static void RemoveKnownSpell(NWObject creature, int classId, int level, int spellId) {
        String sFunc = "RemoveKnownSpell";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, spellId);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, classId);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Add a new spell to creature's spellbook for class.
    public static void AddKnownSpell(NWObject creature, int classId, int level, int spellId) {
        String sFunc = "AddKnownSpell";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, spellId);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, classId);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Gets the maximum count of spell slots for the proivded creature for the provided classId and level.
    public static int GetMaxSpellSlots(NWObject creature, int classId, int level) {
        String sFunc = "GetMaxSpellSlots";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, classId);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    // Gets the maximum hit points for creature for level.
    public static int GetMaxHitPointsByLevel(NWObject creature, int level) {
        String sFunc = "GetMaxHitPointsByLevel";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    // Sets the maximum hit points for creature for level to nValue.
    public static void SetMaxHitPointsByLevel(NWObject creature, int level, int value) {
        String sFunc = "SetMaxHitPointsByLevel";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, value);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Set creature's movement rate.
    public static void SetMovementRate(NWObject creature, int rate) {
        String sFunc = "SetMovementRate";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, rate);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Set creature's raw good/evil alignment value.
    public static void SetAlignmentGoodEvil(NWObject creature, int value) {
        String sFunc = "SetAlignmentGoodEvil";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, value);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Set creature's raw law/chaos alignment value.
    public static void SetAlignmentLawChaos(NWObject creature, int value) {
        String sFunc = "SetAlignmentLawChaos";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, value);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Gets one of creature's cleric domains (either 1 or 2).
    public static int GetClericDomain(NWObject creature, int index) {
        String sFunc = "GetClericDomain";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, index);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    // Sets one of creature's cleric domains (either 1 or 2).
    public static void SetClericDomain(NWObject creature, int index, int domain) {
        String sFunc = "SetClericDomain";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, domain);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, index);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Gets whether or not creature has a specialist school of wizardry.
    public static int GetWizardSpecialization(NWObject creature) {
        String sFunc = "GetWizardSpecialization";

        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    // Sets creature's wizard specialist school.
    public static void SetWizardSpecialization(NWObject creature, int school) {
        String sFunc = "SetWizardSpecialization";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, school);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Get the soundset index for creature.
    public static int GetSoundset(NWObject creature) {
        String sFunc = "GetSoundset";

        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    // Set the soundset index for creature.
    public static void SetSoundset(NWObject creature, int soundset) {
        String sFunc = "SetSoundset";

        NWNX_PushArgumentInt(NWNX_Creature, sFunc, soundset);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Set the base ranks in a skill for creature
    public static void SetSkillRank(NWObject creature, int skill, int rank) {
        String sFunc = "SetSkillRank";
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, rank);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, skill);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Set the classId ID in a particular position for a creature.
// Position should be 0, 1, or 2.
// ClassID should be a valid ID number in classes.2da and be between 0 and 255.
    public static void SetClassByPosition(NWObject creature, int position, int classId) {
        String sFunc = "SetClassByPosition";
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, classId);
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, position);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Set creature's base attack bonus (BAB)
// Modifying the BAB will also affect the creature's attacks per round and its
// eligability for feats, prestige classes, etc.
// The BAB value should be between 0 and 254.
// Setting BAB to 0 will cause the creature to revert to its original BAB based
// on its classes and levels. A creature can never have an actual BAB of zero.
// NOTE: The base game has a function SetBaseAttackBonus(), which actually sets
//       the bonus attacks per round for a creature, not the BAB.
    public static void SetBaseAttackBonus(NWObject creature, int bab) {
        String sFunc = "SetBaseAttackBonus";
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, bab);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Gets the creatures current attacks per round (using equipped weapon)
// bBaseAPR - If true, will return the base attacks per round, based on BAB and
//            equipped weapons, regardless of overrides set by
//            calls to SetBaseAttackBonus() builtin function.
    public static int GetAttacksPerRound(NWObject creature, int bBaseAPR) {
        String sFunc = "GetAttacksPerRound";
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, bBaseAPR);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    // Sets the creature gender
    public static void SetGender(NWObject creature, int gender) {
        String sFunc = "SetGender";
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, gender);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Restore all creature feat uses
    public static void RestoreFeats(NWObject creature) {
        String sFunc = "RestoreFeats";
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Restore all creature special ability uses
    public static void RestoreSpecialAbilities(NWObject creature) {
        String sFunc = "RestoreSpecialAbilities";
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Restore all creature spells per day for given level.
// If level is -1, all spells are restored
    public static void RestoreSpells(NWObject creature, int level) {
        String sFunc = "RestoreSpells";
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, level);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Restore uses for all items carried by the creature
    public static void RestoreItems(NWObject creature) {
        String sFunc = "RestoreItems";
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Sets the creature size. Use CREATURE_SIZE_* constants
    public static void SetSize(NWObject creature, int size) {
        String sFunc = "SetSize";
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, size);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Gets the creature's remaining unspent skill points
    public static int GetSkillPointsRemaining(NWObject creature) {
        String sFunc = "GetSkillPointsRemaining";
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }


    // Sets the creature's remaining unspent skill points
    public static void SetSkillPointsRemaining(NWObject creature, int skillpoints) {
        String sFunc = "SetSkillPointsRemaining";
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, skillpoints);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Sets the creature's racial type
    public static void SetRacialType(NWObject creature, int racialtype) {
        String sFunc = "SetRacialType";
        NWNX_PushArgumentInt(NWNX_Creature, sFunc, racialtype);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

    // Returns the creature's current movement type (MOVEMENT_TYPE_*)
    public static int GetMovementType(NWObject creature) {
        String sFunc = "GetMovementType";
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
        return NWNX_GetReturnValueInt(NWNX_Creature, sFunc);
    }

    // Sets the maximum movement rate a creature can have while walking (not running)
// This allows a creature with movement speed enhancemens to walk at a normal rate.
// Setting the value to -1.0 will remove the cap.
// Default value is 2000.0, which is the base human walk speed.
    public static void SetWalkRateCap(NWObject creature, float fWalkRate) {
        String sFunc = "SetWalkRateCap";
        NWNX_PushArgumentFloat(NWNX_Creature, sFunc, fWalkRate);
        NWNX_PushArgumentObject(NWNX_Creature, sFunc, creature);

        NWNX_CallFunction(NWNX_Creature, sFunc);
    }

}
