package fr.gnokize.object;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryObject {
	
	private String inventoryName;
	private int inventorySize;
	private Inventory inv;
	private List<Items> items;
	private Material mat;
	private int slot;
	
	public InventoryObject(String inventoryName, int inventorySize, List<Items> items, Material mat, int slot) {
		this.inventoryName = inventoryName;
		this.inventorySize = inventorySize;
		this.slot = slot;
		this.items = items;
		this.mat = mat;
	}
	
	public int getSlot() {
		return slot;
	}
	
	public String getInventoryName() {
		return inventoryName.replace('&', '§');
	}
	
	public int getInventorySize() {
		return inventorySize;
	}
	
	public void createInventory() {
		inv = Bukkit.createInventory(null, getInventorySize(), getInventoryName());
	}
	
	public Inventory getInventory() {
		return inv;
	}
	
	public List<Items> getItems() {
		return items;
	}
	
	public Material getMaterial() {
		return mat;
	}
	
	public void initialiseInventory() {
			
		Map<Integer, ItemStack> it = new HashMap<>();
		
	    for(Items items : getItems()) {
	    	
	    	List<String> lores = items.getItems().getItemMeta().getLore().stream()
		    		.map(str -> str.replace("&", "§"))
		    		.collect(Collectors.toList());
				
			ItemStack stack = new ItemStack(items.getItems().getType(), 1);
			ItemMeta s = stack.getItemMeta();
			s.setDisplayName(items.getItems().getItemMeta().getDisplayName().replace('&', '§'));
			s.setLore(lores);
			stack.setItemMeta(s);
				
			it.put(items.getSlot(), stack);
			
	    }
	    
	    while(it.size() != 0) {
	    	for(int i = 0; i <= inv.getSize(); i++) {
		    	if(it.get(i) != null) {
		    		inv.setItem(i, it.get(i));
		    		it.remove(i);
		    	}
		    }
	    }

		ItemStack retour = new ItemStack(Material.BARRIER, 1);
		ItemMeta r = retour.getItemMeta();
		r.setDisplayName("§cRetour");
		retour.setItemMeta(r);
		
		inv.setItem(inv.getSize() - 1, retour);
		
	}

}
