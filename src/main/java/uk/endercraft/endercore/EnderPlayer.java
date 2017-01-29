package uk.endercraft.endercore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.Executors;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import uk.endercraft.endercore.language.LanguageMain;

public class EnderPlayer {

	private final Player bukkit;
	private int dbId = 0;
	private final UUID uuid;
	private int tokens = 0;
	private int coins = 0;
	private String lang = "en_UK";
	private int rank = 0;
	private boolean firstLogin = true;
	private Map<String, MinigameData> minigameData = Maps.newHashMap();
	private final MinigameData cacheData = new MinigameData(0, 0, 0, 0, 0);
	private final List<String> permissions = Lists.newArrayList();
	private PermissionAttachment permAtt;
	private HashMap<String, Object> meta = new HashMap<String, Object>();

	public EnderPlayer(Player p) {
		bukkit = p;
		permAtt = p.addAttachment(EnderCore.get());
		uuid = p.getUniqueId();
		load();
		loadMinigamesScore();
		loadPermissions();
	}
	
	public void setMeta(String key, Object value){
		meta.put(key, value);
	}
	
	public Object getMeta(String key){
		return meta.get(key);
	}

	public MinigameData getCacheData() {
		return cacheData;
	}

	public int getDatabaseID() {
		return dbId;
	}

	public UUID getUuid() {
		return uuid;
	}

	public int getTokens() {
		return tokens;
	}

	public void setTokens(int tokens) {
		this.tokens = tokens;
	}

	public void addTokens(int tokens) {
		this.tokens += tokens;
	}

	public void removeTokens(int tokens) {
		this.tokens -= tokens;
	}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public void addCoins(int coins) {
		this.coins += coins;
	}

