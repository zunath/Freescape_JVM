package Conversation;

import Dialog.*;
import Entities.*;
import GameObject.PlayerGO;
import Helper.ColorToken;
import Conversation.ViewModels.TerritoryFlagMenuModel;
import Data.Repository.PlayerRepository;
import Data.Repository.StructureRepository;
import GameSystems.StructureSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class TerritoryFlag extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");
        DialogPage mainPage = new DialogPage(
                "<SET LATER>",
                "Manage Permissions",
                "Transfer Territory Ownership",
                ColorToken.Red() + "Raze Territory" + ColorToken.End()
        );

        DialogPage managePermissionsPage = new DialogPage(
                ColorToken.Green() + "Manage Permissions" + ColorToken.End() + "\n\nPlease select an option.",
                "Change Build Privacy",
                "Change Player Permissions",
                "Back"
        );

        DialogPage changeBuildPrivacyPage = new DialogPage(
                "<SET LATER>",
                "Set Privacy: Owner Only",
                "Set Privacy: Friends Only",
                "Set Privacy: Public",
                "Back"
        );

        DialogPage playerPermissionsPage = new DialogPage(
                ColorToken.Green() + "Manage Player Permissions" + ColorToken.End() + "\n\n" +
                        "Permissions enable other players to perform actions in this territory.\n\n" +
                        "If you use this feature please be sure to set the territory's privacy setting to " +
                        "'Friends Only' or else the changes you make here won't have any effect."
        );

        DialogPage managePlayerPermissionsPage = new DialogPage(
                "<SET LATER>"
        );
        DialogPage addPlayerPermissionList = new DialogPage(
                "Please select a user you to which you wish to give permissions."
        );

        DialogPage razeTerritoryPage = new DialogPage(
                ColorToken.Red() + "WARNING: " + ColorToken.End() + "You have chosen to raze this entire territory. This will delete all structures, construction sites, stored items and the territory marker permanently.\n\n" +
                        "This action cannot be undone! Are you sure you want to raze this territory?",
                ColorToken.Red() + "Confirm Raze Territory" + ColorToken.End(),
                "Back"
        );

        DialogPage transferOwnershipPage = new DialogPage(
                ColorToken.Red() + "WARNING: " + ColorToken.End() + "You are about to transfer this territory and all of the structures tied to it to another player. " +
                        "All items in containers will be transferred and the friends list will be reset.\n\n" +
                        "Please select a player from the list below to begin the transfer."
        );

        DialogPage confirmTransferOwnershipPage = new DialogPage(
                "<SET LATER>",
                "Confirm Transfer Territory",
                "Back"
        );

        dialog.addPage("MainPage", mainPage);
        dialog.addPage("ManagePermissionsPage", managePermissionsPage);
        dialog.addPage("PlayerPermissionsPage", playerPermissionsPage);
        dialog.addPage("ManagePlayerPermissionsPage", managePlayerPermissionsPage);
        dialog.addPage("AddPlayerPermissionsListPage", addPlayerPermissionList);
        dialog.addPage("ChangeBuildPrivacyPage", changeBuildPrivacyPage);
        dialog.addPage("RazeTerritoryPage", razeTerritoryPage);
        dialog.addPage("TransferOwnershipPage", transferOwnershipPage);
        dialog.addPage("ConfirmTransferOwnershipPage", confirmTransferOwnershipPage);
        return dialog;
    }

    @Override
    public void Initialize() {
        int flagID = StructureSystem.GetTerritoryFlagID(GetDialogTarget());
        TerritoryFlagMenuModel model = new TerritoryFlagMenuModel();
        model.setFlagID(flagID);
        SetDialogCustomData(model);

        StructureRepository structureRepo = new StructureRepository();
        PCTerritoryFlagEntity flag = structureRepo.GetPCTerritoryFlagByID(flagID);
        int vanityCount = structureRepo.GetNumberOfStructuresInTerritory(flagID, true, false);
        int specialCount = structureRepo.GetNumberOfStructuresInTerritory(flagID, false, true);

        String header = ColorToken.Green() + "Territory Management Menu" + ColorToken.End() + "\n\n"
                + ColorToken.Green() + "Vanity Slots: " + ColorToken.End() + vanityCount + " / " + flag.getBlueprint().getVanityCount() + "\n"
                + ColorToken.Green() + "Special Slots: " + ColorToken.End() + specialCount + " / " + flag.getBlueprint().getSpecialCount() + "\n"
                + "Please select an option.";
        SetPageHeader("MainPage", header);

    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {
        switch(pageName)
        {
            case "MainPage":
                HandleMainPageResponse(responseID);
                break;
            case "ManagePermissionsPage":
                HandleManagePermissionsResponse(responseID);
                break;
            case "PlayerPermissionsPage":
                HandlePlayerPermissionsResponse(responseID);
                break;
            case "ManagePlayerPermissionsPage":
                HandleManageUserPermissionsResponse(responseID);
                break;
            case "AddPlayerPermissionsListPage":
                HandleAddPlayerPermissionsListResponse(responseID);
                break;
            case "ChangeBuildPrivacyPage":
                HandleChangeBuildPrivacyResponse(responseID);
                break;
            case "RazeTerritoryPage":
                HandleRazeTerritoryResponse(responseID);
                break;
            case "TransferOwnershipPage":
                HandleTransferOwnershipResponse(responseID);
                break;
            case "ConfirmTransferOwnershipPage":
                HandleConfirmTransferOwnershipResponse(responseID);
                break;
        }
    }

    @Override
    public void EndDialog() {

    }

    private TerritoryFlagMenuModel GetModel()
    {
        return (TerritoryFlagMenuModel)GetDialogCustomData();
    }

    private String BuildChangePrivacyHeader()
    {
        StructureRepository repo = new StructureRepository();
        int flagID = StructureSystem.GetTerritoryFlagID(GetDialogTarget());
        PCTerritoryFlagEntity entity = repo.GetPCTerritoryFlagByID(flagID);

        String header = ColorToken.Green() + "Current Build Privacy: " + ColorToken.End() + entity.getBuildPrivacy().getName() + "\n\n";
        header += ColorToken.Green() + "Owner Only: " + ColorToken.End() + "Only the owner of this territory may build and manipulate structures at this territory.\n";
        header += ColorToken.Green() + "Friends Only: " + ColorToken.End() + "Only you and the people you mark as friends may build and manipulate structures at this territory.\n";
        header += ColorToken.Green() + "Public: " + ColorToken.End() + "Anyone may build and manipulate structures at this territory.";

        return header;
    }

    private void HandleMainPageResponse(int responseID)
    {
        switch (responseID)
        {
            case 1: // Manage Permissions
                ChangePage("ManagePermissionsPage");
                break;
            case 2: // Transfer Ownership
                LoadTransferOwnershipResponses();
                ChangePage("TransferOwnershipPage");
                break;
            case 3: // Raze Territory
                ChangePage("RazeTerritoryPage");
                break;
        }
    }

    private void HandleManagePermissionsResponse(int responseID)
    {
        switch (responseID)
        {
            case 1: // Change Build Privacy
                SetPageHeader("ChangeBuildPrivacyPage", BuildChangePrivacyHeader());
                ChangePage("ChangeBuildPrivacyPage");
                break;
            case 2: // Change Player Permissions
                BuildChangePlayerPermissionsResponses();
                ChangePage("PlayerPermissionsPage");
                break;
            case 3: // Back
                ChangePage("MainPage");
                break;
        }
    }

    private void BuildChangePlayerPermissionsResponses()
    {
        TerritoryFlagMenuModel model = GetModel();
        StructureRepository repo = new StructureRepository();
        DialogPage page = GetPageByName("PlayerPermissionsPage");
        List<PCTerritoryFlagPermissionEntity> permissions = repo.GetPermissionsByFlagID(model.getFlagID());
        ArrayList<String> addedKeys = new ArrayList<>();
        page.getResponses().clear();

        page.addResponse(ColorToken.Green() + "Add Player" + ColorToken.End(), true);

        for(PCTerritoryFlagPermissionEntity perm : permissions)
        {
            if(!addedKeys.contains(perm.getPlayer().getPCID()))
            {
                addedKeys.add(perm.getPlayer().getPCID());
                page.addResponse("Manage Permissions: " + perm.getPlayer().getCharacterName(), true, perm.getPlayer().getPCID());
            }
        }

        page.addResponse("Back", true);
    }

    private void HandlePlayerPermissionsResponse(int responseID)
    {
        DialogResponse response = GetResponseByID("PlayerPermissionsPage", responseID);
        TerritoryFlagMenuModel model = GetModel();

        switch (responseID)
        {
            case 1: // Add Player
                model.setIsAddingPermission(true);
                BuildAddPlayerPermissionsListResponses();
                ChangePage("AddPlayerPermissionsListPage");
                break;
            default:
                if(response.getCustomData() == null)
                {
                    ChangePage("ManagePermissionsPage");
                }
                else
                {
                    model.setIsAddingPermission(false);
                    model.setActivePermissionsUUID((String) response.getCustomData());
                    BuildManagePlayerPermissionsHeader();
                    BuildManagePlayerPermissionsResponses();
                    ChangePage("ManagePlayerPermissionsPage");
                }

                break;
        }
    }

    private void BuildAddPlayerPermissionsListResponses()
    {
        TerritoryFlagMenuModel model = GetModel();
        StructureRepository repo = new StructureRepository();
        DialogPage page = GetPageByName("AddPlayerPermissionsListPage");
        page.getResponses().clear();
        List<PCTerritoryFlagPermissionEntity> existingPermissions = repo.GetPermissionsByFlagID(model.getFlagID());
        ArrayList<String> existingUUIDs = new ArrayList<>();

        for(PCTerritoryFlagPermissionEntity perm : existingPermissions)
        {
            if(!existingUUIDs.contains(perm.getPlayer().getPCID()))
            {
                existingUUIDs.add(perm.getPlayer().getPCID());
            }
        }

        for(NWObject oPC : NWScript.getPCs())
        {
            if(!oPC.equals(GetPC()) && !NWScript.getIsDM(oPC)) {
                PlayerGO pcGO = new PlayerGO(oPC);
                String message = "Add Permissions: ";
                if (existingUUIDs.contains(pcGO.getUUID())) {
                    message = "Manage Permissions: ";
                }

                page.addResponse(message + NWScript.getName(oPC, false), true, pcGO.getUUID());
            }
        }

        page.addResponse("Back", true);
    }

    private void HandleAddPlayerPermissionsListResponse(int responseID)
    {
        DialogResponse response = GetResponseByID("AddPlayerPermissionsListPage", responseID);
        TerritoryFlagMenuModel model = GetModel();

        if(response.getCustomData() == null)
        {
            BuildChangePlayerPermissionsResponses();
            ChangePage("PlayerPermissionsPage");
        }
        else
        {
            model.setActivePermissionsUUID((String)response.getCustomData());
            BuildManagePlayerPermissionsHeader();
            BuildManagePlayerPermissionsResponses();
            ChangePage("ManagePlayerPermissionsPage");
        }
    }

    private void BuildManagePlayerPermissionsHeader()
    {
        TerritoryFlagMenuModel model = GetModel();
        PlayerRepository playerRepo = new PlayerRepository();
        StructureRepository structureRepo = new StructureRepository();
        PlayerEntity playerEntity = playerRepo.GetByPlayerID(model.getActivePermissionsUUID());
        List<PCTerritoryFlagPermissionEntity> permissions = structureRepo.GetPermissionsByPlayerID(model.getActivePermissionsUUID(), model.getFlagID());

        String header = ColorToken.Green() + "Manage Player Permissions" + ColorToken.End() + "\n\n";
        header += ColorToken.Green() + "Player: " + ColorToken.End() + playerEntity.getCharacterName() + "\n\n";
        header += ColorToken.Green() + "Current Permissions:\n\n" + ColorToken.End();

        if(permissions == null || permissions.size() <= 0)
            header += "No permissions have been set for this player.";
        else
        {
            for(PCTerritoryFlagPermissionEntity perm : permissions)
            {
                header += perm.getPermission().getName() + "\n";
            }
        }

        SetPageHeader("ManagePlayerPermissionsPage", header);
    }

    private boolean DoesPlayerPermissionsContainPermission(List<PCTerritoryFlagPermissionEntity> perms, TerritoryFlagPermissionEntity searchPerm)
    {
        if(perms == null) return false;

        for(PCTerritoryFlagPermissionEntity perm : perms)
        {
            if(perm.getPermission().getTerritoryFlagPermissionID() == searchPerm.getTerritoryFlagPermissionID())
            {
                return true;
            }
        }

        return false;
    }

    private void BuildManagePlayerPermissionsResponses()
    {
        TerritoryFlagMenuModel model = GetModel();
        PlayerRepository playerRepo = new PlayerRepository();
        StructureRepository structureRepo = new StructureRepository();
        PlayerEntity playerEntity = playerRepo.GetByPlayerID(model.getActivePermissionsUUID());
        List<PCTerritoryFlagPermissionEntity> playerPermissions = structureRepo.GetPermissionsByPlayerID(model.getActivePermissionsUUID(), model.getFlagID());
        List<TerritoryFlagPermissionEntity> permissions = structureRepo.GetAllTerritorySelectablePermissions();
        DialogPage page = GetPageByName("ManagePlayerPermissionsPage");
        page.getResponses().clear();

        for(TerritoryFlagPermissionEntity perm : permissions)
        {
            boolean hasPermission = DoesPlayerPermissionsContainPermission(playerPermissions, perm);

            if(hasPermission)
            {
                page.addResponse("Remove Permission: " + perm.getName(), true, perm.getTerritoryFlagPermissionID());
            }
            else
            {
                page.addResponse("Add Permission: " + perm.getName(), true, perm.getTerritoryFlagPermissionID());
            }

        }

        page.addResponse("Back", true);
    }

    private void HandleManageUserPermissionsResponse(int responseID)
    {
        TerritoryFlagMenuModel model = GetModel();
        DialogResponse response = GetResponseByID("ManagePlayerPermissionsPage", responseID);

        if(response.getCustomData() == null)
        {
            if(model.isAddingPermission())
            {
                ChangePage("AddPlayerPermissionsListPage");
            }
            else
            {
                BuildChangePlayerPermissionsResponses();
                ChangePage("PlayerPermissionsPage");
            }
            return;
        }

        int permissionID = (int)response.getCustomData();
        StructureRepository repo = new StructureRepository();
        List<PCTerritoryFlagPermissionEntity> pcPermissions = repo.GetPermissionsByPlayerID(model.getActivePermissionsUUID(), model.getFlagID());
        PCTerritoryFlagPermissionEntity foundPerm = null;

        for(PCTerritoryFlagPermissionEntity perm : pcPermissions)
        {
            if(perm.getPermission().getTerritoryFlagPermissionID() == permissionID)
            {
                foundPerm = perm;
                break;
            }
        }

        if(foundPerm == null)
        {
            PlayerRepository playerRepo = new PlayerRepository();
            PCTerritoryFlagPermissionEntity perm = new PCTerritoryFlagPermissionEntity();
            perm.setPlayer(playerRepo.GetByPlayerID(model.getActivePermissionsUUID()));
            perm.setPermission(repo.GetTerritoryPermissionByID(permissionID));
            perm.setPcTerritoryFlag(repo.GetPCTerritoryFlagByID(model.getFlagID()));

            repo.Save(perm);
        }
        else
        {
            repo.Delete(foundPerm);
        }

        BuildManagePlayerPermissionsHeader();
        BuildManagePlayerPermissionsResponses();
    }


    private void HandleChangeBuildPrivacyResponse(int responseID)
    {
        switch (responseID)
        {
            case 4:
                ChangePage("ManagePermissionsPage");
                break;
            default:
                StructureRepository repo = new StructureRepository();
                TerritoryFlagMenuModel model = GetModel();
                PCTerritoryFlagEntity entity = repo.GetPCTerritoryFlagByID(model.getFlagID());
                entity.getBuildPrivacy().setBuildPrivacyTypeID(responseID);
                repo.Save(entity);
                SetPageHeader("ChangeBuildPrivacyPage", BuildChangePrivacyHeader());
                break;
        }
    }

    private void HandleRazeTerritoryResponse(int responseID)
    {
        TerritoryFlagMenuModel model = GetModel();
        switch (responseID)
        {
            case 1: // Confirm / REALLY CONFIRM
                if(model.isConfirmingTerritoryRaze())
                {
                    StructureSystem.RazeTerritory(GetDialogTarget());
                    NWScript.floatingTextStringOnCreature(ColorToken.Red() + "Territory razed!" + ColorToken.End(), GetPC(), false);
                    EndConversation();
                }
                else
                {
                    model.setIsConfirmingTerritoryRaze(true);
                    SetResponseText("RazeTerritoryPage", 1, ColorToken.Red() + "REALLY CONFIRM RAZE TERRITORY" + ColorToken.End());
                }

                break;
            case 2: // Back
                model.setIsConfirmingTerritoryRaze(false);
                SetResponseText("RazeTerritoryPage", 1, ColorToken.Red() + "Confirm Raze Territory" + ColorToken.End());
                ChangePage("MainPage");
                break;
        }
    }


    private void LoadTransferOwnershipResponses()
    {
        DialogPage page = GetPageByName("TransferOwnershipPage");
        page.getResponses().clear();

        for(NWObject player : NWScript.getPCs())
        {
            if(!player.equals(GetPC()) && !NWScript.getIsDM(player))
            {
                PlayerGO pcGO = new PlayerGO(player);
                page.addResponse("Transfer Ownership: " + NWScript.getName(player, false), true, pcGO.getUUID());
            }
        }

        page.addResponse("Back", true);
    }

    private void HandleTransferOwnershipResponse(int responseID)
    {
        DialogResponse response = GetResponseByID("TransferOwnershipPage", responseID);
        if(response.getCustomData() == null)
        {
            ChangePage("MainPage");
            return;
        }

        TerritoryFlagMenuModel model = GetModel();
        String uuid = (String)response.getCustomData();
        model.setTransferUUID(uuid);
        BuildConfirmTransferOwnershipHeader();
        ChangePage("ConfirmTransferOwnershipPage");
    }

    private void BuildConfirmTransferOwnershipHeader()
    {
        TerritoryFlagMenuModel model = GetModel();
        PlayerRepository repo = new PlayerRepository();
        PlayerEntity entity = repo.GetByPlayerID(model.getTransferUUID());

        String header = ColorToken.Red() + "WARNING: " + ColorToken.End() + "You are about to transfer ownership of this territory. This is your last chance to cancel this action.\n\n";
        header += "This territory will be transferred to the following player: " + ColorToken.Green() + entity.getCharacterName() + ColorToken.End() + "\n\n";
        header += "Are you sure you want to transfer this territory?";
        SetPageHeader("ConfirmTransferOwnershipPage", header);
    }

    private void HandleConfirmTransferOwnershipResponse(int responseID)
    {
        TerritoryFlagMenuModel model = GetModel();
        switch (responseID)
        {
            case 1: // Confirm / REALLY CONFIRM
                if(model.isConfirmingTransferTerritory())
                {
                    StructureSystem.TransferTerritoryOwnership(GetDialogTarget(), model.getTransferUUID());
                    EndConversation();
                }
                else
                {
                    model.setIsConfirmingTransferTerritory(true);
                    SetResponseText("ConfirmTransferOwnershipPage", 1, "REALLY CONFIRM TRANSFER TERRITORY");
                }

                break;
            case 2: // Back
                SetResponseText("ConfirmTransferOwnershipPage", 1, "Confirm Transfer Territory");
                model.setIsConfirmingTerritoryRaze(false);
                LoadTransferOwnershipResponses();
                ChangePage("TransferOwnershipPage");
                break;
        }

    }

}
