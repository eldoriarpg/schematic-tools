/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.schematictools;

import de.eldoria.eldoutilities.localization.ILocalizer;
import de.eldoria.eldoutilities.messages.MessageSender;
import de.eldoria.eldoutilities.plugin.EldoPlugin;
import de.eldoria.messageblocker.MessageBlockerAPI;
import de.eldoria.schematicbrush.SchematicBrushReborn;
import de.eldoria.schematictools.commands.BaseCommand;
import de.eldoria.schematictools.configuration.Configuration;
import de.eldoria.schematictools.configuration.elements.Tool;
import de.eldoria.schematictools.configuration.elements.Tools;
import de.eldoria.schematictools.listener.BrushBindListener;
import de.eldoria.schematictools.listener.BrushPasteListener;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.List;

public class SchematicTools extends EldoPlugin {
    private Configuration configuration;

    @Override
    public void onPluginEnable() throws Throwable {
        var sbr = SchematicBrushReborn.instance();
        var messageSender = MessageSender.create(this, "§6[ST]");
        var messageBlocker = MessageBlockerAPI.builder(this).addWhitelisted("[ST]").build();
        ILocalizer.create(this, "en_US").setLocale("en_US");
        configuration = new Configuration(this);

        registerCommand(new BaseCommand(this, sbr, configuration, messageBlocker));

        registerListener(new BrushBindListener(this, sbr, configuration, messageSender),
                new BrushPasteListener(configuration, messageSender));
    }

    @Override
    public List<Class<? extends ConfigurationSerializable>> getConfigSerialization() {
        return List.of(Tools.class, Tool.class);
    }

    @Override
    public void onPluginDisable() throws Throwable {
        configuration.save();
    }
}
