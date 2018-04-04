package de.flo56958.EasyHarvest;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.CropState;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.CocoaPlant;
import org.bukkit.material.CocoaPlant.CocoaPlantSize;
import org.bukkit.material.Crops;
import org.bukkit.material.NetherWarts;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import de.flo56958.bStats.Metrics;

public class EasyHarvest extends JavaPlugin implements Listener {
	
	private WorldGuard wg = new WorldGuard();
	@SuppressWarnings("unchecked")
	private List<String> tools = (List<String>) getConfig().getList("Config.acceptedTools");
	
	@Override
	public void onEnable() {
		@SuppressWarnings("unused")
		Metrics metrics = new Metrics(this);
		Bukkit.getPluginManager().registerEvents(this, this);
		loadConfig();
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
    public void PlayerRightClick(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if (isHoe(p.getInventory().getItemInMainHand().getType())) {
        	if (e.getAction() == Action.RIGHT_CLICK_AIR) { return; }
        	if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
        		if (!(p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR)) {
        			boolean ableToBuild;
    				Block b = p.getTargetBlock( null, 15 );
    				try {
    					ableToBuild = wg.getCanBuild(p, b);
    				} catch (NoClassDefFoundError error) {
    				    ableToBuild = true;
    				} 
        			if (ableToBuild) {
                        harvest(p, b, b.getType());
        			}
            	}
        	}
        }
    }

	private boolean isHoe(Material m) {
		if (getConfig().getBoolean("Config.AllowAnyTool")) {
			return true;
		}
		for (String current : getTools()) {
			if (Material.getMaterial(current) == m) {
				return true;
			}
		}
		return false;
	}
	
	private void harvest(Player p, Block b, Material m) {
		Crops c;
		try {
			c = (Crops) b.getState().getData();
		} catch (Exception e) {
			if (b.getType() == Material.NETHER_WARTS) { harvestNetherWarts(p, b); }
			if (b.getType() == Material.COCOA) { harvestCocoa(p, b); }
			return;
		}
        if (c.getState() == CropState.RIPE){
            b.breakNaturally(new ItemStack(Material.DIAMOND_HOE));
            if (getConfig().getBoolean("Config.replant")) {
                b.setType(m);
            }
            if (getConfig().getBoolean("Config.playSound")) { playSound(b); }
        	if (p.getInventory().getItemInMainHand().getDurability() != 0) { manageDamage(p); }
        }
	}
	
	private void harvestCocoa(Player p, Block b) {
		CocoaPlant cp = (CocoaPlant) b.getState().getData();
        if (cp.getSize() == CocoaPlantSize.LARGE){ 	
        	BlockFace bf = cp.getFacing();
        	b.breakNaturally(new ItemStack(Material.DIAMOND_HOE));
            if (getConfig().getBoolean("Config.replant")) {
            	b.setType(Material.COCOA);
                CocoaPlant cp1 = new CocoaPlant(CocoaPlantSize.SMALL, bf);
                b.getState().setData(cp1);
                b.getState().update();
            }
            if (getConfig().getBoolean("Config.playSound")) { playSound(b); }
        	if (p.getInventory().getItemInMainHand().getDurability() != 0) { manageDamage(p); }
        }
	}

	private void harvestNetherWarts(Player p, Block b) {
		NetherWarts nw = (NetherWarts) b.getState().getData();
	    if (nw.getState() == NetherWartsState.RIPE){
	        b.breakNaturally(new ItemStack(Material.DIAMOND_HOE));
	        if (getConfig().getBoolean("Config.replant")) {
	            b.setType(Material.NETHER_WARTS);
	        }
	        if (getConfig().getBoolean("Config.playSound")) { playSound(b); }
	        if (p.getInventory().getItemInMainHand().getDurability() != 0) { manageDamage(p); }
	    }
	}

	@SuppressWarnings("deprecation")
	private void manageDamage(Player p) {
		if (p.getInventory().getItemInMainHand().getTypeId() == 351) { return; }
        if (getConfig().getBoolean("Config.includeEnchantments")) {
            int lv = p.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.DURABILITY);
            if (lv == 1) {
            	if (new Random().nextInt(100) <= 50) {
            		return;
            	}
            }
            if (lv == 2) {
            	if (new Random().nextInt(100) <= 67) {
            		return;
            	}
            }
            if (lv >= 3) {
            	if (new Random().nextInt(100) <= 75) {
            		return;
            	}
            }
        }
        if (!(p.getInventory().getItemInMainHand().getDurability() == p.getInventory().getItemInMainHand().getType().getMaxDurability())) {
        	p.getInventory().getItemInMainHand().setDurability((short) (p.getInventory().getItemInMainHand().getDurability() + getConfig().getInt("Config.Damage")));
        	if (p.getInventory().getItemInMainHand().getDurability() - p.getInventory().getItemInMainHand().getType().getMaxDurability() == 0) {
				p.getInventory().getItemInMainHand().setAmount(0);
				p.updateInventory();
			}
        } 
	}
	
	private void playSound(Block b) {
    	b.getWorld().playSound(b.getLocation(), Sound.ITEM_HOE_TILL, 1.0F, 0.5F);
	}
	
	private void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	private List<String> getTools() {
		return tools;
	}
	
	public static Plugin getPlugin() {
		return Bukkit.getPluginManager().getPlugin("EasyHarvest");
	}

}