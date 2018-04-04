package de.flo56958.EasyHarvest;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class fnc {
	
	public static ItemStack buildItem(Material m, int amount, String displayName) { //builds itemstack with custom name
		ItemStack is = new ItemStack(m, amount);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(displayName);
		is.setItemMeta(meta); //applies itemmeta
		return is;
	}

}