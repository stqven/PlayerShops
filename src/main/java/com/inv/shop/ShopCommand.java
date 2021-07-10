package com.inv.shop;

import com.inv.Config;
import com.inv.DS.TimeConverter;
import com.inv.list.ListInventory;
import com.inv.settings.SettingsInventory;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShopCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if (args.length == 0) {
            if (Shop.getPlayerShop(p.getName()) == null) {
                Config.sendMessage(p, Config.getCommandsHelp(Config.CREATE));
            } else {
                Config.sendMessage(p, Config.getCommandsHelp(Config.SETTINGS));
            }
            Config.sendMessage(p, Config.getCommandsHelp(Config.LIST_ITEM));
            Config.sendMessage(p, Config.getCommandsHelp(Config.TOGGLE));
            Config.sendMessage(p, Config.getCommandsHelp(Config.OPEN));
        } else {
            if (args[0].equalsIgnoreCase("create")) {
                if (Shop.getPlayerShop(p.getName()) != null) {
                    Config.sendMessage(p, Config.getShopMaxNumber());
                    return false;
                }
                if (args.length == 2) {
                    String shopName = args[1];
                    if (shopName.length() > Config.getMaxShopNameLength()) {
                        Config.sendMessage(p, Config.getMaxShopName());
                        return false;
                    } else if (shopName.length() < Config.getMinShopNameLength()) {
                        Config.sendMessage(p, Config.getMinShopName());
                        return false;
                    }
                    try {
                        Shop shop = Shop.createShop(shopName, p.getName());
                        if (shop != null) {
                            Config.sendMessage(p, Config.getShopCreated(shopName));
                        } else {
                            Config.sendMessage(p, Config.getNameTaken(shopName));
                        }
                    } catch (Exception ex) {}
                } else {
                    Config.sendMessage(p, Config.getCommandsHelp(Config.CREATE));
                }
            } else if (args[0].equalsIgnoreCase("settings")) {
                if (!p.hasPermission("shop.admin") && !p.isOp()) {
                    Config.sendMessage(p,Config.getNoPerms());
                    return false;
                }
                if (Shop.getPlayerShop(p.getName()) == null) {
                    Config.sendMessage(p, Config.getShopNotHaving());
                    return false;
                }
                if (args.length == 1) {
                    Shop shop = Shop.getPlayerShop(p.getName());
                    if (shop == null) {
                        Config.sendMessage(p, Config.getShopNotHaving());
                        return false;
                    }
                    p.openInventory((new SettingsInventory(shop)).getInventory());
                } else {
                    Config.sendMessage(p, Config.getCommandsHelp(Config.SETTINGS));
                }
            } else if (args[0].equalsIgnoreCase("list")) {
                if (args.length >= 2) { // without time
                    String shopName = args[1];
                    Shop shop = Shop.getShop(shopName);
                    if (shop != null) {
                        if (!shop.getStatus()) {
                            Config.sendMessage(p, Config.getShopClosed(shop.getShopName()));
                            return false;
                        }
                        if (args.length == 2) {
                            p.openInventory(new ListInventory(shop).getInventory());
                        } else if (args.length == 3) {
                            p.openInventory(new ListInventory(shop, Double.parseDouble(args[2])).getInventory());
                        } else if (args.length == 4) {
                            p.openInventory(new ListInventory(shop, Double.parseDouble(args[2]), TimeConverter.reverse(args[3])).getInventory());
                        } else {
                            if (args[4].equalsIgnoreCase("hand")) {
                                ItemStack item = p.getInventory().getItemInMainHand().clone();
                                if (item == null || item.getType() == Material.AIR) {
                                    Config.sendMessage(p, Config.getListItemNotFound());
                                    return false;
                                } else if (Config.getBlockedItems().contains(item.getType())) {
                                    Config.sendMessage(p, Config.getListItemBlocked());
                                    return false;
                                }
                                shop.addItem(p.getName(), Double.parseDouble(args[2]), Double.parseDouble(args[3]), item);
                                p.getInventory().getItemInMainHand().setAmount(0);
                            } else {
                                p.openInventory(new ListInventory(shop, Double.parseDouble(args[2]), Double.parseDouble(args[3])).getInventory());
                            }
                        }
                    } else {
                        Config.sendMessage(p, Config.getShopNotExist(shopName));
                    }
                } else {
                    Config.sendMessage(p, Config.getCommandsHelp(Config.LIST_ITEM));
                }
            } else if (args[0].equalsIgnoreCase("toggle")) {
                Shop shop = Shop.getPlayerShop(p.getName());
                if (shop != null) {
                    shop.toggleStatus();
                } else {
                    Config.sendMessage(p, Config.getShopNotExist("[YOUR_SHOP]"));
                }
            } else {
                Shop shop = Shop.getShop(args[0]);
                if (shop != null) {
                    if (!shop.getStatus()) {
                        Config.sendMessage(p, Config.getShopClosed(shop.getShopName()));
                        return false;
                    }
                    p.openInventory(shop.getInventory(p,1));
                } else {
                    Config.sendMessage(p, Config.getShopNotExist(args[0]));
                }
            }
        }
        return true;
    }
}
