package fr.badblock.game.core18R3.merchant.shops;

import java.util.List;
import java.util.Map;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.configuration.BadConfiguration;
import fr.badblock.gameapi.configuration.values.MapBoolean;
import fr.badblock.gameapi.configuration.values.MapList;
import fr.badblock.gameapi.configuration.values.MapNamedLocation;
import fr.badblock.gameapi.configuration.values.MapNumber;
import fr.badblock.gameapi.configuration.values.MapRecipe;
import fr.badblock.gameapi.configuration.values.MapString;
import fr.badblock.gameapi.fakeentities.FakeEntity;
import fr.badblock.gameapi.packets.watchers.WatcherEntity;
import fr.badblock.gameapi.packets.watchers.WatcherSheep;
import fr.badblock.gameapi.packets.watchers.WatcherSkeleton;
import fr.badblock.gameapi.packets.watchers.WatcherVillager;
import fr.badblock.gameapi.packets.watchers.WatcherWitch;
import fr.badblock.gameapi.packets.watchers.WatcherZombie;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.entities.CreatureType;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import fr.badblock.gameapi.utils.merchants.CustomMerchantInventory;
import fr.badblock.gameapi.utils.merchants.CustomMerchantRecipe;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Merchant {
	public static final double VIEW_DISTANCE = 48.0d;
	
	private String									 name;
	private String					   				 entityName;
	private CreatureType			   				 entityType;
	@Setter
	private MapList<MapRecipe, CustomMerchantRecipe> recipes;
	
	private Villager.Profession		   				 profession;
	private DyeColor				   				 color;
	private boolean					   				 isZombieVillager;
	private boolean									 isWitherSkeleton;
	
	private List<MapNamedLocation>     				 handlers;
	private Map<String, FakeEntity<?>> 				 entities;
	
	@SuppressWarnings("deprecation")
	public Merchant(String name, BadConfiguration config){
		this.name 		 = name;
		entityName 		 = config.getValue("entityName", MapString.class, new MapString("shops." + name)).getHandle();
		entityType 		 = CreatureType.matchCreature(
					 	   config.getValue("entityType", MapString.class, new MapString(EntityType.VILLAGER.name())).getHandle()
		);
		profession 		 = Villager.Profession.valueOf(
		 		   		   config.getValue("entity-villager-profession", MapString.class, new MapString(Villager.Profession.LIBRARIAN.name())).getHandle()
		);
		color	   		 = DyeColor.getByWoolData(
				 		   config.getValue("entity-sheep-color", MapNumber.class, new MapNumber()).getHandle().byteValue()
		);
		isZombieVillager = config.getValue("entity-zombie-villager", MapBoolean.class, new MapBoolean()).getHandle();
		isWitherSkeleton = config.getValue("entity-zombie-wither", MapBoolean.class, new MapBoolean()).getHandle();

		recipes    		 = config.getValueList("offers", MapRecipe.class);
		handlers    	 = config.getValueList("handlers", MapNamedLocation.class);

		if(entityType == null)
			entityType = CreatureType.VILLAGER;
	
		createEntities();
	}
	
	public void addEntity(Location location, String name){
		FakeEntity<?> result = null;
		
		if(entityType == CreatureType.VILLAGER){
			FakeEntity<WatcherVillager> villager = GameAPI.getAPI().spawnFakeLivingEntity(location, entityType.bukkit(), WatcherVillager.class);
		
			villager.getWatchers().setProfession(this.profession);
			result = villager;
		} else if(entityType == CreatureType.SHEEP){
			FakeEntity<WatcherSheep> 	sheep    = GameAPI.getAPI().spawnFakeLivingEntity(location, entityType.bukkit(), WatcherSheep.class);
			
			sheep.getWatchers().setColor(this.color);
			result = sheep;
		} else if(entityType == CreatureType.ZOMBIE){
			FakeEntity<WatcherZombie> 	zombie	 = GameAPI.getAPI().spawnFakeLivingEntity(location, entityType.bukkit(), WatcherZombie.class);
		
			zombie.getWatchers().setVillager(this.isZombieVillager);
			result = zombie;
		} else if(entityType == CreatureType.SKELETON){
			FakeEntity<WatcherSkeleton> skeleton = GameAPI.getAPI().spawnFakeLivingEntity(location, entityType.bukkit(), WatcherSkeleton.class);
		
			skeleton.getWatchers().setWither(isWitherSkeleton);
			result = skeleton;
		} else if(entityType == CreatureType.WITCH){
			result = GameAPI.getAPI().spawnFakeLivingEntity(location, entityType.bukkit(), WatcherWitch.class);
		} else {
			result = GameAPI.getAPI().spawnFakeLivingEntity(location, entityType.bukkit(), WatcherEntity.class);
		}
		
		result.getWatchers().setCustomNameVisible(true)
							.setCustomName(new TranslatableString(entityName));
		
		entities.put(name, result);
		
		/*for(Player player : Bukkit.getOnlinePlayers()){
			move((BadblockPlayer) player, player.getLocation().clone().add(0, 64, 0), player.getLocation());
		}*/
		
	}
	
	protected void createEntities(){
		entities = Maps.newConcurrentMap();
		
		for(MapNamedLocation handler : handlers){
			addEntity(handler.getHandle(), handler.getName());
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public BadConfiguration save(){
		BadConfiguration config = GameAPI.getAPI().loadConfiguration(new JsonObject());
		
		config.setValue("entityName", new MapString(entityName));
		config.setValue("entityType", new MapString(entityType.name()));
		config.setValue("entity-villager-profession", new MapString(profession.name()));
		config.setValue("entity-sheep-color", new MapNumber(color.getWoolData()));
		config.setValue("entity-zombie-villager", new MapBoolean(isZombieVillager));
		config.setValueList("offers", recipes);
		config.setValueList("handlers", handlers);
		
		return config;
	}
	
	/*public void move(BadblockPlayer player, Location from, Location to){
		for(MapNamedLocation handler : handlers){
			FakeEntity<?> entity = entities.get(handler.getName());

			boolean bef = from.distance(entity.getLocation()) < VIEW_DISTANCE;
			boolean now = to.distance(entity.getLocation()) < VIEW_DISTANCE;
			
			if(!bef && now)
				entity.show(player);
			else if(bef && !now)
				entity.remove(player);
		}
		
	}*/
	
	public void openInventory(BadblockPlayer player){
		List<CustomMerchantRecipe> content   = recipes.getHandle();
		
		if(content.isEmpty()) return;
		
		CustomMerchantInventory    inventory = GameAPI.getAPI().getCustomMerchantInventory();
		content.forEach(recipe -> inventory.addRecipe(recipe));
		
		inventory.open(player, new TranslatableString(entityName));
	}
	
	public boolean click(BadblockPlayer player, FakeEntity<?> entity){
		
		for(FakeEntity<?> handled : entities.values()){
			if(handled.getId() == entity.getId()){
				openInventory(player);
				return true;
			}
			
		}
		
		return false;
		
	}
}
