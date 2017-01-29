package uk.endercraft.endercore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import org.bukkit.ChatColor;

import com.google.common.collect.Lists;

public class Rank {

	private final int id;
	private final String name;
	private final String displayName;
	private final List<String> permissions = Lists.newArrayList();

	public Rank(int id, String name, String displayName) {
		this.id = id;
		this.name = name;
		this.displayName = displayName;
		loadPermissions();
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getColoredDisplayName() {
		return ChatColor.translateAlternateColorCodes('&', displayName);
	}

	private void loadPermissions() {
		Connection conn = EnderCore.get().getSql().getConnection();
		try {
			permissions.clear();
			conn.setNetworkTimeout(Executors.newCachedThreadPool(), 100);
			PreparedStatement stmt = conn
					.prepareStatement("SELECT permission FROM permissions WHERE type=1 AND obj_key=?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				permissions.add(rs.getString("permission").toLowerCase());
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addPermission(String permission) {
		if (permissions.contains(permission.toLowerCase()))
			return;
		permissions.add(permission.toLowerCase());
		Connection conn = EnderCore.get().getSql().getConnection();
		try {
			PreparedStatement stmt = conn
					.prepareStatement("INSERT INTO permissions (type, obj_key, permission) VALUES(1, ?, ?)");
			stmt.setInt(1, id);
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
		Connection conn = EnderCore.get().getSql().getConnection();
		try {
			PreparedStatement stmt = conn
					.prepareStatement("DELETE FROM permissions WHERE type=1 AND obj_key=? AND permission=?");
			stmt.setInt(1, id);
			stmt.setString(2, permission.toLowerCase());
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getAllPermissions(){
		return new ArrayList<String>(permissions);
	}

	public boolean hasPermission(String permission) {
		return permissions.contains(permission.toLowerCase());
	}

}
