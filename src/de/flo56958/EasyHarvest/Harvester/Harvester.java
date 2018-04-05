package de.flo56958.EasyHarvest.Harvester;

import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import de.flo56958.EasyHarvest.EasyHarvest;
import de.flo56958.EasyHarvest.Extras.fnc;

public class Harvester {
	
	private final static String NAME = "Harvester";
	private final static int RANGE = EasyHarvest.getPlugin().getConfig().getInt("Harvester.Range");

	@SuppressWarnings("deprecation")
	public static void registerMainCraftingRecipe() {
		try {
			ItemStack resultItem = new ItemStack(fnc.buildItem(Material.TRAPPED_CHEST, 1, NAME));
			ShapedRecipe newRecipe = new ShapedRecipe(resultItem); //init recipe
			newRecipe.shape(new String[] { "HOH", "ICI", "HOH" }); //makes recipe
			newRecipe.setIngredient('H', Material.DIAMOND_HOE); //set ingredients
			newRecipe.setIngredient('I', Material.IRON_BLOCK);
			newRecipe.setIngredient('C', Material.CHEST);
			EasyHarvest.getPlugin().getServer().addRecipe(newRecipe); //adds recipe
		} catch (Exception e) {
			EasyHarvest.getPlugin().getLogger().log(Level.WARNING, "Could not register recipe for Harvester!"); //executes if the recipe could not initialize
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void harvest(Block chest, Block crop) {
		Material m = crop.getType();
		Chest c = (Chest) chest.getState();
		crop.setType(Material.AIR); //destroys crop
        if (EasyHarvest.getPlugin().getConfig().getBoolean("Harvester.replant")) {
            crop.setType(m); //replants
        }
        if (EasyHarvest.getPlugin().getConfig().getBoolean("Harvester.playSound")) { harvestSound(crop); }
        if (m.equals(Material.CARROT)) { c.getInventory().addItem(new ItemStack(Material.CARROT_ITEM, EasyHarvest.getPlugin().getConfig().getInt("Harvester.AmountPerHarvest.carrots"))); } //gets drops for crops and put in chest
        if (m.equals(Material.CROPS)) { c.getInventory().addItem(new ItemStack(Material.WHEAT, EasyHarvest.getPlugin().getConfig().getInt("Harvester.AmountPerHarvest.wheat"))); }
        if (m.equals(Material.POTATO)) { c.getInventory().addItem(new ItemStack(Material.POTATO_ITEM, EasyHarvest.getPlugin().getConfig().getInt("Harvester.AmountPerHarvest.potato"))); }
        if (m.equals(Material.BEETROOT_BLOCK)) { c.getInventory().addItem(new ItemStack(Material.BEETROOT, EasyHarvest.getPlugin().getConfig().getInt("Harvester.AmountPerHarvest.beetroot"))); }
        if (m.equals(Material.NETHER_WARTS)) { c.getInventory().addItem(new ItemStack(372, EasyHarvest.getPlugin().getConfig().getInt("Harvester.AmountPerHarvest.netherwart"))); }
	}
	
	private static void harvestSound(Block crop) {
		crop.getWorld().playSound(crop.getLocation(), Sound.ITEM_HOE_TILL, 1.0F, 0.5F); //first number volume, second pitch
	}
	
	public static String getName() {
		return NAME;
	}
	
	public static int getRange() {
		return RANGE;
	}

}