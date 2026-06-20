package ru.spigotmc.destroy.primealchemist.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtils {
   public static String color(String from) {
      Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

      for(Matcher matcher = pattern.matcher(from); matcher.find(); matcher = pattern.matcher(from)) {
         String hexCode = from.substring(matcher.start(), matcher.end());
         String replaceSharp = hexCode.replace('#', 'x');
         char[] ch = replaceSharp.toCharArray();
         StringBuilder builder = new StringBuilder();
         char[] var7 = ch;
         int var8 = ch.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            char c = var7[var9];
            builder.append("&").append(c);
         }

         from = from.replace(hexCode, builder.toString());
      }

      return ChatColor.translateAlternateColorCodes('&', from);
   }

   public static void sendMsg(Player p, String msg) {
      if (msg != null) {
         p.sendMessage(color(msg));
      }
   }
}
