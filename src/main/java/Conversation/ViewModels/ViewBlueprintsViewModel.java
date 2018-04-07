package Conversation.ViewModels;

import Entities.CraftBlueprintCategoryEntity;
import Entities.CraftBlueprintEntity;
import Entities.StructureBlueprintEntity;
import Entities.StructureCategoryEntity;

import java.util.List;

public class ViewBlueprintsViewModel {

    private List<CraftBlueprintEntity> craftBlueprints;

    private List<CraftBlueprintCategoryEntity> craftCategories;

    private List<StructureBlueprintEntity> structureBlueprints;

    private List<StructureCategoryEntity> structureCategories;

    private int mode;

    public List<CraftBlueprintEntity> getCraftBlueprints() {
        return craftBlueprints;
    }

    public void setCraftBlueprints(List<CraftBlueprintEntity> craftBlueprints) {
        this.craftBlueprints = craftBlueprints;
    }

    public List<StructureBlueprintEntity> getStructureBlueprints() {
        return structureBlueprints;
    }

    public void setStructureBlueprints(List<StructureBlueprintEntity> structureBlueprints) {
        this.structureBlueprints = structureBlueprints;
    }

    public List<CraftBlueprintCategoryEntity> getCraftCategories() {
        return craftCategories;
    }

    public void setCraftCategories(List<CraftBlueprintCategoryEntity> craftCategories) {
        this.craftCategories = craftCategories;
    }

    public List<StructureCategoryEntity> getStructureCategories() {
        return structureCategories;
    }

    public void setStructureCategories(List<StructureCategoryEntity> structureCategories) {
        this.structureCategories = structureCategories;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
