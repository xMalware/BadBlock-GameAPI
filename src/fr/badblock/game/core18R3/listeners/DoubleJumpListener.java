package fr.badblock.game.core18R3.listeners;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.game.GameState;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.utils.general.Flags;
import fr.badblock.gameapi.utils.threading.TaskManager;

public class DoubleJumpListener extends BadListener {

	public Map<String, Integer> timesJumped = new HashMap<>();
	public Map<String, Long>    lastTime    = new HashMap<>();

	@EventHandler
	public void join(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!GamePlugin.getAPI().getRunType().equals(RunType.GAME))
		{
			return;
		}
		if (GamePlugin.getAPI().getGameServer().getGameState().equals(GameState.RUNNING))
		{
			return;
		}
		if (player.hasPermission("hub.doublejump"))
		{
			player.setAllowFlight(true);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void setFlyOnJump(PlayerToggleFlightEvent event) {
		BadblockPlayer player = (BadblockPlayer) event.getPlayer();
		Vector jump = player.getLocation().getDirection().multiply(1.5).setY(1);
		if (!GamePlugin.getAPI().getRunType().equals(RunType.GAME))
		{
			return;
		}
		if (GamePlugin.getAPI().getGameServer().getGameState().equals(GameState.RUNNING))
		{
			return;
		}
		if(event.isFlying() && event.getPlayer().getGameMode() != GameMode.CREATIVE)
		{
			if (player.hasPermission("hub.doublejump"))
			{
				event.setCancelled(true);
				if (Flags.isValid(player, "doubleJump"))
				{
					return;
				}
				Integer maxInt = player.getPermissionValue("doublejump", Integer.class);
				int max = 0;
				if (maxInt != null)
				{
					max = maxInt.intValue();
				}
				int jumpZ = timesJumped.containsKey(player.getName()) ? timesJumped.get(player.getName()) : 0;
				if (jumpZ != max)
				{
					player.playEffect(player.getLocation(), Effect.SMOKE, 1);
					player.playSound(player.getLocation(), Sound.DIG_WOOL, 100F, 1F);
					player.setFlying(false);
					player.setVelocity(player.getVelocity().add(jump));
					TaskManager.runTaskLater(new Runnable()
					{

						@Override
						public void run() {
							if (!player.isOnline())
							{
								return;
							}
							player.playEffect(player.getLocation(), Effect.SMOKE, 1);
							TaskManager.runTaskLater(new Runnable()
							{

								@Override
								public void run() {
									if (!player.isOnline())
									{
										return;
									}
									player.playEffect(player.getLocation(), Effect.SMOKE, 1);
									TaskManager.runTaskLater(new Runnable()
									{

										@Override
										public void run() {
											if (!player.isOnline())
											{
												return;
											}
											player.playEffect(player.getLocation(), Effect.SMOKE, 1);
										}

									}, 5);
								}

							}, 5);
						}

					}, 5);
					jumpZ++;
				}
				else if(jumpZ == max) 
				{
					jumpZ = 0;
					long ti = 3000;
					player.setAllowFlight(false);
					Flags.setTemporaryFlag(player, "doubleJump", ti);
					TaskManager.runTaskLater(new Runnable()
					{

						@Override
						public void run() {
							if (!player.isOnline())
							{
								return;
							}
							player.setAllowFlight(true);
						}

					}, (int) (ti / 50));
					lastTime.put(player.getName(), ti);
				}
				timesJumped.put(player.getName(), jumpZ);

			}
		}
	}

}
