package Placeable.CraftingForge;

public class CraftingForgeCommon {

    public static String GetIngotResref(String oreResref)
    {
        String ingotResref;
        switch (oreResref)
        {
            case "copper_ore":
                ingotResref = "copper_ingot";
                break;
            case "tin_ore":
                ingotResref = "tin_ingot";
                break;
            case "iron_ore":
                ingotResref = "iron_ingot";
                break;
            case "gold_ore":
                ingotResref = "gold_ingot";
                break;
            case "platinum_ore":
                ingotResref = "platinum_ingot";
                break;
            case "adamantium_ore":
                ingotResref = "adamantium_ingot";
                break;
            case "cobalt_ore":
                ingotResref = "cobalt_ingot";
                break;
            case "silver_ore":
                ingotResref = "silver_ingot";
                break;
            case "titanium_ore":
                ingotResref = "titanium_ingot";
                break;
            case "mithril_ore":
                ingotResref = "mithril_ingot";
                break;
            default:
                return "";
        }

        return ingotResref;
    }

    public static int GetIngotLevel(String oreResref)
    {
        int level;
        switch (oreResref)
        {
            case "copper_ore":
                level = 3;
                break;
            case "tin_ore":
                level = 8;
                break;
            case "iron_ore":
                level = 13;
                break;
            case "gold_ore":
                level = 18;
                break;
            case "platinum_ore":
                level = 23;
                break;
            case "adamantium_ore":
                level = 28;
                break;
            case "cobalt_ore":
                level = 33;
                break;
            case "silver_ore":
                level = 38;
                break;
            case "titanium_ore":
                level = 43;
                break;
            case "mithril_ore":
                level = 48;
                break;
            default:
                return -1;
        }

        return level;
    }

    public static int GetIngotPerkLevel(String oreResref)
    {
        int level;
        switch (oreResref)
        {
            case "copper_ore":
            case "coal":
                level = 1;
                break;
            case "tin_ore":
                level = 2;
                break;
            case "iron_ore":
                level = 3;
                break;
            case "gold_ore":
                level = 4;
                break;
            case "platinum_ore":
                level = 5;
                break;
            case "adamantium_ore":
                level = 6;
                break;
            case "cobalt_ore":
                level = 7;
                break;
            case "silver_ore":
                level = 8;
                break;
            case "titanium_ore":
                level = 9;
                break;
            case "mithril_ore":
                level = 10;
                break;
            default:
                return -1;
        }

        return level;
    }
}
