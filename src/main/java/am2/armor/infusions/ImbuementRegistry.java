package am2.armor.infusions;

import java.util.ArrayList;

import am2.LogHelper;
import am2.api.ArsMagicaAPI;
import am2.api.items.armor.ArmorImbuement;
import am2.api.items.armor.IImbuementRegistry;
import am2.api.items.armor.ImbuementTiers;
import am2.armor.ArmorHelper;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ImbuementRegistry implements IImbuementRegistry {

	public static final ImbuementRegistry instance = new ImbuementRegistry();

	public static final int SLOT_BOOTS = 3;
	public static final int SLOT_LEGS = 2;
	public static final int SLOT_CHEST = 1;
	public static final int SLOT_HELM = 0;

	@Override
	public void registerImbuement(ArmorImbuement imbuementInstance) {
		GameRegistry.register(imbuementInstance,
				new ResourceLocation(ArsMagicaAPI.getCurrentModId(), imbuementInstance.getID()));
		LogHelper.info(String.format("Registered imbuement: %s", imbuementInstance.getID()));
	}

	@Override
	public ArmorImbuement getImbuementByID(ResourceLocation ID) {
		return ArsMagicaAPI.getArmorImbuementRegistry().getObject(ID);
	}

	@Override
	public ArmorImbuement[] getImbuementsForTier(ImbuementTiers tier, EntityEquipmentSlot armorType) {
		ArrayList<ArmorImbuement> list = new ArrayList<ArmorImbuement>();

		for (ArmorImbuement imbuement : ArsMagicaAPI.getArmorImbuementRegistry().getValues()) {
			if (imbuement.getTier() == tier) {
				for (EntityEquipmentSlot i : imbuement.getValidSlots()) {
					if (i == armorType) {
						list.add(imbuement);
						break;
					}
				}
			}
		}

		return list.toArray(new ArmorImbuement[list.size()]);
	}

	@Override
	public boolean isImbuementPresent(ItemStack stack, ArmorImbuement imbuement) {
		return isImbuementPresent(stack, imbuement.getID());
	}

	@Override
	public boolean isImbuementPresent(ItemStack stack, String id) {
		return ArmorHelper.isInfusionPreset(stack, id);
	}
}
