package com.inv.settings;

import com.inv.Config;
import com.inv.shop.Shop;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class SettingsInventoryEvents implements Listener {

    ArrayList<String> renamingPlayers = new ArrayList<>();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getInventory();
        if (!e.getView().getTitle().equalsIgnoreCase(Config.getShopSettingsInvName())) return;
        e.setCancelled(true);
        PersistentDataContainer pc = inv.getItem(0).getItemMeta().getPersistentDataContainer();
        String shopName = pc.get(NamespacedKey.minecraft("shopname"), PersistentDataType.STRING);
        Shop shop = Shop.getShop(shopName);
        if (shop == null) return;
        int slot = e.getSlot();
        if (slot < 0 || slot >= 27) return;
        ClickType click = e.getClick();
        if (slot == 10) {
            shop.setStatus(!shop.getStatus());
            inv.setItem(10, SettingsInventory.getToggleItem(shop.getStatus()));
        } else if (slot == 12) {
            double time = shop.getMinTime();
            if (click == ClickType.LEFT) {
                time += 3600;
            } else if (click == ClickType.RIGHT) {
                time -= 3600;
            } else if (click == ClickType.SHIFT_LEFT) {
                time += 3600*24;
            } else if (click == ClickType.SHIFT_RIGHT) {
                time -= 3600*24;
            }
            if (time < Config.getMinSellTime()) {
                time = Config.getMinSellTime();
            }
            shop.setMinTime(time);
            inv.setItem(12, SettingsInventory.getTimeItem(time));
        } else if (slot == 14) {
            double price = shop.getMinPirce();
            if (click == ClickType.LEFT) {
                price += 10;
            } else if (click == ClickType.RIGHT) {
                price -= 10;
            } else if (click == ClickType.SHIFT_LEFT) {
                price += 100;
            } else if (click == ClickType.SHIFT_RIGHT) {
                price -= 100;
            }
            if (price < Config.getMinSellPrice()) {
                price = Config.getMinSellPrice();
            }
            shop.setMinPrice(price);
            inv.setItem(14, SettingsInventory.getPriceItem(price));
        } else if (slot == 16) {
            p.closeInventory();
            if (!renamingPlayers.contains(p.getName())) {
                renamingPlayers.add(p.getName());
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        String shopName = e.getMessage();
        if (shopName.startsWith("/")) return;
        Player p = e.getPlayer();
        if (!renamingPlayers.contains(p.getName())) return;
        if (shopName.length() > Config.getMaxShopNameLength()) {
            Config.sendMessage(p, Config.getMaxShopName());
        } else if (shopName.length() < Config.getMinShopNameLength()) {
            Config.sendMessage(p, Config.getMinShopName());
        } else {
            try {
                Shop shop = Shop.getShop(shopName);
                if (shop == null) {
                    Shop.getPlayerShop(p.getName()).rename(shopName);
                    Config.sendMessage(p, Config.getShopRenamed(shopName));
                } else {
                    Config.sendMessage(p, Config.getNameTaken(shopName));
                }
            } catch (Exception ex) {}
        }
        if (renamingPlayers.contains(p.getName())) {
            renamingPlayers.remove(p.getName());
        }
    }
}
