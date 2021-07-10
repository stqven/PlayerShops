package com.inv.list;

import com.inv.Config;
import com.inv.shop.Shop;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ListInventoryEvents implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Inventory inv = e.getClickedInventory();
        Player p = (Player) e.getWhoClicked();
        if (ListInventory.isListInventory(e.getView())) {
            if (e.getSlot() != 22 && e.getClickedInventory().getType() == InventoryType.CHEST) {
                e.setCancelled(true);
            }
            String shopName = ListInventory.getInventoryShopName(e.getView());
            Shop shop = Shop.getShop(shopName);
            ItemMeta mitem = inv.getItem(e.getSlot()).getItemMeta();
            if (e.getSlot() == 20) {
                double added = 0;
                if (e.getClick() == ClickType.LEFT) {
                    added = 3600;
                } else if (e.getClick() == ClickType.SHIFT_LEFT) {
                    added = 3600*24;
                } else if (e.getClick() == ClickType.RIGHT) {
                    added = -3600;
                } else if (e.getClick() == ClickType.SHIFT_RIGHT) {
                    added = -3600*24;
                }
                double time = mitem.getPersistentDataContainer().get(NamespacedKey.minecraft("time"), PersistentDataType.DOUBLE);
                if (time + added >= shop.getMinTime()) {
                    inv.setItem(e.getSlot(), ListInventory.getTimeItemStack(time + added));
                } else {
                    inv.setItem(e.getSlot(), ListInventory.getTimeItemStack(shop.getMinTime()));
                }
            } else if (e.getSlot() == 24) {
                double added = 0;
                if (e.getClick() == ClickType.LEFT) {
                    added = 10;
                } else if (e.getClick() == ClickType.SHIFT_LEFT) {
                    added = 100;
                } else if (e.getClick() == ClickType.RIGHT) {
                    added = -10;
                } else if (e.getClick() == ClickType.SHIFT_RIGHT) {
                    added = -100;
                }
                double price = mitem.getPersistentDataContainer().get(NamespacedKey.minecraft("price"), PersistentDataType.DOUBLE);
                if (price + added >= shop.getMinPirce()) {
                    inv.setItem(e.getSlot(), ListInventory.getPriceItemStack(price + added));
                } else {
                    inv.setItem(e.getSlot(), ListInventory.getPriceItemStack(shop.getMinPirce()));
                }
            } else if (e.getSlot() == 40) {
                p.closeInventory();
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Inventory inv = e.getInventory();
        Player p = (Player) e.getPlayer();
        if (ListInventory.isListInventory(e.getView())) {
            String shopName = ListInventory.getInventoryShopName(e.getView());
            Shop shop = Shop.getShop(shopName);
            if (!shop.getStatus()) return;
            ItemStack item = inv.getItem(22);
            if (item == null || item.getType() == Material.AIR) {
                Config.sendMessage(p, Config.getListItemNotFound());
                return;
            } else if (Config.getBlockedItems().contains(item.getType())) {
                Config.sendMessage(p, Config.getListItemBlocked());
                return;
            }
            double time = inv.getItem(20).getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft("time"), PersistentDataType.DOUBLE);
            double price = inv.getItem(24).getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft("price"), PersistentDataType.DOUBLE);

            shop.addItem(p.getName(), price, time, item);
            Config.sendMessage(p, Config.getItemlisted(shopName, item.getType()));
        }
    }
}
