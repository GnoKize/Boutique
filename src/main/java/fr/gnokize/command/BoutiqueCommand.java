package fr.gnokize.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.gnokize.Boutique;

public class BoutiqueCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			
			Player player = (Player)sender;
			
			player.openInventory(Boutique.inv);
			player.updateInventory();
			
		}
		
		return false;
	}

}
