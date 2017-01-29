package uk.endercraft.endercore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

import uk.endercraft.endercore.commands.TellCMD;
import uk.endercraft.endercore.db.MySQL;
import uk.endercraft.endercore.guiapi.GuiManager;
import uk.endercraft.endercore.listeners.PlayerListener;
import uk.endercraft.endercore.managers.PlayerManager;
import uk.endercraft.endercore.tasks.TabRanksTask;

public abstract class EnderCore extends JavaPlugin {

	private static EnderCore instance;

	private GuiManager guiManager;
	private MySQL sql;

	private List<Rank> ranks = Lists.newArrayList();

	public void onEnable() {
		instance = this;
		sql = new MySQL(getConfig().getString("sql.user", "root"), getConfig().getString("sql.password", ""),
				getConfig().getString("sql.url", "localhost"), getConfig().getInt("sql.port", 3306),
				getConfig().getString("sql.database", "endercraft_mc"));
		registerEvents();
		registerRanks();
		((JavaPlugin) Bukkit.getPluginManager().getPlugin("EnderCore")).getCommand("tell").setExecutor(new TellCMD());
		;
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		for (Player p : Bukkit.getOnlinePlayers())
			PlayerManager.registerPlayer(p);
		new TabRanksTask().runTaskTimer(this, 40L, 40L);
	}

	public void onDisable() {
		for (Player p : Bukkit.getOnlinePlayers())
			PlayerManager.unregisterPlayer(p);
		sql.disconnect();
	}

	private void registerEvents() {
		registerEvent(guiManager = new GuiManager());
		registerEvent(new PlayerListener());
	}

	protected void registerEvent(Listener l) {
		Bukkit.getPluginManager().registerEvents(l, this);
	}

	private void registerRanks() {
		Connection conn = sql.getConnection();
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ranks");
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				ranks.add(new Rank(rs.getInt("id"), rs.getString("name"), rs.getString("display_name")));
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void forceDisable(String errorMessage) {
		getLogger().severe("We've got to disable the plugin! Reason: " + errorMessage);
		Bukkit.getPluginManager().disablePlugin(this);
	}

	public Rank getRank(int id) {
		for (Rank rank : ranks)
			if (rank.getId() == id)
				return rank;
		return new Rank(0, "error", "&cerror");
	}

	public Rank getRank(String name) {
		for (Rank rank : ranks)
			if (rank.getName().equalsIgnoreCase(name))
				return rank;
		return new Rank(0, "error", "&cerror");
	}

	public List<Rank> getAllRanks() {
		return new ArrayList<Rank>(ranks);
	}

	public MySQL getSql() {
		return sql;
	}

	public static EnderCore get() {
		return instance;
	}

	public GuiManager getGuiManager() {
		return guiManager;
	}

	public boolean isMinigame() {
		return getMinigameName() != null;
	}

	public String getMinigameName() {
		return null;
	}

}
