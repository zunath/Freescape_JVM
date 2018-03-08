package Dialog;

import org.nwnx.nwnx2.jvm.NWObject;

public interface IDialogHandler {
    PlayerDialog SetUp(NWObject oPC);
    void Initialize();
    void DoAction(NWObject oPC, String pageName, int responseID);
    void EndDialog();
}
