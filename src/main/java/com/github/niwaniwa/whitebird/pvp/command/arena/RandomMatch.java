package com.github.niwaniwa.whitebird.pvp.command.arena;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.niwaniwa.whitebird.pvp.arena.Arena;
import com.github.niwaniwa.whitebird.pvp.command.duel.AcceptCommand;
import com.github.niwaniwa.whitebird.pvp.util.Util;

public class RandomMatch extends BukkitRunnable implements CommandExecutor {

	private static List<Player> players;

	public final String pre  = "[§9WBC§r]";

	private AcceptCommand accept;

	public RandomMatch(){
		players = new ArrayList<Player>();
		accept = new AcceptCommand();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage(pre+"ゲーム内から実行してください");
			return true;
		}

		Player player = (Player) sender;

		if(Util.getArena(player)!=null){
			sender.sendMessage(pre+"試合中は実行できません");
			return true;
		}

		if(players.contains(player)){
			sender.sendMessage(pre+"既に申請しています");
			return true;
		}

		players.add(player);
		sender.sendMessage(pre+"試合が開始されるまでしばらくお待ちください");
		return true;
	}

	public static List<Player> getPlayers(){
		return players;
	}

	private Player randomPlayer(){
		List<Player> playerList = players;
		if(playerList.isEmpty()){
			return null;
		}
		Collections.shuffle(playerList);
		return playerList.get(0);
	}

	/*
	 * 処理
	 */
	@Override
	public void run() {
		if(players.isEmpty()){return;}
		Player p1 = randomPlayer();
		Player p2 = randomPlayer();

		if(p1.equals(p2)){
			return;
		}

		if(!p1.isOnline()
				|| !p2.isOnline()){
			return;
		}

		if(Util.getArena(p1) !=null
				|| Util.getArena(p2) != null){
			return;
		}

		Arena arena = accept.randomArena();

		if(arena==null){return;}

		accept.settingArena(arena, p1, p2);
		accept.setMode(p1);
		accept.setMode(p2);

	}








}
