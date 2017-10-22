package fr.badblock.game.core18R3.commands;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.game.core18R3.technologies.rabbitlisteners.PlayerPingListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.general.MathsUtils;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class LagCommand extends AbstractCommand {
	public LagCommand() {
		super("lag", new TranslatableString("commands.lag.usage"), GamePermission.PLAYER, GamePermission.PLAYER, GamePermission.PLAYER, "tps", "gc", "bug");
	}

	private static SimpleDateFormat		simpleDateFormat			= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static void send(CommandSender sender) {
		double lagPercent = (GameAPI.getAPI().getGameServer().getPassmarkTps() / 20.0D * 100.0D);
		double speed = MathsUtils.round(lagPercent, 2);
		String rapidity = speed >= 90 ? "&a" + speed : speed >= 80 ? "&b" + speed : speed >= 50 ? "&e" + speed : speed >= 30 ? "&c" + speed : speed >= 20 ? "&4" + speed : "&4&l" + speed;
		String ms = "0";
		
		if (sender instanceof BadblockPlayer) {
			GameBadblockPlayer player = (GameBadblockPlayer) sender;
			String realName = player.getRealName() != null ? player.getRealName() : player.getName();
			int ping = PlayerPingListener.ping.containsKey(realName) ? PlayerPingListener.ping.get(realName) : -1;
			ms = ping < 80 ? "&a" + ping : ping <= 100 ? "&b" + ping : ping < 200 ? "&e" + ping : ping < 300 ? "&c" + ping : ping < 500 ? "&4" + ping : "&4&l" + ping; 
		}
		
		GameAPI.i18n().sendMessage(sender, "commands.lag.message", Bukkit.getServerName(), simpleDateFormat.format(new Date()), rapidity, ms);
		/*sender.sendMessage("&8&l«&b&l-&8&l»&m------&f&8&l«&b&l-&8&l»&b &b&lLag &8&l«&b&l-&8&l»&m------&f&8&l«&b&l-&8&l»");
		sender.sendMessage("&c> &7Nom du serveur: &b" + Bukkit.getServerName());
		sender.sendMessage("&c> &7Heure: &b" + simpleDateFormat.format(new Date()));
		double lagPercent = (getPassMarkTps() / 20.0D * 100.0D);
		double speed = round(lagPercent, 2);
		String rapidity = speed >= 90 ? "&a" + speed : speed >= 80 ? "&b" + speed : speed >= 50 ? "&e" + speed : speed >= 30 ? "&c" + speed : speed >= 20 ? "&4" + speed : "&4&l" + speed; 
		sender.sendMessage("&c> &7Fluidité: " + rapidity + " %");
		if (sender instanceof Player) {
			Player player = (Player) sender;
			int ping = ((CraftPlayer) player).getHandle().ping;
			String ms = ping < 80 ? "&a" + ping : ping <= 100 ? "&b" + ping : ping < 200 ? "&e" + ping : ping < 300 ? "&c" + ping : ping < 500 ? "&4" + ping : "&4&l" + ping; 
			sender.sendMessage("&c> &7Ping: " + ms + " &7ms");
		}
		sender.sendMessage("&8&l«&b&l-&8&l»&m----------------------&f&8&l«&b&l-&8&l»&b");*/
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		send(sender);
		return true;
	}
	
	
}
