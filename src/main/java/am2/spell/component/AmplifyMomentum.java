package am2.spell.component;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.ArsMagica2;
import am2.api.affinity.Affinity;
import am2.api.spell.SpellComponent;
import am2.api.spell.SpellModifiers;
import am2.defs.ItemDefs;
import am2.packet.AMNetHandler;
import am2.particles.AMParticle;
import am2.particles.ParticleFloatUpward;
import am2.utils.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AmplifyMomentum extends SpellComponent {
	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target) {
		double velocity = SpellUtils.countModifiers(SpellModifiers.VELOCITY_ADDED, stack);
		// might be too much
		double bonusX = target.motionX * (0.5 + 0.5 * velocity);
		double bonusY = target.motionY * (0.5 + 0.5 * velocity);
		double bonusZ = target.motionZ * (0.5 + 0.5 * velocity);
		if (target instanceof EntityPlayer) {
			AMNetHandler.INSTANCE.sendVelocityAddPacket(world, (EntityPlayer) target, bonusX, bonusY, bonusZ);
		}
		target.addVelocity(bonusX, bonusY, bonusZ);
		return true;
	}

	@Override
	public float manaCost(EntityLivingBase caster) {
		return 250;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster) {
		return null;
	}

	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.of(SpellModifiers.VELOCITY_ADDED);
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target,
			Random rand, int colorModifier) {
		for (int i = 0; i < 25; ++i) {
			AMParticle particle = (AMParticle) ArsMagica2.proxy.particleManager.spawn(world, "wind", x, y, z);
			if (particle != null) {
				particle.addRandomOffset(1, 2, 1);
				particle.AddParticleController(
						new ParticleFloatUpward(particle, 0, 0.3f + rand.nextFloat() * 0.3f, 1, false));
				particle.setMaxAge(20);
				if (colorModifier > -1) {
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f,
							((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public Set<Affinity> getAffinity() {
		return Sets.newHashSet(Affinity.AIR, Affinity.LIGHTNING);
	}

	// might be way too cheap
	@Override
	public Object[] getRecipe() {
		return new Object[] { new ItemStack(ItemDefs.rune, 1, EnumDyeColor.WHITE.getDyeDamage()),
				new ItemStack(ItemDefs.rune, 1, EnumDyeColor.YELLOW.getDyeDamage()), Items.FIREWORKS, Items.FIREWORKS,
				Items.FIREWORKS, Blocks.SLIME_BLOCK

		};
	}

	@Override
	public float getAffinityShift(Affinity affinity) {
		return 0.01f;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos blockPos, EnumFacing blockFace,
			double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		// TODO Auto-generated method stub
		return false;
	}
}
