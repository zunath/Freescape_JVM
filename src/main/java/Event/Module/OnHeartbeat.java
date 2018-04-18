package Event.Module;
import Common.IScriptEventHandler;
import Data.Repository.DatabaseRepository;
import Data.Repository.PlayerRepository;
import Entities.PlayerEntity;
import GameObject.PlayerGO;
import GameSystems.AbilitySystem;
import GameSystems.CustomEffectSystem;
import GameSystems.FoodSystem;
import GameSystems.ItemSystem;
import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Ability;
import org.nwnx.nwnx2.jvm.constants.BaseItem;
import org.nwnx.nwnx2.jvm.constants.DurationType;
import org.nwnx.nwnx2.jvm.constants.EffectType;

import static org.nwnx.nwnx2.jvm.NWScript.*;

@SuppressWarnings("unused")
public class OnHeartbeat implements IScriptEventHandler {

	private PlayerRepository playerRepo;

	@Override
	public void runScript(NWObject objSelf) {

		playerRepo = new PlayerRepository();

		for(NWObject pc : getPCs())
		{
			if(!getIsDM(pc))
			{
				HandleEffectLifespan(pc);

				PlayerGO pcGO = new PlayerGO(pc);
				PlayerEntity entity = playerRepo.GetByPlayerID(pcGO.getUUID());

				if(entity != null)
				{
					entity = FoodSystem.RunHungerCycle(pc, entity);
					entity = HandleRegenerationTick(pc, entity);
					entity = HandleManaRegenerationTick(pc, entity);
					playerRepo.save(entity);
				}
			}
		}

		SaveCharacters();
        CustomEffectSystem.OnModuleHeartbeat();

		ItemSystem.OnModuleHeartbeat();
		DatabaseRepository databaseRepo = new DatabaseRepository();
		databaseRepo.KeepAlive();
	}

	// Export all characters every minute.
	private void SaveCharacters()
	{
		int currentTick = getLocalInt(NWObject.MODULE, "SAVE_CHARACTERS_TICK") + 1;

		if(currentTick >= 10)
		{
			exportAllCharacters();
			currentTick = 0;
		}

		setLocalInt(NWObject.MODULE, "SAVE_CHARACTERS_TICK", currentTick);
	}

	private PlayerEntity HandleRegenerationTick(NWObject oPC, PlayerEntity entity)
	{
		entity.setRegenerationTick(entity.getRegenerationTick() - 1);
		int rate = 20;
		int amount = entity.getHpRegenerationAmount();

		if(entity.getRegenerationTick() <= 0)
		{
			if(entity.getCurrentHunger() <= 20)
			{
				sendMessageToPC(oPC, "You are hungry and not recovering HP naturally. Eat food to start recovering again.");
			}
			else if(getCurrentHitPoints(oPC) < getMaxHitPoints(oPC))
			{
				// CON bonus
				int con = getAbilityModifier(Ability.CONSTITUTION, oPC);
				if(con > 0)
				{
					amount += con;
				}

				entity = FoodSystem.DecreaseHungerLevel(entity, oPC, 3);

				applyEffectToObject(DurationType.INSTANT, effectHeal(amount), oPC, 0.0f);
			}

			entity.setRegenerationTick(rate);
		}

		return entity;
	}

	private PlayerEntity HandleManaRegenerationTick(NWObject oPC, PlayerEntity entity)
	{
		entity.setCurrentManaTick(entity.getCurrentManaTick() - 1);
		int rate = 20;
		int amount = 1;

		if(entity.getCurrentManaTick() <= 0)
		{
			if(entity.getCurrentHunger() <= 20)
			{
				sendMessageToPC(oPC, "You are hungry and not recovering mana naturally. Eat food to start recovering again.");
			}
			else if(entity.getCurrentMana() < entity.getMaxMana())
			{
				// CHA bonus
				int cha = getAbilityModifier(Ability.CHARISMA, oPC);
				if(cha > 0)
				{
					amount += cha;
				}

				entity = AbilitySystem.RestoreMana(oPC, amount, entity);
			}

			entity.setCurrentManaTick(rate);
		}

		return entity;
	}

	private void HandleEffectLifespan(NWObject oPC)
	{
		for(NWEffect effect: getEffects(oPC))
		{
			String tag = getEffectTag(effect);

			if(tag.equals(""))
			{
				tag = "EFFECT_TICKS 40";
			}

			if(!tag.substring(0, 13).equals("EFFECT_TICKS ")) continue;
			if(getEffectDurationType(effect) != DurationType.PERMANENT) continue;

			int ticks = Integer.parseInt(tag.substring(13)) - 1;

			if(ticks <= 0)
			{
				removeEffect(oPC, effect);
			}
			else
			{
				tagEffect(effect, "EFFECT_TICKS " + ticks);
			}
		}
	}
}

