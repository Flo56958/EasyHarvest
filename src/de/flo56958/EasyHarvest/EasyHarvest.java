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
		Metrics metrics = new Metrics(this); //implements bstats
		Bukkit.getPluginManager().registerEvents(new HarvestbyPlayer(), this); //registers event
		loadConfig(); //creates and / or loads configuration
		if (getConfig().getBoolean("Harvester.enable")) {
			Bukkit.getPluginManager().registerEvents(new HarvesterListener(), this); //registers event
			Harvester.registerMainCraftingRecipe(); //registers recipe for Harvester
		}
		if (getConfig().getBoolean("GrowthStopper.enable")) {
			Bukkit.getPluginManager().registerEvents(new GrowthStopper(), this); //registers event
		}
	}
	public static Plugin getPlugin() { //necessary to do getConfig() in other classes
		return Bukkit.getPluginManager().getPlugin("EasyHarvest");
	}
	
	private void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

}