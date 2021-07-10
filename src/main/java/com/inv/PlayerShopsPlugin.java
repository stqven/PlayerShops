package com.inv;
import com.inv.list.ListInventory;
import com.inv.list.ListInventoryEvents;
import com.inv.settings.SettingsInventoryEvents;
import com.inv.shop.Shop;
import com.inv.shop.ShopCommand;
import com.inv.shop.ShopInventoryEvents;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Logger;

public class PlayerShopsPlugin extends JavaPlugin {

    public static PlayerShopsPlugin instance;
    private static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ = null;

    public void onEnable() {
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        System.out.println("[PlayerShops] The plugin has been enabled");
        System.out.println("[PlayerShops] Developed by inv, nthByte");
        getCommand("shop").setExecutor(new ShopCommand());
        Bukkit.getPluginManager().registerEvents(new ShopInventoryEvents(), this);
        Bukkit.getPluginManager().registerEvents(new ListInventoryEvents(), this);
        Bukkit.getPluginManager().registerEvents(new SettingsInventoryEvents(), this);
        saveDefaultConfig();
        new BukkitRunnable() {
            public void run() {
                for (Shop shop : Shop.getLoadedShops()) {
                    shop.updateItemsExpDate();
                }
                for (Player all : Bukkit.getOnlinePlayers()) {
                    InventoryView inv = all.getOpenInventory();
                    if (ListInventory.isListInventory(inv)) {
                        String shopName = inv.getItem(0).getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft("shopname"), PersistentDataType.STRING);
                        ListInventory.doAnimation(inv.getTopInventory(), ListInventory.x%6, shopName);
                    }
                }
                ListInventory.x++;
            }
        }.runTaskTimer(this, 0L, 10L);
    }

    public void onDisable() {
        log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
        System.out.println("[PlayerShops] The plugin has been disabled");
        System.out.println("[PlayerShops] Developed by inv, nthByte");
        Shop.saveAllShops();
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            econ = economyProvider.getProvider();
        }
        return (econ != null);
    }

    public void onLoad() {
        instance = this;
    }

    public static Economy getEconomy() {
        return econ;
    }
}
