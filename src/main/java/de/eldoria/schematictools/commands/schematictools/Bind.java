/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.schematictools.commands.schematictools;

import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.eldoutilities.commands.command.util.Arguments;
import de.eldoria.eldoutilities.commands.command.util.CommandAssertions;
import de.eldoria.eldoutilities.commands.exceptions.CommandException;
import de.eldoria.eldoutilities.commands.executor.IPlayerTabExecutor;
import de.eldoria.schematictools.configuration.Configuration;
import de.eldoria.schematictools.util.Permissions;
import de.eldoria.schematictools.util.SchematicTool;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// Bind a Schematic Tool onto an item
public class Bind extends AdvancedCommand implements IPlayerTabExecutor {
    private final Configuration configuration;

    public Bind(Plugin plugin, Configuration configuration) {
        super(plugin, CommandMeta.builder("bind")
                .addUnlocalizedArgument("name", true)
                .withPermission(Permissions.USE)
                .build());
        this.configuration = configuration;
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        var hand = player.getInventory().getItemInMainHand();
        CommandAssertions.isFalse(hand == null || hand.getType() == Material.AIR, "Hold the item in your hand.");
        // TODO: Check if blocks still can't be used for tools.
        // CommandAssertions.isFalse(type.isBlock(), "Can not be bound on a block.");

        var byName = configuration.tools().byName(args.get(0).asString());
        CommandAssertions.isTrue(byName.isPresent(), "Tool not found.");

        SchematicTool.initTool(hand, byName.get());

        messageSender().sendMessage(player, "Tool bound.");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        return IPlayerTabExecutor.super.onTabComplete(player, alias, args);
    }
}