	public void removeCoins(int coins) {
		this.coins -= coins;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public int getRankID() {
		return rank;
	}

	public Rank getRank() {
		return EnderCore.get().getRank(rank);
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public boolean isFirstLogin() {
		return firstLogin;
	}

	public Player toBukkit() {
		return bukkit;
	}

	public void sendMessage(String code, Object... complements) {
		toBukkit().sendMessage(LanguageMain.get(this, code, complements));
	}

	public MinigameData getMinigameData(String minigameName) {
		if (minigameData.containsKey(minigameName))
			return minigameData.get(minigameName);
		MinigameData md = new MinigameData(0, 0, 0, 0, 0);
		minigameData.put(minigameName, md);
		return md;
	}

	private void load() {
		Connection conn = EnderCore.get().getSql().getConnection();
		try {
			conn.setNetworkTimeout(Executors.newCachedThreadPool(), 100);
			PreparedStatement stmt = conn
					.prepareStatement("SELECT id, tokens, coins, lang, rank FROM players WHERE uuid=?");
			stmt.setString(1, uuid.toString());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				this.dbId = rs.getInt("id");
				this.tokens = rs.getInt("tokens");
				this.coins = rs.getInt("coins");
				this.lang = rs.getString("lang");
				this.rank = rs.getInt("rank");
				this.firstLogin = false;
				return;
			}
			stmt.close();
			stmt = conn.prepareStatement(
					"INSERT INTO players (uuid, name, coins, tokens, rank, lang) VALUES (?, ?, ?, ?, ?, ?)");
			stmt.setString(1, uuid.toString());
			stmt.setString(2, bukkit.getName());
			stmt.setInt(3, 0);
			stmt.setInt(4, 0);
			stmt.setInt(5, 1);
			stmt.setString(6, "en_UK");
			stmt.execute();
			stmt.close();
			rs = conn.createStatement().executeQuery("SELECT LAST_INSERT_ID()");
			rs.first();
			this.dbId = rs.getInt(1);
			this.rank = 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void loadMinigamesScore() {
		Connection conn = EnderCore.get().getSql().getConnection();
		try {
			conn.setNetworkTimeout(Executors.newCachedThreadPool(), 100);
			PreparedStatement stmt = conn.prepareStatement(
					"SELECT minigames.minigame, minigames.kills, minigames.deaths, minigames.played, minigames.wins, minigames.xp FROM minigames INNER JOIN players ON players.id = minigames.player_id WHERE players.uuid=?");
			stmt.setString(1, uuid.toString());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				minigameData.put(rs.getString("minigames.minigame"),
						new MinigameData(rs.getInt("minigames.kills"), rs.getInt("minigames.deaths"),
								rs.getInt("minigames.played"), rs.getInt("minigames.wins"), rs.getInt("minigames.xp")));
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void loadPermissions() {
		Connection conn = EnderCore.get().getSql().getConnection();
		try {
			permissions.clear();
			conn.setNetworkTimeout(Executors.newCachedThreadPool(), 100);
			PreparedStatement stmt = conn
					.prepareStatement("SELECT permission FROM permissions WHERE type=0 AND obj_key=?");
			stmt.setInt(1, dbId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				permissions.add(rs.getString("permission").toLowerCase());
				permAtt.setPermission(rs.getString("permission").toLowerCase(), true);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (String s : getRank().getAllPermissions()) {
			permAtt.setPermission(s.toLowerCase(), true);
		}
	}

	public void addPermission(String permission) {
		if (permissions.contains(permission.toLowerCase()))
			return;
		permissions.add(permission.toLowerCase());
		permAtt.setPermission(permission.toLowerCase(), true);
		Connection conn = EnderCore.get().getSql().getConnection();
		try {
			PreparedStatement stmt = conn
					.prepareStatement("INSERT INTO permissions (type, obj_key, permission) VALUES(0, ?, ?)");
			stmt.setInt(1, dbId);
			stmt.setString(2, permission.toLowerCase());
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void removePermission(String permission) {
		if (!permissions.contains(permission.toLowerCase()))
			return;
		permissions.remove(permission.toLowerCase());
		permAtt.unsetPermission(permission.toLowerCase());
		Connection conn = EnderCore.get().getSql().getConnection();
		try {
			PreparedStatement stmt = conn
					.prepareStatement("DELETE FROM permissions WHERE type=0 AND obj_key=? AND permission=?");
			stmt.setInt(1, dbId);
			stmt.setString(2, permission.toLowerCase());
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean hasPermission(String permission) {
		if (getRank().hasPermission(permission))
			return true;
		return permissions.contains(permission.toLowerCase());
	}

	public void save() {
		bukkit.removeAttachment(permAtt);
		Connection conn = EnderCore.get().getSql().getConnection();
		try {
			PreparedStatement stmt = conn
					.prepareStatement("UPDATE players SET tokens=?, coins=?, lang=?, name=?, rank=? WHERE uuid=?");
			stmt.setInt(1, tokens);
			stmt.setDouble(2, coins);
			stmt.setString(3, lang);
			stmt.setString(4, bukkit.getName());
			stmt.setInt(5, rank);
			stmt.setString(6, uuid.toString());
			stmt.execute();
			for (Entry<String, MinigameData> entry : minigameData.entrySet()) {
				stmt = conn.prepareStatement(
						"SELECT minigames.id FROM minigames INNER JOIN players ON players.id = minigames.player_id WHERE players.uuid=? AND minigames.minigame=?");
				stmt.setString(1, uuid.toString());
				stmt.setString(2, entry.getKey());
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					stmt = conn.prepareStatement(
							"UPDATE minigames SET kills=?, deaths=?, played=?, wins=?, xp=? WHERE id=?");
					stmt.setInt(1, entry.getValue().getKills());
					stmt.setInt(2, entry.getValue().getDeaths());
					stmt.setInt(3, entry.getValue().getPlayed());
					stmt.setInt(4, entry.getValue().getWins());
					stmt.setInt(5, entry.getValue().getXp());
					stmt.setInt(6, rs.getInt("minigames.id"));
					stmt.execute();
					return;
				}
				stmt.close();
				stmt = conn.prepareStatement(
						"INSERT INTO minigames (player_id, minigame, kills, deaths, played, wins, xp) VALUES (?, ?, ?, ?, ?, ?, ?)");
				stmt.setInt(1, dbId);
				stmt.setString(2, entry.getKey());
				stmt.setInt(3, entry.getValue().getKills());
				stmt.setInt(4, entry.getValue().getDeaths());
				stmt.setInt(5, entry.getValue().getPlayed());
				stmt.setInt(6, entry.getValue().getWins());
				stmt.setInt(7, entry.getValue().getXp());
				stmt.execute();
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public class MinigameData {

		private int kills;
		private int deaths;
		private int played;
		private int wins;
		private int xp;

		/**
		 * @param kills
		 *            Player kills
		 * @param deaths
		 *            Player deaths
		 * @param played
		 *            Played games
		 * @param wins
		 *            Wins
		 * @param xp
		 *            Total xp on game
		 * @author Rexcantor64
		 */
		public MinigameData(int kills, int deaths, int played, int wins, int xp) {
			this.kills = kills;
			this.deaths = deaths;
			this.played = played;
			this.wins = wins;
			this.xp = xp;
		}

		public int getKills() {
			return kills;
		}

		public int getDeaths() {
			return deaths;
		}

		public int getPlayed() {
			return played;
		}

		public int getWins() {
			return wins;
		}

		public int getXp() {
			return xp;
		}

		public void setKills(int kills) {
			this.kills = kills;
		}

		public void addKills(int kills) {
			this.kills += kills;
		}

		public void setDeaths(int deaths) {
			this.deaths = deaths;
		}

		public void addDeaths(int deaths) {
			this.deaths += deaths;
		}

		public void setPlayed(int played) {
			this.played = played;
		}

		public void addPlayed(int played) {
			this.played += played;
		}

		public void setWins(int wins) {
			this.wins = wins;
		}

		public void addWins(int wins) {
			this.wins += wins;
		}

		public void setXp(int xp) {
			this.xp = xp;
		}

		public void addXp(int xp) {
			this.xp += xp;
		}

	}

}
