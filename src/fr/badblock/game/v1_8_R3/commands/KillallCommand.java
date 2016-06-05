package fr.badblock.game.v1_8_R3.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.entities.CreatureType;
import fr.badblock.gameapi.utils.general.StringUtils;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class KillallCommand extends AbstractCommand {
	private static final List<EntityType> no = new ArrayList<>();

	static {
		no.add(EntityType.COMPLEX_PART);
		no.add(EntityType.PLAYER);
		no.add(EntityType.SPLASH_POTION);
		no.add(EntityType.LEASH_HITCH);
		no.add(EntityType.LIGHTNING);
		no.add(EntityType.THROWN_EXP_BOTTLE);
	}

	public KillallCommand() {
		super("killall", new TranslatableString("commands.killall.usage", StringUtils.join(values(), ", ")), GamePermission.ADMIN);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if(args.length == 0){
			return false;
		}

		String name = args[0].toUpperCase();

		Filter filter;

		if(name.equalsIgnoreCase("all")){
			filter = new Filter(type -> {
				return type != EntityType.PLAYER;
			});
		} else if(name.equalsIgnoreCase("monsters")){
			filter = new Filter(type -> {
				CreatureType ctype = CreatureType.getByBukkit(type);

				return ctype != null && ctype.isHostile();
			});
		} else if(name.equalsIgnoreCase("peaceful")){
			filter = new Filter(type -> {
				CreatureType ctype = CreatureType.getByBukkit(type);

				return ctype != null && ctype.isFriendly();
			});
		} else {
			EntityType type;

			try {
				type = EntityType.valueOf(name);

				if(no.contains(type))
					return false;
			} catch(Exception e){
				return false;
			}

			filter = new Filter(t -> {
				return type == t;
			});
		}

		Count count = new Count();

		Bukkit.getWorlds().forEach(world -> {

			world.getEntities().forEach(entity -> {

				if(filter.test(entity.getType())){
					count.inc();
					entity.remove();
				}

			});

		});
		
		int removed = 0;
		
		if(sender instanceof Player){
			removed = remove(Arrays.asList( ((Player) sender).getWorld() ), filter);
		} else removed = remove(Bukkit.getWorlds(), filter);

		new TranslatableString("commands.killall.killed", removed).send(sender);
		return true;
	}

	private int remove(Collection<World> worlds, Filter filter){
		Count count = new Count();
		
		Bukkit.getWorlds().forEach(world -> {

			world.getEntities().forEach(entity -> {

				if(filter.test(entity.getType())){
					count.inc();
					entity.remove();
				}

			});

		});
		
		return count.count;
	}

	private static List<String> values(){
		List<String> result = new ArrayList<>();

		result.add("all");
		result.add("monsters");
		result.add("peaceful");

		for(EntityType type : EntityType.values()){
			if(!no.contains(type))
				result.add(type.name().toLowerCase());
		}

		return result;
	}

	private class Count {
		int count = 0;

		public void inc(){
			count++;
		}
	}

	private class Filter {
		Predicate<EntityType> test;

		public Filter(Predicate<EntityType> test){
			this.test = test;
		}

		public boolean test(EntityType type){
			return test.test(type);
		}
	}
}