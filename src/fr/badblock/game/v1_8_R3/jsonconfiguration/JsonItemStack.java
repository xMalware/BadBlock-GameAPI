package fr.badblock.game.v1_8_R3.jsonconfiguration;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.utils.itemstack.ItemStackFactory;

public class JsonItemStack {
	private int						  amount;
	private Material				  type;
	private short					  durability;
	private boolean 				  unbreakable;
	private Map<Enchantment, Integer> enchants = Maps.newConcurrentMap();
	private String[]				  lore;
	private String					  displayName;
	
	public JsonItemStack(ItemStack item){
		this.amount 	 = item.getAmount();
		this.type		 = item.getType();
		this.durability  = item.getDurability();
		this.unbreakable = item.getItemMeta().spigot().isUnbreakable();
		this.enchants	 = item.getEnchantments();
		this.lore		 = item.getItemMeta().getLore().toArray(new String[0]);
		this.displayName = item.getItemMeta().getDisplayName();
	}
	
	public ItemStack get(){
		ItemStackFactory factory = GameAPI.getAPI().createItemStackFactory()
												   .unbreakable(unbreakable)
												   .lore(lore)
												   .displayName(displayName)
												   .durability(durability)
												   .type(type);
		
		for(Entry<Enchantment, Integer> entry : enchants.entrySet()){
			factory.enchant(entry.getKey(), entry.getValue());
		}
		
		return factory.create(amount);
	}
}
