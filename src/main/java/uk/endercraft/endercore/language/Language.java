package uk.endercraft.endercore.language;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import com.google.common.collect.Maps;

import uk.endercraft.endercore.EnderCore;

public abstract class Language {

	private String name;
	private BannerMeta meta;
	private ItemStack stack;
	private HashMap<String, String> map = Maps.newHashMap();

	public String getName() {
		return name;
	}

	public BannerMeta getMeta() {
		return meta;
	}

	public ItemStack getStack() {
		return stack;
	}

	public HashMap<String, String> getMap() {
		return map;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected void setMeta(BannerMeta meta) {
		this.meta = meta;
	}

	protected void setStack(ItemStack stack) {
		this.stack = stack;
	}

	protected void setMap(HashMap<String, String> map) {
		this.map = map;
	}
	
	public void loadMessages(){
		map.clear();
		try {
			ResourceBundle rb = new PropertyResourceBundle(new FileReader(new File(EnderCore.get().getDataFolder(),
					"languages/" + name + ".language")));
			for(String s : Collections.list(rb.getKeys())){
				map.put(s, rb.getString(s));
			}
		} catch (IOException e) {
			System.out.println("Error while loading language files for " + getName() +
					"! Error: " + e.getMessage());
		}
	}

}
