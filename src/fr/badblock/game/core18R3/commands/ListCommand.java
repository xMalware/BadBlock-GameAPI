package fr.badblock.game.core18R3.commands;

import java.util.ArrayList;
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

@SuppressWarnings("null")
public class ListCommand extends AbstractCommand {
	public ListCommand() {
		super("list", new TranslatableString("commands.list.usage"), GamePermission.MODERATOR, GamePermission.MODERATOR, GamePermission.BMODERATOR);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		Map<String, List<BadblockPlayer>> players = null;
		
		boolean admin = !(sender instanceof BadblockPlayer) || ((BadblockPlayer) sender).hasAdminMode();
		
		Set<BadblockPlayer> set = Bukkit.getOnlinePlayers().stream().map(player -> {
			return (BadblockPlayer) player;
		}).filter(player -> {
			if(player.inGameData(CommandInGameData.class).vanish){
				return admin && isValid(player);
			} else {
				return isValid(player);
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
			if(group == null || concerneds == null || concerneds.isEmpty())
				return;
			
			String prefix = new TranslatableString("permissions.tab." + group).getAsLine(sender);
			sendTranslatedMessage(sender, "commands.list.part", prefix, concerneds.size(), StringUtils.join(concerneds.stream().filter(this::isValid).map(p -> p.getName()), ", "));
		});
		
		return true;
	}
	
	private boolean isValid(BadblockPlayer p){
		return p != null && p.isOnline() && p.isDataFetch();
	}
}