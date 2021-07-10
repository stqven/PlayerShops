package com.inv;

import com.inv.DS.Pair;
import com.inv.DS.TimeConverter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Config {

    public static int CREATE = 0;
    public static int LIST_ITEM = 1;
    public static int UNLIST_ITEM = 2;
    public static int TOGGLE = 3;
    public static int OPEN = 4;
    public static int SETTINGS = 5;

    public static void sendMessage(Player p, String msg) {
        if (msg == null || msg.equalsIgnoreCase("")) return;
        p.sendMessage(msg);
    }

    private static String replaceText(String str, Pair<String, String>... replaces) {
        str = str.replaceAll("&", "§");
        for (Pair<String, String> pair : replaces) {
            str = str.replaceAll(pair.getKey(), pair.getValue());
        }
        return str;
    }

    private static String getText(String path) {
        FileConfiguration cfg = PlayerShopsPlugin.instance.getConfig();
        return (cfg.contains(path)? cfg.getString(path).replaceAll("&", "§").replaceAll("%prefix%", getPrefix()) : null);
    }

    private static String getText(String path, Pair<String, String>... replaces) {
        FileConfiguration cfg = PlayerShopsPlugin.instance.getConfig();
        if (cfg.contains(path)) {
            return replaceText(getText(path), replaces);
        } else {
            return null;
        }
    }

    public static ArrayList<String> getList(String path) {
        ArrayList<String> arr = new ArrayList<String>();
        FileConfiguration cfg = PlayerShopsPlugin.instance.getConfig();
        for (String str : cfg.getStringList(path)) {
            arr.add(replaceText(str, new Pair<String, String>("&", "§")));
        }
        return arr;
    }

    public static ArrayList<String> getList(String path, Pair<String, String>... replaces) {
        ArrayList<String> arr = new ArrayList<String>();
        FileConfiguration cfg = PlayerShopsPlugin.instance.getConfig();
        for (String str : cfg.getStringList(path)) {
            arr.add(replaceText(str, replaces));
        }
        return arr;
    }

    public static ArrayList<String> getShopItemsLore(Pair<String, String>... replaces) {
        return getList("Messages.shop-items-lore", replaces);
    }

    public static ArrayList<String> getUnlistItemsLore(Pair<String, String>... replaces) {
        return getList("Messages.unlist-items-lore", replaces);
    }

    public static ArrayList<String> getListTimeLore() {
        return getList("Messages.list-item-time-lore");
    }

    public static ArrayList<String> getListPriceLore() {
        return getList("Messages.list-item-price-lore");
    }

    public static String getListTimeName(String time) {
        return getText("Messages.list-item-time-name", new Pair<String, String>("%time%", time));
    }

    public static String getListSaveName() {
        return getText("Messages.list-save-name");
    }

    public static ArrayList<String> getListSaveLore() {
        return getList("Messages.list-save-lore");
    }

    public static String getListItemNotFound() {
        return getText("Messages.list-item-not-found");
    }

    public static String getListItemBlocked() {
        return getText("Messages.list-item-blocked");
    }

    public static String getListPriceName(double price) {
        return getText("Messages.list-item-price-name", new Pair<String, String>("%price%", Double.toString(price).replace(".0", "")));
    }

    public static double getMinSellPrice() {
        return Double.parseDouble(getText("Settings.default-min-sell-price"));
    }

    public static double getMinSellTime() {
        return TimeConverter.reverse(getText("Settings.default-min-sell-time"));
    }
    
    public static ArrayList<String> getShopInfoLore(int listed_items, int sell_times, int list_times, double avg_price, String owner) {
        return getList("Messages.shop-info-lore", new Pair<String, String>("%listed_items%", Integer.toString(listed_items)),
                new Pair<String, String>("%sell_times%", Integer.toString(sell_times)),
                new Pair<String, String>("%list_times%", Integer.toString(list_times)),
                new Pair<String, String>("%owner%", owner),
                new Pair<String, String>("%avg_price%", Double.toString(avg_price).replace(".0", "")));
    }

    public static String getShopNameInventory(String shopName) {
        return getText("Messages.shop-name-inventory", new Pair<String, String>("%shop_name%", shopName));
    }

    public static String getShopMaxNumber() {
        return getText("Messages.shop-commands.max-shop-reached");
    }

    public static String getListItemNameInventory(String shopName) {
        return getText("Messages.list-item-name-inventory", new Pair<String, String>("%shop_name%", shopName));
    }

    public static String getPrefix() {
        FileConfiguration cfg = PlayerShopsPlugin.instance.getConfig();
        return (cfg.contains("Messages.prefix")? cfg.getString("Messages.prefix").replaceAll("&", "§") : null);
    }

    public static String getCommandsHelp(int cmd) {
        return getText("Messages.shop-commands.help." + (cmd == CREATE? "create" : (cmd == LIST_ITEM? ".list" : (cmd == UNLIST_ITEM? ".unlist" : (cmd == TOGGLE? ".toggle" : (cmd == OPEN? ".open" : cmd == SETTINGS? ".settings" : "create"))))));
    }

    public static String getShopCreated(String shopName) {
        return getText("Messages.shop-commands.create-success", new Pair<String, String>("%shop_name%", shopName));
    }

    public static String getShopRenamed(String shopName) {
        return getText("Messages.shop-commands.rename-success", new Pair<String, String>("%shop_name%", shopName));
    }

    public static String getItemlisted(String shopName, Material mat) {
        return getText("Messages.shop-commands.list-success", new Pair<String, String>("%shop_name%", shopName), new Pair<String, String>("%item_name%", mat.name().replaceAll("_", " ").toLowerCase()));
    }

    public static String getShopNoMoney() {
        return getText("Messages.shop-commands.shop-no-enough-money");
    }

    public static String getShopSuccessBuy() {
        return getText("Messages.shop-commands.shop-success-buy");
    }

    public static String getItemUnlisted(String shopName, Material mat) {
        return getText("Messages.shop-commands.unlist-success", new Pair<String, String>("%shop_name%", shopName), new Pair<String, String>("%item_name%", mat.name().replaceAll("_", " ").toLowerCase()));
    }

    public static String getNameTaken(String shopName) {
        return getText("Messages.shop-commands.shop-already-taken", new Pair<String, String>("%shop_name%", shopName));
    }

    public static String getShopNotExist(String shopName) {
        return getText("Messages.shop-commands.shop-not-exist", new Pair<String, String>("%shop_name%", shopName));
    }

    public static String getShopToggle(boolean b, String shopName) {
        return getText("Messages.shop-commands.shop-toggle-" + (b? "allow" : "deny"), new Pair<String, String>("%shop_name%", shopName));
    }

    public static int getMaxShopNameLength() {
        return PlayerShopsPlugin.instance.getConfig().getInt("Settings.max-shop-name-length");
    }

    public static int getMinShopNameLength() {
        return PlayerShopsPlugin.instance.getConfig().getInt("Settings.min-shop-name-length");
    }

    public static String getMaxShopName() {
        return getText("Messages.shop-commands.shop-create-max-length");
    }

    public static String getMinShopName() {
        return getText("Messages.shop-commands.shop-create-min-length");
    }

    public static String getNextPage() {
        return getText("Messages.next-page");
    }

    public static String getPreviousPage() {
        return getText("Messages.previous-page");
    }

    public static String getShopToggleBroadcast(boolean b, String shopName) {
        return getText("Messages.shop-commands.shop-toggle-" + (b? "allow" : "deny") + "-broadcast", new Pair<String, String>("%shop_name%", shopName));
    }

    public static ArrayList<Material> getBlockedItems() {
        ArrayList<String> itemsArr = (ArrayList<String>) PlayerShopsPlugin.instance.getConfig().getStringList("blocked-items");
        ArrayList<Material> blocked = new ArrayList<Material>();
        for (String str : itemsArr) {
            for (Material mat : Material.values()) {
                if (str.equalsIgnoreCase(mat.name())) {
                    blocked.add(mat);
                    break;
                }
            }
        }
        return blocked;
    }

    public static String getShopToggledName(boolean status) {
        return getText("Settings.Items.toggle-" + (status? "enabled" : "disabled") + ".name");
    }

    public static ArrayList<String> getShopToggledLore(boolean status) {
        return getList("Settings.Items.toggle-" + (status? "enabled" : "disabled") + ".lore");
    }

    public static String getShopMinPriceName(double min_price) {
        return getText("Settings.Items.min-price.name", new Pair<>("%min_price%" , Double.toString(min_price).replace(".0", "")));
    }

    public static ArrayList<String> getShopMinPriceLore(double min_price) {
        return getList("Settings.Items.min-price.lore", new Pair<>("%min_price%" , Double.toString(min_price).replace(".0", "")));
    }

    public static String getShopMinTimeName(double min_time) {
        return getText("Settings.Items.min-time.name", new Pair<>("%min_time%" , TimeConverter.convert(min_time)));
    }

    public static ArrayList<String> getShopMinTimeLore(double min_time) {
        return getList("Settings.Items.min-time.lore", new Pair<>("%min_time%" , TimeConverter.convert(min_time)));
    }

    public static String getShopRenameName() {
        return getText("Settings.Items.rename.name");
    }

    public static ArrayList<String> getShopRenameLore() {
        return getList("Settings.Items.rename.lore");
    }

    public static String getShopSettingsInvName() {
        return getText("Settings.inventory-name");
    }

    public static String getShopNotHaving() {
        return getText("Messages.shop-commands.shop-not-having");
    }

    public static String getNoPerms() {
        return getText("Messages.shop-commands.no-permissions");
    }

    public static String getShopClosed(String shopName) {
        return getText("Messages.shop-close", new Pair<String, String>("%shop_name%", shopName));
    }
}
