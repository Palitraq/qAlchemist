package ru.spigotmc.destroy.primealchemist.utils;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Eco {
   private static Economy economy;

   public static Economy getEconomy() {
      return economy;
   }

   public static void init() {
      RegisteredServiceProvider<Economy> reg = Bukkit.getServicesManager().getRegistration(Economy.class);
      if (reg != null) {
         economy = (Economy)reg.getProvider();
      }

   }
}
