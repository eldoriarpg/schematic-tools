/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) EldoriaRPG Team and Contributor
 */
package de.eldoria.schematictools;

import de.eldoria.eldoutilities.config.template.PluginBaseConfiguration;
import de.eldoria.eldoutilities.localization.Localizer;
import de.eldoria.eldoutilities.messages.MessageSender;
import de.eldoria.eldoutilities.plugin.EldoPlugin;
import de.eldoria.schematicbrush.SchematicBrushReborn;
import de.eldoria.schematictools.commands.BaseCommand;
import de.eldoria.schematictools.configuration.JacksonConfiguration;
import de.eldoria.schematictools.listener.BrushBindListener;
import de.eldoria.schematictools.listener.BrushPasteListener;
import de.eldoria.schematictools.updates.Updates;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.Tag;

import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

public class SchematicTools extends EldoPlugin {
    private final JacksonConfiguration configuration = new JacksonConfiguration(this);
    private final Updates updates = new Updates(this, configuration);

    @Override
    public Level getLogLevel() {
        return configuration.secondary(PluginBaseConfiguration.KEY).logLevel();
    }

    @Override
    public void onPluginEnable() throws Throwable {
        var sbr = SchematicBrushReborn.instance();
        var localizer = Localizer.builder(this, "en_US")
                .setIncludedLocales("en_US")
                .build();
        var messageSender = MessageSender.builder(this)
                .prefix("<gold>[ST]")
                .addTag(tags -> tags
                        .tag("heading", Tag.styling(NamedTextColor.GOLD))
                        .tag("name", Tag.styling(NamedTextColor.DARK_AQUA))
                        .tag("value", Tag.styling(NamedTextColor.DARK_GREEN))
                        .tag("change", Tag.styling(NamedTextColor.YELLOW))
                        .tag("neutral", Tag.styling(NamedTextColor.AQUA))
                        .tag("inactive", Tag.styling(NamedTextColor.GRAY)))
                .localizer(localizer)
                .register();

        PluginBaseConfiguration base = configuration.secondary(PluginBaseConfiguration.KEY);
        if (base.version() == 0) {
            if (isPreJacksonInstall()) {
                throw new IllegalStateException("The configuration was written by a version older than 1.1.0. Update to 1.1.2 first, start the server once, then update to this version.");
            }
            base.version(1);
            base.lastInstalledVersion(this);
            configuration.save();
        }

        updates.apply();

        registerCommand(new BaseCommand(this, sbr, configuration));

        registerListener(new BrushBindListener(this, sbr, configuration, messageSender),
                new BrushPasteListener(this, configuration, messageSender));
    }

    /**
     * Checks whether the data folder still holds a configuration in the Bukkit serialization format used before 1.1.0.
     *
     * @return true if the main configuration contains Bukkit serialization markers
     * @throws IOException if the configuration file can not be read
     */
    private boolean isPreJacksonInstall() throws IOException {
        var config = getDataFolder().toPath().resolve("config.yml");
        return Files.exists(config) && Files.readString(config).contains("==:");
    }

    @Override
    public void onPluginDisable() throws Throwable {
        updates.stop();
        configuration.save();
    }
}
