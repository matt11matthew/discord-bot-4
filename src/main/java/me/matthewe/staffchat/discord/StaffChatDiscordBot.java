package me.matthewe.staffchat.discord;

import me.matthewe.staffchat.StaffChat;
import me.matthewe.staffchat.StaffChatConfig;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.function.Consumer;

/**
 * Created by Matthew Eisenberg on 7/29/2018 at 4:20 PM for the project StaffChat
 */
public class StaffChatDiscordBot extends ListenerAdapter {
    private StaffChat staffChat;
    private String token;
    private JDA jda;
    private ShardManager shardManager;

    public StaffChatDiscordBot(String token, StaffChat staffChat) {
        this.token = token;
        this.staffChat = staffChat;
    }


    public void start() {
        DefaultShardManagerBuilder defaultShardManagerBuilder = (new DefaultShardManagerBuilder()).setToken(this.token).addEventListeners(this);
        try {
            this.shardManager = defaultShardManagerBuilder.build();
            this.shardManager.getApplicationInfo().queue((applicationInfo) -> {
                this.jda = applicationInfo.getJDA();
            });
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public void async(Consumer<StaffChatDiscordBot> callback) {
        new Thread(() -> callback.accept(this)).start();
    }


    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getMessage().getChannel().getId().equalsIgnoreCase(StaffChatConfig.DISCORD_STAFF_CHAT_CHANNEL_ID) && !event.getAuthor().isBot()) {
            staffChat.sendInGameMessageFromDiscord(event.getMessage());
        }
    }

    public void shutdown() {
        this.shardManager.shutdown();

    }

    public JDA getJDA() {
        return jda;
    }
}
