package com.inv;

import org.bukkit.OfflinePlayer;

public class VaultAPI {

    private static boolean hasMoney(OfflinePlayer p, double amount) {
        return PlayerShopsPlugin.getEconomy().getBalance(p) >= amount;
    }

    public static boolean removeMoney(OfflinePlayer p, double amount) {
        if (hasMoney(p, amount)) {
            PlayerShopsPlugin.getEconomy().withdrawPlayer(p, amount);
            return true;
        } else return false;
    }

    public static void addMoney(OfflinePlayer p, double amount) {
        PlayerShopsPlugin.getEconomy().depositPlayer(p, amount);
    }
}
