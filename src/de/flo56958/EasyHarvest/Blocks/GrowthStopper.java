package de.flo56958.EasyHarvest.Blocks;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

import de.flo56958.EasyHarvest.EasyHarvest;

public class GrowthStopper implements Listener {
	
	@EventHandler
	public void onGrowth(BlockGrowEvent e) {
		if (e.getBlock().getType().equals(Material.AIR)) { e.setCancelled(true); }
		if (e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation().add(0, -2, 0)).getType() == Material.getMaterial(EasyHarvest.getPlugin().getConfig().getString("GrowthStopper.BlockType"))) {
			e.setCancelled(true);
		}
	}

}