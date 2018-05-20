package fr.badblock.game.core18R3.players.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalDouble;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.game.core18R3.players.utils.pnormal.ParticleEffect;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.BukkitUtils;
import lombok.Getter;
import lombok.Setter;

public class Meteor
{

	@Getter@Setter
	private Location loc;

	private	int		 ticks;

	public Meteor(Location loc)
	{
		this.loc = loc;
		//run();
	}

	public void run()
	{
		Bukkit.getScheduler().runTaskTimerAsynchronously(GamePlugin.getAPI(), new Runnable()
		{
			@Override
			public void run()
			{
				if (ticks < 20 * 10)
				{
					ticks++;
				}
				else
				{
					World world = Bukkit.getWorlds().get(0);
					List<Integer> x = new ArrayList<>();
					List<Integer> y = new ArrayList<>();
					List<Integer> z = new ArrayList<>();
					for (BadblockPlayer player : BukkitUtils.getAllPlayers())
					{
						Location location = player.getLocation();
						if (location.getWorld().equals(world))
						{
							x.add(location.getBlockX());
							y.add(location.getBlockY());
							z.add(location.getBlockZ());
						}
					}

					OptionalDouble xx = x.stream().mapToDouble(a -> a).average();
					OptionalDouble yy = y.stream().mapToDouble(a -> a).average();
					OptionalDouble zz = z.stream().mapToDouble(a -> a).average();

					if (xx.isPresent() && zz.isPresent())
					{
						double averageX = xx.getAsDouble();
						double averageZ = zz.getAsDouble();
						double averageY = yy.getAsDouble();
						double maxY = Math.min(averageY, 255 - 70) + 70;
						loc = new Location(world, averageX, maxY, averageZ);
					}
				}
				
				System.out.println(loc.toString());

				for (double i = 0; i <= Math.PI; i += Math.PI / 10)
				{
					double radius = Math.sin(i) * 4;
					double y = Math.cos(i) * 4;
					for (double a = 0; a < Math.PI * 2; a += Math.PI / 10)
					{
						double x = Math.cos(a) * radius;
						double z = Math.sin(a) * radius;
						loc.add(x, y, z);
						ParticleEffect.LAVA.display(0,0,0,0,1,loc, 100000D);
						loc.subtract(x, y, z);
					}
				}
			}
		}, 1, 1);
	}

	static <K,V extends Comparable<? super V>> 
	List<Entry<K, V>> entriesSortedByValues(Map<K,V> map) {

		List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());

		Collections.sort(sortedEntries, 
				new Comparator<Entry<K,V>>() {
			@Override
			public int compare(Entry<K,V> e1, Entry<K,V> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}
		}
				);

		return sortedEntries;
	}



}
