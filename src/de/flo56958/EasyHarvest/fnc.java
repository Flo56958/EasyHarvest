package de.flo56958.EasyHarvest;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class fnc {
	
	public static ItemStack buildItem(Material m, int amount, String displayName, ArrayList<String> lore) {
		ItemStack is = new ItemStack(m, amount);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(displayName);
		meta.setLore(lore);
		is.setItemMeta(meta);
		return is;
	}

}