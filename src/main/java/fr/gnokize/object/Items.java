package fr.gnokize.object;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public class Items {
	
	private ItemStack items;
	private int slot;
	private int coinsPrice;
	private int creditsPrice;
	private List<String> commands;
	private List<String> permission;
	private List<ItemStack> gived;
	
	public Items(ItemStack items, int slot, int coinsPrice, int creditsPrice, List<String> commands, List<String> permission, List<ItemStack> gived) {
		this.items = items;
		this.slot = slot;
		this.coinsPrice = coinsPrice;
		this.creditsPrice = creditsPrice;
		this.commands = commands;
		this.permission = permission;
		this.gived = gived;
	}
	
	public List<ItemStack> getGived() {
		return gived;
	}
	
	public List<String> getPermissions() {
		return permission;
	}
	
	public ItemStack getItems() {
		return items;
	}
	
	public int getSlot() {
		return slot;
	}
	
	public int getCoinsPrice() {
		return coinsPrice;
	}
	
	public int getCreditsPrice() {
		return creditsPrice;
	}
	
	public List<String> getCommands() {
		return commands;
	}

}
