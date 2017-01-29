package uk.endercraft.endercore.guiapi;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;

import uk.endercraft.endercore.EnderCore;

public class Gui {

	private int rows;
	private String title;
	private HashMap<Integer, GuiButton> items = Maps.newHashMap();
	private boolean blocked = true;
	private int timerDelay = -1;
	private Consumer<Gui> timerFunction;

	public Gui(int rows, String title) {
		this.rows = rows;
		this.title = title;
	}

	public Gui(int rows) {
		this(rows, "");
	}

	public Gui(String title) {
		this(1, title);
	}

	public Gui() {
		this(1, "");
	}

	public int nextIndex() {
		for (int i = 0; i < getSize(); i++) {
			if (!items.containsKey(i))
				return i;
		}
		return -1;
	}

	public void addButton(GuiButton button) {
		int index = nextIndex();

		if (index == -1)
			throw new RuntimeException("Inventory is full!");
		setButton(index, button);
	}

	public void setButton(int position, GuiButton button) {
		if (position >= getSize())
			throw new IllegalArgumentException("Position cannot be bigger than the size!");
		if (button == null) {
			removeButton(position);
			return;
		}
		items.put(position, button);
	}

	public GuiButton getButton(int position) {
		return items.get(position);
	}

	public void removeButton(int position) {
		items.remove(position);
	}

	public int getSize() {
		return rows * 9;
	}

	public void open(Player p) {
		Inventory inv = Bukkit.createInventory(null, getSize(), ChatColor.translateAlternateColorCodes('&', title));
		for (Entry<Integer, GuiButton> entry : items.entrySet())
			inv.setItem(entry.getKey(), entry.getValue().getItemStack());
		p.openInventory(inv);
		EnderCore.get().getGuiManager().add(inv, this);
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUpdateTimerDelay(int ticks) {
		timerDelay = ticks;
	}

	public int getUpdateTimerDelay() {
		return timerDelay;
	}

	public void setUpdateTimerFunction(Consumer<Gui> consumer) {
		this.timerFunction = consumer;
	}

	public Consumer<Gui> getUpdateTimerFunction() {
		return timerFunction;
	}

	public void updateInventory(Inventory inv) {
		for (int i = 0; i < getSize(); i++) {
			ItemStack is;
			if (!items.containsKey(i))
				is = null;
			else
				is = items.get(i).getItemStack();
			inv.setItem(i, is);
		}
	}

}
