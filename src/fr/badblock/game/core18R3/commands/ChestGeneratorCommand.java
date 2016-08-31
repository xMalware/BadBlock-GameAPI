package fr.badblock.game.core18R3.commands;

import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.servers.ChestGenerator;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import fr.badblock.gameapi.utils.itemstack.ItemStackUtils;

public class ChestGeneratorCommand extends AbstractCommand {
	private ChestGenerator generator;
	
	public ChestGeneratorCommand(ChestGenerator generator) {
		super("chestgenerator", new TranslatableString("commands.chestg.usage"), GamePermission.ADMIN, "chestg");
		allowConsole(false);
	
		this.generator = generator;
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if(args.length == 0)
			return false;
		
		BadblockPlayer player = (BadblockPlayer) sender;
		
		switch(args[0].toLowerCase()){
			case "reset":
				generator.resetChests();
				player.sendTranslatedMessage("commands.chestg.reseted");
			break;
			case "additem":
				if(args.length == 1)
					return false;
				
				if(!ItemStackUtils.isValid( player.getItemInHand() )){
					player.sendTranslatedMessage("commands.chestg.noitem");
				} else {
					int prob = 0;
					
					try {
						prob = Integer.parseInt(args[1]);
					
						if(prob <= 0)
							throw new IllegalArgumentException();
					} catch(Exception e){
						player.sendTranslatedMessage("commands.chestg.nan", args[1]);
						return true;
					}
					
					generator.addItemInConfiguration(player.getItemInHand(), prob, true);
					player.sendTranslatedMessage("commands.chestg.added", prob);
				}
			break;
		}
		
		return true;
	}
}
