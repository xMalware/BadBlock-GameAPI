package fr.badblock.game.core18R3.commands;

import org.bukkit.command.CommandSender;

import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.general.Flags;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class AuraCommand extends AbstractCommand
{

	public AuraCommand()
	{
		super("aura", new TranslatableString("commands.aura.usage"), GamePermission.PLAYER, GamePermission.PLAYER, GamePermission.PLAYER);
		this.allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args)
	{
		GameBadblockPlayer gbp = (GameBadblockPlayer) sender;
		if (!(gbp.hasPermission("aura.vip"))) {
			gbp.sendTranslatedMessage("aura.permission");
			return true;
		}
		if (Flags.isValid(gbp, "aura"))
		{
			gbp.sendTranslatedMessage("aura.pleasewait");
			return true;
		}
		Flags.setTemporaryFlag(gbp, "aura", 5_000);
		if (args.length == 0)
		{
			gbp.sendTranslatedMessage(gbp.getPlayerData().isAura() ? "aura.disable" : "aura.enable");
			gbp.getPlayerData().setAura(!gbp.getPlayerData().isAura());
			if (gbp.getPlayerData().isAura())
			{
				gbp.getPlayerData().setAuraVisible(true);
				gbp.enableAura();
			}
			gbp.saveGameData();
		}
		else if (args.length > 0)
		{
			String arg = args[0];
			switch (arg)
			{
			case "view":
				gbp.sendTranslatedMessage(gbp.getPlayerData().isAuraVisible() ? "aura.visibleDisable" : "aura.visibleEnable");
				gbp.getPlayerData().setAuraVisible(!gbp.getPlayerData().isAuraVisible());
				gbp.saveGameData();
				break;
			default:
				gbp.sendTranslatedMessage("aura.help");
				break;
			}
		}
		return true;
	}

}
