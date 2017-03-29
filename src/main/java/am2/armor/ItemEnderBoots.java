package am2.armor;

import java.util.List;

import am2.extensions.EntityExtension;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class ItemEnderBoots extends AMArmor {

	public ItemEnderBoots(ArmorMaterial inheritFrom, ArsMagicaArmorMaterial enumarmormaterial, int par3,
			EntityEquipmentSlot par4) {
		super(inheritFrom, enumarmormaterial, par3, par4);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return 0;
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if (player.posY >= world.getActualHeight() && EntityExtension.For(player).getIsFlipped())
			EntityExtension.For(player).setInverted(!EntityExtension.For(player).isInverted());
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List,
			boolean par4) {
		par3List.add(I18n.translateToLocal("am2.tooltip.ender_boots"));
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
	}
}
