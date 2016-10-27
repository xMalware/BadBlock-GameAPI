package fr.badblock.game.core18R3.commands;

import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import fr.badblock.gameapi.utils.itemstack.ItemStackUtils;

public class RepairCommand extends AbstractCommand {
	public RepairCommand() {
		super("repair", new TranslatableString("commands.repair.usage"), "api.repair", "api.repair", "api.repair");
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		BadblockPlayer concerned = (BadblockPlayer) sender;

		if(args.length > 0 && args[0].equalsIgnoreCase("all")){

			int repaired = ItemStackUtils.repair(concerned.getInventory().getContents()) + ItemStackUtils.repair(concerned.getInventory().getArmorContents());

			if(repaired == 0){
				concerned.sendTranslatedMessage("commands.repair.cannotrepair-all");
			} else concerned.sendTranslatedMessage("commands.repair.repaired-all", repaired);
		} else {

			if(!ItemStackUtils.isValid(concerned.getItemInHand())){
				concerned.sendTranslatedMessage("commands.repair.noiteminhand");
			} else {
				int count = ItemStackUtils.repair(concerned.getItemInHand());

				if(count == 0){
					concerned.sendTranslatedMessage("commands.repair.cannotrepair-hand");
				} else concerned.sendTranslatedMessage("commands.repair.repaired-hand");
			}

		}

		return true;
	}
}