package fr.badblock.game.core18R3.players.utils.particle;

import org.bukkit.Location;

import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.game.core18R3.players.utils.particle.ParticleEffect.OrdinaryColor;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.BukkitUtils;
import fr.badblock.gameapi.utils.threading.TaskManager;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class AuraPlayer
{

	private GameBadblockPlayer	player;

	// Caca aura
	private Location			locN;
	private double				radius2 = 2;
	private double				y = 3;
	private double				x = radius2 * Math.cos(3 * y);
	private double				z = radius2 * Math.sin(3 * y);
	private double				y2 = 3 - y;

	public AuraPlayer(GameBadblockPlayer player)
	{
		setPlayer(player);
	}

	public void enableAura()
	{
		String taskName = "aura_" + getPlayer().getName();
		TaskManager.scheduleAsyncRepeatingTask(taskName, new Runnable()
		{
			@Override
			public void run()
			{
				if (!getPlayer().isOnline())
				{
					TaskManager.cancelTaskByName(taskName);
					return;
				}
				if (!getPlayer().getPlayerData().isAura())
				{
					TaskManager.cancelTaskByName(taskName);
					return;	
				}
				if (getPlayer().isSneaking())
				{
					return;
				}
				double d = 0.15;
				Location loc = getPlayer().getLocation().add(0, 0.02, 0);
				locN = loc;
				double radius = 2;
				y -= d;
				if (y <= 0) {
					y = 3;
					return;
				}
				radius = y / 3;
				x = radius * Math.cos(3 * y);
				z = radius * Math.sin(3 * y);
				y2 = 3 - y;
				Location loc2 = new Location(locN.getWorld(), locN.getX() + x,
						locN.getY() + y2, locN.getZ() + z);
				for (int i = 0; i < 5; i++)
				{
					for (BadblockPlayer player : BukkitUtils.getAllPlayers())
					{
						if (!getPlayer().getPlayerData().isAuraVisible() && player.getName().equals(getPlayer().getName()))
						{
							continue;
						}
						fr.badblock.game.core18R3.players.utils.particle.ParticleEffect.REDSTONE.display(player, 
								new OrdinaryColor(getPlayer().getPlayerData().getAuraRed1(), getPlayer().getPlayerData().getAuraGreen1(), getPlayer().getPlayerData().getAuraBlue1()),
								loc2, 64);
					}
				}
				radius = y / 3;
				x = -(radius * Math.cos(3 * y));
				z = -(radius * Math.sin(3 * y));
				y2 = 3 - y;
				if (y <= 0) {
					y = 3;
					return;
				}
				Location loc3 = new Location(locN.getWorld(), locN.getX() + x,
						locN.getY() + y2, locN.getZ() + z);
				for (int i = 0; i < 5; i++)
				{
					for (BadblockPlayer player : BukkitUtils.getAllPlayers())
					{
						if (!getPlayer().getPlayerData().isAuraVisible() && player.getName().equals(getPlayer().getName()))
						{
							continue;
						}
						fr.badblock.game.core18R3.players.utils.particle.ParticleEffect.REDSTONE.display(player, 
								new OrdinaryColor(getPlayer().getPlayerData().getAuraRed2(), getPlayer().getPlayerData().getAuraGreen2(), getPlayer().getPlayerData().getAuraBlue2()),
								loc3, 64);
					}
				}
			}
		}, 0, 1);
	}


}
