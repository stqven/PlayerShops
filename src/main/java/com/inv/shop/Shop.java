package com.inv.shop;

import com.inv.Config;
import com.inv.DS.Pair;
import com.inv.DS.TimeConverter;
import com.inv.PlayerShopsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Shop {

    private static ArrayList<Shop> loadedShops = new ArrayList<Shop>();

    private String ownerName, shopName;
    private ArrayList<ItemStack> shopItems;
    private boolean status;
    private int sell_times, list_times;
    private double min_price, min_time;

    public Shop(String ownerName, String shopName, ArrayList<ItemStack> shopItems, int sell_times, int list_times, boolean status, double min_price, double min_time) {
        this.ownerName = ownerName;
        this.shopName = shopName;
        this.shopItems = shopItems;
        this.sell_times = sell_times;
        this.list_times = list_times;
        this.status = status;
        this.min_time = min_time;
        this.min_price = min_price;
    }

    public Player getOwner() {
        return Bukkit.getPlayer(ownerName);
    }

    public double getMinPirce() {
        return min_price;
    }

    public void rename(String shopName) {
        ArrayList<File> files = getAllShopFile();
        File rename = new File(PlayerShopsPlugin.instance.getDataFolder().getAbsolutePath() + "/" + shopName + ".yml");
        for (File f : files) {
            if (f.getName().equalsIgnoreCase(this.shopName + ".yml")) {
                f.renameTo(rename);
                break;
            }
        }
        this.shopName = shopName;
        for (Player p : getViewers()) {
            p.closeInventory();
            p.sendMessage("§cThis shop has been renamed to " + shopName);
        }
    }

    public double getMinTime() {
        return min_time;
    }

    public void setMinPrice(double min_price) {
        this.min_price = min_price;
    }

    public void setMinTime(double min_time) {
        this.min_time = min_time;
    }

    public void toggleStatus() {
        this.status = !status;
        for (Player all : Bukkit.getOnlinePlayers()) {
            if (all.getInventory() != null && all.getOpenInventory().getTitle().equalsIgnoreCase(Config.getListItemNameInventory(shopName)) ||
                    all.getOpenInventory().getTitle().equalsIgnoreCase(Config.getShopNameInventory(shopName))) {
                all.closeInventory();
                if (all.getName().equalsIgnoreCase(ownerName)) {
                    Config.sendMessage(all, Config.getShopToggle(status, shopName));
                } else {
                    Config.sendMessage(all, Config.getShopClosed(shopName));
                }
            } else {
                if (all.getName().equalsIgnoreCase(ownerName)) {
                    Config.sendMessage(all, Config.getShopToggle(status, shopName));
                } else {
                    Config.sendMessage(all, Config.getShopToggleBroadcast(status, shopName));
                }
            }
        }
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getShopName() {
        return shopName;
    }

    public ArrayList<ItemStack> getShopItems() {
        return shopItems;
    }

    public int getSellTimes() {
        return sell_times;
    }

    public int getListTimes() {
        return list_times;
    }

    public Inventory getInventory(Player p, int page) {
        Inventory inv = Bukkit.createInventory(null, 54, Config.getShopNameInventory(shopName));
        boolean removed = false;
//        for (int i = 0; i < Math.min(45, shopItems.size()); i++) {
        for (int i = 0; i < 45; i++) {
            int index = 45*(page - 1) + i;
            if (index >= shopItems.size()) break;
//            updateLore(shopItems.get(i));
//            double date = shopItems.get(i).getItemMeta().getPersistentDataContainer()
//                    .get(NamespacedKey.minecraft("date"), PersistentDataType.DOUBLE);
//            if (date - 1000 <= System.currentTimeMillis()) {
//                removeItem(shopItems.get(i), false);
//                removed = true;
//            }

//            if (shopItems.get(index).getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft("owner"), PersistentDataType.STRING).equalsIgnoreCase(p.getName())) {
//                inv.setItem(i, convertToOwner(shopItems.get(index)));
//            } else {
                inv.setItem(i, shopItems.get(index));
//            }
        }
        if (removed) getInventory(p, page);
        {
            ItemStack bar = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemMeta mbar = bar.getItemMeta();
            mbar.setDisplayName("§7");
            bar.setItemMeta(mbar);
            inv.setItem(46, bar);
            inv.setItem(47, bar);
            inv.setItem(48, bar);
            inv.setItem(50, bar);
            inv.setItem(51, bar);
            inv.setItem(52, bar);
            if (page == 1) {
                inv.setItem(45, bar);
            }
            if ((shopItems.size() - 1)/45 < page) {
                inv.setItem(53, bar);
            }
        }

        if ((shopItems.size() - 1)/45 >= page) {
            ItemStack bar = new ItemStack(Material.SLIME_BALL);
            ItemMeta mbar = bar.getItemMeta();
            mbar.setDisplayName(Config.getNextPage());
            bar.setItemMeta(mbar);
            inv.setItem(53, bar);
        }

        if (page > 1) {
            ItemStack bar = new ItemStack(Material.SLIME_BALL);
            ItemMeta mbar = bar.getItemMeta();
            mbar.setDisplayName(Config.getPreviousPage());
            bar.setItemMeta(mbar);
            inv.setItem(45, bar);
        }
        {
            ItemStack bar = new ItemStack(Material.BOOK);
            ItemMeta mbar = bar.getItemMeta();
            ArrayList<String> items = Config.getShopInfoLore(shopItems.size(), sell_times, list_times, getAveragePrice(), ownerName + (p.getName().equalsIgnoreCase(ownerName)? " §7(You)" : ""));
            mbar.setDisplayName(items.get(0));
            items.remove(0);
            mbar.getPersistentDataContainer().set(NamespacedKey.minecraft("shopname"), PersistentDataType.STRING, shopName);
            mbar.getPersistentDataContainer().set(NamespacedKey.minecraft("page"), PersistentDataType.INTEGER, page);
            mbar.setLore(items);
            bar.setItemMeta(mbar);
            inv.setItem(49, bar);
        }
        return inv;
    }

    public double getAveragePrice() {
        double total = 0;
        for (ItemStack item : shopItems) {
            total += item.getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft("price"), PersistentDataType.DOUBLE);
        }
        return total/(shopItems.size() == 0? 1 : shopItems.size());
    }

    public void addItem(String itemOwner, double price, double expireDate, ItemStack item) {
        ItemMeta mitem = item.getItemMeta();
        if (mitem.hasLore()) {
            String lore = "";
            for (String str : mitem.getLore()) {
                lore += str + "%%/SPACE/%%";
            }
            mitem.getPersistentDataContainer().set(NamespacedKey.minecraft("lore"), PersistentDataType.STRING, lore);
        } else {
            mitem.getPersistentDataContainer().set(NamespacedKey.minecraft("lore"), PersistentDataType.STRING, "%%/NULL/%%");
        }
        mitem.getPersistentDataContainer().set(NamespacedKey.minecraft("owner"), PersistentDataType.STRING, itemOwner);
        mitem.getPersistentDataContainer().set(NamespacedKey.minecraft("price"), PersistentDataType.DOUBLE, price);
        mitem.getPersistentDataContainer().set(NamespacedKey.minecraft("date"), PersistentDataType.DOUBLE, expireDate);
        mitem.getPersistentDataContainer().set(NamespacedKey.minecraft("startdate"), PersistentDataType.DOUBLE, (double) System.currentTimeMillis()/1000);
        mitem.setLore(Config.getShopItemsLore(new Pair<String, String>("%owner%", "-"),
                new Pair<String, String>("%price%", "-"),
                new Pair<String, String>("%expire_date%", "-"))); // Edit expire date
        item.setItemMeta(mitem);
        updateLore(item);
        shopItems.add(item);
        list_times++;
    }

    public boolean removeItem(ItemStack item, boolean sell) {
        if (shopItems.contains(item)) {
            if (sell) sell_times++;
            shopItems.remove(item);
            reloadHolders();
            return true;
        }
        return false;
    }

    public void updateItemsExpDate() {
        ArrayList<ItemStack> items = getShopItems();
        for (int i = 0; i < items.size(); i++) {
            double date = getItemExpireTime(items.get(i));
            if (date <= 1) {
                removeItem(shopItems.get(i), false);
                continue;
            }
            updateLore(items.get(i));
        }
        reloadHolders();
    }

    public ArrayList<Player> getViewers() {
        ArrayList<Player> viewers = new ArrayList<Player>();
        for (Player all : Bukkit.getOnlinePlayers()) {
            if (all.getOpenInventory() != null) {
                if (all.getOpenInventory().getTitle().equalsIgnoreCase(Config.getShopNameInventory(shopName))) {
                    viewers.add(all);
                }
            }
        }
        return viewers;
    }

    public void reloadHolders() {
        for (Player viewer : getViewers()) {
            int page = viewer.getOpenInventory().getItem(49).getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft("page"), PersistentDataType.INTEGER);
            Inventory inv = getInventory(viewer, page);
            boolean open = false;
            while (inv.isEmpty() && page > 1) {
                inv = getInventory(viewer, page - 1);
                open = true;
            }
            if (open) {
                viewer.openInventory(inv);
            } else {
                for (int i = 0; i < 54; i++) {
                    viewer.getOpenInventory().setItem(i, inv.getItem(i));
                }
            }
        }
    }

    public static double getItemExpireTime(ItemStack item) {
        PersistentDataContainer pc = item.getItemMeta().getPersistentDataContainer();
        double date = pc.get(NamespacedKey.minecraft("date"), PersistentDataType.DOUBLE);
        double startdate = pc.get(NamespacedKey.minecraft("startdate"), PersistentDataType.DOUBLE);
        return startdate + date - System.currentTimeMillis()/1000;
    }

    public static void updateLore(ItemStack item) {
        ItemMeta mitem = item.getItemMeta();
        PersistentDataContainer pc = mitem.getPersistentDataContainer();
        String ownerName = pc.get(NamespacedKey.minecraft("owner"), PersistentDataType.STRING);
        mitem.setLore(Config.getShopItemsLore(new Pair<String, String>("%owner%", ownerName),
                new Pair<String, String>("%price%", Double.toString(pc.get(NamespacedKey.minecraft("price"), PersistentDataType.DOUBLE)).replace(".0", "")),
                new Pair<String, String>("%expire_date%", TimeConverter.convert(getItemExpireTime(item)))));
        item.setItemMeta(mitem);
    }

    public static Shop getShop(String shopName) {
        for (Shop shop : loadedShops) {
            if (shop.getShopName().equalsIgnoreCase(shopName)) return shop;
        }
        File f = getShopFile(shopName);
        if (f != null) {
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
            ArrayList<ItemStack> itemsArr = new ArrayList<ItemStack>();
            ItemStack[] items = (cfg.contains("content"))? (((List<ItemStack>) cfg.get("content")).toArray(new ItemStack[0])) : new ItemStack[]{};
            for (ItemStack item : items) {
                itemsArr.add(item);
            }
            Shop shop = new Shop(cfg.contains("owner")? cfg.getString("owner") : null, shopName, itemsArr, cfg.contains("sell_times")? cfg.getInt("sell_times") : 0, cfg.contains("list_times")? cfg.getInt("list_times") : 0, cfg.contains("status")? cfg.getBoolean("status") : true, cfg.contains("min_price")? cfg.getDouble("min_price") : Config.getMinSellPrice(), cfg.contains("min_price")? cfg.getDouble("min_time") : Config.getMinSellTime());
            loadedShops.add(shop);
            return shop;
        }
        return null;
    }

    public static Shop createShop(String shopName, String ownerName) throws Exception {
        if (getShopFile(shopName) != null) return null;
        try {
            ArrayList<ItemStack> itemsArr = new ArrayList<ItemStack>();
            File f = new File(PlayerShopsPlugin.instance.getDataFolder().getAbsolutePath(), shopName + ".yml");
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
            cfg.set("owner", ownerName);
            cfg.set("sell_times", 0);
            cfg.set("list_times", 0);
            cfg.set("status", true);
            cfg.set("min_price", Config.getMinSellPrice());
            cfg.set("min_time", Config.getMinSellTime());
            cfg.save(f);
            Shop shop = new Shop(cfg.contains("owner")? cfg.getString("owner") : null, shopName, new ArrayList<ItemStack>(), cfg.contains("sell_times")? cfg.getInt("sell_times") : 0, cfg.contains("list_times")? cfg.getInt("list_times") : 0, cfg.contains("status")? cfg.getBoolean("status") : true, cfg.contains("min_price")? cfg.getDouble("min_price") : Config.getMinSellPrice(), cfg.contains("min_price")? cfg.getDouble("min_time") : Config.getMinSellTime());
            loadedShops.add(shop);
            return shop;
        } catch (Exception ex) {
            return null;
        }
    }

    public static Shop getPlayerShop(String ownerName) {
        for (Shop shop : loadedShops) {
            if (shop.getOwnerName().equalsIgnoreCase(ownerName)) return shop;
        }
        for (File file : getAllShopFile()) {
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            if (cfg.contains("owner") && cfg.getString("owner").equalsIgnoreCase(ownerName))
                return Shop.getShop(file.getName().replaceAll(".yml", ""));
        }
        return null;
    }

    private static ArrayList<File> getAllShopFile() {
        ArrayList<File> allFiles = new ArrayList<File>();
        File file = PlayerShopsPlugin.instance.getDataFolder().getAbsoluteFile();
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.exists() && !f.isDirectory() && !f.getName().equalsIgnoreCase("config.yml")) allFiles.add(f);
        }
        return allFiles;
    }

    private static File getShopFile(String shopName) {
        File file = PlayerShopsPlugin.instance.getDataFolder().getAbsoluteFile();
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.exists() && !f.isDirectory() && f.getName().equalsIgnoreCase(shopName + ".yml")) return f;
        }
        return null;
    }

    public static ArrayList<Shop> getLoadedShops() {
        return loadedShops;
    }

    public static void saveAllShops() {
        for (Shop shop : loadedShops) {
            try {
                File f = new File(PlayerShopsPlugin.instance.getDataFolder().getAbsolutePath(), shop.getShopName() + ".yml");
                YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
                cfg.set("content", shop.getShopItems());
                cfg.set("owner", shop.getOwnerName());
                cfg.set("sell_times", shop.getSellTimes());
                cfg.set("list_times", shop.getListTimes());
                cfg.set("status", shop.getStatus());
                cfg.set("min_price", shop.getMinPirce());
                cfg.set("min_time", shop.getMinTime());
                cfg.set("content", shop.getShopItems());
                cfg.save(f);
            } catch (Exception e) {}
        }
    }

}
