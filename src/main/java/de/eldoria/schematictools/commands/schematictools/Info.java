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
import de.eldoria.eldoutilities.localization.MessageComposer;
import de.eldoria.messageblocker.blocker.MessageBlocker;
import de.eldoria.schematicbrush.util.Colors;
import de.eldoria.schematictools.configuration.Configuration;
import de.eldoria.schematictools.configuration.elements.Tool;
import de.eldoria.schematictools.util.Permissions;
import de.eldoria.schematictools.util.SchematicTool;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class Info extends AdvancedCommand implements IPlayerTabExecutor {
    private final MessageBlocker messageBlocker;
    private final Configuration configuration;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final BukkitAudiences audiences;

    public Info(Plugin plugin, MessageBlocker messageBlocker, Configuration configuration) {
        super(plugin, CommandMeta.builder("info")
                .addUnlocalizedArgument("name", false)
                .withPermission(Permissions.Info.CURRENT, Permissions.Info.ALL)
                .build());
        this.messageBlocker = messageBlocker;
        this.configuration = configuration;
        audiences = BukkitAudiences.builder(plugin).build();
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        args.parseQuoted();
        Tool tool;
        if (args.isEmpty()) {
            CommandAssertions.permission(player, false, Permissions.Info.CURRENT);
            var optToolMeta = SchematicTool.getCurrentTool(player);
            CommandAssertions.isTrue(optToolMeta.isPresent(), "Please hold a schematic tool in your hand or enter a name.");
            var optTool = configuration.tools().byId(optToolMeta.get().id());
            tool = optTool.get();
        } else {
            CommandAssertions.permission(player, false, Permissions.Info.ALL);
            var optTool = configuration.tools().byName(args.asString(0));
            CommandAssertions.isTrue(optTool.isPresent(), "Unkown tool name.");
            tool = optTool.get();
        }

        showTool(player, tool);
    }

    public void showTool(Player player, Tool tool) {
        messageBlocker.blockPlayer(player);

        var composer = MessageComposer.create();
        if (player.hasPermission(Permissions.MANAGE)) {
            composer.text(tool.asModifyComponent());
        } else {
            composer.text(tool.asInfoComponent());
        }
        if (player.hasPermission(Permissions.Info.ALL)) {
            composer.newLine().text("<click:run_command:'/schematictools list'><%s>[Back]</click>", Colors.CHANGE);
        }

        messageBlocker.ifEnabled(() -> composer.newLine().text("<click:run_command:'/schematictools chatblock false'><%s>[x]</click>", Colors.REMOVE));
        messageBlocker.announce(player, "[x]");
        audiences.player(player).sendMessage(miniMessage.deserialize(composer.build()));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        if (args.sizeIs(1)) {
            return configuration.tools().complete(args.get(0).asString());
        }
        return Collections.emptyList();
    }
}
