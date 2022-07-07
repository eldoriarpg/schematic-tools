/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.schematictools.configuration;

import de.eldoria.eldoutilities.configuration.EldoConfig;
import de.eldoria.eldoutilities.utils.Consumers;
import de.eldoria.schematictools.configuration.elements.Tools;
import org.bukkit.plugin.Plugin;

public class Configuration extends EldoConfig {
    private Tools tools = new Tools();

    public Configuration(Plugin plugin) {
        super(plugin);
    }

    @Override
    protected void reloadConfigs() {
        tools = loadConfig("tools", Consumers.emptyConsumer(), false).getObject("tools", Tools.class, new Tools());
    }

    @Override
    protected void saveConfigs() {
        loadConfig("tools", Consumers.emptyConsumer(), false).set("tools", tools);
    }

    public Tools tools() {
        return tools;
    }
}
