package fr.badblock.game.core18R3.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.general.StringUtils;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class TeleportCommand extends AbstractCommand {
	public TeleportCommand() {
		super("teleport", new TranslatableString("commands.teleport.usage"), GamePermission.ADMIN, "tp");
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if (args.length == 0)
			return false;
		else if (args.length >= 3)
			return tppos(sender, args);

		List<BadblockPlayer> who = new ArrayList<>();
		BadblockPlayer to = null;

		String toName = null;

		if (args.length == 1) {

			if (sender instanceof Player)
				who.add((BadblockPlayer) sender);
			else
				return false;

			toName = args[0];
			to = (BadblockPlayer) Bukkit.getPlayer(args[0]);

		} else if (args.length == 2) {
			toName = args[1];

			if(args[0].equalsIgnoreCase("@a")){
				who = Bukkit.getOnlinePlayers().stream().map(player -> (BadblockPlayer) player).collect(Collectors.toList());
			} else 	{
				BadblockPlayer player = (BadblockPlayer) Bukkit.getPlayer(args[0]);
			
				if(player != null)
					who.add(player);
			}
			
			to = (BadblockPlayer) Bukkit.getPlayer(args[1]);
		}

		if (who.isEmpty()) {
			new TranslatableString("commands.unknowplayer", args[0]).send(sender);
			return true;
		} else if (to == null) {
			new TranslatableString("commands.unknowplayer", toName).send(sender);
			return true;
		}

		for(BadblockPlayer player : who) {
			player.teleport(to);
			GameAPI.i18n().sendMessage(sender, "commands.teleport.to", to.getName());
		}

		if (!who.contains(sender)) {
			GameAPI.i18n().sendMessage(sender, "commands.teleport.to-other", StringUtils.join(who.stream().map(player -> player.getName()), ", "), to.getName());
		}

		return true;
	}

	private Double get(String arg, double base, CommandSender sender) {
		boolean rel = false;

		if (arg.startsWith("~")) {
			rel = true;
			arg = arg.substring(1);

			if (arg.length() == 0)
				arg = "0";
		}

		Double parsed = null;

		try {
			parsed = Double.parseDouble(arg);
		} catch (Exception e) {
			new TranslatableString("commands.nan", arg).send(sender);
			return null;
		}

		if (rel)
			parsed += base;

		return parsed;
	}

	private boolean tppos(CommandSender sender, String[] args) {
		List<BadblockPlayer> players = new ArrayList<>();

		int begin = 0;

		if (args.length == 4) {
			begin = 1;
			
			if(args[0].equalsIgnoreCase("@a")){
				players = Bukkit.getOnlinePlayers().stream().map(player -> (BadblockPlayer) player).collect(Collectors.toList());
			} else 	{
				BadblockPlayer player = (BadblockPlayer) Bukkit.getPlayer(args[0]);
			
				if(player != null)
					players.add(player);
			}
			
		} else if (!(sender instanceof Player)) {
			return false;
		} else
			players.add((BadblockPlayer) sender);

		if (players.isEmpty()) {
			new TranslatableString("commands.unknowplayer", args[0]).send(sender);
			return true;
		}

		BadblockPlayer relativeTo = null;
		
		if(sender instanceof Player && players.size() > 1)
			relativeTo = (BadblockPlayer) sender;
		else relativeTo = players.get(0);
		
		Double x = get(args[begin], relativeTo.getLocation().getX(), sender);
		if (x == null)
			return true;

		Double y = get(args[begin + 1], relativeTo.getLocation().getY(), sender);
		if (y == null)
			return true;

		Double z = get(args[begin + 2], relativeTo.getLocation().getZ(), sender);
		if (z == null)
			return true;

		Location to = new Location(relativeTo.getWorld(), x, y, z, relativeTo.getLocation().getYaw(),
				relativeTo.getLocation().getPitch());
		
		for(Player player : players){
			player.teleport(to);
			GameAPI.i18n().sendMessage(sender, "commands.teleport.place");
		}

		if (!players.contains(sender)) {
			GameAPI.i18n().sendMessage(sender, "commands.teleport.place-other",  StringUtils.join(players.stream().map(player -> player.getName()), ", "));
		}

		return true;
	}
}
