package fr.badblock.game.v1_8_R3.merchant;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

import fr.badblock.gameapi.utils.merchants.CustomMerchantInventory;
import fr.badblock.gameapi.utils.merchants.CustomMerchantRecipe;
import fr.badblock.gameapi.utils.reflection.ReflectionUtils;
import fr.badblock.gameapi.utils.reflection.Reflector;
import net.minecraft.server.v1_8_R3.MerchantRecipe;
import net.minecraft.server.v1_8_R3.MerchantRecipeList;

public class GameMerchantInventory implements CustomMerchantInventory {
	private MerchantRecipeList recipeList 	   = new MerchantRecipeList();

	@Override
	public CustomMerchantRecipe[] getRecipes() {
		CustomMerchantRecipe[] recipes = new CustomMerchantRecipe[recipeList.size()];
		
		for(int i=0;i<recipeList.size();i++){
			recipes[i] = fromNMS(recipeList.get(i));
		}
		
		return recipes;
	}

	@Override
	public void removeRecipe(CustomMerchantRecipe recipe) {
		recipeList.remove(toNMS(recipe));
	}

	@Override
	public void addRecipe(CustomMerchantRecipe recipe) {
		recipeList.add(toNMS(recipe));
	}

	@Override
	public void applyTo(Villager villager) {
		Object entityVillager = ReflectionUtils.getHandle(villager);
		
		try {
			new Reflector(entityVillager).setFieldValue("bp", recipeList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public CustomMerchantRecipe fromNMS(MerchantRecipe recipe){
		ItemStack a = CraftItemStack.asBukkitCopy(recipe.getBuyItem1());
		ItemStack b = recipe.getBuyItem2() == null ? null : CraftItemStack.asBukkitCopy(recipe.getBuyItem2());
		ItemStack c = CraftItemStack.asBukkitCopy(recipe.getBuyItem3());
		
		return new CustomMerchantRecipe(a, b, c);
	}
	
	public MerchantRecipe toNMS(CustomMerchantRecipe recipe){
		return new MerchantRecipe(CraftItemStack.asNMSCopy(recipe.getFirstItem()),
				recipe.getSecondItem() == null ? null : CraftItemStack.asNMSCopy(recipe.getSecondItem()), 
						CraftItemStack.asNMSCopy(recipe.getResult()), 0, Integer.MAX_VALUE);
	}
}
