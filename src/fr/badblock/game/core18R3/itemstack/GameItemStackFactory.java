package fr.badblock.game.core18R3.itemstack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Wool;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.utils.i18n.I18n;
import fr.badblock.gameapi.utils.i18n.Locale;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import fr.badblock.gameapi.utils.itemstack.ItemStackExtra;
import fr.badblock.gameapi.utils.itemstack.ItemStackFactory;

/**
 * A simple {@link fr.badblock.gameapi.ItemStackFactory} implementation.
 * @author LeLanN
 */
public class GameItemStackFactory implements ItemStackFactory, Cloneable {
	private boolean 				  unbreakable;
	private Map<Enchantment, Integer> enchants;
	private String[]				  lore;
	private String					  displayName;
	private TranslatableString		  tLore;
	private TranslatableString		  tDisplayName;
	private short					  durability;
	private Material				  type;
	private Locale					  locale;
	
	public GameItemStackFactory(){
		this.enchants 	 = new HashMap<>();
		this.type		 = Material.AIR;
	}
	
	public GameItemStackFactory(ItemStack item){
		this.enchants 	 = item.getEnchantments();
		this.unbreakable = item.getItemMeta().spigot().isUnbreakable();
		this.lore		 = item.getItemMeta().getLore().toArray(new String[0]);
		this.displayName = item.getItemMeta().getDisplayName();
		this.durability	 = item.getDurability();
		this.type		 = item.getType();
		this.locale		 = null;
	}
	
	@Override
	public ItemStackFactory unbreakable(boolean unbreakable) {
		this.unbreakable = unbreakable;
		return this;
	}

	@Override
	public ItemStackFactory enchant(Enchantment enchantment, int level) {
		enchants.put(enchantment, level);
		return this;
	}

	@Override
	public ItemStackFactory lore(String... lore) {
		this.lore = lore;
		return this;
	}

	@Override
	public ItemStackFactory displayName(String displayName) {
		this.displayName = displayName;
		return this;
	}
	
	@Override
	public ItemStackFactory lore(TranslatableString lore) {
		this.tLore = lore;
		return this;
	}

	@Override
	public ItemStackFactory displayName(TranslatableString displayName) {
		this.tDisplayName = displayName;
		return this;
	}

	@Override
	public ItemStackFactory durability(short data) {
		this.durability = data;
		return this;
	}

	@Override
	public ItemStackFactory type(Material material) {
		this.type = material;
		return this;
	}

	@Override
	public ItemStack asWool(int amount, DyeColor color) {
		ItemStack colored = create(amount);
		
		if(colored.getData() instanceof Wool){
			Wool meta = (Wool) colored.getItemMeta();
			meta.setColor(color);
			colored.setData(meta);
		}
		
		return colored;
	}

	@Override
	public ItemStack asLeatheredArmor(int amount, Color color) {
		ItemStack leather = create(amount);
		
		if(leather.getItemMeta() instanceof LeatherArmorMeta){
			LeatherArmorMeta meta = (LeatherArmorMeta) leather.getItemMeta();
			meta.setColor(color);
			leather.setItemMeta(meta);
		}
		
		return leather;
	}

	@Override
	public ItemStack asSkull(int amount, String owner) {
		ItemStack skull = create(amount);
		
		if(skull.getItemMeta() instanceof SkullMeta){
			SkullMeta meta = (SkullMeta) skull.getItemMeta();
			meta.setOwner(owner);
			skull.setItemMeta(meta);
		}
		
		return skull;
	}

	@Override
	public ItemStack create(int amount) {
		ItemStack item = new ItemStack(type, amount, durability);
		item.addUnsafeEnchantments(enchants);
		
		ItemMeta meta = item.getItemMeta();
		if(meta != null)
			meta.spigot().setUnbreakable(unbreakable);
		
		I18n i18n = GameAPI.i18n();
		
		if(meta != null && tDisplayName != null && locale != null){
			meta.setDisplayName(tDisplayName.getAsLine(locale));
		} else if(meta != null && displayName != null){
			if(locale != null){
				meta.setDisplayName(i18n.get(locale, displayName)[0]);
			} else meta.setDisplayName(i18n.replaceColors(displayName));
		}
		
		if(meta != null && tLore != null && locale != null){
			meta.setLore(Arrays.asList(tLore.get(locale)));
		} else if(meta != null && lore != null){
			if(locale != null){
				if(lore.length != 0)
					meta.setLore(Arrays.asList(i18n.get(locale, lore[0])));
			} else meta.setLore(i18n.replaceColors(Arrays.asList(lore)));
		}
		
		item.setItemMeta(meta);
		
		return item;
	}

	@Override
	public ItemStackExtra asExtra(int amount) {
		return GameAPI.getAPI().createItemStackExtra(create(amount));
	}

	@Override
	public ItemStackFactory doWithI18n(Locale locale) {
		this.locale = locale;
		
		return this;
	}
	
	@Override
	public ItemStackFactory clone(){
		try {
			return (ItemStackFactory) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
