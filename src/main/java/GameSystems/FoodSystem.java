package GameSystems;

import Data.Repository.PlayerRepository;
import Entities.PlayerEntity;
import Enumerations.CustomEffectType;
import GameObject.PlayerGO;
import Helper.ColorToken;
import Helper.MenuHelper;
import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.Ability;
import org.nwnx.nwnx2.jvm.constants.Animation;
import org.nwnx.nwnx2.jvm.constants.DurationType;

import java.util.concurrent.ThreadLocalRandom;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class FoodSystem {


    public static PlayerEntity RunHungerCycle(NWObject pc, PlayerEntity entity)
    {
        String sAreaTag = getTag(getArea(pc));
        if(sAreaTag.equals("ooc_area") || sAreaTag.equals("death_realm")) return entity;
        int hungerTick = entity.getCurrentHungerTick() - 1;

        if(hungerTick <= 0 && entity.getCurrentHunger() >= 0)
        {
            int penalty = 0;
            hungerTick = 300 + ThreadLocalRandom.current().nextInt(300); // 5 minutes + random amount of time (up to +5 minutes)
            entity.setCurrentHunger(entity.getCurrentHunger() - 1);

            if(entity.getCurrentHunger() >= 40 && entity.getCurrentHunger() <= 50)
            {
                floatingTextStringOnCreature( "You are starving! You should eat soon.", pc, false);
            }
            else if(entity.getCurrentHunger() >= 30 && entity.getCurrentHunger() < 40)
            {
                penalty = 1;
                floatingTextStringOnCreature( "You are starving! You are suffering from starvation penalties.", pc, false);
            }
            else if(entity.getCurrentHunger() >= 20 && entity.getCurrentHunger() < 30)
            {
                penalty = 2;
                floatingTextStringOnCreature( "You are starving! You are suffering from starvation penalties.", pc, false);
            }
            else if(entity.getCurrentHunger() >= 10 && entity.getCurrentHunger() < 20)
            {
                penalty = 3;
                floatingTextStringOnCreature( "You are starving! You are suffering from starvation penalties.", pc, false);
            }
            else if(entity.getCurrentHunger() < 10)
            {
                penalty = 4;
                floatingTextStringOnCreature(ColorToken.Red() + "You are starving! You are about to starve to death!" + ColorToken.End(), pc, false);
            }

            NWEffect[] effects = getEffects(pc);
            for(NWEffect effect: effects)
            {
                if(getEffectTag(effect).equals("EFFECT_HUNGER_PENALTIES"))
                {
                    removeEffect(pc, effect);
                }
            }

            if(penalty > 0)
            {
                NWEffect effect = effectAbilityDecrease(Ability.STRENGTH, penalty);
                effect = effectLinkEffects(effect, effectAbilityDecrease(Ability.DEXTERITY, penalty));
                effect = effectLinkEffects(effect, effectAbilityDecrease(Ability.CONSTITUTION, penalty));

                effect = NWScript.tagEffect(effect, "EFFECT_HUNGER_PENALTIES");
                applyEffectToObject(DurationType.PERMANENT, effect, pc, 0.0f);
            }

            if(entity.getCurrentHunger() <= 0)
            {
                applyEffectToObject(DurationType.INSTANT, effectDeath(false, true), pc, 0.0f);
                entity.setCurrentHunger(20);
                floatingTextStringOnCreature("You starved to death!", pc, false);
            }
        }

        entity.setCurrentHungerTick(hungerTick);

        return entity;
    }

    public static void IncreaseHungerLevel(NWObject oPC, int amount, boolean isTainted)
    {
        if(!getIsPC(oPC) || getIsDM(oPC)) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerRepository repo = new PlayerRepository();
        PlayerEntity entity = repo.GetByPlayerID(pcGO.getUUID());
        entity.setCurrentHunger(entity.getCurrentHunger() + amount);
        if(entity.getCurrentHunger() > entity.getMaxHunger()) entity.setCurrentHunger(entity.getMaxHunger());

        Scheduler.assign(oPC, () -> actionPlayAnimation(Animation.FIREFORGET_SALUTE, 1.0f, 0.0f));

        sendMessageToPC(oPC, "Hunger: " + MenuHelper.BuildBar(entity.getCurrentHunger(), entity.getMaxHunger(), 100));
        repo.save(entity);

        if(isTainted)
        {
            if(ThreadLocalRandom.current().nextInt(100) + 1 <= 40)
            {
                int ticks = 600 + ThreadLocalRandom.current().nextInt(300);

                CustomEffectSystem.ApplyCustomEffect(oPC, oPC, CustomEffectType.FoodDisease, ticks, 0);
            }
        }

    }
}
