package me.matthewe.staffchat.commands;

import me.matthewe.staffchat.StaffChat;
import me.matthewe.staffchat.StaffChatConfig;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * Created by Matthew Eisenberg on 7/29/2018 at 4:45 PM for the project StaffChat
 */
public class StaffChatCommand extends Command {
    private StaffChat staffChat;

    public StaffChatCommand(StaffChat staffChat) {
        super("sc");
        this.staffChat = staffChat;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer && sender.hasPermission(StaffChatConfig.STAFF_PERMISSION)) {
            if (args.length < 1) {
                sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', StaffChatConfig.STAFF_CHAT_USAGE_MESSAGE)));
                return;
            }
            ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
            String message = "";
            for (int i = 0; i < args.length; i++) {
                message += args[i] + " ";
            }
            if (message.endsWith(" ")) {
                message = message.trim();
            }
            staffChat.sendInGameMessage(proxiedPlayer.getDisplayName(), proxiedPlayer.getServer().getInfo().getName(), message);
            staffChat.sendIngameMessageToDiscord(proxiedPlayer.getServer().getInfo().getName(), proxiedPlayer.getUniqueId(), proxiedPlayer.getName(), message);
        }

    }
}
