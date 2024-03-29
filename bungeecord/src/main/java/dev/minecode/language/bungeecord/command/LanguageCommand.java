package dev.minecode.language.bungeecord.command;

import dev.minecode.core.api.CoreAPI;
import dev.minecode.core.api.object.CorePlayer;
import dev.minecode.core.api.object.CorePlugin;
import dev.minecode.core.api.object.Language;
import dev.minecode.language.api.LanguageAPI;
import dev.minecode.language.bungeecord.helper.PluginMessageHelper;
import dev.minecode.language.bungeecord.object.LanguageLanguageBungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LanguageCommand extends Command implements TabExecutor {

    private final CorePlugin corePlugin = LanguageAPI.getInstance().getThisCorePlugin();

    public LanguageCommand(String name) {
        super(name);
    }

    public LanguageCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        CorePlayer coreExecuter = CoreAPI.getInstance().getPlayerManager().getPlayer(commandSender.getName());

        if (!commandSender.hasPermission("language.use")) {
            syntaxMessage(commandSender, coreExecuter);
            return;
        }

        if (args.length == 0) {
            if (!(commandSender instanceof ProxiedPlayer)) {
                commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coreExecuter.getLanguage(corePlugin), LanguageLanguageBungeeCord.languageCommandNoPlayer)
                        .args("language", args, "arg").chatcolorAll().getBaseMessage());
                return;
            }

            if (LanguageAPI.getInstance().isUsingGUI()) {
                PluginMessageHelper.openLanguageChangeGUI((ProxiedPlayer) commandSender);
                return;
            }

            commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coreExecuter.getLanguage(corePlugin), LanguageLanguageBungeeCord.languageCommandLanguageSelection).chatcolorAll().getBaseMessage());

            String repeat = CoreAPI.getInstance().getReplaceManager(coreExecuter.getLanguage(corePlugin), LanguageLanguageBungeeCord.languageCommandLanguageCollection).chatcolorAll().getMessage();
            String isocode;
            for (Language language : CoreAPI.getInstance().getLanguageManager().getLanguages(corePlugin)) {
                isocode = language.getIsocode();
                BaseComponent[] b = CoreAPI.getInstance().getReplaceManager(repeat).language(CoreAPI.getInstance().getLanguageManager().getLanguage(corePlugin, isocode), "language").chatcolorAll().getBaseMessage();
                for (BaseComponent baseComponent : b) {
                    baseComponent.setClickEvent(
                            new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/language " + isocode));
                    baseComponent.setHoverEvent(
                            new HoverEvent(
                                    HoverEvent.Action.SHOW_TEXT,
                                    new Text(CoreAPI.getInstance().getReplaceManager(coreExecuter.getLanguage(corePlugin), LanguageLanguageBungeeCord.languageHoverLanguageChoose)
                                            .language(CoreAPI.getInstance().getLanguageManager().getLanguage(corePlugin, isocode), "language")
                                            .chatcolorAll().getBaseMessage())));
                }
                commandSender.sendMessage(b);
            }
            return;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                syntaxMessage(commandSender, coreExecuter);
                return;
            }

            String isocode = args[0];
            Language language;
            if ((language = CoreAPI.getInstance().getLanguageManager().getLanguage(corePlugin, isocode)) == null) {
                commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coreExecuter.getLanguage(corePlugin), LanguageLanguageBungeeCord.languageCommandNoValidIsocode)
                        .args("language", args, "arg").chatcolorAll().getBaseMessage());
                return;
            }

            Language oldLanguage = coreExecuter.getLanguage(corePlugin);

            if (language == oldLanguage) {
                commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coreExecuter.getLanguage(corePlugin), LanguageLanguageBungeeCord.languageCommandAlreadyChosen)
                        .language(language, "language")
                        .args("language", args, "arg").chatcolorAll().getBaseMessage());
                return;
            }

            coreExecuter.setLanguage(language.getIsocode());
            coreExecuter.save();

            commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coreExecuter.getLanguage(corePlugin), LanguageLanguageBungeeCord.languageCommandChange)
                    .language(coreExecuter.getLanguage(corePlugin), "language")
                    .language(oldLanguage, "oldLanguage")
                    .args("language", args, "arg").chatcolorAll().getBaseMessage());
            return;
        }

        syntaxMessage(commandSender, coreExecuter);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] args) {
        Set<String> tab = new HashSet();
        List<String> list = new ArrayList<>();
        String search = null;

        if (!commandSender.hasPermission("language.use")) {
            return tab;
        }

        if (args.length == 1) {
            for (Language language : CoreAPI.getInstance().getLanguageManager().getLanguages(corePlugin)) {
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

    private void syntaxMessage(CommandSender commandSender, CorePlayer corePlayer) {
        if (!commandSender.hasPermission("language.use")) {
            commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(corePlayer.getLanguage(corePlugin), LanguageLanguageBungeeCord.languageCommandNoPermission).chatcolorAll().getBaseMessage());
            return;
        }

        commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(corePlayer.getLanguage(corePlugin), LanguageLanguageBungeeCord.languageCommandSyntaxInfo).chatcolorAll().getBaseMessage());
        commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(corePlayer.getLanguage(corePlugin), LanguageLanguageBungeeCord.languageCommandSyntaxChoose).chatcolorAll().getBaseMessage());
        commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(corePlayer.getLanguage(corePlugin), LanguageLanguageBungeeCord.languageCommandSyntaxSet).chatcolorAll().getBaseMessage());
    }
}
