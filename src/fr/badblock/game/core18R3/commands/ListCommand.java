package fr.badblock.game.core18R3.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import fr.badblock.game.core18R3.players.ingamedata.CommandInGameData;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.general.StringUtils;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class ListCommand extends AbstractCommand {
	public ListCommand() {
		super("list", new TranslatableString("commands.list.usage"), GamePermission.MODERATOR, GamePermission.MODERATOR, GamePermission.BMODERATOR);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		Map<String, List<BadblockPlayer>> players = new HashMap<>();
		
		Set<BadblockPlayer> set = Bukkit.getOnlinePlayers().stream().map(player -> {
			return (BadblockPlayer) player;
		}).filter(player -> {
			if(player.inGameData(CommandInGameData.class).vanish){
				return player.hasAdminMode();
			} else {
				return true;
			}
		}).collect(Collectors.toSet());
		
		set.stream().forEach(player -> {
			List<BadblockPlayer> fromGroups = players.get(player.getMainGroup());
			
			if(fromGroups == null){
				fromGroups = new ArrayList<>();
				players.put(player.getMainGroup(), fromGroups);
			}
			
			fromGroups.add(player);
		});
		
		int count = set.stream().mapToInt(player -> {
			return 1;
		}).sum();
		
		sendTranslatedMessage(sender, "commands.list.header", count, Bukkit.getMaxPlayers());

		players.forEach((group, concerneds) -> {
			String prefix = concerneds.get(0).getTabGroupPrefix().getAsLine(sender);
		
			sendTranslatedMessage(sender, "commands.list.part", prefix, concerneds.size(), StringUtils.join(concerneds.stream().map(p -> p.getName()), ", "));
		});
		
		return true;
	}
}