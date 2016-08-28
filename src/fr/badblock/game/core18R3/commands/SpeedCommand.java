package fr.badblock.game.core18R3.commands;

import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class SpeedCommand extends AbstractCommand {
	public SpeedCommand() {
		super("speed", new TranslatableString("commands.speed.usage"), GamePermission.ADMIN);
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if (args.length != 1)
			return false;
		BadblockPlayer concerned = (BadblockPlayer) sender;
		float speed = -1F;
		try {
			speed = Float.parseFloat(args[0]);
		} catch (Exception error) {
			concerned.sendTranslatedMessage("commands.speed.notavalue");
			return true;
		}
		speed /= 10D;
		if (speed < 0 || speed > 10) {
			concerned.sendTranslatedMessage("commands.speed.outofrange");
			return true;
		}
		if (concerned.isFlying()) {
			concerned.setFlySpeed(speed);
			concerned.sendTranslatedMessage("commands.speed.setflyingspeed", speed);
			return true;
		}
		concerned.setWalkSpeed(speed);
		concerned.sendTranslatedMessage("commands.speed.setwalkspeed", speed);
		return true;
	}
}