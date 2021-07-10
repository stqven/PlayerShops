package com.inv.settings;

import com.inv.Config;
import com.inv.shop.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class SettingsInventory {
    Shop shop;

    public SettingsInventory(Shop shop) {
        this.shop = shop;
    }

    public static ItemStack getToggleItem(boolean status) {
        ItemStack item;
        if (status) {
            item = new ItemStack(Material.POTION);
        } else {
            item = new ItemStack(Material.GLASS_BOTTLE);
        }
        ItemMeta mitem = item.getItemMeta();
        mitem.setDisplayName(Config.getShopToggledName(status));
        mitem.setLore(Config.getShopToggledLore(status));
        item.setItemMeta(mitem);
        return item;
    }

    public static ItemStack getTimeItem(double time) {
        ItemStack item = new ItemStack(Material.CLOCK);
        ItemMeta mitem = item.getItemMeta();
        mitem.setDisplayName(Config.getShopMinTimeName(time));
        mitem.setLore(Config.getShopMinTimeLore(time));
        item.setItemMeta(mitem);
        return item;
    }

    public static ItemStack getPriceItem(double price) {
        ItemStack item = new ItemStack(Material.GOLD_INGOT);
        ItemMeta mitem = item.getItemMeta();
        mitem.setDisplayName(Config.getShopMinPriceName(price));
        mitem.setLore(Config.getShopMinPriceLore(price));
        item.setItemMeta(mitem);
        return item;
    }

    public static ItemStack getRenameItem() {
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta mitem = item.getItemMeta();
        mitem.setDisplayName(Config.getShopRenameName());
        mitem.setLore(Config.getShopRenameLore());
        item.setItemMeta(mitem);
        return item;
    }

    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(null, 27, Config.getShopSettingsInvName());
        {
            ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemMeta mitem = item.getItemMeta();
            mitem.setDisplayName("");
            item.setItemMeta(mitem);
            for (int i = 0; i < 27; i++) {
                inv.setItem(i, item);
            }
            inv.setItem(10, getToggleItem(shop.getStatus()));
            inv.setItem(12, getTimeItem(shop.getMinTime()));
            inv.setItem(14, getPriceItem(shop.getMinPirce()));
            inv.setItem(16, getRenameItem());
        }
        ItemMeta mitem = inv.getItem(0).getItemMeta();
        mitem.getPersistentDataContainer().set(NamespacedKey.minecraft("shopname"), PersistentDataType.STRING, shop.getShopName());
        inv.getItem(0).setItemMeta(mitem);
        return inv;
    }
}
