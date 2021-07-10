package com.inv.list;

import com.inv.Config;
import com.inv.DS.TimeConverter;
import com.inv.shop.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ListInventory {

    public static int x = 0;

    Shop shop;
    double price;
    double time;

    public ListInventory(Shop shop) {
        this.shop = shop;
        this.price = shop.getMinPirce();
        this.time = shop.getMinTime();
    }

    public ListInventory(Shop shop, double price) {
        this.shop = shop;
        this.price = Math.max(shop.getMinPirce(), price);
        this.time = shop.getMinTime();
    }

    public ListInventory(Shop shop, double price, double time) {
        this.shop = shop;
        this.price = Math.max(shop.getMinPirce(), price);
        this.time = Math.max(shop.getMinTime(), time);
    }

    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(null, 45, Config.getListItemNameInventory(shop.getShopName()));
        inv.setItem( 20, getTimeItemStack(time));
        inv.setItem(24, getPriceItemStack(price));
        inv.setItem(40, getSaveItemStack());
        doAnimation(inv, x%6, shop.getShopName());
        return inv;
    }

    public static String getInventoryShopName(InventoryView inv) {
        if (!isListInventory(inv)) return null;
        return inv.getItem(0).getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft("shopname"), PersistentDataType.STRING);
    }

    public static boolean isListInventory(InventoryView inv) {
        ItemStack item = inv.getItem(0);
        if (item != null && item.getType() != Material.AIR &&
                item.getItemMeta().getPersistentDataContainer().has(NamespacedKey.minecraft("shopname"), PersistentDataType.STRING)) {
            String shopName = item.getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft("shopname"), PersistentDataType.STRING);
            if (inv.getTitle().equalsIgnoreCase(Config.getListItemNameInventory(shopName))) {
                return true;
            }
        }
        return false;
    }

    public static ItemStack getPriceItemStack(double price) {
        ItemStack item = new ItemStack(Material.GOLD_INGOT);
        ItemMeta mitem = item.getItemMeta();
        mitem.setDisplayName(Config.getListPriceName(price));
        mitem.setLore(Config.getListPriceLore());
        mitem.getPersistentDataContainer().set(NamespacedKey.minecraft("price"), PersistentDataType.DOUBLE, price);
        item.setItemMeta(mitem);
        return item;
    }

    public static ItemStack getSaveItemStack() {
        ItemStack item = new ItemStack(Material.MAGMA_CREAM);
        ItemMeta mitem = item.getItemMeta();
        mitem.setDisplayName(Config.getListSaveName());
        mitem.setLore(Config.getListSaveLore());
        item.setItemMeta(mitem);
        return item;
    }

    public static ItemStack getTimeItemStack(double time) {
        ItemStack item = new ItemStack(Material.CLOCK);
        ItemMeta mitem = item.getItemMeta();
        mitem.setDisplayName(Config.getListTimeName(TimeConverter.convert(time)));
        mitem.setLore(Config.getListTimeLore());
        mitem.getPersistentDataContainer().set(NamespacedKey.minecraft("time"), PersistentDataType.DOUBLE, time);
        item.setItemMeta(mitem);
        return item;
    }

    public static void doAnimation(Inventory inv, int index, String shopName) {
        ItemStack black = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemStack green = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta mitem = black.getItemMeta();
        mitem.setDisplayName("§7");
        black.setItemMeta(mitem);
        green.setItemMeta(mitem);
        for (int i = 0; i < 45; i++) {
            if (i == 22 || i == 20 || i == 24 || i == 40) continue;
            inv.setItem(i, black);
        }
        if (index == 0) {
            inv.setItem(13, green);
            inv.setItem(21, green);
            inv.setItem(23, green);
            inv.setItem(31, green);
        } else if (index == 1) {
            inv.setItem(4, green);
            inv.setItem(12, green);
            inv.setItem(14, green);
            inv.setItem(30, green);
            inv.setItem(32, green);
        } else if (index == 2) {
            inv.setItem(3, green);
            inv.setItem(5, green);
            inv.setItem(11, green);
            inv.setItem(15, green);
            inv.setItem(19, green);
            inv.setItem(25, green);
            inv.setItem(29, green);
            inv.setItem(33, green);
            inv.setItem(39, green);
            inv.setItem(41, green);
        } else if (index == 3) {
            inv.setItem(2, green);
            inv.setItem(6, green);
            inv.setItem(10, green);
            inv.setItem(16, green);
            inv.setItem(18, green);
            inv.setItem(26, green);
            inv.setItem(28, green);
            inv.setItem(34, green);
            inv.setItem(38, green);
            inv.setItem(42, green);
        } else if (index == 4) {
            inv.setItem(1, green);
            inv.setItem(7, green);
            inv.setItem(9, green);
            inv.setItem(17, green);
            inv.setItem(27, green);
            inv.setItem(35, green);
            inv.setItem(37, green);
            inv.setItem(43, green);
        } else if (index == 5) {
            inv.setItem(0, green);
            inv.setItem(8, green);
            inv.setItem(36, green);
            inv.setItem(44, green);
        }
        ItemMeta mitem2 = inv.getItem(0).getItemMeta();
        mitem2.getPersistentDataContainer().set(NamespacedKey.minecraft("shopname"), PersistentDataType.STRING, shopName);
        inv.getItem(0).setItemMeta(mitem2);
    }
}
