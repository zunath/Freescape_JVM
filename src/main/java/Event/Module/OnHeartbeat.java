package Event.Module;
import Common.Constants;
import Common.IScriptEventHandler;
import Data.Repository.DatabaseRepository;
import Data.Repository.PlayerRepository;
import Entities.PlayerEntity;
import GameObject.PlayerGO;
import GameSystems.CustomEffectSystem;
import GameSystems.FoodSystem;
import GameSystems.MagicSystem;
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
					entity = HandleRegenerationTick(pc, entity);
					entity = HandleManaRegenerationTick(pc, entity);
					entity = HandleFoodTick(pc, entity);
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
		int rate = Constants.BaseHPRegenRate;
		int amount = entity.getHpRegenerationAmount();

		if(entity.getRegenerationTick() <= 0)
		{
			if(NWScript.getCurrentHitPoints(oPC) < NWScript.getMaxHitPoints(oPC))
			{
				// Safe zones grant +5 HP regen
				boolean isInSafeZone = NWScript.getLocalInt(NWScript.getArea(oPC), "SAFE_ZONE") == 1;
				if(isInSafeZone)
				{
					amount += 5;
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
		int rate = Constants.BaseManaRegenRate;
		int amount = Constants.BaseManaRegenAmount + ((NWScript.getAbilityScore(oPC, Ability.CHARISMA, false) - 10) / 2);

		if(entity.getCurrentManaTick() <= 0)
		{
			if(entity.getCurrentMana() < entity.getMaxMana())
			{
				// Safe zones grant +5 mana regen
				boolean isInSafeZone = NWScript.getLocalInt(NWScript.getArea(oPC), "SAFE_ZONE") == 1;
				if(isInSafeZone)
				{
					amount += 5;
				}

				entity = MagicSystem.RestoreMana(oPC, amount, entity);
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

