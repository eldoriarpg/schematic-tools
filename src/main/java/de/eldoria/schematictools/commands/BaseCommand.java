/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.schematictools.commands;

import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.messageblocker.blocker.MessageBlocker;
import de.eldoria.schematicbrush.SchematicBrushReborn;
import de.eldoria.schematictools.commands.schematictools.Bind;
import de.eldoria.schematictools.commands.schematictools.Create;
import de.eldoria.schematictools.commands.schematictools.Info;
import de.eldoria.schematictools.commands.schematictools.List;
import de.eldoria.schematictools.commands.schematictools.Modify;
import de.eldoria.schematictools.commands.schematictools.Remove;
import de.eldoria.schematictools.configuration.Configuration;
import org.bukkit.plugin.Plugin;

public class BaseCommand extends AdvancedCommand {
    public BaseCommand(Plugin plugin, SchematicBrushReborn sbr, Configuration configuration, MessageBlocker messageBlocker) {
        super(plugin, CommandMeta.builder("schematicTools")
                .withSubCommand(new Bind(plugin, configuration))
                .withSubCommand(new Create(plugin, sbr, configuration))
                .withSubCommand(new List(plugin, messageBlocker, configuration))
                .withSubCommand(new Info(plugin, messageBlocker, configuration))
                .withSubCommand(new Modify(plugin, configuration, sbr))
                .withSubCommand(new Remove(plugin, configuration))
                .build());
    }
}
