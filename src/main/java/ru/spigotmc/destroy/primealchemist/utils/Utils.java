package ru.spigotmc.destroy.primealchemist.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.spigotmc.destroy.primealchemist.configurations.Menu;

public class Utils {
   public static void fillInventory(Player p, Inventory inv) {
      if (!Storage.chance.containsKey(p.getUniqueId())) {
         Storage.chance.put(p.getUniqueId(), Menu.getStandardChance());
      }

      if (!Storage.price.containsKey(p.getUniqueId())) {
         Storage.price.put(p.getUniqueId(), Menu.getChanceGiverPrice());
      }

      ItemStack item;
      ItemMeta meta;
      ArrayList lore;
      Iterator var5;
      String path;
      if (Menu.isChanceGiverEnable()) {
         item = Menu.getChanceGiverMaterial();
         meta = item.getItemMeta();
         lore = new ArrayList();
         var5 = Menu.getChanceGiverLore().iterator();

         while(var5.hasNext()) {
            path = (String)var5.next();
            lore.add(ChatUtils.color(path).replace("%chance%", String.valueOf(Storage.chance.get(p.getUniqueId()))).replace("%price%", String.valueOf(Storage.price.get(p.getUniqueId()))));
         }

         meta.setLore(lore);
         meta.setDisplayName(ChatUtils.color(Menu.getChanceGiverName()).replace("%percent%", String.valueOf(Menu.getChanceGiverChance())));
         item.setItemMeta(meta);
         var5 = Menu.getChanceGiverSlots().iterator();

         while(var5.hasNext()) {
            Integer i = (Integer)var5.next();
            inv.setItem(i, item);
         }
      }

      item = Menu.getMixedPotionMaterial();
      meta = item.getItemMeta();
      lore = new ArrayList();
      var5 = Menu.getMixedPotionLore().iterator();

      while(var5.hasNext()) {
         path = (String)var5.next();
         lore.add(ChatUtils.color(path).replace("%chance%", String.valueOf(Storage.chance.get(p.getUniqueId()))).replace("%price%", String.valueOf(Menu.getMixPrice())));
      }

      meta.setLore(lore);
      meta.setDisplayName(ChatUtils.color(Menu.getMixedPotionName()));
      item.setItemMeta(meta);
      inv.setItem(Menu.getMixedPotionSlot(), item);
      var5 = Menu.getDividerItems().iterator();

      while(true) {
         while(var5.hasNext()) {
            path = (String)var5.next();
            String s = "divider-items." + path + ".";
            String material = Menu.getConfig().getString(s + "material");
            if (material == null) {
               Bukkit.getConsoleSender().sendMessage("Укажите материал для " + path);
            } else {
               ItemStack divider;
               if (material.startsWith("head-")) {
                  String[] split = material.split("-");
                  divider = getSkull(split[1], path);
               } else {
                  try {
                     divider = new ItemStack(Material.valueOf(material));
                  } catch (IllegalArgumentException var14) {
                     divider = new ItemStack(Material.BARRIER);
                     Bukkit.getConsoleSender().sendMessage("Укажите верный материал для " + path);
                  }
               }

               List<String> lore1 = new ArrayList();
               if (Menu.getConfig().get(s + "lore") != null) {
                  Iterator var11 = Menu.getConfig().getStringList(s + "lore").iterator();

                  while(var11.hasNext()) {
                     String s1 = (String)var11.next();
                     lore1.add(ChatUtils.color(s1));
                  }
               }

               ItemMeta dividerMeta = divider.getItemMeta();
               if (Menu.getConfig().getString(s + "name") != null) {
                  dividerMeta.setDisplayName(ChatUtils.color(Menu.getConfig().getString(s + "name")));
               }

               divider.setItemMeta(dividerMeta);
               Iterator var22 = Menu.getConfig().getIntegerList(s + "slots").iterator();

               while(var22.hasNext()) {
                  Integer i = (Integer)var22.next();
                  inv.setItem(i, divider);
               }
            }
         }

         ItemStack exit = Menu.getExitMaterial();
         ItemMeta exitMeta = exit.getItemMeta();
         exitMeta.setDisplayName(ChatUtils.color(Menu.getExitName()));
         exit.setItemMeta(exitMeta);
         Iterator var18 = Menu.getExitSlots().iterator();

         while(var18.hasNext()) {
            Integer i = (Integer)var18.next();
            inv.setItem(i, exit);
         }

         return;
      }
   }

   public static ItemStack getSkull(String url, String path) {
      ItemStack item = new ItemStack(Material.PLAYER_HEAD);
      if (Bukkit.getBukkitVersion().contains("1.12")) {
         item = new ItemStack(Material.valueOf("HEAD"));
      }

      ItemMeta meta = item.getItemMeta();
      GameProfile profile = new GameProfile(UUID.randomUUID(), "");
      profile.getProperties().put("textures", new Property("textures", url));

      try {
         Field profileField = meta.getClass().getDeclaredField("profile");
         profileField.setAccessible(true);
         profileField.set(meta, profile);
      } catch (IllegalAccessException | NoSuchFieldException | IllegalArgumentException var6) {
         Bukkit.getLogger().severe("Такой головы не существует: " + path);
      }

      item.setItemMeta(meta);
      return item;
   }
}
