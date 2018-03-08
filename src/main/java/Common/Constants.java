package Common;

public class Constants {
	// Name of the tag and resref of the PC database item
	public static final String PCDatabaseTag = "database";
	// Name of the unique PC ID number variable (stored on PC's database item)
	public static final String PCIDNumberVariable = "PC_ID_NUMBER";
    public static final String GunEquippedTemporaryVariable = "GUN_TEMP_GUN_EQUIPPED";

	public static final int BaseHitPoints = 30;
	public static final int BaseStrength = 10;
	public static final int BaseDexterity = 10;
	public static final int BaseConstitution = 10;
	public static final int BaseIntelligence = 10;
	public static final int BaseWisdom = 10;
	public static final int BaseCharisma = 10;

	public static final int BaseHPRegenAmount = 1;
	public static final int BaseHPRegenRate = 20;
	public static final int BaseHungerRate = 60;
	public static final int BaseManaRegenAmount = 1;
	public static final int BaseManaRegenRate = 20;

	// The following is the number of inventory slots available in NWN, not to be confused by the inventory limitations
	// imposed in this module.
    public static final int NumberOfInventorySlots = 18;

	// Update this to run players through the migration process on entry
	public static final int PlayerVersionNumber = 1;
}
