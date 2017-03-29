package am2.spell.component;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import am2.ArsMagica2;
import am2.api.affinity.Affinity;
import am2.api.blocks.MultiblockGroup;
import am2.api.blocks.MultiblockStructureDefinition;
import am2.api.power.IPowerNode;
import am2.api.rituals.IRitualInteraction;
import am2.api.rituals.RitualShapeHelper;
import am2.api.spell.SpellComponent;
import am2.api.spell.SpellModifiers;
import am2.buffs.BuffEffectIllumination;
import am2.defs.BlockDefs;
import am2.defs.ItemDefs;
import am2.defs.PotionEffectsDefs;
import am2.items.ItemOre;
import am2.particles.AMParticle;
import am2.power.PowerNodeRegistry;
import am2.utils.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Light extends SpellComponent implements IRitualInteraction {

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos pos, EnumFacing blockFace, double impactX,
			double impactY, double impactZ, EntityLivingBase caster) {
		if (world.getBlockState(pos).getBlock().equals(BlockDefs.obelisk)) {
			if (RitualShapeHelper.instance.matchesRitual(this, world, pos)) {
				if (!world.isRemote) {
					RitualShapeHelper.instance.consumeReagents(this, world, pos);
					RitualShapeHelper.instance.consumeShape(this, world, pos);
					world.setBlockState(pos, BlockDefs.celestialPrism.getDefaultState());
					PowerNodeRegistry.For(world).registerPowerNode((IPowerNode<?>) world.getTileEntity(pos));
				} else {

				}

				return true;
			}
		}

		if (world.getBlockState(pos).getBlock() == Blocks.AIR)
			blockFace = null;
		if (blockFace != null) {
			pos = pos.offset(blockFace);
		}

		if (world.getBlockState(pos).getBlock() != Blocks.AIR)
			return false;

		if (!world.isRemote) {
			// TODO Colors
			world.setBlockState(pos, BlockDefs.blockMageTorch.getDefaultState());
		}

		return true;
	}

	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.of(SpellModifiers.COLOR);
	}

	// private int getColorMeta(ItemStack spell){
	// int meta = 15;
	// int color = 0xFFFFFF;
	// if (SpellUtils.instance.modifierIsPresent(SpellModifiers.COLOR, spell,
	// 0)){
	// ISpellModifier[] mods = SpellUtils.instance.getModifiersForStage(spell,
	// 0);
	// int ordinalCount = 0;
	// for (ISpellModifier mod : mods){
	// if (mod instanceof Colour){
	// byte[] data = SpellUtils.instance.getModifierMetadataFromStack(spell,
	// mod, 0, ordinalCount++);
	// color = (int)mod.getModifier(SpellModifiers.COLOR, null, null, null,
	// data);
	// }
	// }
	// }
	//
	// for (int i = 0; i < 16; ++i){
	// if (((ItemDye)Items.dye).field_150922_c[i] == color){
	// meta = i;
	// break;
	// }
	// }
	//
	// return meta;
	// }

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target) {
		if (target instanceof EntityLivingBase) {
			int duration = SpellUtils.getModifiedInt_Mul(PotionEffectsDefs.default_buff_duration, stack, caster, target,
					world, SpellModifiers.DURATION);
			// duration = SpellUtils.modifyDurationBasedOnArmor(caster,
			// duration);
			if (!world.isRemote)
				((EntityLivingBase) target).addPotionEffect(new BuffEffectIllumination(duration,
						SpellUtils.countModifiers(SpellModifiers.BUFF_POWER, stack)));
			return true;
		}
		return false;
	}

	@Override
	public float manaCost(EntityLivingBase caster) {
		return 50;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster) {
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target,
			Random rand, int colorModifier) {
		for (int i = 0; i < 5; ++i) {
			AMParticle particle = (AMParticle) ArsMagica2.proxy.particleManager.spawn(world, "sparkle2", x, y, z);
			if (particle != null) {
				particle.addRandomOffset(1, 0.5, 1);
				particle.addVelocity(rand.nextDouble() * 0.2 - 0.1, rand.nextDouble() * 0.2,
						rand.nextDouble() * 0.2 - 0.1);
				particle.setAffectedByGravity();
				particle.setDontRequireControllers();
				particle.setMaxAge(5);
				particle.setParticleScale(0.1f);
				particle.setRGBColorF(0.6f, 0.2f, 0.8f);
				if (colorModifier > -1) {
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f,
							((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public Set<Affinity> getAffinity() {
		return Sets.newHashSet(Affinity.NONE);
	}

	@Override
	public Object[] getRecipe() {
		return new Object[] { new ItemStack(ItemDefs.rune, 1, EnumDyeColor.WHITE.getDyeDamage()), BlockDefs.cerublossom,
				Blocks.TORCH, BlockDefs.vinteumTorch };
	}

	@Override
	public float getAffinityShift(Affinity affinity) {
		return 0.01f;
	}

	@Override
	public MultiblockStructureDefinition getRitualShape() {
		MultiblockStructureDefinition newDef = new MultiblockStructureDefinition("celestialPurification");
		newDef.groups = Lists.newArrayList(RitualShapeHelper.instance.purification.groups);
		MultiblockGroup obelisk = new MultiblockGroup("obelisk",
				Lists.newArrayList(BlockDefs.obelisk.getDefaultState()), true);
		obelisk.addBlock(new BlockPos(0, 0, 0));
		newDef.addGroup(obelisk);
		return newDef;
	}

	@Override
	public ItemStack[] getReagents() {
		return new ItemStack[] { new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_MOONSTONE),
				new ItemStack(ItemDefs.manaFocus) };
	}

	@Override
	public int getReagentSearchRadius() {
		return 3;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getResult() {
		return new ItemStack(BlockDefs.celestialPrism);
	}
}
