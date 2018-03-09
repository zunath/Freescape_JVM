package Helper;

import org.nwnx.nwnx2.jvm.NWScript;

public class MenuHelper {

    public static String BuildBar(int currentValue, int requiredValue, int numberOfBars, String colorToken)
    {
        String xpBar = "";
        int highlightedBars = NWScript.floatToInt((float) currentValue / (float) requiredValue * numberOfBars);

        for(int bar = 1; bar <= numberOfBars; bar++)
        {
            if(bar <= highlightedBars)
            {
                xpBar += colorToken + "|" + ColorToken.End();
            }
            else
            {
                xpBar += ColorToken.White() + "|" + ColorToken.End();
            }
        }


        return xpBar;
    }

    public static String BuildBar(int currentValue, int requiredValue, int numberOfBars)
    {
        return BuildBar(currentValue, requiredValue, numberOfBars, ColorToken.Orange());
    }

}
