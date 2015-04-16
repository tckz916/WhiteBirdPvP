package com.github.niwaniwa.whitebird.pvp.event;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.niwaniwa.whitebird.pvp.arena.Arena;

public class WhiteBirdDuelAcceptEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	Player player;
	Player target;
	Arena arena;

	public WhiteBirdDuelAcceptEvent(Player player, Player target,Arena arena) {
		this.player = player;
		this.target = target;
		this.arena = arena;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public List<Player> getPlayers(){
		List<Player> temp = new ArrayList<Player>();
		temp.add(player);
		temp.add(target);
		return temp;
	}

	public Arena getArena(){
		return arena;
	}
}
