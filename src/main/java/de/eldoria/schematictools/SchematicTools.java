/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) EldoriaRPG Team and Contributor
 */
package de.eldoria.schematictools;

import de.eldoria.eldoutilities.config.template.PluginBaseConfiguration;
import de.eldoria.eldoutilities.localization.ILocalizer;
import de.eldoria.eldoutilities.messages.MessageSender;
import de.eldoria.eldoutilities.plugin.EldoPlugin;
import de.eldoria.messageblocker.MessageBlockerAPI;
import de.eldoria.schematicbrush.SchematicBrushReborn;
import de.eldoria.schematictools.commands.BaseCommand;
import de.eldoria.schematictools.configuration.Configuration;
import de.eldoria.schematictools.configuration.JacksonConfiguration;
import de.eldoria.schematictools.configuration.LegacyConfiguration;
import de.eldoria.schematictools.configuration.elements.Tool;
import de.eldoria.schematictools.configuration.elements.ToolRemoval;
import de.eldoria.schematictools.configuration.elements.Tools;
import de.eldoria.schematictools.listener.BrushBindListener;
import de.eldoria.schematictools.listener.BrushPasteListener;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.List;
import java.util.logging.Level;

public class SchematicTools extends EldoPlugin {
    private JacksonConfiguration configuration;

    @Override
    public void onPluginEnable() throws Throwable {
        var sbr = SchematicBrushReborn.instance();
        var messageSender = MessageSender.create(this, "§6[ST]");
        var messageBlocker = MessageBlockerAPI.builder(this).addWhitelisted("[ST]").build();
        ILocalizer.create(this, "en_US").setLocale("en_US");
        configuration = new JacksonConfiguration(this);
        PluginBaseConfiguration base = configuration.secondary(PluginBaseConfiguration.KEY);
        if (base.version() == 0) {
            var legacyConfiguration = new LegacyConfiguration(this);
            getLogger().log(Level.INFO, "Migrating configuration to jackson.");
            configuration.main().toolRemoval(legacyConfiguration.toolRemoval());
            configuration.replace(JacksonConfiguration.TOOLS, legacyConfiguration.tools());
            base.version(1);
            base.lastInstalledVersion(this);
            configuration.save();
        }

        registerCommand(new BaseCommand(this, sbr, configuration, messageBlocker));

        registerListener(new BrushBindListener(this, sbr, configuration, messageSender),
                new BrushPasteListener(this, configuration, messageSender));
    }

    @Override
    public List<Class<? extends ConfigurationSerializable>> getConfigSerialization() {
        return List.of(Tools.class, Tool.class, ToolRemoval.class);
    }

    @Override
    public void onPluginDisable() throws Throwable {
        configuration.save();
    }
}
