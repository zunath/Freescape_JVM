package Event.Module;
import Common.IScriptEventHandler;
import Data.Repository.DatabaseRepository;
import Data.Repository.PlayerRepository;
import Entities.PlayerEntity;
import GameObject.PlayerGO;
import GameSystems.AbilitySystem;
import GameSystems.CustomEffectSystem;
import GameSystems.FoodSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Ability;
import org.nwnx.nwnx2.jvm.constants.DurationType;

@SuppressWarnings("unused")
public class OnHeartbeat implements IScriptEventHandler {

	private PlayerRepository playerRepo;

	@Override
	public void runScript(NWObject objSelf) {

		playerRepo = new PlayerRepository();

		NWObject[] players = NWScript.getPCs();
		for(NWObject pc : players)
		{
			if(!NWScript.getIsDM(pc))
			{
				PlayerGO pcGO = new PlayerGO(pc);
				PlayerEntity entity = playerRepo.GetByPlayerID(pcGO.getUUID());

				if(entity != null)
				{
					entity = HandleFoodTick(pc, entity);
					entity = HandleRegenerationTick(pc, entity);
					entity = HandleManaRegenerationTick(pc, entity);
					playerRepo.save(entity);
				}
			}
		}

		SaveCharacters();
        CustomEffectSystem.OnModuleHeartbeat();

		DatabaseRepository databaseRepo = new DatabaseRepository();
		databaseRepo.KeepAlive();
	}

	// Export all characters every minute.
	private void SaveCharacters()
	{
		int currentTick = NWScript.getLocalInt(NWObject.MODULE, "SAVE_CHARACTERS_TICK") + 1;

		if(currentTick >= 10)
		{
			NWScript.exportAllCharacters();
			currentTick = 0;
		}

		NWScript.setLocalInt(NWObject.MODULE, "SAVE_CHARACTERS_TICK", currentTick);
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
				NWScript.sendMessageToPC(oPC, "You are hungry and not recovering HP naturally. Eat food to start recovering again.");
			}
			else if(NWScript.getCurrentHitPoints(oPC) < NWScript.getMaxHitPoints(oPC))
			{
				// CON bonus
				int con = NWScript.getAbilityModifier(Ability.CONSTITUTION, oPC);
				if(con > 0)
				{
					amount += con;
				}

				NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectHeal(amount), oPC, 0.0f);
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
				NWScript.sendMessageToPC(oPC, "You are hungry and not recovering mana naturally. Eat food to start recovering again.");
			}
			else if(entity.getCurrentMana() < entity.getMaxMana())
			{
				// CHA bonus
				int cha = NWScript.getAbilityModifier(Ability.CHARISMA, oPC);
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

	private PlayerEntity HandleFoodTick(NWObject oPC, PlayerEntity entity)
	{
		return FoodSystem.RunHungerCycle(oPC, entity);
	}
}

