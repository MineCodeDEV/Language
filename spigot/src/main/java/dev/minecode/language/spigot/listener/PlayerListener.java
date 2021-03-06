package dev.minecode.language.spigot.listener;

import dev.minecode.core.api.CoreAPI;
import dev.minecode.language.api.LanguageAPI;
import dev.minecode.language.spigot.LanguageSpigot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    public PlayerListener() {
        Bukkit.getPluginManager().registerEvents(this, LanguageSpigot.getInstance());
    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (LanguageAPI.getInstance().isForceOpenInventory()) {
            if (CoreAPI.getInstance().getPlayerManager().getPlayer(player.getUniqueId()).isLanguageEmpty()) {
                Bukkit.getScheduler().runTaskLater(LanguageSpigot.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        player.openInventory(LanguageSpigot.getInstance().getInventoryManager().getLanguageInventory().get(CoreAPI.getInstance().getLanguageManager().getDefaultLanguage(LanguageAPI.getInstance().getThisCorePlugin())));
                    }
                }, 10);
            }
        }
    }
}
