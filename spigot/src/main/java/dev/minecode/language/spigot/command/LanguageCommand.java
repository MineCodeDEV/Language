package dev.minecode.language.spigot.command;

import dev.minecode.core.api.CoreAPI;
import dev.minecode.core.api.object.CorePlayer;
import dev.minecode.core.api.object.CorePlugin;
import dev.minecode.core.api.object.Language;
import dev.minecode.language.api.LanguageAPI;
import dev.minecode.language.spigot.LanguageSpigot;
import dev.minecode.language.spigot.object.LanguageLanguageSpigot;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LanguageCommand implements CommandExecutor, TabCompleter {

    private final CorePlugin corePlugin = LanguageAPI.getInstance().getThisCorePlugin();

    public LanguageCommand() {
        PluginCommand pluginCommand = LanguageSpigot.getInstance().getCommand("language");
        pluginCommand.setExecutor(this);
        pluginCommand.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        CorePlayer coreExecuter = CoreAPI.getInstance().getPlayerManager().getPlayer(commandSender.getName());

        if (!commandSender.hasPermission("language.use")) {
            commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coreExecuter.getLanguage(corePlugin), LanguageLanguageSpigot.languageCommandNoPermission)
                    .args(command.getName(), args, "arg").chatcolorAll().getMessage());
            return true;
        }

        if (args.length == 0) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coreExecuter.getLanguage(corePlugin), LanguageLanguageSpigot.languageCommandNoPlayer)
                        .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                return true;
            }

            if (LanguageAPI.getInstance().isUsingGUI()) {
                Language language = coreExecuter.getLanguage(corePlugin);
                if (language == null)
                    language = CoreAPI.getInstance().getLanguageManager().getDefaultLanguage(corePlugin);
                ((Player) commandSender).openInventory(LanguageSpigot.getInstance().getInventoryManager().getLanguageInventory().get(language));
                return true;
            }

            commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coreExecuter.getLanguage(corePlugin), LanguageLanguageSpigot.languageCommandLanguageSelection).chatcolorAll().getMessage());

            String repeat = CoreAPI.getInstance().getReplaceManager(coreExecuter.getLanguage(corePlugin), LanguageLanguageSpigot.languageCommandLanguageCollection).chatcolorAll().getMessage();
            String isocode;
            for (Language language : CoreAPI.getInstance().getLanguageManager().getAllLanguages(corePlugin)) {
                isocode = language.getIsocode();
                BaseComponent[] b = CoreAPI.getInstance().getReplaceManager(repeat)
                        .language(CoreAPI.getInstance().getLanguageManager().getLanguage(corePlugin, isocode), "language").chatcolorAll().getBaseMessage();
                for (BaseComponent baseComponent : b) {
                    baseComponent.setClickEvent(
                            new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/language " + isocode));
                    baseComponent.setHoverEvent(
                            new HoverEvent(
                                    HoverEvent.Action.SHOW_TEXT,
                                    CoreAPI.getInstance().getReplaceManager(coreExecuter.getLanguage(corePlugin), LanguageLanguageSpigot.languageHoverText)
                                            .language(CoreAPI.getInstance().getLanguageManager().getLanguage(corePlugin, isocode), "language").chatcolorAll().getBaseMessage()));
                }
                ((Player) commandSender).spigot().sendMessage(b);
            }
            return true;
        }

        if (args.length == 1) {
            String isocode = args[0];
            Language language;
            if ((language = CoreAPI.getInstance().getLanguageManager().getLanguage(corePlugin, isocode)) == null) {
                commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coreExecuter.getLanguage(corePlugin), LanguageLanguageSpigot.languageCommandNoValidIsocode)
                        .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                return true;
            }

            Language oldLanguage = coreExecuter.getLanguage(corePlugin);
            coreExecuter.setLanguage(language.getIsocode());
            coreExecuter.save();

            commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coreExecuter.getLanguage(corePlugin), LanguageLanguageSpigot.languageCommandChange)
                    .language(coreExecuter.getLanguage(corePlugin), "language")
                    .language(oldLanguage, "oldLanguage")
                    .args(command.getName(), args, "arg").chatcolorAll().getMessage());
            return true;
        }

        commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coreExecuter.getLanguage(corePlugin), LanguageLanguageSpigot.languageCommandSyntaxInfo).chatcolorAll().getMessage());
        commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coreExecuter.getLanguage(corePlugin), LanguageLanguageSpigot.languageCommandSyntaxChoose).chatcolorAll().getMessage());
        commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coreExecuter.getLanguage(corePlugin), LanguageLanguageSpigot.languageCommandSyntaxSet).chatcolorAll().getMessage());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        ArrayList<String> tab = new ArrayList<>();
        List<String> list = new ArrayList<>();
        String search = null;

        if (!commandSender.hasPermission("language.use")) {
            return tab;
        }

        if (args.length == 1) {
            for (Language language : CoreAPI.getInstance().getLanguageManager().getAllLanguages(corePlugin)) {
                list.add(language.getIsocode());
            }
            search = args[0].toLowerCase();
        }

        for (String start : list) {
            if (start.toLowerCase().startsWith(search))
                tab.add(start);
        }

        return tab;
    }
}
