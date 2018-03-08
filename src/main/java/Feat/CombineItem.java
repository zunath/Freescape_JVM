package Feat;

import Entities.ItemCombinationEntity;
import Helper.ColorToken;
import Common.IScriptEventHandler;
import NWNX.NWNX_Events;
import Data.Repository.ItemCombinationRepository;
import GameSystems.ProgressionSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

import java.util.Objects;

public class CombineItem implements IScriptEventHandler {

    private final String COMBINE_MESSAGE_FAILED = "Combination failed.";
    private final String COMBINE_MESSAGE_LOW_SKILL = "Your mixing skill is too low to combine those items.";


    @Override
    public void runScript(NWObject oPC) {
        ItemCombinationRepository repo = new ItemCombinationRepository();
        NWObject oItemA = NWNX_Events.OnItemUsed_GetItem();
        NWObject oItemB = NWNX_Events.OnItemUsed_GetTarget();
        String resrefA = NWScript.getResRef(oItemA);
        String resrefB = NWScript.getResRef(oItemB);

        int mixingSkill = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_MIXING);

        if(NWScript.getItemPossessor(oItemA) != oPC || NWScript.getItemPossessor(oItemB) != oPC || oItemA == oItemB)
        {
            NWScript.sendMessageToPC(oPC, ColorToken.Red() + COMBINE_MESSAGE_FAILED + ColorToken.End());
            return;
        }

        ItemCombinationEntity entity = repo.getByItemResrefs(resrefA, resrefB);
        if(entity == null)
        {
            NWScript.sendMessageToPC(oPC, ColorToken.Red() + COMBINE_MESSAGE_FAILED + ColorToken.End());
        }
        else if(mixingSkill >= entity.getMixingRequired())
        {
            CombineItemProcess(oPC, oItemA, oItemB, entity, mixingSkill);
        }
        else
        {
            NWScript.sendMessageToPC(oPC, ColorToken.Red() + COMBINE_MESSAGE_LOW_SKILL + ColorToken.End());
        }

    }



    private void CombineItemProcess(NWObject oPC, NWObject oItemA, NWObject oItemB, ItemCombinationEntity entity, int iMixingSkill)
    {
        int iHQQuantity = entity.getHqQuantity();
        int iQuantity = 0;
        String sResrefA = NWScript.getResRef(oItemA);
        String sResrefB = NWScript.getResRef(oItemB);

        // Special quantity codes functionality:
        //     SA: Quantity is the stack size of ItemA
        //     SB: Quantity is the stack size of ItemB
        if(Objects.equals("" + entity.getQuantity(), "SA"))
        {
            if(Objects.equals(sResrefB, entity.getItemA())) iQuantity = NWScript.getItemStackSize(oItemB);
            if(Objects.equals(sResrefA, entity.getItemA())) iQuantity = NWScript.getItemStackSize(oItemA);
            iHQQuantity = iQuantity;
        }
        else if(Objects.equals("" + entity.getQuantity(), "SB"))
        {
            if(Objects.equals(sResrefB, entity.getItemB())) iQuantity = NWScript.getItemStackSize(oItemB);
            if(Objects.equals(sResrefA, entity.getItemB())) iQuantity = NWScript.getItemStackSize(oItemA);
            iHQQuantity = iQuantity;
        }
        // Otherwise use the specified quantities and HQ quantities
        else iQuantity = entity.getQuantity();

        // Safety check
        if(iQuantity < 1) iQuantity = 1;

        if(!entity.isItemAInfinite())
        {
            if(Objects.equals(sResrefB, entity.getItemA()))
                NWScript.destroyObject(oItemB, 0.0f);
            if(Objects.equals(sResrefA, entity.getItemA()))
                NWScript.destroyObject(oItemA, 0.0f);
        }
        if(!entity.isItemBInfinite())
        {
            if(Objects.equals(sResrefB, entity.getItemB()))
                NWScript.destroyObject(oItemB, 0.0f);
            if(Objects.equals(sResrefA, entity.getItemB()))
                NWScript.destroyObject(oItemA, 0.0f);
        }

        String sOutputResref = entity.getOutput();
        boolean bHQMade = false;

        // Check to see if this is an HQ creation
        if(!Objects.equals(entity.getHqOutput(), "") && iHQQuantity > 0 && iMixingSkill > 0)
        {
            // 50% chance to HQ at max mixing level (Level 20)
            int iChance = NWScript.floatToInt(iMixingSkill * 2.5f);
            if(NWScript.random(100) + 1 <= iChance)
            {
                sOutputResref = entity.getHqOutput();
                iQuantity = iHQQuantity;
                bHQMade = true;
            }
        }

        // Mixing skill also allows players to make more of an item than normal, if they aren't making an HQ item already
        // The quantity made increases at a rate of an additional 2.5% per level of mixing.
        if(!bHQMade && iQuantity > 1)
        {
            iQuantity = NWScript.floatToInt(((1 + (0.025f * iMixingSkill)) * iQuantity));
        }

        NWObject oOutput = NWScript.createItemOnObject(sOutputResref, oPC, iQuantity, "");
        int iStackSize = NWScript.getItemStackSize(oOutput);

        // Some items aren't stackable. If that's the case then we need to loop through and create each item
        if(iStackSize == 1)
        {
            iQuantity --;

            int iCurrentItem;
            for(iCurrentItem = 1; iCurrentItem <= iQuantity; iCurrentItem++)
            {
                NWScript.createItemOnObject(sOutputResref, oPC, iQuantity, "");
            }
        }
        // Sometimes the item's stack size will be reached. If that's the case then we need to continue spawning
        // items until all of them have been created
        else if(iStackSize < iQuantity)
        {
            // We've already created the first stack, so remove that from the quantity
            iQuantity = iQuantity - iStackSize;

            while(iQuantity > 0)
            {
                // Still at least as large as the max stack size - create the new stack and reduce quantity
                if(iQuantity >= iStackSize)
                {
                    NWScript.createItemOnObject(sOutputResref, oPC, iStackSize, "");
                    iQuantity = iQuantity - iStackSize;
                }
                // There's no more to create after this stack - create the new stack and set quantity to 0
                else if(iQuantity < iStackSize)
                {
                    NWScript.createItemOnObject(sOutputResref, oPC, iQuantity, "");
                    iQuantity = 0;
                }
            }
        }
    }
    
}
