package fr.gnokize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import fr.gnokize.command.BoutiqueCommand;
import fr.gnokize.inventory.InventoryInteract;
import fr.gnokize.object.InventoryObject;
import fr.gnokize.object.Items;
import net.milkbowl.vault.economy.Economy;

public class Boutique extends JavaPlugin {
	
	public static List<InventoryObject> inventory = new ArrayList<>();
	public static List<Items> allItems = new ArrayList<>();
	
	public static Economy economy;
	
	public static Inventory invCoins;
	public static Inventory inv;
	
	private static Boutique instance;
	
	public static Boutique getInstance() {
		return instance;
	}
	
	@Override
	public void onEnable() {
        
        if (!setupEconomy()) {
            Bukkit.shutdown();
        }
		
        instance = this;
		
		saveDefaultConfig();
		
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(new InventoryInteract(), this);
		
		getCommand("boutique").setExecutor(new BoutiqueCommand());
		
		inv = Bukkit.createInventory(null, 27, "§cBoutique");
		invCoins = Bukkit.createInventory(null, 27, "§6Argents");
		
		ItemStack coins = new ItemStack(Material.GOLD_INGOT, 1);
		ItemMeta c = coins.getItemMeta();
		c.setDisplayName("§6Argents");
		coins.setItemMeta(c);
		
		ItemStack retour = new ItemStack(Material.BARRIER, 1);
		ItemMeta r = retour.getItemMeta();
		r.setDisplayName("§cRetour");
		retour.setItemMeta(r);
		
		invCoins.setItem(11, coins);
		invCoins.setItem(13, retour);
		
		ConfigurationSection section = getConfig().getConfigurationSection("inventory");
		for(String s : section.getKeys(false)) {
			
			String inventoryName = section.getString(s + ".inventory");
			int inventorySize = section.getInt(s + ".size");
			int slot = section.getInt(s +  ".slot");
			List<Items> item = new ArrayList<>();
			String ma = section.getString(s + ".item");
			Material matt = Material.matchMaterial(ma);
			if(matt == null) matt = Material.BARRIER;
			
			ConfigurationSection sec = getConfig().getConfigurationSection("inventory." + s + ".items");
			for(String se : sec.getKeys(false)) {
				
				String name = sec.getString(se + ".name");
				List<String> lore = sec.getStringList(se + ".lore");
				List<String> commands = sec.getStringList(se + ".commands");
				List<String> permission = sec.getStringList(se + ".permissions");
				List<ItemStack> gived = new ArrayList<>();
				int data = sec.getInt(se + ".data");
				String m = sec.getString(se + ".item");
				Material mat = Material.matchMaterial(m);
				if(mat == null) mat = Material.BARRIER;
				
				int slots = sec.getInt(se + ".slot");
				int creditsPrice = sec.getInt(se + ".credits");
				int coinsPrice = sec.getInt(se + ".coins");
				
				ItemStack it = new ItemStack(mat, 1, (byte)data);
				ItemMeta i = it.getItemMeta();
				i.setDisplayName(name);
				i.setLore(lore);
				it.setItemMeta(i);
				
				if(getConfig().getConfigurationSection("inventory." + s + ".items." + se + ".give") != null) {
					
					ConfigurationSection secs = getConfig().getConfigurationSection("inventory." + s + ".items." + se + ".give");
					for(String give : secs.getKeys(false)) {
						
						String g = secs.getString(give + ".item");
						Material gs = Material.matchMaterial(g);
						if(gs == null) gs = Material.BARRIER;
						
						int a = secs.getInt(give + ".amount");
						String n = secs.getString(give + ".name").replace('&', '§');
						byte d = (byte) secs.getInt(give + ".data");
						
						ItemStack stack = new ItemStack(gs, a, d);
						ItemMeta st = stack.getItemMeta();
						st.setDisplayName(n.replace('&', '§'));
						stack.setItemMeta(st);
						
						gived.add(stack);
						
					}
					
				}
				
				Items items = new Items(it, slots, coinsPrice, creditsPrice, commands, permission, gived);
				item.add(items);
				allItems.add(items);
				
			}
			
			InventoryObject inv = new InventoryObject(inventoryName, inventorySize, item, matt, slot);
			inv.createInventory();
			inv.initialiseInventory();
			
			inventory.add(inv);
			
		}
		
		Map<Integer, ItemStack> i = new HashMap<>();
		
		for(InventoryObject invs : Boutique.inventory) {
			
			ItemStack its = new ItemStack(invs.getMaterial(), 1);
			ItemMeta is = its.getItemMeta();
			is.setDisplayName(invs.getInventoryName());
			its.setItemMeta(is);
			
			i.put(invs.getSlot(), its);
			
		}
		
	    while(i.size() != 0) {
	    	for(int a = 0; a <= inv.getSize(); a++) {
		    	if(i.get(a) != null) {
		    		inv.setItem(a, i.get(a));
		    		i.remove(a);
		    	}
		    }
	    }
		
	}
	
	@Override
	public void onDisable() {
		
	}
	
    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
    
    public String getString(String s) {
    	return getConfig().getString("messages." + s).replace('&', '§');
    }

}
