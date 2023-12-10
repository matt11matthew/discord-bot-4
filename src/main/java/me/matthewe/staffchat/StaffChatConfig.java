package me.matthewe.staffchat;

import net.atherial.api.config.BungeecordConfig;
import net.atherial.api.config.SerializedName;

/**
 * Created by Matthew Eisenberg on 7/29/2018 at 4:29 PM for the project StaffChat
 */
public class StaffChatConfig extends BungeecordConfig {
    @SerializedName("messages.staffChatUsage")
    public static String STAFF_CHAT_USAGE_MESSAGE = "&cCorrect Usage: /sc &7(message)";
    @SerializedName("messages.staffChatFromDiscord")
    public static  String STAFF_CHAT_FROM_DISCORD_MESSAGE = "&8&l(&c&lSC&8&l) &b%name%#%discriminator%: &f%message%";

    @SerializedName("messages.discord.embedColor")
    public static Integer EMBED_COLOR = 0x00d235;
    @SerializedName("messages.discord.content")
    public static String DISCORD_CONTENT = "%message%";
    @SerializedName("messages.discord.author")
    public static String DISCORD_AUTHOR= "%username% (%server%)";
    @SerializedName("messages.staffChatOtherServer")
    public static  String STAFF_CHAT_DIFF_SERVER_MESSAGE = "&8&l(&c&lSC&8&l) &b%name% &8&l(&a&l%server%&8&l)&f: &a%message%";
    @SerializedName("messages.staffChatSameServer")
    public static  String STAFF_CHAT_SAME_SERVER_MESSAGE = "&8&l(&ac&lSC&8&l) &b%name%&f: &a%message%";
    @SerializedName("discord.token")
    public static String DISCORD_TOKEN = "none";

    @SerializedName("discord.staffchatChannel")
    public static String DISCORD_STAFF_CHAT_CHANNEL_ID = "none";
    @SerializedName("settings.staffPermission")
    public static String STAFF_PERMISSION = "staffchat.use";

    @SerializedName("settings.webhook")
    public static String WEBHOOK = "none";

    public StaffChatConfig(net.md_5.bungee.api.plugin.Plugin plugin) {
        super("config.yml", plugin);
    }
}
