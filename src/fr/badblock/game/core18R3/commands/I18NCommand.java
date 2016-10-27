package fr.badblock.game.core18R3.commands;

import org.bukkit.command.CommandSender;

import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class I18NCommand extends AbstractCommand {
	public I18NCommand() {
		super("i18n", new TranslatableString("commands.i18n.usage"), GamePermission.ADMIN, GamePermission.ADMIN, GamePermission.ADMIN);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if(args.length == 0)
			return false;
		
		if(args[0].equalsIgnoreCase("reload")){
			GamePlugin.getInstance().loadI18n();
			new TranslatableString("commands.i18r.reload").send(sender);
		} else if(args[0].equalsIgnoreCase("save")){
			GamePlugin.getInstance().getI18n().save();
			new TranslatableString("commands.i18r.save").send(sender);			
		}
		
		return true;
	}
}
