package de.flo56958.EasyHarvest;

import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.material.CocoaPlant;
import org.bukkit.material.Crops;
import org.bukkit.material.NetherWarts;

public class HarvesterListener implements Listener {
	
	@EventHandler
	public void onGrowth (BlockGrowEvent e) {
		if (e.getBlock().getType().equals(Material.AIR)) { return; }
		if (e.getNewState().getData() instanceof CocoaPlant) { return; }
		if (e.getNewState().getData() instanceof Crops) { if (!(((Crops) e.getNewState().getData()).getState().equals(CropState.RIPE))) { return; } }
		if (e.getNewState().getData() instanceof NetherWarts) { if (!(((NetherWarts) e.getNewState().getData()).getState().equals(NetherWartsState.RIPE))) { return; } }
		for (int x = -Harvester.getRange(); x <= Harvester.getRange(); x++) {
			for (int z = -Harvester.getRange(); z <= Harvester.getRange(); z++) {
				Location l = e.getBlock().getLocation().add(x, 0, z);
				Block b = e.getBlock().getWorld().getBlockAt(l);
				if (b.getType() == Material.TRAPPED_CHEST) {
					if (((Chest)b.getState()).getInventory().getTitle().equals(Harvester.getName())) {
						Harvester.harvest(b, e.getNewState().getBlock());
						e.setCancelled(true);
						return;
					}
				}
			}
		}
	}

}
