package uk.endercraft.endercore.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class CustomStack {

	private Material material;
	private String displayName;
	private int amount = 1;
	private short durability = 0;
	private String[] lore;
	private Object[][] enchantments;
	private boolean itemInfoHidden = true;
	private boolean unbreakable = false;
	private ItemMeta previousMeta;

	public CustomStack setMaterial(Material type) {
		material = type;
		return this;
	}

	public CustomStack setDisplayName(String name) {
		this.displayName = ChatColor.translateAlternateColorCodes('&', name);
		return this;
	}

	public CustomStack setAmount(int amount) {
		this.amount = amount;
		return this;
	}

	public CustomStack setDurability(short durability) {
		this.durability = durability;
		return this;
	}

	public CustomStack setLore(String... lore) {
		this.lore = lore;
		return this;
	}

	public CustomStack setEnchantments(Object[]... enchants) {
		enchantments = enchants;
		return this;
	}

	public CustomStack setItemInfoHidden(boolean status) {
		itemInfoHidden = status;
		return this;
	}

	public CustomStack setUnbreakable(boolean status) {
		unbreakable = status;
		return this;
	}

	public CustomStack setPreviousMeta(ItemMeta previousMeta) {
		this.previousMeta = previousMeta;
		return this;
	}

	public ItemStack build() {
		ItemStack stack = new ItemStack(material, amount);
		ItemMeta stackMeta;
		if (previousMeta == null)
			stackMeta = stack.getItemMeta();
		else
			stackMeta = previousMeta;

		if (this.displayName != null) {
			stackMeta.setDisplayName(displayName);
		}

		if (this.durability != 0) {
			stack.setDurability(durability);
		}

		if (lore != null) {
			List<String> loreList = new ArrayList<String>();

			for (String s : lore) {
				loreList.add(ChatColor.translateAlternateColorCodes('&', s));
			}

			stackMeta.setLore(loreList);
		}

		if (enchantments != null) {
			for (Object[] enchant : enchantments) {
				if (enchant.length == 2) {
					stack.addUnsafeEnchantment((Enchantment) enchant[0], Integer.parseInt((String) enchant[1]));
				}
			}
		}

		if (itemInfoHidden) {
			stackMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			stackMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
			stackMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		}

		if (unbreakable) {
			stackMeta.spigot().setUnbreakable(true);
		}

		stack.setItemMeta(stackMeta);
		return stack;
	}

}
