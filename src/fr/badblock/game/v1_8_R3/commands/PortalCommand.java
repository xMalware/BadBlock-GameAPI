package fr.badblock.game.v1_8_R3.commands;

import org.bukkit.command.CommandSender;

import fr.badblock.game.v1_8_R3.GamePlugin;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.configuration.values.MapLocation;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.portal.Portal;
import fr.badblock.gameapi.portal.Portal.PortalType;
import fr.badblock.gameapi.utils.general.StringUtils;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class PortalCommand extends AbstractCommand {
	public PortalCommand() {
		super("portal", new TranslatableString("portals.command.usage"), GamePermission.ADMIN);
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		BadblockPlayer concerned = (BadblockPlayer) sender;

		if(args.length == 0){
			return false;
		}
		
		switch(args[0].toLowerCase()){
			case "list":
				concerned.sendTranslatedMessage("portals.command.list", StringUtils.join(GamePlugin.getInstance().getPortals().keySet(), ", "));
			break;
			case "create":
				if(args.length == 1)
					return false;
				if(concerned.getSelection() == null){
					concerned.sendTranslatedMessage("commands.noselection", StringUtils.join(GamePlugin.getInstance().getPortals().keySet(), ", "));
					return true;
				}
				
				args[1] = args[1].toLowerCase();
				
				if(GamePlugin.getInstance().getPortals().containsKey(args[1])){
					concerned.sendTranslatedMessage("portals.command.already-exist", args[1]);
					return true;
				}
				
				Portal portal = new Portal(concerned.getSelection());
				GamePlugin.getInstance().addPortal(args[1], portal);
				
				concerned.sendTranslatedMessage("portals.command.created");
			break;
			case "remove":
				if(args.length == 1)
					return false;
				
				args[1] = args[1].toLowerCase();
				
				if(!GamePlugin.getInstance().getPortals().containsKey(args[1])){
					concerned.sendTranslatedMessage("portals.command.not-exist", args[1]);
					return true;
				}
				
				GamePlugin.getInstance().removePortal(args[1]);
				
				concerned.sendTranslatedMessage("portals.command.removed");
			break;
			case "permission":
				if(args.length <= 2)
					return false;
				
				args[1] = args[1].toLowerCase();
				
				if(!GamePlugin.getInstance().getPortals().containsKey(args[1])){
					concerned.sendTranslatedMessage("portals.command.not-exist", args[1]);
					return true;
				}
				
				Portal p = GamePlugin.getInstance().getPortals().get(args[1]);
				p.setPermission(args[2]);
				
				GamePlugin.getInstance().savePortal(p);
				concerned.sendTranslatedMessage("portals.command.changed");
			break;
			case "cooldown":
				if(args.length <= 2)
					return false;
				
				args[1] = args[1].toLowerCase();
				
				if(!GamePlugin.getInstance().getPortals().containsKey(args[1])){
					concerned.sendTranslatedMessage("portals.command.not-exist", args[1]);
					return true;
				}
				
				int cooldown = 0;
				
				try {
					cooldown = Integer.parseInt(args[2]);
				} catch(Exception e){
					return false;
				}
				
				p = GamePlugin.getInstance().getPortals().get(args[1]);
				p.setCooldown(cooldown);
				
				GamePlugin.getInstance().savePortal(p);
				concerned.sendTranslatedMessage("portals.command.changed");
			break;
			case "destination_srv":
				if(args.length <= 2)
					return false;
				
				args[1] = args[1].toLowerCase();
				
				if(!GamePlugin.getInstance().getPortals().containsKey(args[1])){
					concerned.sendTranslatedMessage("portals.command.not-exist", args[1]);
					return true;
				}
				
				p = GamePlugin.getInstance().getPortals().get(args[1]);
				p.setServer(args[2]);
				p.setType(PortalType.BUNGEE_PORTAL);
				
				GamePlugin.getInstance().savePortal(p);
				concerned.sendTranslatedMessage("portals.command.changed");
			break;
			case "destination_loc":
				if(args.length < 2)
					return false;
				
				args[1] = args[1].toLowerCase();
				
				if(!GamePlugin.getInstance().getPortals().containsKey(args[1])){
					concerned.sendTranslatedMessage("portals.command.not-exist", args[1]);
					return true;
				}
				
				p = GamePlugin.getInstance().getPortals().get(args[1]);
				p.setPlace(new MapLocation(concerned.getLocation()));
				p.setType(PortalType.NORMAL_PORTAL);
				
				GamePlugin.getInstance().savePortal(p);
				concerned.sendTranslatedMessage("portals.command.changed");
			break;
		}
		
		return true;
	}
}