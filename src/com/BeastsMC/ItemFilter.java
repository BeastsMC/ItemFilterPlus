package com.BeastsMC;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemFilter extends JavaPlugin {
	public Logger log;
	private Configuration fConf;
	public List<Integer> bannedItemIds = new ArrayList<Integer>();
	public Material replaceItemFrameItem;
	public void onEnable() {
		log = this.getLogger();
		getServer().getPluginManager().registerEvents(new IFPlayerListener(this), this);
		loadConfig();
	}
	private void loadConfig() {
		fConf = getConfig();
		bannedItemIds = fConf.getIntegerList("banned-items");
		Integer replaceID = fConf.getInt("replace.itemframe");
		replaceItemFrameItem = Material.getMaterial(replaceID);
	}
	public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
		if(args.length!=1) {
			return false;
		}
		if(args[0].equalsIgnoreCase("reload")) {
			sender.sendMessage(ChatColor.AQUA + "[ItemFilter] Reloading...");
			loadConfig();
			sender.sendMessage(ChatColor.AQUA + "[ItemFilter] Done reloading");
			return true;
		} else if(args[0].equalsIgnoreCase("print")) {
			sender.sendMessage(ChatColor.AQUA + "[ItemFilter] The following is a list of items that will be filtered:");
			sender.sendMessage(bannedItemIds.toString());
			return true;
		} else if(args[0].equalsIgnoreCase("purge")) {
			sender.sendMessage(ChatColor.AQUA + "[ItemFilter] Searching all loaded ItemFrames for banned blocks.");
			for(World world : getServer().getWorlds()) {
				for(ItemFrame itemframe : world.getEntitiesByClass(ItemFrame.class)) {
					if(bannedItemIds.contains(itemframe.getItem().getTypeId())) {
						itemframe.setItem(new ItemStack(replaceItemFrameItem));
					}
				}
			}
			sender.sendMessage(ChatColor.AQUA + "[ItemFilter] Done purging");
			return true;
		}
		return false;
	}

}
