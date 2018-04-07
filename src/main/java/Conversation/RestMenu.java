package Conversation;

import Data.Repository.SkillRepository;
import Dialog.DialogBase;
import Dialog.DialogPage;
import Dialog.IDialogHandler;
import Dialog.PlayerDialog;
import Entities.PlayerEntity;
import GameObject.PlayerGO;
import GameSystems.SkillSystem;
import Helper.ColorToken;
import Helper.MenuHelper;
import Data.Repository.OverflowItemRepository;
import Data.Repository.PlayerRepository;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import static org.nwnx.nwnx2.jvm.NWScript.*;

@SuppressWarnings("UnusedDeclaration")
public class RestMenu extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {

        PlayerDialog dialog = new PlayerDialog("MainPage");
        DialogPage mainPage = new DialogPage(
                BuildMainPageHeader(oPC),
                ColorToken.Green() + "Open Overflow Inventory" + ColorToken.End(),
                "View Skills",
                "View Perks",
                "View Blueprints",
                "Dice Bag",
                "Emote Menu",
                "View Key Items",
                "Modify Clothes",
                "Character Management",
                "Open Trash Can (Destroy Items)");

        dialog.addPage("MainPage", mainPage);

        return dialog;
    }

    @Override
    public void Initialize()
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        OverflowItemRepository repo = new OverflowItemRepository();
        long overflowCount = repo.GetPlayerOverflowItemCount(pcGO.getUUID());

        if(overflowCount <= 0)
        {
            SetResponseVisible("MainPage", 1, false);
        }
    }

    @Override
    public void DoAction(final NWObject oPC, String pageName, int responseID) {
        switch (pageName) {
            case "MainPage":
                switch (responseID) {
                    // Open Overflow Inventory
                    case 1:
                        final NWObject storage = createObject(ObjectType.PLACEABLE, "overflow_storage", getLocation(oPC), false, "");
                        Scheduler.assign(oPC, () -> actionInteractObject(storage));
                        break;
                    // View Skills
                    case 2:
                        SwitchConversation("ViewSkills");
                        break;
                    // View Perks
                    case 3:
                        SwitchConversation("ViewPerks");
                        break;
                    // View Blueprints
                    case 4:
                        SwitchConversation("ViewBlueprints");
                        break;
                    // Dice Bag
                    case 5:
                        setLocalObject(oPC, "dmfi_univ_target", oPC);
                        setLocalLocation(oPC, "dmfi_univ_location", getLocation(oPC));
                        setLocalString(oPC, "dmfi_univ_conv", "pc_dicebag");
                        Scheduler.assign(oPC, () -> {
                            clearAllActions(false);
                            actionStartConversation(oPC, "dmfi_universal", true, false);
                        });
                        break;
                    // Emote Menu
                    case 6:
                        setLocalObject(oPC, "dmfi_univ_target", oPC);
                        setLocalLocation(oPC, "dmfi_univ_location", getLocation(oPC));
                        setLocalString(oPC, "dmfi_univ_conv", "pc_emote");
                        Scheduler.assign(oPC, () -> {
                            clearAllActions(false);
                            actionStartConversation(oPC, "dmfi_universal", true, false);
                        });
                        break;
                    // Key Item Categories Page
                    case 7:
                        SwitchConversation("KeyItems");
                        break;
                    // Modify Clothes
                    case 8:
                        Scheduler.assign(oPC, () -> actionStartConversation(oPC, "x0_skill_ctrap", true, false));
                        break;
                    // Character Management
                    case 9:
                        SwitchConversation("CharacterManagement");
                        break;
                    // Open Trash Can (Destroy Items)
                    case 10:
                        EndConversation();
                        NWObject trashCan = createObject(ObjectType.PLACEABLE, "reo_trash_can", getLocation(oPC), false, "");
                        Scheduler.assignNow(oPC, () -> actionInteractObject(trashCan));
                        Scheduler.delay(trashCan, 200, () ->setUseableFlag(trashCan, false));
                        break;
                }
                break;

        }
    }

    @Override
    public void EndDialog()
    {
    }

    private String BuildMainPageHeader(NWObject oPC)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerRepository playerRepo = new PlayerRepository();
        SkillRepository skillRepo = new SkillRepository();
        PlayerEntity playerEntity = playerRepo.GetByPlayerID(pcGO.getUUID());
        Integer totalSkillCount = skillRepo.GetPCTotalSkillCount(pcGO.getUUID());

        String header = ColorToken.Green() + "Name: " + ColorToken.End() + getName(oPC, false) + "\n\n";
        header += ColorToken.Green() + "Skill Points: " + ColorToken.End() + totalSkillCount + " / " + SkillSystem.SkillCap + "\n";
        header += ColorToken.Green() + "Unallocated SP: " + ColorToken.End() + playerEntity.getUnallocatedSP() + "\n";
        header += ColorToken.Green() + "Hunger:   " + ColorToken.End() + MenuHelper.BuildBar(playerEntity.getCurrentHunger(), playerEntity.getMaxHunger(), 100) + "\n";
        header += ColorToken.Green() + "Mana:      " + ColorToken.End() + MenuHelper.BuildBar(playerEntity.getCurrentMana(), playerEntity.getMaxMana(), 100, ColorToken.Custom(32, 223, 219)) + "\n";

        return header;
    }


}
