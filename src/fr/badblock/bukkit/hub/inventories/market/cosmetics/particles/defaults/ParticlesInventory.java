package fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.defaults;

import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.inventoryitems.BackCosmeticsItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data.AnimatedBallParticleItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data.AtomParticleItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data.BleedParticleItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data.CloudParticleItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data.CubeParticleItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data.CylinderParticleItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data.DiscoBallParticleItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data.DnaParticleItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data.DonutParticleItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data.DragonParticleItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data.EarthParticleItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data.FlameParticleItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data.FountainParticleItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data.HelixParticleItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data.HillParticleItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data.LoveParticleItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data.MusicParticleItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data.ShieldParticleItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data.SphereParticleItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data.StarParticleItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data.TextParticleItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data.TornadoParticleItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data.VortexParticleItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data.WarpParticleItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data.WaveParticleItem;
import fr.badblock.bukkit.hub.inventories.settings.items.BlueStainedGlassPaneItem;

public class ParticlesInventory extends CustomInventory {

	public ParticlesInventory() {
		super("hub.items.particlesinventory", 4);
		BlueStainedGlassPaneItem blueStainedGlassPaneItem = new BlueStainedGlassPaneItem();
		this.setItem(blueStainedGlassPaneItem, 28, 29, 30, 31, 32, 33, 34);
		this.addItem(new AnimatedBallParticleItem());
		// this.addItem(new ArcParticleItem());
		this.addItem(new AtomParticleItem());
		this.addItem(new BleedParticleItem());
		// this.addItem(new CircleParticleItem());
		this.addItem(new CloudParticleItem());
		// this.addItem(new ColoredImageParticleItem());
		// this.addItem(new ConeParticleItem());
		this.addItem(new CubeParticleItem());
		this.addItem(new CylinderParticleItem());
		this.addItem(new DiscoBallParticleItem());
		this.addItem(new DnaParticleItem());
		this.addItem(new DonutParticleItem());
		this.addItem(new DragonParticleItem());
		this.addItem(new EarthParticleItem());
		// this.addItem(new EquationParticleItem());
		this.addItem(new FlameParticleItem());
		this.addItem(new FountainParticleItem());
		// this.addItem(new GridParticleItem());
		// this.addItem(new HeartParticleItem());
		this.addItem(new HelixParticleItem());
		this.addItem(new HillParticleItem());
		// this.addItem(new IconParticleItem());
		// this.addItem(new ImageParticleItem());
		// this.addItem(new JumpParticleItem());
		// this.addItem(new LineParticleItem());
		this.addItem(new LoveParticleItem());
		this.addItem(new MusicParticleItem());
		this.addItem(new ShieldParticleItem());
		// this.addItem(new SkyRocketParticleItem());
		// this.addItem(new SmokeParticleItem());
		this.addItem(new SphereParticleItem());
		this.addItem(new StarParticleItem());
		this.addItem(new TextParticleItem());
		this.addItem(new TornadoParticleItem());
		// this.addItem(new TraceParticleItem());
		// this.addItem(new TurnParticleItem());
		this.addItem(new VortexParticleItem());
		this.addItem(new WarpParticleItem());
		this.addItem(new WaveParticleItem());
		this.setItem(27, new RemoveParticlesCosmeticsItem());
		this.setItem(35, new BackCosmeticsItem());
	}

}
