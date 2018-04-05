package de.flo56958.EasyHarvest;

import java.util.List;
import java.util.Random;

import org.bukkit.CropState;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
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

import de.flo56958.EasyHarvest.Extras.WorldGuard;

import org.bukkit.material.Crops;
import org.bukkit.material.NetherWarts;

public class HarvestbyPlayer implements Listener {
	
	private WorldGuard wg = new WorldGuard(); //outside of methods because of exceptions if worldguard isn`t installed
	@SuppressWarnings("unchecked")
	private List<String> tools = (List<String>) EasyHarvest.getPlugin().getConfig().getList("Player.acceptedTools");

	@EventHandler (priority = EventPriority.MONITOR)
    public void PlayerRightClick(PlayerInteractEvent e){
    	if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK)) { return; }
        Player p = e.getPlayer();
        if (isHoe(p.getInventory().getItemInMainHand().getType())) { //check if item in main hand is eligible
        	if (!(p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR)) { //checks if player is in survival or adventure mode
        		boolean ableToBuild;
    			Block b = p.getTargetBlock( null, 15 );
    			try { //check if worldguard is installed
    				ableToBuild = wg.getCanBuild(p, b);
    			} catch (NoClassDefFoundError error) { //no worldguard is installed
    			    ableToBuild = true;
    			} 
        		if (ableToBuild) { //checks if player has right to build
                    harvest(p, b, b.getType());
            	}
        	}
        }
    }

	private boolean isHoe(Material m) {
		if (EasyHarvest.getPlugin().getConfig().getBoolean("Player.AllowAnyTool")) {
			return true;
		}
		for (String current : getTools()) { //cycles through all configured materials/items
			if (Material.getMaterial(current) == m) {
				return true;
			}
		}
		return false; //if item is not eligible
	}
	
	private void harvest(Player p, Block b, Material m) {
		Crops c;
		try {
			c = (Crops) b.getState().getData(); //works only with beetroot, carrots, potatoes, wheat/crops
		} catch (Exception e) {
			if (b.getType() == Material.NETHER_WARTS) { harvestNetherWarts(p, b); } //else check for netherwarts and cocoa
			if (b.getType() == Material.COCOA) { harvestCocoa(p, b); }
			return; //necessary for no exceptions when comparing Cropstate.RIPE with c.getState()
		}
        if (c.getState() == CropState.RIPE){
            b.breakNaturally(new ItemStack(Material.DIAMOND_HOE)); //drops items and destroys crop
            if (EasyHarvest.getPlugin().getConfig().getBoolean("Player.replant")) {
                b.setType(m);
            }
            if (EasyHarvest.getPlugin().getConfig().getBoolean("Player.playSound")) { playSound(b); }
        	if (p.getInventory().getItemInMainHand().getDurability() != 0) { manageDamage(p); }
        }
	}
	
	private void harvestCocoa(Player p, Block b) {
		CocoaPlant cp = (CocoaPlant) b.getState().getData();
        if (cp.getSize() == CocoaPlantSize.LARGE){ 	
        	BlockFace bf = cp.getFacing();
        	b.breakNaturally(new ItemStack(Material.DIAMOND_HOE)); //drops items and destroys crop
            if (EasyHarvest.getPlugin().getConfig().getBoolean("Player.replant")) {
            	b.setType(Material.COCOA);
                BlockState bs = b.getState(); //necessary because bug with not placing cocoa plant correctly
                CocoaPlant cp1 = new CocoaPlant(CocoaPlantSize.SMALL, bf); //if not: cocoa plant wont be placed
                bs.setData(cp1);
                bs.update();
            }
            if (EasyHarvest.getPlugin().getConfig().getBoolean("Player.playSound")) { playSound(b); }
        	if (p.getInventory().getItemInMainHand().getDurability() != 0) { manageDamage(p); }
        }
	}

	private void harvestNetherWarts(Player p, Block b) {
		NetherWarts nw = (NetherWarts) b.getState().getData();
	    if (nw.getState() == NetherWartsState.RIPE){
	        b.breakNaturally(new ItemStack(Material.DIAMOND_HOE)); //drops items and destroys crop
	        if (EasyHarvest.getPlugin().getConfig().getBoolean("Player.replant")) {
	            b.setType(Material.NETHER_WARTS);
	        }
	        if (EasyHarvest.getPlugin().getConfig().getBoolean("Player.playSound")) { playSound(b); }
	        if (p.getInventory().getItemInMainHand().getDurability() != 0) { manageDamage(p); }
	    }
	}

	@SuppressWarnings("deprecation")
	private void manageDamage(Player p) {
		if (p.getInventory().getItemInMainHand().getTypeId() == 351) { return; } //checks for dye item (bug with dye durability [fixed]); needs attention in future MC updates
        if (EasyHarvest.getPlugin().getConfig().getBoolean("Player.includeEnchantments")) {
            int lv = p.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.DURABILITY);
            if (lv == 1) { if (new Random().nextInt(100) <= 50) { return; } } //lv 1: 50% no loss
            if (lv == 2) { if (new Random().nextInt(100) <= 67) { return; } } //lv 2: 67% no loss
            if (lv == 3) { if (new Random().nextInt(100) <= 75) { return; } } //lv 3: 75% no loss
            if (lv > 3) { return; } //unsafe enchants don´t lose any durability
        }
        if (!(p.getInventory().getItemInMainHand().getDurability() == p.getInventory().getItemInMainHand().getType().getMaxDurability())) {
        	p.getInventory().getItemInMainHand().setDurability((short) (p.getInventory().getItemInMainHand().getDurability() + EasyHarvest.getPlugin().getConfig().getInt("Player.ToolDamage"))); //mc logic: durability down means durability short up
        	if (p.getInventory().getItemInMainHand().getDurability() - p.getInventory().getItemInMainHand().getType().getMaxDurability() == 0) { //checks if item is used up and should be broken (bug with negative durability [fixed])
				p.getInventory().getItemInMainHand().setAmount(0); //deletes item
				p.updateInventory(); //updates client inventory (not sure if necessary)
			}
        } 
	}
	
	private void playSound(Block b) {
    	b.getWorld().playSound(b.getLocation(), Sound.ITEM_HOE_TILL, 1.0F, 0.5F); //first number volume, second pitch
	}

	private List<String> getTools() {
		return tools;
	}
	
}
