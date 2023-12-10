package me.matthewe.staffchat;


import com.mrpowergamerbr.temmiewebhook.DiscordEmbed;
import com.mrpowergamerbr.temmiewebhook.DiscordMessage;
import com.mrpowergamerbr.temmiewebhook.TemmieWebhook;
import com.mrpowergamerbr.temmiewebhook.embed.AuthorEmbed;
import me.matthewe.staffchat.commands.StaffChatCommand;
import me.matthewe.staffchat.discord.StaffChatDiscordBot;
import net.dv8tion.jda.core.entities.Message;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.UUID;

import static me.matthewe.staffchat.StaffChatConfig.*;

public class StaffChat extends Plugin {
    private static StaffChat instance;
    private StaffChatConfig staffChatConfig;
    private StaffChatDiscordBot discordBot;
    private TemmieWebhook webhook;


    @Override
    public void onEnable() {
        instance = this;
        this.staffChatConfig = new StaffChatConfig(this);

        if (StaffChatConfig.DISCORD_TOKEN.equalsIgnoreCase("none")) {
            System.err.println("============================================");
            System.err.println("Please configure discord bot token, then reboot");
            System.err.println("============================================");
            return;
        }
        if (StaffChatConfig.WEBHOOK.equalsIgnoreCase("none")) {
            System.err.println("============================================");
            System.err.println("Please configure discord webhook url, then reboot");
            System.err.println("============================================");
            return;
        }

        if (StaffChatConfig.DISCORD_STAFF_CHAT_CHANNEL_ID.equalsIgnoreCase("none")) {
            System.err.println("============================================");
            System.err.println("Please configure discord staff chat channel, then reboot");
            System.err.println("============================================");
            return;
        }
        this.webhook = new TemmieWebhook(StaffChatConfig.WEBHOOK);

        this.discordBot = new StaffChatDiscordBot(StaffChatConfig.DISCORD_TOKEN, this);
        this.discordBot.start();

        this.getProxy().getPluginManager().registerCommand(this, new StaffChatCommand(this));
    }

    @Override
    public void onDisable() {
        this.discordBot.shutdown();
    }

    public void sendInGameMessage(String username, String server, String message) {
        getProxy().getPlayers().stream().filter(proxiedPlayer -> proxiedPlayer.hasPermission(STAFF_PERMISSION)).forEach(proxiedPlayer -> {
            if (!proxiedPlayer.getServer().getInfo().getName().equalsIgnoreCase(server)) {
                proxiedPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', STAFF_CHAT_DIFF_SERVER_MESSAGE)
                        .replaceAll("%server%", server)
                        .replaceAll("%name%", username)
                        .replaceAll("%message%", ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message))));
            } else {
                proxiedPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', StaffChatConfig.STAFF_CHAT_SAME_SERVER_MESSAGE)
                        .replaceAll("%name%", username)
                        .replaceAll("%server%", server)
                        .replaceAll("%message%", ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message))));
            }

        });
    }

    public void sendIngameMessageToDiscord(String server, UUID uuid, String playerName, String message) {
        discordBot.async(discordBot -> {

            String url = "https://crafatar.com/avatars/" + uuid.toString().replaceAll("-", "") + "?size=32&default=MHF_Steve&overlay";
            DiscordEmbed discordEmbed = DiscordEmbed.builder()
                    .author(AuthorEmbed.builder()
                            .name(new String(DISCORD_AUTHOR).replaceAll("%username%", playerName).replaceAll("%server%", server))
                            .icon_url(url)
                            .url(url)
                            .build())
                    .color(EMBED_COLOR)
                    .description(new String(DISCORD_CONTENT).replaceAll("%message%", message)).build();
            this.webhook.sendMessage(DiscordMessage.builder().avatarUrl(url).textToSpeech(false).username(new String(DISCORD_AUTHOR).replaceAll("%username%", playerName).replaceAll("%server%", server)).embed(discordEmbed).build());
        });
    }

    public void sendInGameMessageFromDiscord(Message message) {
        getProxy().getPlayers().stream().filter(proxiedPlayer -> proxiedPlayer.hasPermission(STAFF_PERMISSION)).forEach(proxiedPlayer -> {
            proxiedPlayer.sendMessage(ChatMessageType.CHAT, new TextComponent(ChatColor.translateAlternateColorCodes('&', StaffChatConfig.STAFF_CHAT_FROM_DISCORD_MESSAGE)
                    .replaceAll("%message%", message.getContentDisplay())
                    .replaceAll("%name%", message.getAuthor().getName())
                    .replaceAll("%discriminator%", message.getAuthor().getDiscriminator())));
        });
    }


    public StaffChatDiscordBot getDiscordBot() {
        return discordBot;
    }

    public static StaffChat getInstance() {
        return instance;
    }
}
