package am2.spell.component;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.ArsMagica2;
import am2.api.affinity.Affinity;
import am2.api.spell.SpellComponent;
import am2.api.spell.SpellModifiers;
import am2.particles.AMParticle;
import am2.particles.ParticleHoldPosition;
import am2.extensions.EntityExtension;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Repair extends SpellComponent {

	@Override
	public Object[] getRecipe() {
		return new Object[] { new ItemStack(Blocks.ANVIL), new ItemStack(Items.CLOCK) };
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target) {
		if (target instanceof EntityPlayer && !world.isRemote) {
			ItemStack[] armorList = ((EntityPlayer) target).inventory.armorInventory;
			ItemStack mainHand = ((EntityPlayer) target).getHeldItemMainhand();
			ItemStack offHand = ((EntityPlayer) target).getHeldItemOffhand();
			for (ItemStack armor : armorList) {
				int armorDamage = armor.getItemDamage();
				int armorCost = armorDamage * 5;
				if (EntityExtension.For(caster).getCurrentMana() >= armorCost) {
					EntityExtension.For(caster).deductMana(armorCost);
					armor.setItemDamage(0);
				} else {
					// OOM
					return true;
				}
			}
			if (mainHand != null && mainHand != stack) {
				repairStack(mainHand, caster, 5);
			}
			if (offHand != null && offHand != stack) {
				repairStack(offHand, caster, 5);
			}
			return true;

		} else if (target instanceof EntityCreature) {
			Iterable<ItemStack> armorList = target.getArmorInventoryList();
			ItemStack mainHand = ((EntityCreature) target).getHeldItemMainhand();
			ItemStack offHand = ((EntityCreature) target).getHeldItemOffhand();
			for (ItemStack armor : armorList) {
				int armorDamage = armor.getItemDamage();
				int armorCost = armorDamage * 5;
				if (EntityExtension.For(caster).getCurrentMana() >= armorCost) {
					EntityExtension.For(caster).deductMana(armorCost);
					armor.setItemDamage(0);
				} else {
					// OOM
					return true;
				}
			}
			if (mainHand != null && mainHand != stack) {
				repairStack(mainHand, caster, 5);
			}
			if (offHand != null && offHand != stack) {
				repairStack(offHand, caster, 5);
			}
			return true;

		}
		return false;
	}

	private void repairStack(ItemStack item, EntityLivingBase caster, int costMultiplier) {
		int cost = item.getItemDamage() * costMultiplier;
		if (EntityExtension.For(caster).getCurrentMana() >= cost) {
			EntityExtension.For(caster).deductMana(cost);
			item.setItemDamage(0);
		}
	}

	@Override
	public float manaCost(EntityLivingBase caster) {
		return 150;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster) {
		// reagents system is broken right now
		// return new ItemStack[]{
		// new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_PURIFIED_VINTEUM)
		// };
		return null;
	}

	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.noneOf(SpellModifiers.class);
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target,
			Random rand, int colorModifier) {
		AMParticle particle = (AMParticle) ArsMagica2.proxy.particleManager.spawn(world, "radiant", x + 0.5, y + 0.5,
				z + 0.5);
		if (particle != null) {
			particle.AddParticleController(new ParticleHoldPosition(particle, 20, 1, false));
			particle.setMaxAge(20);
			particle.setParticleScale(0.3f);
			particle.setRGBColorF(0.5f, 1f, 0f);
			particle.SetParticleAlpha(0.1f);
			if (colorModifier > -1) {
				particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f,
						(colorModifier & 0xFF) / 255.0f);
			}
		}
	}

	@Override
	public Set<Affinity> getAffinity() {
		return Sets.newHashSet(Affinity.FIRE);
	}

	@Override
	public float getAffinityShift(Affinity affinity) {
		return 0;
	}

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos blockPos, EnumFacing blockFace,
			double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
		// TODO Auto-generated method stub

	}

}
