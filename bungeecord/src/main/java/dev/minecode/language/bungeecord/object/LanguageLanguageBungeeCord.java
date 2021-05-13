package dev.minecode.language.bungeecord.object;

import dev.minecode.core.api.object.LanguageAbstract;

public enum LanguageLanguageBungeeCord implements LanguageAbstract {

    languageCommandNoPermission("language", "command", "noPermission"),
    languageCommandNoPlayer("language", "command", "noPlayer"),
    languageCommandSyntaxInfo("language", "command", "syntax", "info"),
    languageCommandSyntaxChoose("language", "command", "syntax", "choose"),
    languageCommandSyntaxSet("language", "command", "syntax", "set"),
    languageCommandNoValidIsocode("language", "command", "noValidIsocode"),
    languageCommandChange("language", "command", "change"),
    languageCommandLanguageCollection("language", "command", "languageCollection"),
    languageCommandLanguageSelection("language", "command", "languageSelection"),

    languageHoverText("language", "hover", "text");

    private final String[] path;

    LanguageLanguageBungeeCord(String... path) {
        this.path = path;
    }

    public String[] getPath() {
        return path;
    }
}