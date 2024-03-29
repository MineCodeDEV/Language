package dev.minecode.language.common;

import dev.minecode.core.api.CoreAPI;
import dev.minecode.core.api.manager.UpdateManager;
import dev.minecode.core.api.object.CorePlugin;
import dev.minecode.language.api.LanguageAPI;
import dev.minecode.language.common.api.LanguageAPIProvider;

public class LanguageCommon {

    private static LanguageCommon instance;

    public LanguageCommon() {
        makeInstances();

        CorePlugin corePlugin = LanguageAPI.getInstance().getThisCorePlugin();
        UpdateManager updateManager = CoreAPI.getInstance().getUpdateManager(corePlugin);

        if (updateManager.checkUpdateAvailable())
            System.out.println("[" + corePlugin.getName() + "] There is a newer Version available! You can download it at " + updateManager.getReleaseURL(updateManager.getMatchingRelease()));
    }

    public static LanguageCommon getInstance() {
        return instance;
    }

    private void makeInstances() {
        instance = this;
        new LanguageAPIProvider();
    }
}
