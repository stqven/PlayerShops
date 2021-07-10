package com.inv.shop;

import com.inv.Config;
import com.inv.VaultAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;

public class ShopInventoryEvents implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Inventory inv = e.getClickedInventory();
        if (inv.getSize() >= 53 && inv.getItem(49) != null && inv.getItem(49).getType() == Material.BOOK && inv.getItem(49).getItemMeta().getPersistentDataContainer().has(NamespacedKey.minecraft("shopname"), PersistentDataType.STRING)) {
            Player p = (Player) e.getWhoClicked();
            String shopName = inv.getItem(49).getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft("shopname"), PersistentDataType.STRING);
            Shop shop = Shop.getShop(shopName);
            e.setCancelled(true);
            if (shop == null) return;
            int slot = e.getSlot();
            ItemStack item = e.getCurrentItem();
            if (item == null || item.getType() == Material.AIR) return;
            ItemMeta mitem = item.getItemMeta();
            if (slot >= 0 && slot < 45) {
                double price = mitem.getPersistentDataContainer().get(NamespacedKey.minecraft("price"), PersistentDataType.DOUBLE);
                String ownerName = mitem.getPersistentDataContainer().get(NamespacedKey.minecraft("owner"), PersistentDataType.STRING);
                if (ownerName.equalsIgnoreCase(p.getName()) || VaultAPI.removeMoney(p, price)) {
                    shop.removeItem(item, true);
                    if (mitem.getPersistentDataContainer().has(NamespacedKey.minecraft("lore"), PersistentDataType.STRING)) {
                        String str = mitem.getPersistentDataContainer().get(NamespacedKey.minecraft("lore"), PersistentDataType.STRING);
                        if (!str.equals("%%/NULL/%%")) {
                            ArrayList<String> lore = new ArrayList<String>(Arrays.asList(str.split("%%/SPACE/%%")));
                            mitem.setLore(lore);
                        } else {
                            mitem.setLore(null);
                        }
                    } else {
                        mitem.setLore(null);
                    }
                    if (mitem.getPersistentDataContainer().has(NamespacedKey.minecraft("owner"), PersistentDataType.STRING)) {
                        mitem.getPersistentDataContainer().remove(NamespacedKey.minecraft("owner"));
                    }
                    if (mitem.getPersistentDataContainer().has(NamespacedKey.minecraft("price"), PersistentDataType.DOUBLE)) {
                        mitem.getPersistentDataContainer().remove(NamespacedKey.minecraft("price"));
                    }
                    if (mitem.getPersistentDataContainer().has(NamespacedKey.minecraft("date"), PersistentDataType.DOUBLE)) {
                        mitem.getPersistentDataContainer().remove(NamespacedKey.minecraft("date"));
                    }
                    if (mitem.getPersistentDataContainer().has(NamespacedKey.minecraft("lore"), PersistentDataType.STRING)) {
                        mitem.getPersistentDataContainer().remove(NamespacedKey.minecraft("lore"));
                    }
                    if (mitem.getPersistentDataContainer().has(NamespacedKey.minecraft("startdate"), PersistentDataType.DOUBLE)) {
                        mitem.getPersistentDataContainer().remove(NamespacedKey.minecraft("startdate"));
                    }
                    item.setItemMeta(mitem);
                    p.getInventory().addItem(item);
                    if (ownerName.equalsIgnoreCase(p.getName())) {
                        Config.sendMessage(p, Config.getItemUnlisted(shopName, item.getType()));
                    } else {
                        Config.sendMessage(p, Config.getShopSuccessBuy());
                        VaultAPI.addMoney(Bukkit.getOfflinePlayer(ownerName), price);
                    }
                } else {
                    Config.sendMessage(p, Config.getShopNoMoney());
                }
            } else {
                if (inv.getItem(49).getItemMeta().getPersistentDataContainer().has(NamespacedKey.minecraft("page"), PersistentDataType.INTEGER)) {

                    int page = inv.getItem(49).getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft("page"), PersistentDataType.INTEGER);
                    if (item.getItemMeta().getDisplayName().equals(Config.getNextPage())) {
                        if ((shop.getShopItems().size() - 1)/45 >= page) {
                            p.openInventory(shop.getInventory(p, page + 1));
                        }
                    } else if (item.getItemMeta().getDisplayName().equals(Config.getPreviousPage())) {
                        if (page > 1) {
                            p.openInventory(shop.getInventory(p, page - 1));
                        }
                    }
                }
            }
        }
    }
}
