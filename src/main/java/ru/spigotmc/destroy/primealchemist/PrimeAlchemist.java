package ru.spigotmc.destroy.primealchemist;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.spigotmc.destroy.primealchemist.commands.OpenCommand;
import ru.spigotmc.destroy.primealchemist.configurations.Config;
import ru.spigotmc.destroy.primealchemist.configurations.Menu;
import ru.spigotmc.destroy.primealchemist.listeners.AlchemistClickListener;
import ru.spigotmc.destroy.primealchemist.listeners.AnimationClickListener;
import ru.spigotmc.destroy.primealchemist.listeners.CloseInventoryListener;
import ru.spigotmc.destroy.primealchemist.utils.ChatUtils;
import ru.spigotmc.destroy.primealchemist.utils.Eco;
import ru.spigotmc.destroy.primealchemist.utils.Metrics;

public final class PrimeAlchemist extends JavaPlugin {
   private void msg(String msg) {
      String prefix = ChatUtils.color("&aPrimeAlchemist &7| &f");
      Bukkit.getConsoleSender().sendMessage(ChatUtils.color(prefix + msg));
   }

   public void onEnable() {
      Eco.init();
      Bukkit.getPluginManager().registerEvents(new AlchemistClickListener(), this);
      Bukkit.getPluginManager().registerEvents(new CloseInventoryListener(), this);
      Bukkit.getPluginManager().registerEvents(new AnimationClickListener(), this);
      this.getCommand("alchemist").setExecutor(new OpenCommand());
      Config.loadYaml(this);
      Menu.loadYaml(this);
      if (Config.getConfig().getBoolean("enable-metrics")) {
         new Metrics(this, 17498);
      }

      Bukkit.getConsoleSender().sendMessage("");
      this.msg("&aПлагин запущен. &fВерсия: &dv" + this.getDescription().getVersion());
      this.msg("Разработчик: &bhttps://vk.com/emptycsgo");
      Bukkit.getConsoleSender().sendMessage("");
   }

   public void onDisable() {
      Bukkit.getConsoleSender().sendMessage("");
      this.msg("&cПлагин выключен. &fВерсия: &dv" + this.getDescription().getVersion());
      this.msg("Разработчик: &bhttps://vk.com/emptycsgo");
      Bukkit.getConsoleSender().sendMessage("");
   }
}
