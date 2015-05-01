package com.github.niwaniwa.whitebird.pvp.command.duel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffect;

import com.github.niwaniwa.whitebird.pvp.arena.Arena;
import com.github.niwaniwa.whitebird.pvp.arena.ArenaType;
import com.github.niwaniwa.whitebird.pvp.util.Util;

public class AcceptCommand implements CommandExecutor {

	public final String pre  = "[§9WBC§r]";

	@Override
	public boolean onCommand(CommandSender sender,
			Command command, String label,
			String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage(pre+"ゲーム内から実行してください");
			return true;
		} else if(args.length==0){sender.sendMessage(pre+"§cプレイヤーを指定してください。"); return true;}

		Player player = (Player) sender;
		Player target = Util.getPlayer(args[0]);

		if(target == null){sender.sendMessage(pre+"§cプレイヤーが見つかりません。"); return true;}

		if(Util.getArena(player)!=null){
			sender.sendMessage(pre+"§c現在試合中です。");
			return true;
		} else if(Util.getArena(target)!=null){
			sender.sendMessage(pre+"§c"+target.getName()+"は現在試合中です。");
			return true;
		}

		HashMap<Player,Player> map = DuelCommand.getDuelPlayers();

		if(!Util.contains(map, target)
				|| map.get(target)==null){
			sender.sendMessage(pre+"§c貴方は申請を受けていません。");
			return true;
		}

		if(map.get(target).getName() != player.getName()){
			sender.sendMessage(pre+"§c"+target.getName()+"から申請は受けていません。");
			sender.sendMessage(pre+"§cもう一度申請し直してください。");
			return true;
		}

		Arena gameArena = randomArena();
		if(gameArena == null){
			sender.sendMessage(pre+"現在満員です");
			target.sendMessage(pre+"現在満員です");
			return true;
		}

		setMode(player);
		setMode(target);

		settingArena(gameArena, player, target);

		DuelCommand.getDuelPlayers().remove(target);

		return true;
	}

	/**
	 * ランダムにアリーナを設定 ※選択方式を変更
	 * @return Arena
	 */
	public Arena randomArena(){
		List<Arena> arenaList = Arena.getWaitArenas();
		if(arenaList.isEmpty()){
			return null;
		}
		Collections.shuffle(arenaList);
		return arenaList.get(0);
	}

	public void setMode(Player player){
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setGameMode(GameMode.SURVIVAL);
		removeEffects(player);
	}

	private void removeEffects(Player player){
		for(PotionEffect effect : player.getActivePotionEffects()){
			player.removePotionEffect(effect.getType());
		}
		player.setFireTicks(1);
	}

	public void settingArena(Arena arena, Player player1, Player player2){
		int i = 0;
		try{
		for(Location loc : arena.getLocations()){
			loc.setWorld(arena.getWorld());
			if(i==0){
				player1.teleport(loc);
				i++;
			} else if(i==1){
				player2.teleport(loc);
				i++;
			}
		}
		}catch(NullPointerException e){
			System.out.println("Location is null");
			errorMsg(player1);
			errorMsg(player2);
//			arena.stopArena();
//			arena.reload();
			player1.teleport(Bukkit.getWorld("world").getSpawnLocation(),TeleportCause.ENDER_PEARL);
			player2.teleport(Bukkit.getWorld("world").getSpawnLocation(),TeleportCause.ENDER_PEARL);
			DuelCommand.getDuelPlayers().remove(player2);
			return;
		}

		String temp = "§6： Map : §e"+arena.getArenaName();

		player1.sendMessage(temp);
		player2.sendMessage(temp);

		String temp2 = "§6： 対戦相手 ： §d";

		player1.sendMessage(temp2+player2.getName());
		player2.sendMessage(temp2+player1.getName());

		arena.getArenaPlayers().clear();
		arena.addPlayer(player1);
		arena.addPlayer(player2);
		arena.setType(ArenaType.STARTING);

	}

	private void errorMsg(Player p){
		p.sendMessage(pre+"§c問題が発生したため試合を開始することはできませんでした");
		p.sendMessage(pre+"§c申請は破棄されるため再度申請ください");
		p.sendMessage(pre+"§c/report <内容>で報告");
	}
}
