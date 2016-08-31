package fr.badblock.game.core18R3.merchant;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import fr.badblock.gameapi.utils.merchants.CustomMerchantInventory;
import fr.badblock.gameapi.utils.merchants.CustomMerchantRecipe;
import fr.badblock.gameapi.utils.reflection.ReflectionUtils;
import fr.badblock.gameapi.utils.reflection.Reflector;
import net.minecraft.server.v1_8_R3.EntityVillager;
import net.minecraft.server.v1_8_R3.MerchantRecipe;
import net.minecraft.server.v1_8_R3.MerchantRecipeList;
import net.minecraft.server.v1_8_R3.MinecraftServer;

public class GameMerchantInventory implements CustomMerchantInventory {
	private MerchantRecipeList 			 recipeList = new MerchantRecipeList();
	
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
			new Reflector(entityVillager).setFieldValue("br", recipeList);
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

	@Override
	public void open(BadblockPlayer player) {
		EntityVillager villager = new EntityVillager(MinecraftServer.getServer().getWorld());

		applyTo((Villager) villager.getBukkitEntity());
		GameBadblockPlayer gplayer = (GameBadblockPlayer) player;
		gplayer.getHandle().openTrade(villager);
	}

	@Override
	public void open(BadblockPlayer player, TranslatableString customName) {
		FakeMerchant merchant = new FakeMerchant(player, recipeList, customName);
		
		GameBadblockPlayer gplayer = (GameBadblockPlayer) player;
		gplayer.getHandle().openTrade(merchant);
	}
}
