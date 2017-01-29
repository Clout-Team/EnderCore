package uk.endercraft.endercore.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class ItemStackUtils {

	/**
	 * 
	 * @deprecated Use CustomStack instead.
	 */
	public static ItemStack createItem(Material material, int amount, String displayName) {
		ItemStack a = new ItemStack(material, amount);
		if (displayName.length() > 0) {
			ItemMeta b = a.getItemMeta();
			b.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
			a.setItemMeta(b);
		}
		return a;
	}
	
	/**
	 * 
	 * @deprecated Use CustomStack instead.
	 */
	public static ItemStack createItem(Material material, int amount, String displayName, String[] lore) {
		try {
			ItemStack a = new ItemStack(material, amount);
			ItemMeta b = a.getItemMeta();
			if (displayName.length() > 0)
				b.setDisplayName(displayName);
			List<String> c = new ArrayList<String>();
			for (String d : lore)
				c.add(d);
			b.setLore(c);
			a.setItemMeta(b);
			return a;
		} catch (NullPointerException ex) {
			System.out.println("NullPointer on itemstack!");
			System.out.println("Material=" + material);
			System.out.println("Amount=" + amount);
			System.out.println(ex.getMessage());
			return new ItemStack(Material.BARRIER);
		}

	}
	
	/**
	 * 
	 * @deprecated Use CustomStack instead.
	 */
	public static ItemStack createItem(Material material, int amount, int durability, String displayName) {
		ItemStack a = createItem(material, amount, displayName);
		a.setDurability((short) durability);
		return a;
	}

	/**
	 * 
	 * @deprecated Use CustomStack instead.
	 */
	public static ItemStack createItem(Material material, int amount, int durability, String displayName,
			String[] lore) {
		ItemStack a = createItem(material, amount, displayName, lore);
		a.setDurability((short) durability);
		return a;
	}

	/**
	 * 
	 * @deprecated Use CustomStack instead.
	 */
	public static ItemStack createItem(Material material, int amount, String displayName, Object[]... enchantments) {
		ItemStack a = createItem(material, amount, displayName);
		for (Object[] enc : enchantments) {
			if (enc.length == 2)
				a.addUnsafeEnchantment((Enchantment) enc[0], Integer.parseInt((String) enc[1]));
		}
		return a;
	}

	/**
	 * 
	 * @deprecated Use CustomStack instead.
	 */
	public static ItemStack createItem(Material material, int amount, short durability, String displayName,
			List<String> lore, Object[][] enchantments) {
		ItemStack a = createItem(material, amount, durability, displayName, lore.toArray(new String[] {}));
		for (Object[] enc : enchantments) {
			if (enc[0] != null)
				a.addUnsafeEnchantment((Enchantment) enc[0], (Integer) enc[1]);
		}
		return a;
	}

	/**
	 * 
	 * @deprecated Use CustomStack instead.
	 */
	public static ItemStack changeAmount(ItemStack itemStack, int amount) {
		ItemStack a = itemStack.clone();
		a.setAmount(amount);
		return a;
	}

	public static ItemStack setLore(ItemStack itemStack, String... lore) {
		ItemMeta im = itemStack.getItemMeta();
		im.setLore(Arrays.asList(lore));
		itemStack.setItemMeta(im);
		return itemStack;
	}

}