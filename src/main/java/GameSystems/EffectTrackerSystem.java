package GameSystems;

import GameObject.PlayerGO;
import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.DurationType;

import java.util.HashSet;
import java.util.LinkedHashMap;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class EffectTrackerSystem {

    private static LinkedHashMap<String, Integer> effectTicks;

    static
    {
        effectTicks = new LinkedHashMap<>();
    }

    public static void ProcessPCEffects(NWObject oPC)
    {
        HashSet<String> foundIDs = new HashSet<>();

        for(NWEffect effect: getEffects(oPC))
        {
            String pcUUID = new PlayerGO(oPC).getUUID();
            String effectKey = pcUUID + "_" + getEffectType(effect);

            if(getEffectDurationType(effect) != DurationType.PERMANENT) continue;
            if(!getEffectTag(effect).equals("")) continue;

            int ticks = effectTicks.getOrDefault(effectKey, 40) - 1;

            if(ticks <= 0)
            {
                removeEffect(oPC, effect);
                effectTicks.remove(effectKey);
            }
            else
            {
                foundIDs.add(effectKey);
                effectTicks.put(effectKey, ticks);
            }
        }

        Object[] effectArray = effectTicks.keySet().toArray();
        for(int x = effectTicks.size()-1; x >= 0; x--)
        {
            String key = (String)effectArray[x];
            if(!foundIDs.contains(key))
            {
                effectTicks.remove(key);
            }
        }

    }

}
