package fr.badblock.game.v1_8_R3.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.BlockIterator;

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.events.ProjectileHitBlockEvent;
import fr.badblock.gameapi.utils.reflection.ReflectionUtils;
import fr.badblock.gameapi.utils.reflection.Reflector;

public class ProjectileHitBlockCaller extends BadListener {
	@EventHandler
	private void onProjectileHit(final ProjectileHitEvent e) {
		boolean arrow = e.getEntityType() == EntityType.ARROW;

		if (!arrow) {
			World world = e.getEntity().getWorld();
			BlockIterator iterator = new BlockIterator(world, e.getEntity().getLocation().toVector(), e.getEntity().getVelocity().normalize(), 0, 4);
			Block hitBlock = null;

			while (iterator.hasNext()) {
				hitBlock = iterator.next();

				if (hitBlock.getType() != Material.AIR)
					break;
			}

			if (hitBlock != null) {
				Bukkit.getServer().getPluginManager().callEvent(new ProjectileHitBlockEvent(e.getEntity(), hitBlock));
			}

			return;
		}

		Bukkit.getScheduler().scheduleSyncDelayedTask(GameAPI.getAPI(), new Runnable() {
			@Override
			public void run() {
				try {
					Object handler = ReflectionUtils.getHandle(e.getEntity());

					int x = getX(handler);
					int y = getY(handler);
					int z = getZ(handler);

					if (isValidBlock(y)) {
						Block block = e.getEntity().getWorld().getBlockAt(x, y, z);
						Bukkit.getServer().getPluginManager().callEvent(new ProjectileHitBlockEvent(e.getEntity(), block));
					}
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		});
	}

	private int getX(Object handler) throws Exception {
		return (int) new Reflector(handler).getFieldValue("d");
	}

	private int getY(Object handler) throws Exception {
		return (int) new Reflector(handler).getFieldValue("e");
	}
	
	private int getZ(Object handler) throws Exception {
		return (int) new Reflector(handler).getFieldValue("f");
	}

	private boolean isValidBlock(int y) {
		return y != -1;
	}
}
