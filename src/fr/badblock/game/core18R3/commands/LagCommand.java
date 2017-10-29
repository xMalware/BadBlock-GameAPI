package fr.badblock.game.core18R3.commands;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.game.core18R3.technologies.rabbitlisteners.PlayerPingListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.general.MathsUtils;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import net.md_5.bungee.api.ChatColor;

public class LagCommand extends AbstractCommand
{

	public LagCommand()
	{
		super("lag", new TranslatableString("commands.lag.usage"), GamePermission.PLAYER, GamePermission.PLAYER, GamePermission.PLAYER, "tps", "gc", "bug");
	}

	private static SimpleDateFormat		simpleDateFormat			= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void send(CommandSender sender)
	{
		double lagPercent = (GameAPI.getAPI().getGameServer().getPassmarkTps() / 20.0D * 100.0D);
		double speed = MathsUtils.round(lagPercent, 2);
		String rapidity = speed >= 90 ? "&a" + speed : speed >= 80 ? "&b" + speed : speed >= 50 ? "&e" + speed : speed >= 30 ? "&c" + speed : speed >= 20 ? "&4" + speed : "&4&l" + speed;
		String ms = "0";

		if (sender instanceof BadblockPlayer)
		{
			GameBadblockPlayer player = (GameBadblockPlayer) sender;
			String realName = player.getRealName() != null ? player.getRealName() : player.getName();
			int ping = PlayerPingListener.ping.containsKey(realName) ? PlayerPingListener.ping.get(realName) : -1;
			ms = ping < 80 ? "&a" + ping : ping <= 100 ? "&b" + ping : ping < 200 ? "&e" + ping : ping < 300 ? "&c" + ping : ping < 500 ? "&4" + ping : "&4&l" + ping; 
		}

		StringBuilder versioning = new StringBuilder();
		for (Plugin plugin : Bukkit.getPluginManager().getPlugins())
		{
			if (plugin == null)
			{
				continue;
			}
			if (!plugin.getName().toLowerCase().contains("bad"))
			{
				continue;
			}
			String version = getManifestInfo(plugin.getClass());
			if (version.length() > 8)
			{
				version = version.substring(0, 7);
			}
			versioning.append(GameAPI.i18n().get(sender, "commands.lag.plugin", plugin.getName(), version) + System.lineSeparator());
		}

		GameAPI.i18n().sendMessage(sender, "commands.lag.message", 
				Bukkit.getServerName(),
				simpleDateFormat.format(new Date()),
				rapidity,
				ms,
				Bukkit.getVersion());

		String string = versioning.toString();
		if (!string.isEmpty())
		{
			String[] s = string.split(System.lineSeparator());
			GameAPI.i18n().sendMessage(sender, "commands.lag.plugins", s.length);
			for (String m : s)
			{
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', m));
			}
			sender.sendMessage(" ");
		}
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args)
	{
		send(sender);
		return true;
	}

	public static String getManifestInfo(Class<?> mainClass)
	{
		try
		{
			String className = mainClass.getSimpleName() + ".class";
			String classPath = mainClass.getResource(className).toString();
			String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) + 
					"/META-INF/MANIFEST.MF";
			Manifest manifest = new Manifest(new URL(manifestPath).openStream());
			Attributes attr = manifest.getMainAttributes();
			String value = attr.getValue("Implementation-Version");
			return value;
		}
		catch (Exception error)
		{
			return "unknown";
		}
	}

}
