package am2.buffs;

import am2.defs.PotionEffectsDefs;
import net.minecraft.entity.EntityLivingBase;

public class BuffEffectInstantMana extends BuffEffect {

	public BuffEffectInstantMana(int duration, int amplifier) {
		super(PotionEffectsDefs.instantMana, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving) {

	}

	@Override
	public void stopEffect(EntityLivingBase entityliving) {

	}

	@Override
	protected String spellBuffName() {
		return "Instant Mana";
	}

}
