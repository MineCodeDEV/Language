package dev.minecode.language.bungeecord.listener;

import dev.minecode.core.api.CoreAPI;
import dev.minecode.language.api.LanguageAPI;
import dev.minecode.language.bungeecord.LanguageBungeeCord;
import dev.minecode.language.bungeecord.helper.PluginMessageHelper;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

public class PlayerListener implements Listener {

    public PlayerListener() {
        LanguageBungeeCord.getInstance().getProxy().getPluginManager().registerListener(LanguageBungeeCord.getInstance(), this);
    }

    @EventHandler
    public void handlePlayerJoin(PostLoginEvent event) {
        ProxiedPlayer proxiedPlayer = event.getPlayer();

        if (LanguageAPI.getInstance().isForceOpenInventory())
            if (CoreAPI.getInstance().getPlayerManager().getPlayer(proxiedPlayer.getUniqueId()).isLanguageEmpty())
                ProxyServer.getInstance().getScheduler().schedule(LanguageBungeeCord.getInstance(), () ->
                        PluginMessageHelper.openLanguageChangeGUI(proxiedPlayer), 1, TimeUnit.SECONDS);
    }
}
