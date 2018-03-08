package GameObject;

import Enumerations.CustomItemProperty;
import GameSystems.DurabilitySystem;
import org.nwnx.nwnx2.jvm.NWItemProperty;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class GunGO {
    private NWObject gun;

    private int gunType;
    private int firepower;
    private int range;
    private int reloadSetting;
    private int ammoType;
    private int rateOfFire;
    private int magazineSize;
    private int criticalRating;
    private boolean isEnhancedAmmoAvailable;
    private boolean isIncendiaryAmmoAvailable;
    private float durability;

    public GunGO(NWObject gun)
    {
        this.gun = gun;
        NWItemProperty[] itemProperties = NWScript.getItemProperties(gun);
        durability = DurabilitySystem.GetItemDurability(gun);

        for(NWItemProperty ipCurItemProperty : itemProperties)
        {
            int iPropertyType = NWScript.getItemPropertyType(ipCurItemProperty);
            int iPropertySubType = NWScript.getItemPropertySubType(ipCurItemProperty);

            // Store the gun type. (GUN_TYPE_*)
            if(iPropertyType == CustomItemProperty.FirearmType)
            {
                gunType = NWScript.getItemPropertySubType(ipCurItemProperty);
            }
            // Store the gun's firepower. This is found in the iprp_firepowcost.2da file.
            else if(iPropertyType == CustomItemProperty.Firepower)
            {
                int iIndex = NWScript.getItemPropertyCostTableValue(ipCurItemProperty);
                firepower = Integer.parseInt(NWScript.get2DAString("iprp_firepowcost", "Label", iIndex));
            }
            // Store the gun's range setting. (IP_CONST_FIREARM_RANGE_*)
            else if(iPropertyType == CustomItemProperty.FirearmRange)
            {
                range = NWScript.getItemPropertySubType(ipCurItemProperty);
            }
            // Store the gun's reload setting. (GUN_RELOAD_SPEED_*)
            else if(iPropertyType == CustomItemProperty.FirearmReloadSpeed)
            {
                reloadSetting = NWScript.getItemPropertySubType(ipCurItemProperty);
            }
            // Store the gun's ammo type. (IP_CONST_FIREARM_AMMO_TYPE_*)
            else if(iPropertyType == CustomItemProperty.FirearmAmmoType)
            {
                ammoType = NWScript.getItemPropertySubType(ipCurItemProperty);
            }
            // Store the gun's rate of fire. (GUN_ATTACK_SPEED_*)
            else if(iPropertyType == CustomItemProperty.FirearmRateOfFire)
            {
                rateOfFire = NWScript.getItemPropertySubType(ipCurItemProperty);
            }
            // Store the gun's magazine capacity
            else if(iPropertyType == CustomItemProperty.FirearmMagazineCapacity)
            {
                magazineSize = NWScript.getItemPropertyCostTableValue(ipCurItemProperty);
            }
            // Store the gun's critical rating
            else if(iPropertyType == CustomItemProperty.FirearmCriticalRating)
            {
                criticalRating = NWScript.getItemPropertyCostTableValue(ipCurItemProperty);
            }
            // Store what bonus ammo types are available to this gun
            else if(iPropertyType == CustomItemProperty.FirearmAdditionalAmmoClasses)
            {
                if(iPropertySubType == 1) isEnhancedAmmoAvailable = true;
                else if(iPropertySubType == 2) isIncendiaryAmmoAvailable = true;
            }
        }
    }

    public NWObject getGun() {
        return gun;
    }

    public void setGun(NWObject gun) {
        this.gun = gun;
    }

    public int getGunType() {
        return gunType;
    }

    public void setGunType(int gunType) {
        this.gunType = gunType;
    }

    public int getFirepower() {
        return firepower;
    }

    public void setFirepower(int firepower) {
        this.firepower = firepower;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getReloadSetting() {
        return reloadSetting;
    }

    public void setReloadSetting(int reloadSetting) {
        this.reloadSetting = reloadSetting;
    }

    public int getAmmoType() {
        return ammoType;
    }

    public void setAmmoType(int ammoType) {
        this.ammoType = ammoType;
    }

    public int getRateOfFire() {
        return rateOfFire;
    }

    public void setRateOfFire(int rateOfFire) {
        this.rateOfFire = rateOfFire;
    }

    public int getMagazineSize() {
        return magazineSize;
    }

    public void setMagazineSize(int magazineSize) {
        this.magazineSize = magazineSize;
    }

    public int getCriticalRating() {
        return criticalRating;
    }

    public void setCriticalRating(int criticalRating) {
        this.criticalRating = criticalRating;
    }

    public boolean isEnhancedAmmoAvailable() {
        return isEnhancedAmmoAvailable;
    }

    public void setEnhancedAmmoAvailable(boolean isEnhancedAmmoAvailable) {
        this.isEnhancedAmmoAvailable = isEnhancedAmmoAvailable;
    }

    public boolean isIncendiaryAmmoAvailable() {
        return isIncendiaryAmmoAvailable;
    }

    public void setIncendiaryAmmoAvailable(boolean isIncendiaryAmmoAvailable) {
        this.isIncendiaryAmmoAvailable = isIncendiaryAmmoAvailable;
    }

    public float getDurability() {
        return durability;
    }

    public void setDurability(float durability) {
        this.durability = durability;
    }
}
