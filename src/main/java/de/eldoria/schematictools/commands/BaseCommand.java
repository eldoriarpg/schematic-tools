/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) EldoriaRPG Team and Contributor
 */
package de.eldoria.schematictools.commands;

import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.schematicbrush.SchematicBrushReborn;
import de.eldoria.schematictools.commands.schematictools.Bind;
import de.eldoria.schematictools.commands.schematictools.Create;
import de.eldoria.schematictools.commands.schematictools.Info;
import de.eldoria.schematictools.commands.schematictools.List;
import de.eldoria.schematictools.commands.schematictools.Modify;
import de.eldoria.schematictools.commands.schematictools.Remove;
import de.eldoria.schematictools.configuration.Configuration;
import de.eldoria.schematictools.listener.BrushBindListener;
import org.bukkit.plugin.Plugin;

public class BaseCommand extends AdvancedCommand {
    public BaseCommand(
            Plugin plugin, SchematicBrushReborn sbr, Configuration configuration, BrushBindListener bindListener) {
        super(
                plugin,
                CommandMeta.builder("schematictools")
                        .buildSubCommands((cmds, builder) -> {
                            cmds.add(new Bind(plugin, configuration, bindListener));
                            cmds.add(new Create(plugin, sbr, configuration));
                            cmds.add(new List(plugin, configuration));
                            var info = new Info(plugin, configuration);
                            cmds.add(info);
                            cmds.add(new Modify(plugin, configuration, sbr, info));
                            cmds.add(new Remove(plugin, configuration));
                        })
                        .build());
    }
}
