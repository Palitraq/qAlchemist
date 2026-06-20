package ru.spigotmc.destroy.primealchemist.menus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import ru.spigotmc.destroy.primealchemist.configurations.Menu;
import ru.spigotmc.destroy.primealchemist.utils.Utils;

public class Alchemist {
   private static Inventory inv;

   public static void open(Player p) {
      inv = Bukkit.createInventory(p, Menu.getSize(), "§8" + Menu.getTitle());
      Utils.fillInventory(p, inv);
      p.openInventory(inv);
   }

   public static void update(Player p, Inventory inv) {
      Utils.fillInventory(p, inv);
      p.updateInventory();
   }
}
