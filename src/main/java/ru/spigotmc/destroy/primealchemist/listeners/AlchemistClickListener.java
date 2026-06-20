package ru.spigotmc.destroy.primealchemist.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import ru.spigotmc.destroy.primealchemist.configurations.Config;
import ru.spigotmc.destroy.primealchemist.configurations.Menu;
import ru.spigotmc.destroy.primealchemist.menus.Alchemist;
import ru.spigotmc.destroy.primealchemist.menus.Animation;
import ru.spigotmc.destroy.primealchemist.utils.ChatUtils;
import ru.spigotmc.destroy.primealchemist.utils.Eco;
import ru.spigotmc.destroy.primealchemist.utils.Storage;
import ru.spigotmc.destroy.primealchemist.utils.StorageUtils;

public class AlchemistClickListener implements Listener {
   @EventHandler
   public void onClickInventory(InventoryClickEvent e) {
      if (e.getView().getTitle().equals("§8" + Menu.getTitle())) {
         Player player = (Player)e.getWhoClicked();
         if (e.getClick().isKeyboardClick() && !e.getClick().isLeftClick() && !e.getClick().isRightClick() && !e.getClick().isShiftClick()) {
            e.setCancelled(true);
            return;
         }

         if (e.getClickedInventory() == null) {
            return;
         }

         if (e.getCurrentItem() == null) {
            return;
         }

         String potion;
         if (e.getClickedInventory() == player.getInventory() || e.getInventory() == player.getInventory()) {
            if (e.getCurrentItem().getType() == Material.POTION) {
               PotionMeta meta = (PotionMeta)e.getCurrentItem().getItemMeta();
               if (meta.getBasePotionData().getType().getEffectType() == null) {
                  if (Config.getConfig().getBoolean("unlimited-mix") && meta.hasCustomEffects()) {
                     Iterator var24 = Config.getConfig().getStringList("blocked-effects").iterator();

                     while(var24.hasNext()) {
                        potion = (String)var24.next();
                        String[] split = potion.split(":");
                        Iterator var28 = meta.getCustomEffects().iterator();

                        while(var28.hasNext()) {
                           PotionEffect effect = (PotionEffect)var28.next();
                           if (effect.getType().getName().equalsIgnoreCase(split[0]) && effect.getAmplifier() >= Integer.parseInt(split[1]) - 1) {
                              ChatUtils.sendMsg(player, Config.getConfig().getString("messages.potion-level"));
                              e.setCancelled(true);
                              return;
                           }
                        }
                     }

                     return;
                  }

                  ChatUtils.sendMsg(player, Config.getConfig().getString("messages.null-potion"));
                  e.setCancelled(true);
                  return;
               }
            } else if (e.getCurrentItem().getType() != Material.POTION && e.getCurrentItem().getType() != Material.AIR) {
               ChatUtils.sendMsg(player, Config.getConfig().getString("messages.null-potion"));
               e.setCancelled(true);
               return;
            }

            return;
         }

         if (e.getSlot() == Menu.getMixedPotionSlot() && e.getInventory().getItem(Menu.getMixedPotionSlot()) != null) {
            e.setCancelled(true);
            int chance = (int)(Math.random() * 100.0D);
            if (Eco.getEconomy().getBalance(player) < (double)Menu.getMixPrice()) {
               ChatUtils.sendMsg(player, Config.getConfig().getString("messages.no-money"));
               return;
            }

            if (e.getInventory().getItem(Menu.getFirstPotionSlot()) == null || e.getInventory().getItem(Menu.getSecondPotionSlot()) == null) {
               ChatUtils.sendMsg(player, Config.getConfig().getString("messages.no-potions"));
               return;
            }

            ItemStack item1 = e.getInventory().getItem(Menu.getFirstPotionSlot());
            ItemStack item2 = e.getInventory().getItem(Menu.getSecondPotionSlot());
            PotionMeta meta1 = (PotionMeta)item1.getItemMeta();
            PotionMeta meta2 = (PotionMeta)item2.getItemMeta();
            
            if (meta1.getBasePotionData().getType() == PotionType.WATER || meta2.getBasePotionData().getType() == PotionType.WATER || meta1.getBasePotionData().getType() == PotionType.AWKWARD || meta2.getBasePotionData().getType() == PotionType.AWKWARD) {
               ChatUtils.sendMsg(player, Config.getConfig().getString("messages.water-mix"));
               return;
            }

            Eco.getEconomy().withdrawPlayer(player, (double)Menu.getMixPrice());
            ItemStack item;
            if (chance <= (Integer)Storage.chance.get(player.getUniqueId())) {
               item = new ItemStack(Material.POTION);
               PotionMeta meta = (PotionMeta)item.getItemMeta();
               List<PotionType> pt = new ArrayList();
               pt.add(PotionType.FIRE_RESISTANCE);
               pt.add(PotionType.NIGHT_VISION);
               pt.add(PotionType.INVISIBILITY);
               pt.add(PotionType.LUCK);
               List<PotionType> pt2 = new ArrayList();
               pt2.add(PotionType.SLOW_FALLING);
               pt2.add(PotionType.WEAKNESS);
               List<PotionType> pt3 = new ArrayList();
               pt3.add(PotionType.HEALING);
               pt3.add(PotionType.HARMING);
               List<PotionType> pt4 = new ArrayList();
               pt4.add(PotionType.REGENERATION);
               pt4.add(PotionType.POISON);
               PotionEffect potionEffect;
               PotionData data;
               PotionEffect potionEffect1;
               PotionEffect potionEffect2;
               PotionEffect effect;
               Iterator var33;
               // Добавляем эффекты из первого зелья
               if (meta1.hasCustomEffects() && !meta1.getCustomEffects().isEmpty()) {
                  var33 = meta1.getCustomEffects().iterator();

                  while(var33.hasNext()) {
                     effect = (PotionEffect)var33.next();
                     meta.addCustomEffect(effect, true);
                  }
               } else if (meta1.getBasePotionData().getType().getEffectType() != null) {
                  data = meta1.getBasePotionData();
                  if (data.getType() == PotionType.TURTLE_MASTER) {
                     if (data.isUpgraded()) {
                        potionEffect1 = new PotionEffect(PotionEffectType.SLOWNESS, 400, 3);
                        potionEffect2 = new PotionEffect(PotionEffectType.RESISTANCE, 400, 3);
                     } else if (data.isExtended()) {
                        potionEffect1 = new PotionEffect(PotionEffectType.SLOWNESS, 800, 3);
                        potionEffect2 = new PotionEffect(PotionEffectType.RESISTANCE, 800, 2);
                     } else {
                        potionEffect1 = new PotionEffect(PotionEffectType.SLOWNESS, 400, 3);
                        potionEffect2 = new PotionEffect(PotionEffectType.RESISTANCE, 400, 2);
                     }

                     meta.addCustomEffect(potionEffect1, true);
                     meta.addCustomEffect(potionEffect2, true);
                  } else {
                     if (data.isUpgraded()) {
                        potionEffect = new PotionEffect(data.getType().getEffectType(), 1800, 1);
                        if (pt4.contains(data.getType())) {
                           potionEffect = new PotionEffect(data.getType().getEffectType(), 440, 1);
                        }
                     } else if (data.isExtended()) {
                        potionEffect = new PotionEffect(data.getType().getEffectType(), 9600, 0);
                        if (pt2.contains(data.getType())) {
                           potionEffect = new PotionEffect(data.getType().getEffectType(), 4800, 0);
                        }
                        if (pt4.contains(data.getType())) {
                           potionEffect = new PotionEffect(data.getType().getEffectType(), 1800, 1);
                        }
                     } else {
                        potionEffect = new PotionEffect(data.getType().getEffectType(), 3600, 0);
                        if (pt2.contains(data.getType())) {
                           potionEffect = new PotionEffect(data.getType().getEffectType(), 2400, 0);
                        }
                        if (pt4.contains(data.getType())) {
                           potionEffect = new PotionEffect(data.getType().getEffectType(), 900, 0);
                        }
                     }

                     if (pt3.contains(data.getType())) {
                        if (data.isUpgraded()) {
                           potionEffect = new PotionEffect(data.getType().getEffectType(), 0, 1);
                        } else {
                           potionEffect = new PotionEffect(data.getType().getEffectType(), 0, 0);
                        }
                     }

                     if (pt.contains(data.getType())) {
                        if (data.isExtended()) {
                           potionEffect = new PotionEffect(data.getType().getEffectType(), 9600, 0);
                        } else {
                           potionEffect = new PotionEffect(data.getType().getEffectType(), 3600, 0);
                        }
                     }

                     meta.addCustomEffect(potionEffect, true);
                  }
               }

               // Добавляем эффекты из второго зелья
               if (meta2.hasCustomEffects() && !meta2.getCustomEffects().isEmpty()) {
                  var33 = meta2.getCustomEffects().iterator();

                  while(var33.hasNext()) {
                     effect = (PotionEffect)var33.next();
                     meta.addCustomEffect(effect, true);
                  }
               } else if (meta2.getBasePotionData().getType().getEffectType() != null) {
                  data = meta2.getBasePotionData();
                  if (data.getType() == PotionType.TURTLE_MASTER) {
                     if (data.isUpgraded()) {
                        potionEffect1 = new PotionEffect(PotionEffectType.SLOWNESS, 400, 3);
                        potionEffect2 = new PotionEffect(PotionEffectType.RESISTANCE, 400, 3);
                     } else if (data.isExtended()) {
                        potionEffect1 = new PotionEffect(PotionEffectType.SLOWNESS, 800, 3);
                        potionEffect2 = new PotionEffect(PotionEffectType.RESISTANCE, 800, 2);
                     } else {
                        potionEffect1 = new PotionEffect(PotionEffectType.SLOWNESS, 400, 3);
                        potionEffect2 = new PotionEffect(PotionEffectType.RESISTANCE, 400, 2);
                     }

                     meta.addCustomEffect(potionEffect1, true);
                     meta.addCustomEffect(potionEffect2, true);
                  } else {
                     if (data.isUpgraded()) {
                        potionEffect = new PotionEffect(data.getType().getEffectType(), 1800, 1);
                        if (pt4.contains(data.getType())) {
                           potionEffect = new PotionEffect(data.getType().getEffectType(), 440, 1);
                        }
                     } else if (data.isExtended()) {
                        potionEffect = new PotionEffect(data.getType().getEffectType(), 9600, 0);
                        if (pt2.contains(data.getType())) {
                           potionEffect = new PotionEffect(data.getType().getEffectType(), 4800, 0);
                        }
                        if (pt4.contains(data.getType())) {
                           potionEffect = new PotionEffect(data.getType().getEffectType(), 1800, 1);
                        }
                     } else {
                        potionEffect = new PotionEffect(data.getType().getEffectType(), 3600, 0);
                        if (pt2.contains(data.getType())) {
                           potionEffect = new PotionEffect(data.getType().getEffectType(), 2400, 0);
                        }
                        if (pt4.contains(data.getType())) {
                           potionEffect = new PotionEffect(data.getType().getEffectType(), 900, 0);
                        }
                     }

                     if (pt3.contains(data.getType())) {
                        if (data.isUpgraded()) {
                           potionEffect = new PotionEffect(data.getType().getEffectType(), 0, 1);
                        } else {
                           potionEffect = new PotionEffect(data.getType().getEffectType(), 0, 0);
                        }
                     }

                     if (pt.contains(data.getType())) {
                        if (data.isExtended()) {
                           potionEffect = new PotionEffect(data.getType().getEffectType(), 9600, 0);
                        } else {
                           potionEffect = new PotionEffect(data.getType().getEffectType(), 3600, 0);
                        }
                     }

                     meta.addCustomEffect(potionEffect, true);
                  }
               }

               String rgb = Config.getConfig().getString("potion-color");
               String[] split = rgb.split(";");
               int r = Integer.parseInt(split[0]);
               int g = Integer.parseInt(split[1]);
               int b = Integer.parseInt(split[2]);
               meta.setColor(Color.fromRGB(r, g, b));
               meta.setDisplayName(ChatUtils.color(Config.getConfig().getString("potion-name")));
               item.setItemMeta(meta);
               e.getInventory().setItem(Menu.getFirstPotionSlot(), new ItemStack(Material.AIR));
               e.getInventory().setItem(Menu.getSecondPotionSlot(), new ItemStack(Material.AIR));
               Storage.price.put(player.getUniqueId(), Menu.getChanceGiverPrice());
               Storage.chance.put(player.getUniqueId(), Menu.getStandardChance());
               (new Animation(player, item)).run();
            } else {
               e.getInventory().setItem(Menu.getFirstPotionSlot(), new ItemStack(Material.AIR));
               e.getInventory().setItem(Menu.getSecondPotionSlot(), new ItemStack(Material.AIR));
               Storage.price.put(player.getUniqueId(), Menu.getChanceGiverPrice());
               Storage.chance.put(player.getUniqueId(), Menu.getStandardChance());
               item = new ItemStack(Material.BARRIER);
               ItemMeta meta = item.getItemMeta();
               meta.setDisplayName("§cНеудача :(");
               item.setItemMeta(meta);
               (new Animation(player, item)).run();
            }
         }

         Iterator var19 = Menu.getDividerItems().iterator();

         while(var19.hasNext()) {
            String s = (String)var19.next();
            potion = "divider-items." + s + ".";
            Iterator var25 = Menu.getConfig().getIntegerList(potion + "slots").iterator();

            while(var25.hasNext()) {
               Integer i = (Integer)var25.next();
               if (e.getSlot() == i) {
                  e.setCancelled(true);
                  return;
               }
            }
         }

         var19 = Menu.getExitSlots().iterator();

         Integer i;
         while(var19.hasNext()) {
            i = (Integer)var19.next();
            if (e.getSlot() == i) {
               e.setCancelled(true);
               player.closeInventory();
               return;
            }
         }

         if (Menu.isChanceGiverEnable()) {
            var19 = Menu.getChanceGiverSlots().iterator();

            while(var19.hasNext()) {
               i = (Integer)var19.next();
               if (e.getSlot() == i) {
                  e.setCancelled(true);
                  if ((Integer)Storage.chance.get(player.getUniqueId()) >= Menu.getMaxChance()) {
                     ChatUtils.sendMsg(player, Config.getConfig().getString("messages.max-chance"));
                     return;
                  }

                  if (Eco.getEconomy().getBalance(player) < (double)(Integer)Storage.price.get(player.getUniqueId())) {
                     ChatUtils.sendMsg(player, Config.getConfig().getString("messages.no-money"));
                     return;
                  }

                  Eco.getEconomy().withdrawPlayer(player, (double)(Integer)Storage.price.get(player.getUniqueId()));
                  StorageUtils.addChance(player);
                  StorageUtils.addPrice(player);
                  Alchemist.update(player, e.getInventory());
               }
            }
         }
      }

   }
}
