package fr.badblock.game.core18R3.commands;

import static fr.badblock.game.core18R3.GamePlugin.FOLDER_KITS;
import static fr.badblock.game.core18R3.GamePlugin.getInstance;
import static fr.badblock.gameapi.GameAPI.getInternalGameName;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import fr.badblock.game.core18R3.players.data.GameKit;
import fr.badblock.game.core18R3.players.data.GameKit.KitLevel;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.general.JsonUtils;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class KitsCommand extends AbstractCommand {
	public KitsCommand() {
		super("kits", new TranslatableString("commands.kits.usage"), GamePermission.ADMIN);
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		BadblockPlayer player = (BadblockPlayer) sender;
		
		if(args.length <= 1){
			return false;
		}

		args[0] = args[0].toLowerCase();
		
		File folder = new File(getInstance().getDataFolder() + File.separator + FOLDER_KITS, getInternalGameName());
		
		if(!folder.exists()){
			player.sendTranslatedMessage("commands.kits.no-game", args[0]); return false;
		}
		
		GameKit kit = getKit(folder, args[0]);
		
		if(kit == null && !args[1].equalsIgnoreCase("create")){
			player.sendTranslatedMessage("commands.kits.unknow-kit", args[0]);
			return true;
		} else if(kit != null && args[1].equalsIgnoreCase("create")){
			player.sendTranslatedMessage("commands.kits.know-kit", args[0]);
			return true;
		}

		if(args[1].equalsIgnoreCase("create")){

			kit = new GameKit(args[0], false, Material.DIAMOND_SWORD.name(), (short) 0, new KitLevel[]{
					new KitLevel(new String[0], 100, GameAPI.getAPI().getKitContentManager().createFromInventory(player))
			});
			
			save(folder, kit);
			player.sendTranslatedMessage("commands.kits.create", args[0]);
			
		} else if(args[1].equalsIgnoreCase("addlevel")){
			
			List<KitLevel> levels = new ArrayList<>();
			
			for(KitLevel level : kit.getLevels()) levels.add(level);
			
			levels.add(new KitLevel(new String[0], 100, GameAPI.getAPI().getKitContentManager().createFromInventory(player)));
			
			kit.setLevels(levels.toArray(new KitLevel[0]));
			
			save(folder, kit);
			player.sendTranslatedMessage("commands.kits.addlevel", args[0]);
			
		} else if(args[1].equalsIgnoreCase("info")){
			
			player.sendTranslatedMessage("commands.kit.info", kit.getKitName(), kit.isVIP(), kit.getLevels().length);
			
		} else {
			if(args.length == 2){
				player.sendTranslatedMessage("commands.kits.usage-level"); return true;
			}
			
			KitLevel level = null;
			
			try {
				level = kit.getLevels()[Integer.parseInt(args[2])];
			} catch(Exception e){
				player.sendTranslatedMessage("commands.kits.unknow-level", args[2]); return true;
			}
			
			if(args[1].equalsIgnoreCase("loadlevel")){

				GameAPI.getAPI().getKitContentManager().give(level.getStuff(), player);
				player.sendTranslatedMessage("commands.kits.load", args[0], args[2]);
				
			} else if(args[1].equalsIgnoreCase("changelevel")){
				
				level.setStuff(GameAPI.getAPI().getKitContentManager().createFromInventory(player));
				save(folder, kit);
				player.sendTranslatedMessage("commands.kits.change", args[0], args[2]);
				
			}
		}

		return true;
	}

	private void save(File folder, GameKit kit){
		if(!folder.exists()){
			folder.mkdirs();
		}
		
		File file = new File(folder, kit.getKitName().toLowerCase() + ".json");
		JsonUtils.save(file, kit, true);
	}
	
	private GameKit getKit(File folder, String name){
		if(!folder.exists()){
			folder.mkdirs();
		}
		
		File file = new File(folder, name.toLowerCase() + ".json");
		
		if(!file.exists()){
			return null;
		} else {
			return JsonUtils.load(file, GameKit.class);
		}
	}
}
