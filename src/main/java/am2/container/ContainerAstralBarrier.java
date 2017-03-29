package am2.container;

import am2.api.items.ISpellFocus;
import am2.blocks.tileentity.TileEntityAstralBarrier;
import am2.container.slot.AM2Container;
import am2.container.slot.SlotGhostRune;
import am2.container.slot.SlotSpellFocusOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAstralBarrier extends AM2Container {
	private TileEntityAstralBarrier barrier;

	private static final int PLAYER_INVENTORY_START = 4;
	private static final int PLAYER_ACTION_BAR_START = 31;
	private static final int PLAYER_ACTION_BAR_END = 40;

	public ContainerAstralBarrier(InventoryPlayer inventoryplayer, TileEntityAstralBarrier receptacle) {
		this.barrier = receptacle;
		addSlotToContainer(new SlotGhostRune(receptacle, 1, 62, 8)); // inventory,
																		// index,
																		// x, y
		addSlotToContainer(new SlotGhostRune(receptacle, 2, 80, 8)); // inventory,
																		// index,
																		// x, y
		addSlotToContainer(new SlotGhostRune(receptacle, 3, 98, 8)); // inventory,
																		// index,
																		// x, y

		addSlotToContainer(new SlotSpellFocusOnly(receptacle, 0, 80, 48));

		// display player inventory
		for (int i = 0; i < 3; i++) {
			for (int k = 0; k < 9; k++) {
				addSlotToContainer(new Slot(inventoryplayer, k + i * 9 + 9, 8 + k * 18, 98 + i * 18));
			}
		}

		// display player action bar
		for (int j1 = 0; j1 < 9; j1++) {
			addSlotToContainer(new Slot(inventoryplayer, j1, 8 + j1 * 18, 156));
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return barrier.isUseableByPlayer(entityplayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i) {
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(i);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (i < PLAYER_INVENTORY_START) {
				if (!mergeItemStack(itemstack1, PLAYER_INVENTORY_START, PLAYER_ACTION_BAR_END, true)) {
					return null;
				}
			} else if (i >= PLAYER_INVENTORY_START && i < PLAYER_ACTION_BAR_START) // from
																					// player
																					// inventory
			{
				if (!mergeSpecialItems(itemstack1, slot)) {
					if (!mergeItemStack(itemstack1, PLAYER_ACTION_BAR_START, PLAYER_ACTION_BAR_END, false)) {
						return null;
					}
				} else {
					return null;
				}
			} else if (i >= PLAYER_ACTION_BAR_START && i < PLAYER_ACTION_BAR_END) {
				if (!mergeSpecialItems(itemstack1, slot)) {
					if (!mergeItemStack(itemstack1, PLAYER_INVENTORY_START, PLAYER_ACTION_BAR_START - 1, false)) {
						return null;
					}
				} else {
					return null;
				}
			} else if (!mergeItemStack(itemstack1, PLAYER_INVENTORY_START, PLAYER_ACTION_BAR_END, false)) {
				return null;
			}

			if (itemstack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize != itemstack.stackSize) {
				slot.onSlotChange(itemstack1, itemstack);
			} else {
				return null;
			}
		}
		return itemstack;
	}

	private boolean mergeSpecialItems(ItemStack stack, Slot slot) {
		if (stack.getItem() instanceof ISpellFocus) {
			Slot focusSlot = (Slot) inventorySlots.get(3);
			if (!focusSlot.getHasStack()) {
				focusSlot.putStack(new ItemStack(stack.getItem(), 1, stack.getItemDamage()));
				focusSlot.onSlotChanged();
				stack.stackSize--;
				if (stack.stackSize == 0) {
					slot.putStack(null);
					slot.onSlotChanged();
				}
				return true;
			}
		}
		return false;
	}
}
