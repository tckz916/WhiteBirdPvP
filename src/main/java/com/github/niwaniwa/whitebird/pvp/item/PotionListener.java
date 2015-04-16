package com.github.niwaniwa.whitebird.pvp.item;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;

public class PotionListener implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR)
				|| event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			if(event.getItem()==null
					|| !event.getItem().getType().equals(Material.POTION)){return;}
			Potion potion = Potion.fromItemStack(event.getItem());
			if(potion.isSplash()){
				event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
			}

		}
	}
}