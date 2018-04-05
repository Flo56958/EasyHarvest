package de.flo56958.EasyHarvest.Harvester;

import org.bukkit.CropState;
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
	public void onGrowth(BlockGrowEvent e) {
		if (e.getBlock().getType().equals(Material.AIR)) { return; } //Checks for SugarCane
		if (e.getNewState().getData() instanceof CocoaPlant) { return; } //Checks for Cocoa
		if (e.getNewState().getData() instanceof Crops) { if (!(((Crops) e.getNewState().getData()).getState().equals(CropState.RIPE))) { return; } } //Checks if Crop is RIPE
		if (e.getNewState().getData() instanceof NetherWarts) { if (!(((NetherWarts) e.getNewState().getData()).getState().equals(NetherWartsState.RIPE))) { return; } } //Checks if NertherWart is RIPE
		for (int x = -Harvester.getRange(); x <= Harvester.getRange(); x++) { //Both for-statements check the area around the crop for a Harvester
			for (int z = -Harvester.getRange(); z <= Harvester.getRange(); z++) {
				Block b = e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation().add(x, 0, z));
				if (b.getType() == Material.TRAPPED_CHEST) {
					if (((Chest)b.getState()).getInventory().getTitle().equals(Harvester.getName())) { //Checks for Harvester-Chest
						Harvester.harvest(b, e.getNewState().getBlock());
						e.setCancelled(true); //if not set to true, would negate the effect of harvest()
						return; //necessary to break for-statement if the chest was found and harvest() got called
					}
				}
			}
		}
	}

}
