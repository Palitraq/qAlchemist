package ru.spigotmc.destroy.primealchemist.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.spigotmc.destroy.primealchemist.menus.Alchemist;

public class OpenCommand implements CommandExecutor {
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (sender instanceof Player) {
         Alchemist.open((Player)sender);
      }

      return true;
   }
}
