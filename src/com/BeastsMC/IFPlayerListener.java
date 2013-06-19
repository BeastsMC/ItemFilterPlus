package com.BeastsMC;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class IFPlayerListener implements Listener {
	private final ItemFilter plugin;
	public IFPlayerListener(ItemFilter main) {
		plugin = main;
	}
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p  = e.getPlayer();
		if(p.hasPermission("itemfilter.exempt")) {
			return;
		}
		Inventory inven = p.getInventory();
		removeBannedItems(inven);
	}
	//Handles chest openings, but not player inventory openings
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onChestOpenEvent(InventoryOpenEvent e) {
		if(e.getPlayer().hasPermission("itemfilter.exempt")) {
			return;
		}
		Inventory inven = e.getInventory();
		removeBannedItems(inven);
	}
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onInventoryInteract(InventoryClickEvent e) {
		if(e.getWhoClicked().hasPermission("itemfilter.exempt")) {
			return;
		}
		ItemStack slotItem = e.getCurrentItem();
		ItemStack cursorItem = e.getCursor();
		if(slotItem !=null && plugin.bannedItemIds.contains(slotItem.getTypeId())) {
			e.setCurrentItem(new ItemStack(0));
		}
		if(cursorItem != null && plugin.bannedItemIds.contains(cursorItem.getTypeId())) {
			e.setCursor(new ItemStack(0));
		}
	}
	private void removeBannedItems(Inventory inven) {
		for(Integer bannedID : plugin.bannedItemIds) {
			if(inven.contains(bannedID)) {
				inven.remove(bannedID);
			}
		}
	}
}
