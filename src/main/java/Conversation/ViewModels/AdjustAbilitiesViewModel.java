package Conversation.ViewModels;

import Entities.AbilityCategoryEntity;
import Entities.PCEquippedAbilityEntity;

import java.util.List;

public class AdjustAbilitiesViewModel {

    private int numberOfAbilitySlots;

    private boolean isEquipping;

    private int currentSlot;

    private PCEquippedAbilityEntity equippedAbilities;

    private List<AbilityCategoryEntity> categories;

    private int selectedAbilityID;

    public int getNumberOfAbilitySlots() {
        return numberOfAbilitySlots;
    }

    public void setNumberOfAbilitySlots(int numberOfAbilitySlots) {
        this.numberOfAbilitySlots = numberOfAbilitySlots;
    }

    public boolean isEquipping() {
        return isEquipping;
    }

    public void setEquipping(boolean equipping) {
        isEquipping = equipping;
    }

    public int getCurrentSlot() {
        return currentSlot;
    }

    public void setCurrentSlot(int currentSlot) {
        this.currentSlot = currentSlot;
    }

    public PCEquippedAbilityEntity getEquippedAbilities() {
        return equippedAbilities;
    }

    public void setEquippedAbilities(PCEquippedAbilityEntity equippedAbilities) {
        this.equippedAbilities = equippedAbilities;
    }

    public List<AbilityCategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<AbilityCategoryEntity> categories) {
        this.categories = categories;
    }

    public int getSelectedAbilityID() {
        return selectedAbilityID;
    }

    public void setSelectedAbilityID(int selectedAbilityID) {
        this.selectedAbilityID = selectedAbilityID;
    }
}
