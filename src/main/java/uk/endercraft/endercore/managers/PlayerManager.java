package uk.endercraft.endercore.managers;

import java.util.Map;

import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import uk.endercraft.endercore.EnderPlayer;

public class PlayerManager {

	private static PlayerManager instance;

	private static PlayerManager get() {
		if (instance == null)
			instance = new PlayerManager();
		return instance;
	}

	private Map<Player, EnderPlayer> players = Maps.newHashMap();

	public static EnderPlayer getData(Player p) {
		return get().players.get(p);
	}

	public static EnderPlayer registerPlayer(Player p) {
		if (get().players.containsKey(p))
			return getData(p);
		EnderPlayer ep = new EnderPlayer(p);
		setPlayer(p, ep);
		return ep;
	}

	public static void setPlayer(Player p, EnderPlayer ip) {
		get().players.put(p, ip);
	}

	public static void unregisterPlayer(Player p) {
		if (!get().players.containsKey(p))
			return;
		EnderPlayer ep = getData(p);
		ep.save();
		removePlayer(p);
	}

	public static EnderPlayer removePlayer(Player p) {
		return get().players.remove(p);
	}

}
