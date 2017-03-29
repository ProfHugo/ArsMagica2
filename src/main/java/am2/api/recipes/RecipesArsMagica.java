package am2.api.recipes;

import java.util.ArrayList;
import java.util.Map;

import com.google.common.collect.Lists;

import am2.blocks.CraftingEssenceExtractor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public abstract class RecipesArsMagica {

	protected Map<Integer, RecipeArsMagica> RecipeList;

	public void addRefinerRecipe(ItemStack output, ItemStack[] components) {
		if (output != null && components.length == 5)
			AddRecipe(components, output);
	}

	public void AddRecipe(ItemStack[] blockIDs, ItemStack itemstack) {
		RecipeArsMagica newRecipe = new RecipeArsMagica(blockIDs, itemstack);
		AddRecipe(newRecipe);
	}

	public void AddRecipe(RecipeArsMagica newRecipe) {
		// ensure duplicate recipes are not added
		for (int i = 0; i < this.RecipeList.size(); ++i) {
			if (itemAt(i).recipeIsMatch(newRecipe.getRecipeItems())) {
				return;
			}
		}
		RecipeList.put(RecipeList.size(), newRecipe);
	}

	public ItemStack GetResult(ItemStack[] items, EntityPlayer player) {
		return matchingRecipe(items, player);
	}

	public ItemStack getRecipeFuel(ItemStack[] contents, EntityPlayer player) {
		for (int i = 0; i < RecipeList.size(); ++i) {
			RecipeArsMagica item = itemAt(i);
			if (item.recipeIsMatch(contents)) {
				return item.getFuelID();
			}
		}
		return null;
	}

	public ItemStack matchingRecipe(ItemStack[] contents, EntityPlayer player) {
		for (int i = 0; i < RecipeList.size(); ++i) {
			RecipeArsMagica item = itemAt(i);
			if (item.recipeIsMatch(contents)) {
				return item.getOutput();
			}
		}
		return null;

	}

	public ItemStack matchingRecipe(CraftingEssenceExtractor matrix) {
		ItemStack[] items = new ItemStack[matrix.getSizeInventory()];
		for (int i = 0; i < matrix.getSizeInventory(); ++i) {
			items[i] = matrix.getStackInSlot(i);
		}
		return matchingRecipe(items, null);
	}

	public RecipeArsMagica recipeFor(ItemStack stack) {
		for (int i = 0; i < RecipeList.size(); ++i) {
			RecipeArsMagica item = itemAt(i);
			if (item.getOutput().getItem() == stack.getItem()
					&& item.getOutput().getItemDamage() == stack.getItemDamage()) {
				return item;
			}
		}
		return null;
	}

	public RecipeArsMagica itemAt(int i) {
		return (RecipeArsMagica) RecipeList.get(Integer.valueOf(i));
	}

	public Map<Integer, RecipeArsMagica> GetRecipeList() {
		return RecipeList;
	}

	public ArrayList<RecipeArsMagica> getAllRecipes() {
		return Lists.newArrayList(RecipeList.values());
	}
}
