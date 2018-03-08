package Event.Module;
import Entities.PlayerEntity;
import GameObject.PlayerGO;
import Common.IScriptEventHandler;
import Data.Repository.PlayerRepository;
import GameSystems.ActivityLoggingSystem;
import GameSystems.RadioSystem;
import org.nwnx.nwnx2.jvm.*;

@SuppressWarnings("unused")
public class OnClientLeave implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {
		RadioSystem radioSystem = new RadioSystem();
		NWObject pc = NWScript.getExitingObject();

		if(NWScript.getIsPC(pc))
        {
            NWScript.exportSingleCharacter(pc);
        }

		SaveCharacter(pc);
		ActivityLoggingSystem.OnModuleClientLeave();
	}

	private void SaveCharacter(NWObject pc) {

		if(NWScript.getIsDM(pc)) return;

		PlayerGO gameObject = new PlayerGO(pc);
		PlayerRepository repo = new PlayerRepository();
		String uuid = gameObject.getUUID();

		PlayerEntity entity = repo.GetByPlayerID(uuid);
		entity.setCharacterName(NWScript.getName(pc, false));
		entity.setHitPoints(NWScript.getCurrentHitPoints(pc));

		repo.save(entity);
	}
}
