package org.bknibb.bk_meteor_addon;

import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.bknibb.bk_meteor_addon.commands.LocatePlayerCommand;
import org.bknibb.bk_meteor_addon.modules.MineplayRemoveOfflineRobloxPlayers;
import org.bknibb.bk_meteor_addon.modules.PlayerEsp;
import org.bknibb.bk_meteor_addon.modules.PlayerLoginLogoutNotifier;
import org.bknibb.bk_meteor_addon.modules.PlayerTracers;
import org.slf4j.Logger;

public class BkMeteorAddon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("BkMeteorAddon");

    @Override
    public void onInitialize() {
        LOG.info("Initializing Bk Meteor Addon");

        // Modules
        Modules.get().add(new PlayerEsp());
        Modules.get().add(new PlayerTracers());
        Modules.get().add(new PlayerLoginLogoutNotifier());
        Modules.get().add(new MineplayRemoveOfflineRobloxPlayers());

        // Commands
        Commands.add(new LocatePlayerCommand());
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "org.bknibb.bk_meteor_addon";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("Bknibb", "bk-meteor-addon");
    }
}
