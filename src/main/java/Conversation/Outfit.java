package Conversation;

import Dialog.*;
import Entities.PCOutfitEntity;
import GameObject.PlayerGO;
import Helper.ColorToken;
import Data.Repository.PCOutfitRepository;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.InventorySlot;
import org.nwnx.nwnx2.jvm.constants.ItemApprArmorModel;
import org.nwnx.nwnx2.jvm.constants.ItemApprType;

@SuppressWarnings("UnusedDeclaration")
public class Outfit extends DialogBase implements IDialogHandler {

    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");

        DialogPage mainPage = new DialogPage(
                "Please select an option.",
                "Save Outfit",
                "Load Outfit",
                "Back"
        );

        DialogPage saveOutfitPage = new DialogPage(
                "Please select a slot to save the outfit in.\n\nRed slots are unused. Green slots contain stored data. Selecting a green slot will overwrite whatever is in that slot."
        );

        DialogPage loadOutfitPage = new DialogPage(
                "Please select an outfit to load."
        );

        dialog.addPage("MainPage", mainPage);
        dialog.addPage("SaveOutfitPage", saveOutfitPage);
        dialog.addPage("LoadOutfitPage", loadOutfitPage);
        return dialog;
    }

    @Override
    public void Initialize()
    {

    }

    @Override
    public void DoAction(final NWObject oPC, String pageName, int responseID) {

        switch(pageName)
        {
            case "MainPage":
            {
                switch (responseID)
                {
                    case 1: // Save Outfit
                        ShowSaveOutfitOptions();
                        ChangePage("SaveOutfitPage");
                        break;
                    case 2: // Load Outfit
                        ShowLoadOutfitOptions();
                        ChangePage("LoadOutfitPage");
                        break;
                    case 3: // Back
                        Scheduler.assign(oPC, () -> NWScript.actionStartConversation(oPC, "x0_skill_ctrap", true, false));
                        break;
                }
                break;
            }
            case "SaveOutfitPage":
            {
                HandleSaveOutfit(responseID);
                break;
            }
            case "LoadOutfitPage":
            {
                HandleLoadOutfit(responseID);
                break;
            }
        }
    }

    private void HandleSaveOutfit(int responseID)
    {
        DialogResponse response = GetResponseByID("SaveOutfitPage", responseID);
        if(response.getText().equals("Back"))
        {
            ChangePage("MainPage");
            return;
        }


        NWObject oPC = GetPC();
        NWObject oClothes = NWScript.getItemInSlot(InventorySlot.CHEST, oPC);
        PlayerGO pcGO = new PlayerGO(oPC);
        PCOutfitRepository repo = new PCOutfitRepository();
        PCOutfitEntity entity = repo.GetByUUID(pcGO.getUUID());

        if(entity == null)
        {
            entity = new PCOutfitEntity();
            entity.setPlayerID(pcGO.getUUID());
        }

        if(oClothes == NWObject.INVALID)
        {
            NWScript.floatingTextStringOnCreature(ColorToken.Red() + "You do not have clothes equipped." + ColorToken.End(), oPC, false);
            return;
        }

        byte[] clothesData = SCORCO.saveObject(oClothes);
        if(responseID == 1) entity.setOutfit1(clothesData);
        else if(responseID == 2) entity.setOutfit2(clothesData);
        else if(responseID == 3) entity.setOutfit3(clothesData);
        else if(responseID == 4) entity.setOutfit4(clothesData);
        else if(responseID == 5) entity.setOutfit5(clothesData);
        else if(responseID == 6) entity.setOutfit6(clothesData);
        else if(responseID == 7) entity.setOutfit7(clothesData);
        else if(responseID == 8) entity.setOutfit8(clothesData);
        else if(responseID == 9) entity.setOutfit9(clothesData);
        else if(responseID == 10) entity.setOutfit10(clothesData);

        repo.Save(entity);

        ShowSaveOutfitOptions();
    }

    private void HandleLoadOutfit(int responseID)
    {
        DialogResponse response = GetResponseByID("LoadOutfitPage", responseID);

        if(response.getCustomData() == null)
        {
            ChangePage("MainPage");
        }
        else
        {
            NWObject oPC = GetPC();
            PlayerGO pcGO = new PlayerGO(oPC);
            int outfitID = (int)response.getCustomData();
            PCOutfitRepository repo = new PCOutfitRepository();
            PCOutfitEntity entity = repo.GetByUUID(pcGO.getUUID());

            NWObject oTempStorage = NWScript.getObjectByTag("OUTFIT_BARREL", 0);
            NWLocation lLocation = NWScript.getLocation(oTempStorage);
            NWObject oClothes = NWScript.getItemInSlot(InventorySlot.CHEST, oPC);
            NWObject storedClothes = NWObject.INVALID;
            NWScript.setLocalString(oClothes, "TEMP_OUTFIT_UUID", pcGO.getUUID());

            if(outfitID == 1) storedClothes = SCORCO.loadObject(entity.getOutfit1(), lLocation, oTempStorage);
            else if(outfitID == 2) storedClothes = SCORCO.loadObject(entity.getOutfit2(), lLocation, oTempStorage);
            else if(outfitID == 3) storedClothes = SCORCO.loadObject(entity.getOutfit3(), lLocation, oTempStorage);
            else if(outfitID == 4) storedClothes = SCORCO.loadObject(entity.getOutfit4(), lLocation, oTempStorage);
            else if(outfitID == 5) storedClothes = SCORCO.loadObject(entity.getOutfit5(), lLocation, oTempStorage);
            else if(outfitID == 6) storedClothes = SCORCO.loadObject(entity.getOutfit6(), lLocation, oTempStorage);
            else if(outfitID == 7) storedClothes = SCORCO.loadObject(entity.getOutfit7(), lLocation, oTempStorage);
            else if(outfitID == 8) storedClothes = SCORCO.loadObject(entity.getOutfit8(), lLocation, oTempStorage);
            else if(outfitID == 9) storedClothes = SCORCO.loadObject(entity.getOutfit9(), lLocation, oTempStorage);
            else if(outfitID == 10) storedClothes = SCORCO.loadObject(entity.getOutfit10(), lLocation, oTempStorage);

            NWObject oCopy = NWScript.copyItem(oClothes, oTempStorage, true);
            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.LBICEP, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.LBICEP), true);
            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.LBICEP, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.LBICEP), true);

            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.BELT, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.BELT), true);
            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.BELT, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.BELT), true);

            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.LFOOT, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.LFOOT), true);
            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.LFOOT, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.LFOOT), true);

            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.LFOREARM, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.LFOREARM), true);
            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.LFOREARM, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.LFOREARM), true);

            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.LHAND, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.LHAND), true);
            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.LHAND, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.LHAND), true);

            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.LSHIN, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.LSHIN), true);
            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.LSHIN, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.LSHIN), true);

            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.LSHOULDER, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.LSHOULDER), true);
            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.LSHOULDER, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.LSHOULDER), true);

            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.LTHIGH, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.LTHIGH), true);
            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.LTHIGH, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.LTHIGH), true);

            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.NECK, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.NECK), true);
            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.NECK, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.NECK), true);

            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.PELVIS, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.PELVIS), true);
            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.PELVIS, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.PELVIS), true);

            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.RBICEP, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.RBICEP), true);
            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.RBICEP, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.RBICEP), true);

            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.RFOOT, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.RFOOT), true);
            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.RFOOT, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.RFOOT), true);

            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.RFOREARM, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.RFOREARM), true);
            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.RFOREARM, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.RFOREARM), true);

            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.RHAND, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.RHAND), true);
            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.RHAND, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.RHAND), true);

            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.ROBE, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.ROBE), true);
            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.ROBE, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.ROBE), true);

            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.RSHIN, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.RSHIN), true);
            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.RSHIN, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.RSHIN), true);

            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.RSHOULDER, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.RSHOULDER), true);
            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.RSHOULDER, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.RSHOULDER), true);

            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.RTHIGH, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.RTHIGH), true);
            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.RTHIGH, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.RTHIGH), true);

            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.TORSO, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_MODEL, ItemApprArmorModel.TORSO), true);
            oCopy = NWScript.copyItemAndModify(oCopy, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.TORSO, NWScript.getItemAppearance(storedClothes, ItemApprType.ARMOR_COLOR, ItemApprArmorModel.TORSO), true);

            final NWObject oFinal = NWScript.copyItem(oCopy, oPC, true);
            NWScript.deleteLocalString(oFinal, "TEMP_OUTFIT_UUID");
            NWScript.destroyObject(oCopy, 0.0f);
            NWScript.destroyObject(oClothes, 0.0f);
            NWScript.destroyObject(storedClothes, 0.0f);

            Scheduler.assign(oPC, () -> NWScript.actionEquipItem(oFinal, InventorySlot.CHEST));

            for(NWObject item : NWScript.getItemsInInventory(oTempStorage))
            {
                if(NWScript.getLocalString(item, "TEMP_OUTFIT_UUID").equals(pcGO.getUUID()))
                {
                    NWScript.destroyObject(item, 0.0f);
                }
            }

            ShowLoadOutfitOptions();
        }


    }

    private void ShowSaveOutfitOptions()
    {
        DialogPage page = GetPageByName("SaveOutfitPage");
        PlayerGO pcGO = new PlayerGO(GetPC());
        PCOutfitRepository repo = new PCOutfitRepository();
        PCOutfitEntity entity = repo.GetByUUID(pcGO.getUUID());
        if(entity == null) entity = new PCOutfitEntity();

        page.getResponses().clear();

        String responseText = (entity.getOutfit1() == null ? ColorToken.Red() :
                ColorToken.Green()) + "Save in Slot 1" + ColorToken.End();
        page.getResponses().add(new DialogResponse(responseText));

        responseText = (entity.getOutfit2() == null ? ColorToken.Red() :
                ColorToken.Green()) + "Save in Slot 2" + ColorToken.End();
        page.getResponses().add(new DialogResponse(responseText));

        responseText = (entity.getOutfit3() == null ? ColorToken.Red() :
                ColorToken.Green()) + "Save in Slot 3" + ColorToken.End();
        page.getResponses().add(new DialogResponse(responseText));

        responseText = (entity.getOutfit4() == null ? ColorToken.Red() :
                ColorToken.Green()) + "Save in Slot 4" + ColorToken.End();
        page.getResponses().add(new DialogResponse(responseText));

        responseText = (entity.getOutfit5() == null ? ColorToken.Red() :
                ColorToken.Green()) + "Save in Slot 5" + ColorToken.End();
        page.getResponses().add(new DialogResponse(responseText));

        responseText = (entity.getOutfit6() == null ? ColorToken.Red() :
                ColorToken.Green()) + "Save in Slot 6" + ColorToken.End();
        page.getResponses().add(new DialogResponse(responseText));

        responseText = (entity.getOutfit7() == null ? ColorToken.Red() :
                ColorToken.Green()) + "Save in Slot 7" + ColorToken.End();
        page.getResponses().add(new DialogResponse(responseText));

        responseText = (entity.getOutfit8() == null ? ColorToken.Red() :
                ColorToken.Green()) + "Save in Slot 8" + ColorToken.End();
        page.getResponses().add(new DialogResponse(responseText));

        responseText = (entity.getOutfit9() == null ? ColorToken.Red() :
                ColorToken.Green()) + "Save in Slot 9" + ColorToken.End();
        page.getResponses().add(new DialogResponse(responseText));

        responseText = (entity.getOutfit10() == null ? ColorToken.Red() :
                ColorToken.Green()) + "Save in Slot 10" + ColorToken.End();
        page.getResponses().add(new DialogResponse(responseText));

        page.getResponses().add(new DialogResponse("Back"));

    }

    private void ShowLoadOutfitOptions()
    {
        DialogPage page = GetPageByName("LoadOutfitPage");
        PlayerGO pcGO = new PlayerGO(GetPC());
        PCOutfitRepository repo = new PCOutfitRepository();
        PCOutfitEntity entity = repo.GetByUUID(pcGO.getUUID());
        if(entity == null) entity = new PCOutfitEntity();

        page.getResponses().clear();

        if(entity.getOutfit1() != null)
            page.getResponses().add(new DialogResponse("Load from Slot 1", 1));
        if(entity.getOutfit2() != null)
            page.getResponses().add(new DialogResponse("Load from Slot 2", 2));
        if(entity.getOutfit3() != null)
            page.getResponses().add(new DialogResponse("Load from Slot 3", 3));
        if(entity.getOutfit4() != null)
            page.getResponses().add(new DialogResponse("Load from Slot 4", 4));
        if(entity.getOutfit5() != null)
            page.getResponses().add(new DialogResponse("Load from Slot 5", 5));
        if(entity.getOutfit6() != null)
            page.getResponses().add(new DialogResponse("Load from Slot 6", 6));
        if(entity.getOutfit7() != null)
            page.getResponses().add(new DialogResponse("Load from Slot 7", 7));
        if(entity.getOutfit8() != null)
            page.getResponses().add(new DialogResponse("Load from Slot 8", 8));
        if(entity.getOutfit9() != null)
            page.getResponses().add(new DialogResponse("Load from Slot 9", 9));
        if(entity.getOutfit10() != null)
            page.getResponses().add(new DialogResponse("Load from Slot 10", 10));

        page.getResponses().add(new DialogResponse("Back"));
    }


    @Override
    public void EndDialog() {

    }
}
