package fr.badblock.game.v1_8_R3.listeners;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.events.ProjectileHitBlockEvent;
import fr.badblock.gameapi.utils.reflection.ReflectionUtils;
import fr.badblock.gameapi.utils.reflection.Reflector;

public class ProjectileHitBlockCaller extends BadListener {
	@EventHandler
	private void onProjectileHit(final ProjectileHitEvent e) {
		boolean arrow = e.getEntityType() == EntityType.ARROW;
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(GameAPI.getAPI(), new Runnable() {
			public void run() {
				try {
					Object handler = ReflectionUtils.getHandle(e.getEntity());

					int x = getX(handler, arrow);
					int y = getY(handler, arrow);
					int z = getZ(handler, arrow);

					if(isValidBlock(y)) {
						Block block = e.getEntity().getWorld().getBlockAt(x, y, z);
						Bukkit.getServer().getPluginManager().callEvent(new ProjectileHitBlockEvent(e.getEntity(), block));
					}
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		});
	}

	private int getX(Object handler, boolean arrow) throws Exception {
		return (int) new Reflector(handler).getFieldValue(arrow ? "d" : "blockX");
	}

	private int getY(Object handler, boolean arrow) throws Exception {
		return (int) new Reflector(handler).getFieldValue(arrow ? "e" : "blockY");
	}

	private int getZ(Object handler, boolean arrow) throws Exception {
		return (int) new Reflector(handler).getFieldValue(arrow ? "f" : "blockZ");
	}

	private boolean isValidBlock(int y) {
		return y != -1;
	}
}
