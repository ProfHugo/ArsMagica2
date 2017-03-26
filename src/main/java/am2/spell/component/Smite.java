package am2.spell.component;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.ArsMagica2;
import am2.api.DamageSources;
import am2.api.affinity.Affinity;
import am2.api.spell.SpellComponent;
import am2.api.spell.SpellModifiers;
import am2.defs.ItemDefs;
import am2.items.ItemOre;
import am2.particles.AMParticle;
import am2.utils.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Smite extends SpellComponent{

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		if (!(target instanceof EntityLivingBase)) return false;
		float baseDamage = 4;
		double damage = SpellUtils.getModifiedDouble_Add(baseDamage, stack, caster, target, world, SpellModifiers.DAMAGE);
		if (target instanceof EntityPlayer){
			if (((EntityPlayer)target).isCreative()){
				damage*=10;
				target.attackEntityFrom(DamageSources.causeHolyDamage((EntityLivingBase) target), (float)damage);
				EntityLightningBolt bolt = new EntityLightningBolt(world, target.posX, target.posY, target.posZ, true);
				world.addWeatherEffect(bolt);
				return true;
			}
		}else if (!target.isNonBoss()){
			damage*=10;
			caster.attackEntityFrom(DamageSources.causeHolyDamage((EntityLivingBase) target), (float)damage);
			EntityLightningBolt bolt = new EntityLightningBolt(world, target.posX, target.posY, target.posZ, true);
			EntityLightningBolt bolt2 = new EntityLightningBolt(world, caster.posX, caster.posY, caster.posZ, true);
			world.addWeatherEffect(bolt);
			world.addWeatherEffect(bolt2);
		}
		return true;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 400;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		for (int i = 0; i < 5; ++i){
			AMParticle particle = (AMParticle)ArsMagica2.proxy.particleManager.spawn(world, "sparkle2", x, y, z);
			if (particle != null){
				particle.addRandomOffset(1, 0.5, 1);
				particle.addVelocity(rand.nextDouble() * 0.2 - 0.1, rand.nextDouble() * 0.2, rand.nextDouble() * 0.2 - 0.1);
				particle.setAffectedByGravity();
				particle.setDontRequireControllers();
				particle.setMaxAge(5);
				particle.setParticleScale(0.1f);
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}
	}
	
	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.of(SpellModifiers.DAMAGE);
	}

	
	@Override
	public Set<Affinity> getAffinity(){
		return Sets.newHashSet(Affinity.LIGHTNING);
	}
	
	@Override
	public Object[] getRecipe(){
		return new Object[]{
				new ItemStack(ItemDefs.rune, 1, EnumDyeColor.PINK.getDyeDamage()),
				new ItemStack(Blocks.WOOL, 4, 6),
				Items.LEATHER_HELMET,
				Items.LEATHER_CHESTPLATE,
				Items.LEATHER_LEGGINGS,
				Items.LEATHER_BOOTS,
				Items.STICK,
				Items.DIAMOND_SWORD,
				new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_VINTEUM)
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.01f;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {}

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos blockPos, EnumFacing blockFace,
			double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		// TODO Auto-generated method stub
		return false;
	}
}
