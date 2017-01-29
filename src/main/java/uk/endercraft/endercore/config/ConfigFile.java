package uk.endercraft.endercore.config;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;

import com.google.common.collect.Lists;

import uk.endercraft.endercore.EnderCore;

public class ConfigFile {

	private YamlConfiguration config;
	private File file;

	/**
	 * @author Rexcantor64
	 * @param fileName
	 *            The name of the YAML file, without .yml.
	 */
	public ConfigFile(String fileName) {
		File folder = EnderCore.get().getDataFolder();
		if (!folder.exists() && !folder.mkdirs()) {
			EnderCore.get().getLogger().severe("Failed to create the plugin folder!");
			return;
		}
		File confF = new File(folder, fileName + ".yml");
		if (!confF.exists())
			EnderCore.get().saveResource(fileName + ".yml", false);
		config = YamlConfiguration.loadConfiguration(confF);
		file = confF;
	}

	public YamlConfiguration toBukkit() {
		return config;
	}

	public File getFile() {
		return file;
	}

	public String getColoredString(String path, String def) {
		return ChatColor.translateAlternateColorCodes('&', toBukkit().getString(path, def));
	}

	public List<String> getColoredStringList(String path) {
		List<String> rawList = toBukkit().getStringList(path);
		List<String> coloredList = Lists.newArrayList();
		if (rawList == null)
			return coloredList;
		for (String a : rawList)
			coloredList.add(ChatColor.translateAlternateColorCodes('&', a));
		return coloredList;
	}

	public Object[][] getEnchantmentsArray(String path) {
		List<String> raw = toBukkit().getStringList(path);
		if (raw == null || raw.size() == 0)
			return new Object[][] {};
		Object[][] enchants = new Object[2][raw.size()];
		for (int i = 0; i < raw.size(); i++) {
			String[] array = raw.get(i).split(";");
			enchants[i] = new Object[] { Enchantment.getByName(array[0]), Integer.parseInt(array[1]) };
		}
		return enchants;
	}

	public void save() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
