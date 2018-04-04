package de.flo56958.EasyHarvest;

import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class Harvester {
	
	private final static String NAME = "Harvester";
	private final static int RANGE = EasyHarvest.getPlugin().getConfig().getInt("Harvester.Range");

	@SuppressWarnings("deprecation")
	public static void registerMainCraftingRecipe() {
		try {
			ItemStack resultItem = new ItemStack(fnc.buildItem(Material.TRAPPED_CHEST, 1, NAME, null));
			ItemMeta meta = resultItem.getItemMeta();
			/////////////////add identifier
			//meta.setLore("Place it near crops to work");
			resultItem.setItemMeta(meta);
			ShapedRecipe newRecipe = new ShapedRecipe(resultItem);
			newRecipe.shape(new String[] {
					"HOH", "ICI", "HOH" 	});
			newRecipe.setIngredient('H', Material.DIAMOND_HOE);
			newRecipe.setIngredient('I', Material.IRON_BLOCK);
			newRecipe.setIngredient('C', Material.CHEST);
			EasyHarvest.getPlugin().getServer().addRecipe(newRecipe);
		} catch (Exception e) {
			EasyHarvest.getPlugin().getLogger().log(Level.WARNING, "Could not register recipe for Harvester!");
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void harvest(Block chest, Block crop) {
		Material m = crop.getType();
		Chest c = (Chest) chest.getState();
		crop.setType(Material.AIR);
        if (EasyHarvest.getPlugin().getConfig().getBoolean("Harvester.replant")) {
            crop.setType(m);
        }
        if (EasyHarvest.getPlugin().getConfig().getBoolean("Harvester.playSound")) { harvestSound(crop); }
        if (m.equals(Material.CARROT)) { c.getInventory().addItem(new ItemStack(Material.CARROT_ITEM, EasyHarvest.getPlugin().getConfig().getInt("Harvester.AmountPerHarvest.carrots"))); }
        if (m.equals(Material.CROPS)) { c.getInventory().addItem(new ItemStack(Material.WHEAT, EasyHarvest.getPlugin().getConfig().getInt("Harvester.AmountPerHarvest.wheat"))); }
        if (m.equals(Material.POTATO)) { c.getInventory().addItem(new ItemStack(Material.POTATO_ITEM, EasyHarvest.getPlugin().getConfig().getInt("Harvester.AmountPerHarvest.potato"))); }
        if (m.equals(Material.BEETROOT_BLOCK)) { c.getInventory().addItem(new ItemStack(Material.BEETROOT, EasyHarvest.getPlugin().getConfig().getInt("Harvester.AmountPerHarvest.beetroot"))); }
        if (m.equals(Material.NETHER_WARTS)) { c.getInventory().addItem(new ItemStack(372, EasyHarvest.getPlugin().getConfig().getInt("Harvester.AmountPerHarvest.netherwart"))); }
	}
	
	private static void harvestSound(Block crop) {
		crop.getWorld().playSound(crop.getLocation(), Sound.ITEM_HOE_TILL, 1.0F, 0.5F);
	}
	
	public static String getName() {
		return NAME;
	}
	
	public static int getRange() {
		return RANGE;
	}

}