package GameSystems;

import Bioware.Position;
import Data.Repository.PlayerRepository;
import Entities.PlayerEntity;
import Enumerations.AbilityType;
import Enumerations.CustomAnimationType;
import Enumerations.GunType;
import Enumerations.ProfessionType;
import GameObject.GunGO;
import GameObject.PlayerGO;
import Helper.ColorToken;
import NWNX.NWNX_Events;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.*;
import org.nwnx.nwnx2.jvm.constants.Action;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class CombatSystem {

    // CONSTANTS
    private final String CBT_OVR_SVAR_ATTACK_ALLOWED = "ATTACK_ALLOWED";
    private final String CBT_OVR_SVAR_ATTACK_TARGET = "ATTACK_TARGET";
    private final String CBT_OVR_SVAR_ATTACK_SHOTS_TO_FIRE = "ATTACK_SHOTS_TO_FIRE";
    private final String CBT_OVR_SVAR_ATTACK_SILENCED = "ATTACK_SILENCED";
    private final String CBT_OVR_SVAR_ATTACK_ANIMATION = "ATTACK_ANIMATION";

    private final int CBT_OVR_ANIMATION_NONE    = 0;
    private final int CBT_OVR_SOUND_SHOT_HEAVY_PISTOL_9MM = 8205;
    private final int CBT_OVR_SOUND_SHOT_SMG_762 = 8211;
    private final int CBT_OVR_SOUND_SHOT_ASSAULT_RIFLE_762 = 8213;
    private final int CBT_OVR_SOUND_SHOT_SHOTGUN_12G = 8218;
    private final int CBT_OVR_SOUND_SHOT_SILENCED_PISTOL = 8224;
    private final int CBT_OVR_SOUND_SHOT_SILENCED_RIFLE = 8225;

    private final String GUN_LOW_AMMO_MESSAGE = "Low ammo!";
    private final String GUN_OUT_OF_AMMO_MESSAGE = "Out of ammo!";

    private final String GUN_TEMP_PREVENT_AMMO_BOX_BUG = "GUN_TEMP_PREVENT_AMMO_BOX_BUG";
    private final float GUN_LOW_AMMO_MESSAGE_DISPLAY_PERCENTAGE = 0.20f;
    private final String GUN_MAGAZINE_BULLET_COUNT = "GUN_MAGAZINE_BULLET_COUNT";
    private final int IP_CONST_FIREARM_RANGE_NEAR          = 1;
    private final int IP_CONST_FIREARM_RANGE_MID           = 2;
    private final int IP_CONST_FIREARM_RANGE_FAR           = 3;


    private final float GUN_RANGE_NEAR_MINIMUM = 3.5f;
    private final float GUN_RANGE_NEAR_MAXIMUM = 6.0f;
    private final float GUN_RANGE_MID_MINIMUM  = 5.5f;
    private final float GUN_RANGE_MID_MAXIMUM  = 8.5f;
    private final float GUN_RANGE_FAR_MINIMUM  = 14.0f;
    private final float GUN_RANGE_FAR_MAXIMUM  = 27.5f;

    private final String GUN_TEMP_AMMO_LOADED_TYPE     = "GUN_TEMP_AMMO_LOADED_TYPE";

    private final int GUN_AMMUNITION_PRIORITY_BASIC = 0;
    private final int GUN_AMMUNITION_PRIORITY_ENHANCED = 1;
    private final int GUN_AMMUNITION_PRIORITY_INCENDIARY = 2;
    private final String GUN_GAS_CANISTER_TAG = "gas_canister";
    private final String GUN_TEMP_GUN_EQUIPPED = "GUN_EQUIPPED";
    private final String GUN_FIREARM_MAGAZINE_RESREF = "firearm_magazine";
    private final String GUN_ENHANCED_FIREARM_MAGAZINE_RESREF = "e_gun_mag";
    private final String GUN_INCENDIARY_FIREARM_MAGAZINE_RESREF = "i_gun_mag";
    private final String GUN_TEMP_AMMO_TYPE = "AMMO_TYPE";
    private final String GUN_TEMP_UNLOADING_AMMO = "UNLOADING_AMMO";
    private final String GUN_NORMAL_AMMO_BOX_PREFIX = "ammo_box_";
    private final String GUN_ENHANCED_AMMO_BOX_PREFIX = "e_ammo_box_";
    private final String GUN_INCENDIARY_AMMO_BOX_PREFIX = "i_ammo_box_";
    private final String GUN_TEMP_RELOADING_GUN = "RELOADING_GUN";

    private final int IP_CONST_FIREARM_RELOAD_SPEED_VERY_SLOW = 1;
    private final int IP_CONST_FIREARM_RELOAD_SPEED_SLOW      = 2;
    private final int IP_CONST_FIREARM_RELOAD_SPEED_FAST      = 4;
    private final int IP_CONST_FIREARM_RELOAD_SPEED_VERY_FAST = 5;
    private final int IP_CONST_FIREARM_RELOAD_SPEED_MEDIUM    = 3;

    private final float GUN_RELOAD_SPEED_VERY_SLOW = 4.2f;
    private final float GUN_RELOAD_SPEED_SLOW = 3.6f;
    private final float GUN_RELOAD_SPEED_MEDIUM = 2.8f;
    private final float GUN_RELOAD_SPEED_FAST = 2.2f;
    private final float GUN_RELOAD_SPEED_VERY_FAST = 1.2f;

    private final int CBT_OVR_SOUND_GUN_RELOAD = 8200;
    private final String GUN_TEMP_AMMO_PRIORITY = "GUN_AMMO_PRIORITY";

    private final String GUN_TEMP_CURRENT_RATE_OF_FIRE = "GUN_CUR_RATE_OF_FIRE";
    private final int IP_CONST_FIREARM_ROF_SEMI_AUTOMATIC = 1;
    private final int IP_CONST_FIREARM_ROF_AUTOMATIC      = 2;

    private final int GUN_WEAPON_MODE_THREE_ROUND_BURST = 1;
    private final int GUN_TYPE_INVALID = 0;

    // END CONSTANTS

    public void OnModuleEquip()
    {
        NWObject oPC = NWScript.getPCItemLastEquippedBy();
        NWObject oItem = NWScript.getPCItemLastEquipped();
        int iBulletCount = NWScript.getLocalInt(oItem, GUN_MAGAZINE_BULLET_COUNT);

        if(iBulletCount > 0 && NWScript.getLocalInt(oItem, GUN_TEMP_GUN_EQUIPPED) == 0 && DurabilitySystem.GetItemDurability(oItem) != 0)
        {
            GunGO stGunInfo = new GunGO(oItem);
            final NWObject oAmmo;

            int iLoadedType = NWScript.getLocalInt(oItem, GUN_TEMP_AMMO_LOADED_TYPE);
            if(iLoadedType == GUN_AMMUNITION_PRIORITY_ENHANCED)
            {
                oAmmo = NWScript.createItemOnObject(GUN_ENHANCED_FIREARM_MAGAZINE_RESREF, oPC, iBulletCount, "");
            }
            else if(iLoadedType == GUN_AMMUNITION_PRIORITY_INCENDIARY) {
                oAmmo = NWScript.createItemOnObject(GUN_INCENDIARY_FIREARM_MAGAZINE_RESREF, oPC, iBulletCount, "");
            }
            // Default to basic ammo if all else fails
            else
            {
                oAmmo = NWScript.createItemOnObject(GUN_FIREARM_MAGAZINE_RESREF, oPC, iBulletCount, "");
            }

            // Prevent exploits by making the loaded ammo cursed
            NWScript.setItemCursedFlag(oAmmo, true);
            NWScript.setLocalInt(oAmmo, GUN_TEMP_AMMO_TYPE, stGunInfo.getAmmoType());

            Scheduler.assign(oPC, () -> {
                NWScript.clearAllActions(false);
                NWScript.actionEquipItem(oAmmo, InventorySlot.ARROWS);
            });

            // Prevent this from firing on module entry
            NWScript.setLocalInt(oItem, GUN_TEMP_GUN_EQUIPPED, 1);
        }
    }

    public void OnModuleUnequip()
    {
        NWObject oPC = NWScript.getPCItemLastUnequippedBy();
        NWObject oItem = NWScript.getPCItemLastUnequipped();
        NWObject oGun = NWScript.getItemInSlot(InventorySlot.RIGHTHAND, oPC);
        String sTag = NWScript.getTag(oItem);
        int iAmmoType = NWScript.getLocalInt(oItem, GUN_TEMP_AMMO_TYPE);
        int iBulletCount = NWScript.getLocalInt(oItem, GUN_MAGAZINE_BULLET_COUNT);

        // If gun is unequipped, destroy ammo. The bullets are persistently saved on the gun as a variable (GUN_MAGAZINE_BULLET_COUNT)
        if(iBulletCount > 0)
        {
            NWObject oAmmo = NWScript.getItemInSlot(InventorySlot.ARROWS, oPC);

            if(NWScript.getIsObjectValid(oAmmo))
            {
                NWScript.setLocalInt(oAmmo, GUN_TEMP_UNLOADING_AMMO, 1);
                NWScript.destroyObject(oAmmo, 0.0f);
            }

            // Makes the OnEquip script fire when this gun is equipped again.
            NWScript.deleteLocalInt(oItem, GUN_TEMP_GUN_EQUIPPED);
        }

        // If ammo magazine is unequipped, convert to ammo box
        else if(NWScript.getLocalInt(oItem, GUN_TEMP_UNLOADING_AMMO) == 0)
        {
            iBulletCount = NWScript.getLocalInt(oGun, GUN_MAGAZINE_BULLET_COUNT);
            String sAmmoBoxTag = GUN_NORMAL_AMMO_BOX_PREFIX + iAmmoType;

            if(Objects.equals(sTag, GUN_ENHANCED_FIREARM_MAGAZINE_RESREF))
            {
                sAmmoBoxTag = GUN_ENHANCED_AMMO_BOX_PREFIX + iAmmoType;
            }
            else if(Objects.equals(sTag, GUN_INCENDIARY_FIREARM_MAGAZINE_RESREF))
            {
                sAmmoBoxTag = GUN_INCENDIARY_AMMO_BOX_PREFIX + iAmmoType;
            }

            if(Objects.equals(sTag, GUN_FIREARM_MAGAZINE_RESREF) || Objects.equals(sTag, GUN_ENHANCED_FIREARM_MAGAZINE_RESREF) || Objects.equals(sTag, GUN_INCENDIARY_FIREARM_MAGAZINE_RESREF))
            {
                NWScript.createItemOnObject(sAmmoBoxTag, oPC, iBulletCount, "");

                NWScript.destroyObject(oItem, 0.0f);

                // Remove persistent bullet counter
                NWScript.deleteLocalInt(oGun, GUN_MAGAZINE_BULLET_COUNT);
                // Remove temporary variables
                NWScript.deleteLocalInt(oGun, GUN_TEMP_AMMO_LOADED_TYPE);
                // Update gun name
                UpdateItemName(oGun);
            }
        }
    }

    public void OnModuleAttack(final NWObject oAttacker)
    {
        final NWObject oTarget = NWNX_Events.OnCombatRoundStart_GetTarget();
        PlayerGO pcGO = new PlayerGO(oAttacker);

        if(!PVPSanctuarySystem.IsPVPAttackAllowed(oAttacker, oTarget)) return;

        // PC is too busy to make an attack right now
        if(NWScript.getLocalInt(oAttacker, "RELOADING_GUN") == 1 ||
           NWScript.getCurrentAction(oAttacker) == Action.SIT ||
           NWScript.getLocalInt(oAttacker, "LOCKPICK_TEMPORARY_CURRENTLY_PICKING_LOCK") == 1 ||
           pcGO.isBusy())
        {
            NWScript.floatingTextStringOnCreature(ColorToken.Red() + "You are too busy to attack right now.", oAttacker, false);
            Scheduler.delay(oAttacker, 1, () -> NWScript.clearAllActions(false));
            return;
        }
        // PC is dead or dying
        if(NWScript.getCurrentHitPoints(oAttacker) <= 0)
        {
            Scheduler.delay(oAttacker, 1, () -> NWScript.clearAllActions(false));
            return;
        }

        NWObject oArea = NWScript.getArea(oAttacker);

        // Area is designated as "No PVP"
        if(NWScript.getIsPC(oTarget) && NWScript.getLocalInt(oArea, "NO_PVP") == 1 && NWScript.getIsPC(oAttacker))
        {
            Scheduler.delay(oAttacker, 1, () -> NWScript.clearAllActions(false));
            NWScript.floatingTextStringOnCreature(ColorToken.Red() + "You cannot engage in PVP in this area." + ColorToken.End(), oAttacker, false);
            return;
        }

        // Stop trying to attack a DM, dumbass
        if(NWScript.getIsDM(oTarget))
        {
            Scheduler.delay(oAttacker, 1, () -> NWScript.clearAllActions(false));
            return;
        }

        boolean bLineOfSight;

        // Determine which animation to use
        NWObject oRightHand = NWScript.getItemInSlot(InventorySlot.RIGHTHAND, oAttacker);
        NWObject oLeftHand = NWScript.getItemInSlot(InventorySlot.LEFTHAND, oAttacker);
        GunGO stGun1Info = new GunGO(oRightHand);
        GunGO stGun2Info = new GunGO(oLeftHand);
        int iNumberOfShots = 1;
        boolean bUsingFirearm = false;

        // All firearms must be marked to use the custom combat system
        if(stGun1Info.getGunType() > 0 || stGun2Info.getGunType() > 0)
        {
            // Disable sanctuary and other OnAreaEnter buffs
            NWEffect[] effects = NWScript.getEffects(oAttacker);

            for(NWEffect eEffect : effects)
            {
                int iEffectType = NWScript.getEffectType(eEffect);
                if(iEffectType == EffectType.DAMAGE_REDUCTION || iEffectType == EffectType.SANCTUARY)
                {
                    NWScript.removeEffect(oAttacker, eEffect);
                }
            }
            // Disable stealth mode
            if(NWScript.getActionMode(oAttacker, ActionMode.STEALTH))
            {
                NWScript.setActionMode(oAttacker, ActionMode.STEALTH, false);
            }
            bLineOfSight = NWScript.lineOfSightObject(oAttacker, oTarget) == 1;
            // No line of sight and we're using the custom combat system.
            // Prevent from continuing.
            if(!bLineOfSight || NWScript.getLocalInt(oAttacker, "TEMP_DISABLE_FIRING") == 1)
            {
                Scheduler.delay(oAttacker, 1, () -> NWScript.clearAllActions(false));
                return;
            }
            SetAttackAllowed(oAttacker, 1);
            bUsingFirearm = true;
        }
        // Otherwise, the PC isn't using a firearm. Use the default combat system
        else
        {
            NWScript.setLocalInt(oAttacker, CBT_OVR_SVAR_ATTACK_ANIMATION, CBT_OVR_ANIMATION_NONE);
            SetAttackAllowed(oAttacker, 0);
            SetNumberShotsToFire(oAttacker, 0);
        }

        if(bUsingFirearm)
        {
            Scheduler.delay(oAttacker, 1, () -> NWScript.clearAllActions(false));

            // Mode is determined by the right hand weapon. Figure out how many shots to fire based on mode
            if(NWScript.getLocalInt(oRightHand, "GUN_CUR_RATE_OF_FIRE") == 1)
            {
                iNumberOfShots = 3;
            }
            SetNumberShotsToFire(oAttacker, iNumberOfShots);

            float fDelay = 0.0f;
            SetTarget(oAttacker, oTarget);

            if (NWScript.getDistanceBetween(oAttacker, oTarget) > 0.5f)
            {
                if (GetAttackAllowed(oAttacker) == 1)
                {
                    if (NWScript.getCommandable(oAttacker) && NWScript.getIsObjectValid(oTarget))
                    {
                        Scheduler.delay(oAttacker, 1, () -> NWScript.clearAllActions(true));

                        NWVector attackerVector = NWScript.getPosition(oAttacker);
                        NWVector targetVector = NWScript.getPosition(oTarget);
                        NWVector vDiff = new NWVector(
                                attackerVector.getX() - targetVector.getX(),
                                attackerVector.getY() - targetVector.getY(),
                                attackerVector.getZ() - targetVector.getZ());

                        float vAngle = Math.abs(NWScript.vectorToAngle(vDiff) - NWScript.getFacing(oAttacker) - 180);

                        if (vAngle > 1.0 && vAngle < 359.0)
                        {
                            Position.TurnToFaceObject(oTarget, oAttacker);
                            fDelay = 0.75f;
                        }
                        // Quick players can sometimes get more than one cycle of shots off each attack. This should stop that.
                        NWScript.setLocalInt(oAttacker, "TEMP_DISABLE_FIRING", 1);

                        Scheduler.delay(oAttacker, (int) (fDelay * 1000.0f), () -> {
                            MainAttack(oAttacker, oTarget);
                            NWScript.deleteLocalInt(oAttacker, "TEMP_DISABLE_FIRING");
                        });
                    }
                }
            }
        }
    }

    public void ReloadAmmo(final NWObject oPC, final NWObject oGun, boolean bDualWield)
    {
        // Time it takes to reload this weapon
        float fDelay = 0.0f;
        GunGO stGunInfo = new GunGO(oGun);

        // PC can't reload non-guns
        if(stGunInfo.getAmmoType() == 0)
        {
            NWScript.floatingTextStringOnCreature(ColorToken.Red() + "Cannot reload that weapon!" + ColorToken.End(), oPC, false);
            return;
        }

        // PC cancels reloading if he or she selects it again
        if(NWScript.getLocalInt(oPC, GUN_TEMP_RELOADING_GUN) == 1)
        {
            // NPCs must finish loading - they cannot cancel.
            if(!NWScript.getIsPC(oPC))
            {
                return;
            }

            NWScript.floatingTextStringOnCreature(ColorToken.LightPurple() + "Reloading canceled." + ColorToken.End(), oPC, false);
            NWScript.deleteLocalInt(oPC, GUN_TEMP_RELOADING_GUN);
            // Allow PC to be commanded again
            NWScript.setCommandable(true, oPC);
            return;
        }

        float fAnimationSpeed = 0.0f;
        // Determine reloading speed
        switch(stGunInfo.getReloadSetting())
        {
            case IP_CONST_FIREARM_RELOAD_SPEED_VERY_SLOW:
            {
                fDelay = GUN_RELOAD_SPEED_VERY_SLOW;
                fAnimationSpeed = 0.4f;
                break;
            }
            case IP_CONST_FIREARM_RELOAD_SPEED_SLOW:
            {
                fDelay = GUN_RELOAD_SPEED_SLOW;
                fAnimationSpeed = 0.5f;
                break;
            }
            case IP_CONST_FIREARM_RELOAD_SPEED_MEDIUM:
            {
                fDelay = GUN_RELOAD_SPEED_MEDIUM;
                fAnimationSpeed = 0.6f;
                break;
            }
            case IP_CONST_FIREARM_RELOAD_SPEED_FAST:
            {
                fDelay = GUN_RELOAD_SPEED_FAST;
                fAnimationSpeed = 0.7f;
                break;
            }
            case IP_CONST_FIREARM_RELOAD_SPEED_VERY_FAST:
            {
                fDelay = GUN_RELOAD_SPEED_VERY_FAST;
                fAnimationSpeed = 0.9f;
                break;
            }
        }

        // Quick Reload ability occasionally grants instantaneous reload
        if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.QuickReload))
        {
            if(ThreadLocalRandom.current().nextInt(0, 100) <= 10)
            {
                fDelay = 0.01f;
                fAnimationSpeed = 2.0f;
            }
        }


        UnloadAmmo(oPC, oGun);

        // Mark player as reloading (prevents them from firing shots and other actions)
        NWScript.setLocalInt(oPC, GUN_TEMP_RELOADING_GUN, 1);

        // Play animations
        NWEffect eSound = NWScript.effectVisualEffect(CBT_OVR_SOUND_GUN_RELOAD, false);
        int iAnimation;
        float fAnimationLength;
        float fSoundDelay = 0.0f;
        NWLocation lLocation = NWScript.getLocation(oPC);

        if (bDualWield)
        {
            fAnimationLength = 0.0f;
            fSoundDelay = 2.8f;
            iAnimation = CustomAnimationType.ReloadDual;
        }
        else
        {
            fAnimationLength = 0.0f;
            iAnimation = CustomAnimationType.Reload;
        }

        // Player will be set to uncommandable during the time it takes to reload the weapon.

        final int animationID = iAnimation;
        final float animationSpeed = fAnimationSpeed;
        final float animationLength = fAnimationLength;
        Scheduler.assign(oPC, () -> {
            NWScript.clearAllActions(true);
            NWScript.playAnimation(animationID, animationSpeed, animationLength);
            NWScript.setCommandable(false, oPC);
        });

        Scheduler.delay(oPC, (int) (fDelay * 1000), () -> NWScript.setCommandable(true, oPC));

        NWScript.applyEffectAtLocation(DurationType.INSTANT, eSound, lLocation, 0.0f);
        if (bDualWield)
        {
            Scheduler.delay(oPC, (int) (fSoundDelay * 1000), () -> {
                NWEffect eSound1 = NWScript.effectVisualEffect(CBT_OVR_SOUND_GUN_RELOAD, false);
                NWScript.applyEffectAtLocation(DurationType.INSTANT, eSound1, NWScript.getLocation(oPC), 0.0f);
            });
        }

        Scheduler.delay(oPC, (int) (fDelay * 1000), () -> LoadAmmo(oPC, oGun, -1));
    }

    private void LoadAmmo(final NWObject oPC, final NWObject oGun, int iAmmoOverride)
    {
        GunGO stGunInfo = new GunGO(oGun);
        int iAmmoPriority = NWScript.getLocalInt(oGun, GUN_TEMP_AMMO_PRIORITY);

        // An ammo override was set - we're now looking this type of ammo instead of what the gun is set to look for.
        if(iAmmoOverride > -1) iAmmoPriority = iAmmoOverride;

        String sTag = GUN_NORMAL_AMMO_BOX_PREFIX + stGunInfo.getAmmoType();
        String sMagazineResref = GUN_FIREARM_MAGAZINE_RESREF;

        // Enhanced ammo is priority here
        if(iAmmoPriority == GUN_AMMUNITION_PRIORITY_ENHANCED)
        {
            sMagazineResref = GUN_ENHANCED_FIREARM_MAGAZINE_RESREF;
            sTag = GUN_ENHANCED_AMMO_BOX_PREFIX + stGunInfo.getAmmoType();
        }
        // Incendiary ammo is priority here
        else if(iAmmoPriority == GUN_AMMUNITION_PRIORITY_INCENDIARY)
        {
            sMagazineResref = GUN_INCENDIARY_FIREARM_MAGAZINE_RESREF;
            sTag = GUN_INCENDIARY_AMMO_BOX_PREFIX + stGunInfo.getAmmoType();
        }

        // Item equipped doesn't match the gun. Stop the loading
        if(!NWScript.getItemInSlot(InventorySlot.RIGHTHAND, oPC).equals(oGun))
        {
            NWScript.deleteLocalInt(oPC, GUN_TEMP_RELOADING_GUN);
            return;
        }

        // PC decided to cancel reloading.
        if(NWScript.getLocalInt(oPC, GUN_TEMP_RELOADING_GUN) == 0) return;

        // Look for ammo (PCs only - NPCs get ammo for free)
        int iAmmoFound = 0;
        NWObject[] inventory = NWScript.getItemsInInventory(oPC);
        if(NWScript.getIsPC(oPC))
        {
            // As long as more ammo items are found and clip isn't full,
            // keep converting ammo boxes to magazines
            for(NWObject oAmmo : inventory)
            {
                if(iAmmoFound >= stGunInfo.getMagazineSize()) break;

                // Currently selected item matches the tag of the ammo we're looking for
                if(Objects.equals(NWScript.getTag(oAmmo), sTag))
                {
                    int iStackSize = NWScript.getItemStackSize(oAmmo);

                    if(iStackSize + iAmmoFound > stGunInfo.getMagazineSize())
                    {
                        iStackSize = iStackSize - (stGunInfo.getMagazineSize() - iAmmoFound);
                        iAmmoFound = stGunInfo.getMagazineSize();
                        NWScript.setItemStackSize(oAmmo, iStackSize);
                        break;
                    }
                    else if(iStackSize + iAmmoFound <= stGunInfo.getMagazineSize())
                    {
                        NWScript.destroyObject(oAmmo, 0.0f);
                        iAmmoFound = iAmmoFound + iStackSize;
                    }
                }
            }

            // Found no enhanced ammunition. Switch to basic ammunition and run this function again.
            if(iAmmoFound == 0)
            {
                if(iAmmoPriority == GUN_AMMUNITION_PRIORITY_ENHANCED || iAmmoPriority == GUN_AMMUNITION_PRIORITY_INCENDIARY)
                {
                    LoadAmmo(oPC, oGun, GUN_AMMUNITION_PRIORITY_BASIC);
                    return;
                }
            }
        }
        else
        {
            // NPCs - Full amount of ammo on the magazine
            iAmmoFound = stGunInfo.getMagazineSize();
        }


        // Create an ammo magazine on PC and equip it automatically
        final NWObject oAmmo = NWScript.createItemOnObject(sMagazineResref, oPC, iAmmoFound, "");
        // Prevent exploits by making the loaded ammo cursed
        NWScript.setItemCursedFlag(oAmmo, true);
        NWScript.setLocalInt(oAmmo, GUN_TEMP_AMMO_TYPE, stGunInfo.getAmmoType());

        // Store bullet count on the firearm
        NWScript.setLocalInt(oGun, GUN_MAGAZINE_BULLET_COUNT, iAmmoFound);

        // October 16, 2010 - Added a check to make sure the ammo created is on the inventory of the player.
        // Otherwise, PCs could exploit this by reloading constantly with a full inventory (causing the created ammo to drop to the ground)
        // PCs could then pick up the ammo and equip it - bypassing ammo caps on firearms
        if(!NWScript.getItemPossessor(oAmmo).equals(oPC))
        {
            NWScript.destroyObject(oAmmo, 0.0f);
            NWScript.createItemOnObject(sTag, oPC, iAmmoFound, "");
            NWScript.floatingTextStringOnCreature(ColorToken.Red() + "Reloading failed. Your inventory is too full or you have no ammo." + ColorToken.End(), oPC, false);
            NWScript.deleteLocalInt(oPC, GUN_TEMP_RELOADING_GUN);
        }
        else
        {
            Scheduler.assign(oPC, () -> {
                NWScript.clearAllActions(false);
                NWScript.actionEquipItem(oAmmo, InventorySlot.ARROWS);
                NWScript.deleteLocalInt(oPC, GUN_TEMP_RELOADING_GUN);
            });

            // Mark ammunition type (basic, enhanced, etc)
            NWScript.setLocalInt(oGun, GUN_TEMP_AMMO_LOADED_TYPE, iAmmoPriority);
        }

        UpdateItemName(oGun);
    }

    private void UnloadAmmo(NWObject oPC, NWObject oGun)
    {
        String sResref;
        GunGO gunGO = new GunGO(oGun);
        int iAmmo = gunGO.getAmmoType();

        // Weapon uses no ammo, end.
        if(iAmmo <= 0){return;}

        NWObject oAmmo = NWScript.getItemInSlot(InventorySlot.ARROWS, oPC);

        // Ammo not valid. Just end
        if(!NWScript.getIsObjectValid(oAmmo)) return;

        // Check the ammo resref to see if we're dealing with basic ammo, or another type
        sResref = NWScript.getResRef(oAmmo);

        // Basic bullets
        if(Objects.equals(sResref, GUN_FIREARM_MAGAZINE_RESREF))
        {
            sResref = GUN_NORMAL_AMMO_BOX_PREFIX + iAmmo;
        }
        // Enhanced bullets
        else if(Objects.equals(sResref, GUN_ENHANCED_FIREARM_MAGAZINE_RESREF))
        {
            sResref = GUN_ENHANCED_AMMO_BOX_PREFIX + iAmmo;
        }
        // Incendiary bullets
        else if(Objects.equals(sResref, GUN_INCENDIARY_FIREARM_MAGAZINE_RESREF))
        {
            sResref = GUN_INCENDIARY_AMMO_BOX_PREFIX + iAmmo;
        }

        // Prevents the ammo from firing the module OnUnequip script
        // which would duplicate the ammo rounds
        NWScript.setLocalInt(oAmmo, GUN_TEMP_UNLOADING_AMMO, 1);

        // Note: NPCs get ammo for free. No need to create ammo boxes on them.
        if(NWScript.getIsPC(oPC))
        {
            int iStackSize = NWScript.getItemStackSize(oAmmo);
            NWScript.createItemOnObject(sResref, oPC, iStackSize, "");
        }

        NWScript.destroyObject(oAmmo, 0.0f);

        // Remove persistent bullet counter
        NWScript.deleteLocalInt(oGun, GUN_MAGAZINE_BULLET_COUNT);

        UpdateItemName(oGun);
    }


    public void ChangeGunAmmoPriority(NWObject oWeapon)
    {
        NWObject oPC = NWScript.getItemPossessor(oWeapon);
        int iCurrentAmmoPriority = NWScript.getLocalInt(oWeapon, GUN_TEMP_AMMO_PRIORITY);
        GunGO stGunInfo = new GunGO(oWeapon);

        // Currently using basic ammunition - prioritize enhanced ammo
        if(iCurrentAmmoPriority == 0 && stGunInfo.isEnhancedAmmoAvailable())
        {
            NWScript.setLocalInt(oWeapon, GUN_TEMP_AMMO_PRIORITY, GUN_AMMUNITION_PRIORITY_ENHANCED);
            NWScript.sendMessageToPC(oPC, ColorToken.LightPurple() + "Ammunition Priority: Enhanced" + ColorToken.End());
        }
        // Currently using enhanced or basic ammunition (if enhanced is not available) - prioritize incendiary ammo
        else if(iCurrentAmmoPriority != 2 && stGunInfo.isIncendiaryAmmoAvailable())
        {
            NWScript.setLocalInt(oWeapon, GUN_TEMP_AMMO_PRIORITY, GUN_AMMUNITION_PRIORITY_INCENDIARY);
            NWScript.sendMessageToPC(oPC, ColorToken.LightPurple() + "Ammunition Priority: Incendiary" + ColorToken.End());
        }
        // Prioritize basic ammo on all other circumstances
        else
        {
            NWScript.deleteLocalInt(oWeapon, GUN_TEMP_AMMO_PRIORITY);
            NWScript.sendMessageToPC(oPC, ColorToken.LightPurple() + "Ammunition Priority: Basic" + ColorToken.End());
        }
    }

    public void ChangeWeaponMode(NWObject oWeapon)
    {
        NWObject oPC = NWScript.getItemPossessor(oWeapon);
        GunGO stGunInfo = new GunGO(oWeapon);
        int iCurMode = NWScript.getLocalInt(oWeapon, GUN_TEMP_CURRENT_RATE_OF_FIRE);

        // Weapon cannot change rate of fire or the rate of fire
        // item property is missing
        if(stGunInfo.getRateOfFire() == -1)
        {
            NWScript.sendMessageToPC(oPC, ColorToken.Red() + "You cannot change this weapon's mode of fire." + ColorToken.End());
        }
        // Semi-Auto cannot change rate of fire
        else if(stGunInfo.getRateOfFire() == IP_CONST_FIREARM_ROF_SEMI_AUTOMATIC)
        {
            NWScript.sendMessageToPC(oPC, ColorToken.Red() + "This weapon has no alternative modes of fire." + ColorToken.End());
        }
        // Automatic can switch between Semi-Auto and 3-Round Burst
        else if(stGunInfo.getRateOfFire() == IP_CONST_FIREARM_ROF_AUTOMATIC)
        {
            // Currently in Semi-Auto mode. Switch to Automatic.
            if(iCurMode == 0)
            {
                NWScript.setLocalInt(oWeapon, GUN_TEMP_CURRENT_RATE_OF_FIRE, GUN_WEAPON_MODE_THREE_ROUND_BURST);
                NWScript.sendMessageToPC(oPC, ColorToken.LightPurple() + "Rate of Fire: 3-Round Burst" + ColorToken.End());
            }
            // Currently in 3-Round Burst mode. Switch to Semi-Auto.
            else
            {
                NWScript.deleteLocalInt(oWeapon, GUN_TEMP_CURRENT_RATE_OF_FIRE);
                NWScript.sendMessageToPC(oPC, ColorToken.LightPurple() + "Rate of Fire: Semi-Auto" + ColorToken.End());
            }
        }
    }

    private void UpdateItemName(NWObject oItem)
    {
        String sName = NWScript.getName(oItem, true);
        int iBulletCount = NWScript.getLocalInt(oItem, GUN_MAGAZINE_BULLET_COUNT);
        GunGO stGunInfo = new GunGO(oItem);
        String sNewName = "";

        // Firearms - Display current and max bullets chambered in the gun
        if(stGunInfo.getMagazineSize() > 0)
        {
            sNewName = sName + ColorToken.Custom(0, 255, 0) + " (" + iBulletCount + "/" + stGunInfo.getMagazineSize() + ")" + ColorToken.End();
        }

        // Update item name
        NWScript.setName(oItem, sNewName);
    }

    public boolean NPCCombatRoutine(final NWObject oNPC, NWObject oTarget)
    {
        if(oTarget == NWObject.INVALID)
        {
            oTarget = NWScript.getNearestCreature(CreatureType.REPUTATION, ReputationType.ENEMY, oNPC, 1, -1, -1, -1, -1);
        }
        final NWObject oFinalTarget = oTarget;

        NWObject oWeapon = NWScript.getItemInSlot(InventorySlot.RIGHTHAND, oNPC);
        GunGO stGunInfo = new GunGO(oWeapon);
        int iBulletCount = NWScript.getLocalInt(oWeapon, GUN_MAGAZINE_BULLET_COUNT);
        boolean bFireDefaultScript = true;

        if (stGunInfo.getGunType() != GUN_TYPE_INVALID && NWScript.getHasFeat(1116, oNPC)) // 1116 = Reload Feat
        {
            bFireDefaultScript = false;

            // Out of ammo or have no target and not at max ammo.
            if(iBulletCount <= 0 || (iBulletCount < stGunInfo.getMagazineSize() && oFinalTarget == NWObject.INVALID))
            {
                Scheduler.assign(oNPC, () -> NWScript.actionUseFeat(1116, oNPC));
            }
            else
            {
                if(NWScript.getIsEnemy(oFinalTarget, oNPC) && oFinalTarget != NWObject.INVALID)
                {
                    Scheduler.assign(oNPC, () -> NWScript.actionAttack(oFinalTarget, true));
                }
            }
        }

        return bFireDefaultScript;
    }


    private void SetNumberShotsToFire(NWObject oAttacker, int iNumberOfShots)
    {
        NWScript.setLocalInt(oAttacker, CBT_OVR_SVAR_ATTACK_SHOTS_TO_FIRE, iNumberOfShots);
    }

    private void SetTarget(NWObject oAttacker, NWObject oTarget)
    {
        NWScript.setLocalObject(oAttacker, CBT_OVR_SVAR_ATTACK_TARGET, oTarget);
        // Notify Target based on skill check???
    }

    private int GetAttackAllowed(NWObject oAttacker)
    {
        return NWScript.getLocalInt(oAttacker, CBT_OVR_SVAR_ATTACK_ALLOWED);
    }

    private void SetAttackAllowed(NWObject oAttacker, int bAttackAllowed)
    {
        NWScript.setLocalInt(oAttacker, CBT_OVR_SVAR_ATTACK_ALLOWED, bAttackAllowed);
    }

    private void MainAttack(final NWObject oAttacker, final NWObject oTarget)
    {

        NWObject oWeapon1 = NWScript.getItemInSlot(InventorySlot.RIGHTHAND, oAttacker);
        NWObject oWeapon2 = NWScript.getItemInSlot(InventorySlot.LEFTHAND, oAttacker);
        GunGO stGun1Info = new GunGO(oWeapon1);
        GunGO stGun2Info = new GunGO(oWeapon2);

        int iShots = NWScript.getLocalInt(oAttacker, CBT_OVR_SVAR_ATTACK_SHOTS_TO_FIRE);
        if (iShots < 1) iShots = 1;

        // Make sure there's ammo available
        NWObject oMagazine = NWScript.getItemInSlot(InventorySlot.ARROWS, oAttacker);
        int iMagazineSize = NWScript.getItemStackSize(oMagazine);

        if(iMagazineSize > stGun1Info.getMagazineSize() + stGun2Info.getMagazineSize())
        {
            iMagazineSize = stGun1Info.getMagazineSize() + stGun2Info.getMagazineSize();
            NWScript.setItemStackSize(oMagazine, iMagazineSize);
        }

        if(iMagazineSize < iShots)
        {
            // No more bullets. Inform attacker.
            if(iMagazineSize == 0)
            {
                NWScript.floatingTextStringOnCreature(GUN_OUT_OF_AMMO_MESSAGE, oAttacker, false);
                return;
            }
            // Shoot off the last round(s)
            else
            {
                iShots = iMagazineSize;
            }
        }

        float fNextAttackDelay = 0.0f;
        float fSoundDelay = 0.0f;
        float fShotDelay = 0.0f;
        float fAnimationLength= 0.0f;
        float fAnimationDelay = 0.0f;
        float fAnimationSpeed = 0.0f;
        float fDualDelay = 0.0f;
        int iCurrentShot;

        int iAnimation = 0;

        // Rifles
        if (stGun1Info.getGunType() == GunType.Rifle || stGun1Info.getGunType() == GunType.Invalid)
        {
            iAnimation = CustomAnimationType.Rifle; // Default to Rifle Animation if iAnimation is Invalid
            fNextAttackDelay = 0.8f;
            fSoundDelay = 0.1f; // 0.15 very decent
            fShotDelay = 0.7f; //3.5 original
            fAnimationLength = 0.5f; // 0.158 very decent
            fAnimationDelay = 0.5f; // 0.3 very decent
            fAnimationSpeed = 2.0f; // 7.0 very decent
        }
        // Handguns
        else if (stGun1Info.getGunType() == GunType.Handgun && stGun2Info.getGunType() == GunType.Invalid)
        {
            iAnimation = CustomAnimationType.Pistol;
            fNextAttackDelay = 0.5f;
            fSoundDelay = 0.5f; // 0.15 very decent
            fShotDelay = 0.7f; //18
            fAnimationLength = 0.5f; // 0.158 very decent
            fAnimationDelay = 0.5f; // 0.3 very decent
            fAnimationSpeed = 2.0f; // 7.0 very decent
        }
        // Magnums
        else if (stGun1Info.getGunType() == GunType.Magnum && stGun2Info.getGunType() == GunType.Invalid)
        {
            iAnimation = CustomAnimationType.Pistol;
            fNextAttackDelay = 0.8f;
            fSoundDelay = 0.5f; // 0.15 very decent
            fShotDelay = 0.7f; //18
            fAnimationLength = 0.5f; // 0.158 very decent
            fAnimationDelay = 0.5f; // 0.3 very decent
            fAnimationSpeed = 2.0f; // 7.0 very decent
        }
        // Dual Handguns or Dual Magnums (but not 1 handgun, 1 magnum)
        else if (stGun1Info.getGunType() == GunType.Handgun && stGun2Info.getGunType() == GunType.Handgun ||
                stGun1Info.getGunType() == GunType.Magnum && stGun2Info.getGunType() == GunType.Magnum)
        {
            iAnimation = CustomAnimationType.PistolDual;
            fNextAttackDelay = 0.8f;
            fSoundDelay = 0.5f; // 0.15 very decent
            fShotDelay = 0.7f; //18
            fAnimationLength = 0.5f; // 0.158 very decent
            fAnimationDelay = 0.5f; // 0.3 very decent
            fAnimationSpeed = 2.0f; // 7.0 very decent
            fDualDelay = 0.2f;
        }
        // SMGs
        else if (stGun1Info.getGunType() == GunType.SMG && stGun2Info.getGunType() == GunType.Invalid)
        {
            iAnimation = CustomAnimationType.SMG;
            fNextAttackDelay = 0.8f;
            fSoundDelay = 0.1f; // 0.15 very decent
            fShotDelay = 0.35f; //18
            fAnimationLength = 0.1f; // 0.158 very decent
            fAnimationDelay = 0.25f; // 0.3 very decent
            fAnimationSpeed = 8.0f; // 7.0 very decent
        }
        // Dual SMGs
        else if (stGun1Info.getGunType() == GunType.SMG && stGun2Info.getGunType() == GunType.SMG)
        {
            iAnimation = CustomAnimationType.SMGDual;
            fNextAttackDelay = 0.8f;
            fSoundDelay = 0.1f;
            fShotDelay = 0.35f;
            fAnimationLength = 0.1f;
            fAnimationDelay = 0.25f;
            fAnimationSpeed = 8.0f;
            fDualDelay = 0.05f;
        }
        // Assault Rifles (Rifle animation, SMG stats)
        else if (stGun1Info.getGunType() == GunType.AssaultRifle)
        {
            iAnimation = CustomAnimationType.Rifle;
            fNextAttackDelay = 0.8f;
            fSoundDelay = 0.1f;
            fShotDelay = 0.35f;
            fAnimationLength = 0.1f;
            fAnimationDelay = 0.25f;
            fAnimationSpeed = 8.0f;
            fDualDelay = 0.05f;
        }

        // Shotguns
        else if (stGun1Info.getGunType() == GunType.Shotgun)
        {
            iAnimation = CustomAnimationType.Shotgun;
            fNextAttackDelay = 0.8f;
            fSoundDelay = 0.80f;
            fShotDelay = 1.0f;

            if (iShots > 1)
            {
                fAnimationLength = 0.60f + iShots * 0.02f;
            }
            else
            {
                fAnimationLength = 0.60f;
            }

            fAnimationDelay = 1.0f;
            fAnimationSpeed = 1.20f;
        }

        final int animationID = iAnimation;
        final float animationSpeed = fAnimationSpeed;
        final float animationLength = fAnimationLength * iShots + fAnimationDelay;


        Scheduler.assign(oAttacker, () -> {
            NWScript.clearAllActions(true);
            NWScript.setFacingPoint(NWScript.getPosition(oTarget));
            NWScript.playAnimation(animationID, animationSpeed, animationLength);
            NWScript.setCommandable(false, oAttacker);
        });

        Scheduler.delay(oAttacker, (int) ((fAnimationLength + fNextAttackDelay) * 1000), () -> NWScript.setCommandable(true, oAttacker));

        // Loop for attack
        for (iCurrentShot = 1; iCurrentShot < iShots+1; iCurrentShot++)
        {
            // Out of ammo or target is dead - we're done here.
            if(iMagazineSize <= 0 ||
                    (NWScript.getIsPC(oTarget) && NWScript.getCurrentHitPoints(oTarget) <= -11) || // PCs
                    (!NWScript.getIsPC(oTarget) && NWScript.getIsDead(oTarget))) // NPCs
            {
                break;
            }
            // Reduce magazine bullets by 1 (or destroy the stack if it's the last bullet)
            else if(iMagazineSize == 1)
            {
                iMagazineSize = 0;
                NWScript.destroyObject(oMagazine, 0.0f);
            }
            else
            {
                iMagazineSize = iMagazineSize - 1;
                NWScript.setItemStackSize(oMagazine, iMagazineSize);
            }

            if (iAnimation == CustomAnimationType.Pistol || iAnimation == CustomAnimationType.PistolDual || iAnimation == CustomAnimationType.Shotgun)
            {
                // Each additional shot increases the sound delay
                if (iCurrentShot != 1)
                {
                    fShotDelay += fSoundDelay;
                }
            }
            else
            {
                // Default Timings
                fShotDelay += fSoundDelay;
            }

            final NWObject weapon1 = oWeapon1;
            final NWObject weapon2 = oWeapon2;
            final int animation = iAnimation;

            Scheduler.delay(oAttacker, (int) (fShotDelay * 1000), () -> FireShot(oAttacker, oTarget, weapon1, animation));


            // Second Shot for dual-pistols/SMGs
            if (iAnimation == CustomAnimationType.PistolDual || iAnimation == CustomAnimationType.SMGDual)
            {
                // Again, we need to ensure there's bullets available for use. Otherwise the second shot won't take NWEffect

                if(iMagazineSize <= 0)
                {
                    break;
                }
                // Reduce magazine bullets by 1 (or destroy the stack if it's the last bullet)
                else if(iMagazineSize == 1)
                {
                    iMagazineSize = 0;
                    NWScript.setLocalInt(oAttacker, GUN_TEMP_PREVENT_AMMO_BOX_BUG, 1);
                    NWScript.destroyObject(oMagazine, 0.0f);

                    Scheduler.delay(oAttacker, 100, () -> NWScript.deleteLocalInt(oAttacker, GUN_TEMP_PREVENT_AMMO_BOX_BUG));
                }
                else
                {
                    iMagazineSize = iMagazineSize - 1;
                    NWScript.setItemStackSize(oMagazine, iMagazineSize);
                }

                Scheduler.delay(oAttacker, (int)((fDualDelay + fShotDelay) * 1000), () -> FireShot(oAttacker, oTarget, weapon2, animation));
            }
        }

        // Display "Low Ammo" message when bullets reach specified threshold
        int iMaxMagazineSize = NWScript.floatToInt(stGun1Info.getMagazineSize() * GUN_LOW_AMMO_MESSAGE_DISPLAY_PERCENTAGE);
        if(iMaxMagazineSize < 1) iMaxMagazineSize = 1;

        // Update bullet count on the gun
        NWScript.setLocalInt(oWeapon1, GUN_MAGAZINE_BULLET_COUNT, iMagazineSize);

        if(iMagazineSize <= 0)
        {
            NWScript.floatingTextStringOnCreature(GUN_OUT_OF_AMMO_MESSAGE, oAttacker, false);
        }
        else if(iMagazineSize <= iMaxMagazineSize)
        {
            NWScript.floatingTextStringOnCreature(GUN_LOW_AMMO_MESSAGE, oAttacker, false);
        }

        // Update gun name to reflect change in ammo currently chambered
        UpdateItemName(oWeapon1);

        // Fire durability system for gun
        DurabilitySystem.RunItemDecay(oAttacker, oWeapon1);
    }

    private void FireShot(NWObject oAttacker, NWObject oTarget, NWObject oWeapon, int iAnimation)
    {
        NWLocation lLocation = NWScript.getLocation(oAttacker);
        NWEffect eSound;
        int iSilenced = NWScript.getLocalInt(oAttacker, CBT_OVR_SVAR_ATTACK_SILENCED);

        GunGO stGunInfo = new GunGO(oWeapon);

        // Pistols
        if (iAnimation == CustomAnimationType.Pistol || iAnimation == CustomAnimationType.PistolDual)
        {
            if (iSilenced == 1)
            {
                eSound = NWScript.effectVisualEffect(CBT_OVR_SOUND_SHOT_SILENCED_PISTOL, false);
            }
            else
            {
                eSound = NWScript.effectVisualEffect(CBT_OVR_SOUND_SHOT_HEAVY_PISTOL_9MM, false);
            }
        }
        // SMGs
        else if (iAnimation == CustomAnimationType.SMG || iAnimation == CustomAnimationType.SMGDual)
        {
            if (iSilenced == 1)
            {
                eSound = NWScript.effectVisualEffect(CBT_OVR_SOUND_SHOT_SILENCED_PISTOL, false);
            }
            else
            {
                eSound = NWScript.effectVisualEffect(CBT_OVR_SOUND_SHOT_SMG_762, false);
            }
        }
        // Shotguns
        else if (iAnimation == CustomAnimationType.Shotgun)
        {
            eSound = NWScript.effectVisualEffect(CBT_OVR_SOUND_SHOT_SHOTGUN_12G, false);
        }
        else
        {
            if (iSilenced == 1)
            {
                eSound = NWScript.effectVisualEffect(CBT_OVR_SOUND_SHOT_SILENCED_RIFLE, false);
            }
            else
            {
                eSound = NWScript.effectVisualEffect(CBT_OVR_SOUND_SHOT_ASSAULT_RIFLE_762, false);
            }
        }

        NWScript.applyEffectAtLocation(DurationType.INSTANT, eSound, lLocation, 0.0f);

        // Fire shotgun blast
        if (iAnimation == CustomAnimationType.Shotgun)
        {
            FireShotgun(oAttacker, oTarget, stGunInfo);
        }
        // Otherwise, fire normal bullet
        else
        {
            FireBullet(oAttacker, oTarget, oWeapon, stGunInfo);
        }
    }

    private int bIsCriticalHit = 0;
    private int CalculateDamage(final NWObject oAttacker, final NWObject oTarget, GunGO stGunInfo, int iFirearmSkill, boolean isPoliceOfficerProfession)
    {
        bIsCriticalHit = 0;

        float fDistance = NWScript.getDistanceBetween(oAttacker, oTarget);
        if(fDistance == 0.0) return 0;
        iFirearmSkill = iFirearmSkill + 1;
        float fMultiplier = ((NWScript.random(30) + 40)) * 0.001f + (iFirearmSkill * 0.005f);
        float fMaxRange = 0.0f;
        int iCriticalChance = 1 + stGunInfo.getCriticalRating();

        if(isPoliceOfficerProfession && stGunInfo.getGunType() == GunType.Handgun)
            iCriticalChance += 3;

        int iChanceToHit = 100;

        // Each weapon has a "sweet spot". When a player is within this range they receive a 0.02 multiplier bonus.

        // "Near" range setting
        if(stGunInfo.getRange() == IP_CONST_FIREARM_RANGE_NEAR)
        {
            fMaxRange = GUN_RANGE_NEAR_MAXIMUM;
            if(fDistance >= GUN_RANGE_NEAR_MINIMUM && fDistance <= GUN_RANGE_NEAR_MAXIMUM)
            {
                fMultiplier = fMultiplier + 0.02f;
            }
        }
        // "Mid" range setting
        else if(stGunInfo.getRange() == IP_CONST_FIREARM_RANGE_MID)
        {
            fMaxRange = GUN_RANGE_MID_MAXIMUM;
            if(fDistance >= GUN_RANGE_MID_MINIMUM && fDistance <= GUN_RANGE_MID_MAXIMUM)
            {
                fMultiplier = fMultiplier + 0.02f;
            }
        }
        // "Far" range setting
        else if(stGunInfo.getRange() == IP_CONST_FIREARM_RANGE_FAR)
        {
            fMaxRange = GUN_RANGE_FAR_MAXIMUM;
            if(fDistance >= GUN_RANGE_FAR_MINIMUM && fDistance <= GUN_RANGE_FAR_MAXIMUM)
            {
                fMultiplier = fMultiplier + 0.02f;
            }
            // Rifles (weapons with the "Far" range setting) receive a penalty to accuracy if a target is too close.
            // 3% accuracy loss per half meter below minimum range
            else if(fDistance < GUN_RANGE_FAR_MINIMUM)
            {
                iChanceToHit = iChanceToHit - NWScript.floatToInt((3 * (Math.abs(fDistance - GUN_RANGE_FAR_MINIMUM) / 0.5f)));
            }
        }

        // Multiplier falls off starting at max range + 2.5 meters at a rate of 0.005 per half meter outside of max range
        // Chance to head shot also decreases at a rate of 2% loss per half meter outside max range
        // Accuracy lost at a rate of 3% per half meter past max range
        fMaxRange = fMaxRange + 2.5f;
        if(fDistance > fMaxRange)
        {
            fMaxRange = Math.abs(fMaxRange - fDistance);
            fMultiplier = fMultiplier - (0.005f * (fMaxRange / 0.5f));
            if(iCriticalChance < 0) iCriticalChance = 0;
            iChanceToHit = iChanceToHit - NWScript.floatToInt((3 * (fMaxRange / 0.5f)));
        }

        // Critical hit (Head shot) rolls. Note that shotguns do not have a chance to head shot since their spread is powerful already.
        if(NWScript.random(100) <= iCriticalChance && stGunInfo.getGunType() != GunType.Shotgun)
        {
            fMultiplier = fMultiplier + 0.04f;
            bIsCriticalHit = 1;
        }

        // Enhanced ammunition grants an additional 0.015 to the multiplier.
        int iAmmunitionType = NWScript.getLocalInt(stGunInfo.getGun(), GUN_TEMP_AMMO_LOADED_TYPE);
        if(iAmmunitionType == GUN_AMMUNITION_PRIORITY_ENHANCED)
        {
            fMultiplier = fMultiplier + 0.015f;
        }

        // Round current durability to nearest whole number, then calculate the multiplier based on the percent difference.
        float percentDamaged = Math.round(DurabilitySystem.GetItemDurability(stGunInfo.getGun())) / DurabilitySystem.GetMaxItemDurability(stGunInfo.getGun());

        int iFirepower = NWScript.floatToInt(stGunInfo.getFirepower() * percentDamaged);
        int iDamage = NWScript.floatToInt(iFirepower * fMultiplier);

        if(bIsCriticalHit == 1){
            iDamage *= 2;
        }

        // Attack missed - return zero damaged
        if(NWScript.random(100) > iChanceToHit) return 0;

        // Always deal at least 1 damage when an attack hits
        if(iDamage <= 0) iDamage = 1;

        // Incendiary ammunition deals fire damage to the target over time. It also applies a nifty visual to the target.
        if(iAmmunitionType == GUN_AMMUNITION_PRIORITY_INCENDIARY)
        {
            NWScript.applyEffectToObject(DurationType.TEMPORARY, NWScript.effectVisualEffect(VfxDur.INFERNO_CHEST, false), oTarget, 3.0f);
            int bCurrentlyOnFire = NWScript.getLocalInt(oTarget, "INCENDIARY_DAMAGE_COUNTER");
            NWScript.setLocalInt(oTarget, "INCENDIARY_DAMAGE_COUNTER", 3);

            // We don't want multiple copies of this function firing at the same time, so ensure there's not another
            // one already going
            if(bCurrentlyOnFire == 0)
            {
                Scheduler.delay(oAttacker, 1000, () -> IncendiaryDamage(oAttacker, oTarget, NWScript.random(3) + 3));
            }
        }

        return iDamage;
    }

    public static void IncendiaryDamage(final NWObject oAttacker, final NWObject oTarget, final int iDamage)
    {
        // Exit when target is dead or invalid
        if(NWScript.getIsDead(oTarget) || !NWScript.getIsObjectValid(oTarget)) return;

        int iCounter = NWScript.getLocalInt(oTarget, "INCENDIARY_DAMAGE_COUNTER") - 1;
        Scheduler.assign(oAttacker, () -> NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectDamage(iDamage, DamageType.FIRE, DamagePower.NORMAL), oTarget, 0.0f));

        // Exit when the incendiary NWEffect wears off
        if(iCounter <= 0) return;

        // Otherwise update the counter and call this function again in one second
        NWScript.setLocalInt(oTarget, "INCENDIARY_DAMAGE_COUNTER", iCounter);

        Scheduler.delay(oAttacker, 1000, () -> IncendiaryDamage(oAttacker, oTarget, NWScript.random(3) + 3));
        
    }

    private void FireShotgun(NWObject oAttacker, NWObject oTarget, GunGO stGunInfo)
    {
        float fRangeDistance = 7.5f;
        NWVector vOrigin = NWScript.getPosition(oAttacker);
        NWLocation lTarget = NWScript.getLocation(oTarget);

        boolean isMiss = true;

        int iShotgunSkill = ProgressionSystem.GetPlayerSkillLevel(oAttacker, ProgressionSystem.SkillType_SHOTGUN_PROFICIENCY);
        int iShotgunAccuracy = ProgressionSystem.GetPlayerSkillLevel(oAttacker, ProgressionSystem.SkillType_SHOTGUN_ACCURACY);

        int numberAttacked = 0;
        NWObject[] shapeTargets = NWScript.getObjectsInShape(Shape.SPELLCYLINDER, fRangeDistance, lTarget, true, ObjectType.CREATURE, vOrigin);
        for(NWObject oShapeTarget : shapeTargets)
        {
            // PCs hit whatever is in range.
            // NPCs hit only enemies in range
            if(NWScript.getIsPC(oAttacker) || (!NWScript.getIsPC(oAttacker) && NWScript.getIsEnemy(oShapeTarget, oAttacker)))
            {
                if(oShapeTarget != oAttacker && !NWScript.getIsDead(oShapeTarget) && !NWScript.getIsDead(oAttacker))
                {
                    if(NWScript.random(30) > (NWScript.getAC(oShapeTarget, 0) + (numberAttacked*2)) - iShotgunAccuracy)
                    {
                        int iDamage = CalculateDamage(oAttacker, oShapeTarget, stGunInfo, iShotgunSkill, false);

                        if(iDamage > 0)
                        {
                            NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectDamage(iDamage, DamageType.PIERCING, DamagePower.NORMAL), oShapeTarget, 0.0f);
                            NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectVisualEffect(VfxComBloodSpark.LARGE, false), oShapeTarget, 0.0f);
                            NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectVisualEffect(VfxComChunkRed.SMALL, false), oShapeTarget, 0.0f);
                            isMiss = false;
                        }
                    }
                    else
                    {
                        isMiss = true;
                    }

                    numberAttacked++;
                }
            }
        }

        if(isMiss)
        {
            int iNumberOfSparks = NWScript.random(4) + 1;

            for(int s = 1; s <= iNumberOfSparks; s++)
            {
                NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectVisualEffect(VfxComBloodSpark.LARGE, true), oTarget, 0.0f);
            }
        }
    }

    private void FireBullet(NWObject oAttacker, NWObject oTarget, NWObject oWeapon, GunGO stGunInfo)
    {
        boolean isPoliceOfficerProfession = false;
        if(NWScript.getIsPC(oAttacker) && !NWScript.getIsDM(oAttacker) && !NWScript.getIsDMPossessed(oAttacker))
        {
            PlayerGO pcGO = new PlayerGO(oAttacker);
            PlayerRepository pcRepo = new PlayerRepository();
            PlayerEntity pcEntity = pcRepo.GetByPlayerID(pcGO.getUUID());

            isPoliceOfficerProfession = pcEntity.getProfessionID() == ProfessionType.PoliceOfficer;
        }

        NWVector vPosition = NWScript.getPosition(oAttacker);
        NWLocation lTargetLocation = NWScript.getLocation(oTarget);
        int bMiss;
        float fClosestDistance = 0.0f;
        float fStunDuration = 0.0f;
        int iTargetAC = NWScript.getAC(oTarget, 0);

        // Get the correct skill, based on the type of firearm being used
        // Also determine how long the stun NWEffect lasts, based on weapon
        int iProficiencySkill = 0;
        int iAccuracySkill = 0;
        switch(stGunInfo.getGunType())
        {
            case GunType.Handgun:
                iProficiencySkill = ProgressionSystem.GetPlayerSkillLevel(oAttacker, ProgressionSystem.SkillType_HANDGUN_PROFICIENCY);
                iAccuracySkill = ProgressionSystem.GetPlayerSkillLevel(oAttacker, ProgressionSystem.SkillType_HANDGUN_ACCURACY);
                fStunDuration = 2.0f;
                break;
            case GunType.Rifle:
                iProficiencySkill = ProgressionSystem.GetPlayerSkillLevel(oAttacker, ProgressionSystem.SkillType_RIFLE_PROFICIENCY);
                iAccuracySkill = ProgressionSystem.GetPlayerSkillLevel(oAttacker, ProgressionSystem.SkillType_RIFLE_ACCURACY);
                fStunDuration = 2.0f;
                break;
            case GunType.SMG:
                iProficiencySkill = ProgressionSystem.GetPlayerSkillLevel(oAttacker, ProgressionSystem.SkillType_SMG_PROFICIENCY);
                iAccuracySkill = ProgressionSystem.GetPlayerSkillLevel(oAttacker, ProgressionSystem.SkillType_SMG_ACCURACY);
                fStunDuration = 2.0f;
                break;
            case GunType.AssaultRifle:
                iProficiencySkill = ProgressionSystem.GetPlayerSkillLevel(oAttacker, ProgressionSystem.SkillType_SMG_PROFICIENCY);
                iAccuracySkill = ProgressionSystem.GetPlayerSkillLevel(oAttacker, ProgressionSystem.SkillType_SMG_ACCURACY);
                fStunDuration = 2.0f;
                break;
            case GunType.Magnum:
                iProficiencySkill = ProgressionSystem.GetPlayerSkillLevel(oAttacker, ProgressionSystem.SkillType_MAGNUM_PROFICIENCY);
                iAccuracySkill = ProgressionSystem.GetPlayerSkillLevel(oAttacker, ProgressionSystem.SkillType_MAGNUM_ACCURACY);
                fStunDuration = 2.0f;
                break;
        }

        // Gas canisters always take priority when targeting. Otherwise players will cry about not hitting them when they want to.
        String sTag = NWScript.getTag(oTarget);
        if(!Objects.equals(sTag, GUN_GAS_CANISTER_TAG))
        {
            NWObject[] shapeTargets = NWScript.getObjectsInShape(Shape.SPELLCYLINDER, 16.0f, lTargetLocation, true, ObjectType.CREATURE, vPosition);

            for(NWObject oCylinderTarget : shapeTargets)
            {
                // PCs hit whatever is in range.
                // NPCs hit only enemies in range
                if(NWScript.getIsPC(oAttacker) || (!NWScript.getIsPC(oAttacker) && NWScript.getIsEnemy(oCylinderTarget, oAttacker)))
                {
                    // Found a valid target. See if it's closest to attacker
                    if(!NWScript.getIsDead(oCylinderTarget) && oAttacker != oCylinderTarget && !NWScript.getIsDead(oAttacker))
                    {
                        float fDistanceCheck = NWScript.getDistanceBetween(oCylinderTarget, oAttacker);

                        if(fDistanceCheck < fClosestDistance || fClosestDistance == 0.0)
                        {
                            PlayerGO playerGO = new PlayerGO(oCylinderTarget);
                            // Check for sanctuary status
                            if(!playerGO.hasPVPSanctuary())
                            {
                                oTarget = oCylinderTarget;
                                fClosestDistance = fDistanceCheck;
                            }
                        }
                    }
                }
            }
        }

        // Do accuracy check
        iTargetAC = iTargetAC - iAccuracySkill;

        if(NWScript.random(30) + 1 > iTargetAC)
        {
            bMiss = 0;
        }
        else
        {
            bMiss = 1;
        }

        // Calculate damage
        int iDamage = CalculateDamage(oAttacker, oTarget, stGunInfo, iProficiencySkill, isPoliceOfficerProfession);
        if(iDamage <= 0) bMiss = 1;

        if(bMiss == 0)
        {
            if(bIsCriticalHit == 1)
            {
                NWScript.floatingTextStringOnCreature("Critical hit!", oAttacker, false);
            }

            NWEffect eSparkEffect = NWScript.effectVisualEffect(VfxComBloodSpark.LARGE, false);

            int iStoppingPower = 0;
            NWItemProperty[] itemProperties = NWScript.getItemProperties(oWeapon);

            for(NWItemProperty ipStoppingPower : itemProperties)
            {
                if(NWScript.getItemPropertyType(ipStoppingPower) == 139) // 139 = Stopping Power item property
                {
                    iStoppingPower = NWScript.getItemPropertyCostTableValue(ipStoppingPower);
                    break;
                }
            }

            if(iStoppingPower >= 100)
            {
                NWScript.applyEffectToObject(DurationType.TEMPORARY, NWScript.effectCutsceneImmobilize(), oTarget, fStunDuration);
            }
            else if(iStoppingPower > 0)
            {
                NWScript.applyEffectToObject(DurationType.TEMPORARY, NWScript.effectMovementSpeedDecrease(iStoppingPower), oTarget, fStunDuration);
            }

            NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectDamage(iDamage, DamageType.PIERCING, DamagePower.NORMAL), oTarget, 0.0f);
            NWScript.applyEffectToObject(DurationType.INSTANT, eSparkEffect, oTarget, 0.0f);
        }
        else
        {
            NWScript.sendMessageToPC(oAttacker, ColorToken.Gray() + "You missed your target." + ColorToken.End());
        }
    }




}
