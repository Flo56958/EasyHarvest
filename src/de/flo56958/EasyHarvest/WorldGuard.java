package de.flo56958.EasyHarvest;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class WorldGuard { //partially copied from worldguard wiki
	private WorldGuardPlugin getWorldGuard() {
	    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard"); //gets worldguard plugin

	    // WorldGuard may not be loaded
	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	        return null; //no worldguard is installed
	    }

	    return (WorldGuardPlugin) plugin; //worldguard is installed
	}
	
	public boolean getCanBuild(Player p, Block b) { //get if player can build in specific areas
		return getWorldGuard().canBuild(p, b);
	}
}
