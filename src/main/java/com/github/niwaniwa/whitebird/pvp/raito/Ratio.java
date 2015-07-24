package com.github.niwaniwa.whitebird.pvp.raito;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.github.niwaniwa.whitebird.core.player.WhiteBirdPlayer;

public class Ratio {

	private static List<Ratio> data = new ArrayList<Ratio>();

	private static final int defaultValue = 800;

	public static List<Ratio> getRatios(){
		return data;
	}

	public static Ratio getRatio(WhiteBirdPlayer wbp){
		return getRatio(wbp.getPlayer());
	}

	public static Ratio getRatio(Player player){
		for(Ratio r : Ratio.getRatios()){
			if(r.getPlayer().equals(player)){
				return r;
			}
		}
		return newRatio(player,false);
	}

	public static Ratio newRatio(WhiteBirdPlayer wbp, boolean check){
		return newRatio(wbp.getPlayer(), check);
	}

	public static Ratio newRatio(Player player, boolean check){
		if(check){
			return getRatio(player);
		}
		return new Ratio(player);
	}

	public static int ratioMath(int winRatio, int loseRatio){
		return (int) (16+(loseRatio-winRatio)*0.04);
	}

	/**
	 *
	 * @param winRatio
	 * @param loseRatio
	 * @param limit true : 5 ~ 20
	 * @return int ratio
	 */
	public static int ratioMath(int winRatio, int loseRatio, boolean limit){
		int i = ratioMath(winRatio, loseRatio);
		if(limit){
			if(i < 5){i=5;}
			else if(20 < i){i=20;}
		}
		return i;
	}

	int value;
	Player player;

	private Ratio(Player player){
		this.value = defaultValue;
		this.player = player;
		data.add(this);
		try {
			saveRatio();
		} catch (IOException e) {}
	}

	public int getRatio(){
		return value;
	}

	public Player getPlayer(){
		return player;
	}

	public void setRatio(int ratio){
		this.value = ratio;
		try {
			this.saveRatio();
		} catch (IOException e) {}
	}

	@Deprecated
	public void setPlayer(Player player){
		this.player = player;
	}

	final String path = "plugins/whitebird/players/ratio/";

	private File setting() throws IOException{
		if (!new File(path).exists()) {
			new File(path).mkdirs();
		}
		File file = new File(path + getPlayer().getUniqueId().toString()
				+ ".yml");
		if (!file.exists()) {
			new File(path).mkdirs();
			file.createNewFile();
		}
		return file;
	}

	public void saveRatio() throws IOException{
		File file = setting();
		YamlConfiguration yml = new YamlConfiguration();
		yml.set("ratio", value);
		yml.save(file);
	}

	public void loadRatio() throws IOException, InvalidConfigurationException{
		File file = setting();
		YamlConfiguration yml = new YamlConfiguration();
		yml.load(file);
		setRatio(Integer.getInteger((String) yml.get("ratio")));

	}

}
