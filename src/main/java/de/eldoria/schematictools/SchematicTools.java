/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.schematictools;

import de.eldoria.eldoutilities.messages.MessageSender;
import de.eldoria.eldoutilities.plugin.EldoPlugin;
import de.eldoria.messageblocker.MessageBlockerAPI;
import de.eldoria.schematicbrush.SchematicBrushReborn;
import de.eldoria.schematictools.commands.Tools;
import de.eldoria.schematictools.configuration.Configuration;

public class SchematicTools extends EldoPlugin {
    private Configuration configuration;

    @Override
    public void onPluginEnable() throws Throwable {
        var sbr = SchematicBrushReborn.instance();
        MessageSender.create(this, "[ST]");
        var messageBlocker = MessageBlockerAPI.builder(this).addWhitelisted("[ST]").build();

        configuration = new Configuration(this);

        registerCommand(new Tools(this, sbr, configuration, messageBlocker));
    }

    @Override
    public void onPluginDisable() throws Throwable {
        configuration.save();
    }
}
