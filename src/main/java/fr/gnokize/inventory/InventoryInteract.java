package fr.gnokize.inventory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.gnokize.Boutique;
import fr.gnokize.object.InventoryObject;
import fr.gnokize.object.Items;
import net.milkbowl.vault.economy.Economy;

public class InventoryInteract implements Listener {
	
	public Map<Player, Items> playerItems = new HashMap<>();
	private Economy economy = Boutique.economy;
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		
		Player player = (Player) event.getWhoClicked();
		ItemStack it = event.getCurrentItem();
		Inventory inv = event.getInventory();
		if(economy == null) {
			player.closeInventory();
			player.sendMessage("§cErreur : il doit y avoir au minimum une économie active");
			return;
		}
		
		if(it == null || inv == null || !it.hasItemMeta()) return;
		
		if(inv.getName().equalsIgnoreCase("§cBoutique")) {
			
			for(InventoryObject inventory : Boutique.inventory) {
				
				if(it.getItemMeta().getDisplayName().equalsIgnoreCase(inventory.getInventoryName())) {
					
					Inventory invs = inventory.getInventory();
					
					player.openInventory(invs);
					player.updateInventory();
					
				}
				
			}
			
			event.setCancelled(true);
			
		}
		
		if(inv.getName().equalsIgnoreCase("§6Argents")) {
			
			event.setCancelled(true);
			
			if(playerItems.containsKey(player)) {
				
				Items its = playerItems.get(player);
				
				if(it.getType() == Material.BARRIER) {
					player.closeInventory();
				}
				
				if(it.getType() == Material.GOLD_INGOT) {
					
					it.getItemMeta().setLore(Arrays.asList("", "§6Argent : " + its.getCoinsPrice(), ""));
					
					player.closeInventory();
					player.openInventory(Boutique.invCoins);
					
					player.updateInventory();
					
					if((int) economy.getBalance(player) >= its.getCoinsPrice()) {
						
						economy.withdrawPlayer(player, its.getCoinsPrice());
						
						for(String s : its.getCommands()) {
							Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), s.replace("%player%", player.getName()).replace("%item%", its.getItems().getItemMeta().getDisplayName()));
						}
						
						for(ItemStack stack : its.getGived()) {
							player.getInventory().addItem(stack);
						}
						
						playerItems.remove(player);
						
						player.closeInventory();
						
					} else {
						player.sendMessage(Boutique.getInstance().getString("not-coins"));
					}
					
				}
				
			} else {
				player.closeInventory();
			}
			
		}
				
		for(InventoryObject invs : Boutique.inventory) {
			if(inv.getName().equalsIgnoreCase(invs.getInventoryName())) {
				
				event.setCancelled(true);
				
				if(it.getType() == Material.BARRIER) {
					player.closeInventory();
					player.openInventory(Boutique.inv);
					player.updateInventory();
				}
				
				for(Items its : invs.getItems()) {
					
					if(it.getType() == Material.STAINED_GLASS_PANE) {event.setCancelled(true); continue;}
					if(it.getType() == Material.BARRIER) continue;
					if(inv.getName().equalsIgnoreCase("§cBoutique") || inv.getName().equalsIgnoreCase("§6Argents")) return;
					Items items = null;
					if(its.getItems().getItemMeta().getDisplayName().replace('§', '&').equalsIgnoreCase(it.getItemMeta().getDisplayName().replace('§', '&'))) {
						items = its;
					}
					if(items == null) {
						continue;
					}
					
					for(String s : items.getPermissions()) {
						if(player.hasPermission(s)) {
							player.sendMessage(Boutique.getInstance().getString("not-buy"));
							return;
						}
					}
					
					playerItems.remove(player);
					
					if(items.getCoinsPrice() != 0 && items.getCreditsPrice() == 0) {
						playerItems.put(player, items);
						player.openInventory(Boutique.invCoins);
					}

					if(items != null) break;
					
				}
				
			}
		}
		
	}

}
