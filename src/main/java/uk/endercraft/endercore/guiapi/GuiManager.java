package uk.endercraft.endercore.guiapi;

import java.util.HashMap;
import java.util.function.Consumer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Maps;

import uk.endercraft.endercore.EnderCore;

public class GuiManager implements Listener {

	private HashMap<Inventory, Gui> open = Maps.newHashMap();
	private HashMap<Gui, BukkitRunnable> openRunnables = Maps.newHashMap();

	public void add(final Inventory inv, final Gui gui) {
		open.put(inv, gui);
		if (gui.getUpdateTimerDelay() > 0) {
			final Consumer<Gui> consumer = gui.getUpdateTimerFunction();
			BukkitRunnable br = new BukkitRunnable() {

				public void run() {
					consumer.accept(gui);
					gui.updateInventory(inv);
				}
			};
			openRunnables.put(gui, br);
			br.runTaskTimer(EnderCore.get(), gui.getUpdateTimerDelay(), gui.getUpdateTimerDelay());
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (!open.containsKey(e.getInventory()))
			return;
		Gui gui = open.get(e.getInventory());
		if (gui.isBlocked())
			e.setCancelled(true);
		if (e.getClickedInventory() == null)
			return;
		GuiButton btn = gui.getButton(e.getRawSlot());
		if (btn == null)
			return;
		btn.getEvent().onClick(e);
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Gui gui = open.remove(e.getInventory());
		if (gui == null)
			return;
		BukkitRunnable r = openRunnables.get(gui);
		if (r != null)
			r.cancel();
		openRunnables.remove(gui);
	}

}
