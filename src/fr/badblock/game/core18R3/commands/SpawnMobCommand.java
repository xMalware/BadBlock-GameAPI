package fr.badblock.game.core18R3.commands;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.entities.CreatureType;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import fr.badblock.gameapi.utils.i18n.Word.WordDeterminant;

public class SpawnMobCommand extends AbstractCommand {
	public SpawnMobCommand() {
		super("spawnmob", new TranslatableString("commands.spawnmob.usage"), GamePermission.ADMIN, "spawnentity");
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if (args.length < 2) {
			return false;
		}

		CreatureType creature = CreatureType.matchCreature(args[0]);
		int count = 1;
		BadblockPlayer player = null;
		if (creature == null) {
			sendTranslatedMessage(sender, "commands.spawnmob.unknowmob", args[0]);
		}

		try {
			count = Integer.parseInt(args[1]);

			if (count <= 0)
				throw new Exception();
		} catch (Exception e) {
			sendTranslatedMessage(sender, "commands.nan", args[1]);
			return true;
		}

		if (args.length >= 3) {
			player = (BadblockPlayer) Bukkit.getPlayer(args[2]);
		} else if (sender instanceof Player == false) {
			return false;
		} else {
			player = (BadblockPlayer) sender;
		}

		if (player == null) {
			sendTranslatedMessage(sender, "commands.unknowplayer", args[2]);
			return true;
		}

		Location spawnLocation = GameAPI.getAPI().getSafeLocation(player.getLocation());
		World world = spawnLocation.getWorld();

		for (int i = 0; i < count; i++) {
			Entity e = world.spawnEntity(spawnLocation, creature.bukkit());

			if (e instanceof Ageable) {
				((Ageable) e).setAdult();
			}

			if (e instanceof Zombie) {
				((Zombie) e).setBaby(false);
			}
		}

		sendTranslatedMessage(sender, "commands.spawnmob.spawned", creature.getWord(count > 1, WordDeterminant.SIMPLE), count);

		return true;
	}
	
	@Override
	public Collection<String> doTab(CommandSender sender, String[] args){
		if(args.length > 1)
			return super.doTab(sender, args);
		else return Arrays.stream(CreatureType.values()).map(type -> type.getName()).collect(Collectors.toSet());
	}
}