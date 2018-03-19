package CustomEffect;

import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.Ability;
import org.nwnx.nwnx2.jvm.constants.DurationType;

import java.util.concurrent.ThreadLocalRandom;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class FoodDiseaseEffect implements ICustomEffectHandler {
    @Override
    public void Apply(NWObject oCaster, NWObject oTarget) {
        NWEffect effect = effectAbilityDecrease(Ability.STRENGTH, ThreadLocalRandom.current().nextInt(5)+1);
        effect = effectLinkEffects(effect, effectAbilityDecrease(Ability.CONSTITUTION, ThreadLocalRandom.current().nextInt(5)+1));
        effect = effectLinkEffects(effect, effectAbilityDecrease(Ability.DEXTERITY, ThreadLocalRandom.current().nextInt(5)+1));
        effect = effectLinkEffects(effect, effectAbilityDecrease(Ability.INTELLIGENCE, ThreadLocalRandom.current().nextInt(5)+1));
        effect = effectLinkEffects(effect, effectAbilityDecrease(Ability.WISDOM, ThreadLocalRandom.current().nextInt(5)+1));
        effect = effectLinkEffects(effect, effectAbilityDecrease(Ability.CHARISMA, ThreadLocalRandom.current().nextInt(5)+1));

        effect = tagEffect(effect, "FOOD_DISEASE_EFFECT");
        applyEffectToObject(DurationType.PERMANENT, effect, oTarget, 0.0f);
    }

    @Override
    public void Tick(NWObject oCaster, NWObject oTarget) {

    }

    @Override
    public void WearOff(NWObject oCaster, NWObject oTarget) {
        NWEffect effects[] = getEffects(oTarget);
        for(NWEffect effect: effects)
        {
            if(getEffectTag(effect).equals("FOOD_DISEASE_EFFECT"))
            {
                removeEffect(oTarget, effect);
            }
        }
    }
}
