package dev.minecode.language.bungeecord.helper;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PluginMessageHelper {
    public static void openLanguageChangeGUI(ProxiedPlayer proxiedPlayer) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF("Language");
            dataOutputStream.writeUTF("OpenLanguageChangeGUI");
            dataOutputStream.writeUTF(proxiedPlayer.getUniqueId().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        ServerInfo serverInfo = proxiedPlayer.getServer().getInfo();
        serverInfo.sendData("MineCode", byteArrayOutputStream.toByteArray());
    }
}