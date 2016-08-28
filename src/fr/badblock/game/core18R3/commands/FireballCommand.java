package fr.badblock.game.core18R3.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.WitherSkull;
import org.bukkit.util.Vector;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class FireballCommand extends AbstractCommand {

	public FireballCommand() {
		super("fireball", new TranslatableString("commands.fireball.usage"), GamePermission.ADMIN);
		this.allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		BadblockPlayer badblockPlayer = (BadblockPlayer) sender;
		Class<? extends Entity> type = Fireball.class;
		Projectile projectile;
		int speed = 2;
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("small")) {
				type = SmallFireball.class;
			} else if (args[0].equalsIgnoreCase("arrow")) {
				type = Arrow.class;
			} else if (args[0].equalsIgnoreCase("skull")) {
				type = WitherSkull.class;
			} else if (args[0].equalsIgnoreCase("egg")) {
				type = Egg.class;
			} else if (args[0].equalsIgnoreCase("snowball")) {
				type = Snowball.class;
			} else if (args[0].equalsIgnoreCase("expbottle")) {
				type = ThrownExpBottle.class;
			} else if (args[0].equalsIgnoreCase("large")) {
				type = LargeFireball.class;
			}
		}
		final Vector direction = badblockPlayer.getEyeLocation().getDirection().multiply(speed);
		projectile = (Projectile) badblockPlayer.getWorld()
				.spawn(badblockPlayer.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), type);
		projectile.setShooter(badblockPlayer);
		projectile.setVelocity(direction);
		return true;
	}
}