package de.flo56958.EasyHarvest;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import de.flo56958.EasyHarvest.Blocks.GrowthStopper;
import de.flo56958.EasyHarvest.Harvester.Harvester;
import de.flo56958.EasyHarvest.Harvester.HarvesterListener;
import de.flo56958.bStats.Metrics;

public class EasyHarvest extends JavaPlugin {
	
	@Override
	public void onEnable() {
		@SuppressWarnings("unused")
		Metrics metrics = new Metrics(this);
		Bukkit.getPluginManager().registerEvents(new HarvestbyPlayer(), this);
		loadConfig();
		if (getConfig().getBoolean("Harvester.enable")) {
			Bukkit.getPluginManager().registerEvents(new HarvesterListener(), this);
			Harvester.registerMainCraftingRecipe();
		}
		if (getConfig().getBoolean("GrowthStopper.enable")) {
			Bukkit.getPluginManager().registerEvents(new GrowthStopper(), this);
		}
	}
	public static Plugin getPlugin() {
		return Bukkit.getPluginManager().getPlugin("EasyHarvest");
	}
	
	private void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

}