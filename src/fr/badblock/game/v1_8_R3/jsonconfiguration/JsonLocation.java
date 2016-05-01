package fr.badblock.game.v1_8_R3.jsonconfiguration;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class JsonLocation {
	private String world;
	private double x;
	private double y;
	private double z;
	private float  yaw;
	private float  pitch;
	
	public JsonLocation(Location location){
		world = location.getWorld().getName();
		x	  = location.getX();
		y	  = location.getY();
		z	  = location.getZ();
		
		yaw	  = location.getYaw();
		pitch = location.getPitch();
	}
	
	public Location get(){
		return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
	}
}
