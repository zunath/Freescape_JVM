package Common;

public class Constants {
	// Name of the tag and resref of the PC database item
	public static final String PCDatabaseTag = "database";
	// Name of the unique PC ID number variable (stored on PC's database item)
	public static final String PCIDNumberVariable = "PC_ID_NUMBER";

	public static final int BaseHPRegenAmount = 1;
	public static final int BaseHPRegenRate = 20;
	public static final int BaseManaRegenAmount = 1;
	public static final int BaseManaRegenRate = 20;
	public static final int BaseHungerRate = 60;

	// The following is the number of inventory slots available in NWN
    public static final int NumberOfInventorySlots = 18;

	// Update this to run players through the migration process on entry
	public static final int PlayerVersionNumber = 1;
}
