/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) EldoriaRPG Team and Contributor
 */
package de.eldoria.schematictools.commands.schematictools;

import de.eldoria.eldoutilities.commands.Completion;
import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.eldoutilities.commands.command.util.Arguments;
import de.eldoria.eldoutilities.commands.command.util.CommandAssertions;
import de.eldoria.eldoutilities.commands.command.util.Input;
import de.eldoria.eldoutilities.commands.exceptions.CommandException;
import de.eldoria.eldoutilities.commands.executor.IPlayerTabExecutor;
import de.eldoria.eldoutilities.utils.Futures;
import de.eldoria.schematicbrush.SchematicBrushReborn;
import de.eldoria.schematictools.commands.schematictools.util.BrushLoader;
import de.eldoria.schematictools.configuration.Configuration;
import de.eldoria.schematictools.util.Permissions;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Modifies an existing schematic tool.
 */
public class Modify extends AdvancedCommand implements IPlayerTabExecutor {
    private static final List<String> FIELDS = List.of("brushName", "name", "usages", "permission");
    private static final String UNLIMITED = "-1";
    private final Configuration configuration;
    private final SchematicBrushReborn sbr;
    private final Info info;

    public Modify(Plugin plugin, Configuration configuration, SchematicBrushReborn sbr, Info info) {
        super(plugin, CommandMeta.builder("modify")
                                 .addUnlocalizedArgument("name", true)
                                 .addUnlocalizedArgument("field", true)
                                 .addUnlocalizedArgument("value", true)
                                 .withPermission(Permissions.MANAGE)
                                 .build());
        this.configuration = configuration;
        this.sbr = sbr;
        this.info = info;
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        args.parseQuoted();
        var optionalTool = configuration.tools().byName(args.asString(0));

        CommandAssertions.isTrue(optionalTool.isPresent(), "error.unknowntoolname");

        var value = args.get(2);

        var tool = optionalTool.get();
        switch (args.asString(1).toLowerCase(Locale.ROOT)) {
            case "brushname" -> {
                var storage = sbr.storageRegistry().activeStorage();
                var container = BrushLoader.getContainer(player, storage, value.asString(), args);
                container.get(BrushLoader.storedName(value.asString())).thenAccept(brush -> {
                    CommandAssertions.isTrue(brush.isPresent(), "error.nobrushfound");
                    tool.brush(container.owner(), brush.get());
                }).whenComplete(Futures.whenComplete(suc -> {
                    configuration.save();
                    info.showTool(player, tool);
                }, err -> handleCommandError(player, err)));
            }
            case "name" -> {
                CommandAssertions.isFalse(configuration.tools().byName(value.asString()).isPresent(), "Name is already in use.");
                tool.name(value.asString());
                configuration.save();
                info.showTool(player, tool);
            }
            case "usages" -> {
                var usages = value.asInt();
                if (usages != -1) {
                    CommandAssertions.min(usages, 1);
                }
                tool.usages(usages);
                configuration.save();
                info.showTool(player, tool);
            }
            case "permission" -> {
                tool.permission(value.asString());
                configuration.save();
                info.showTool(player, tool);
            }
            default -> throw CommandException.message("error.invalidfieldname");
        }

    }

    private Optional<OfflinePlayer> playerByName(String name) {
        for (var offlinePlayer : plugin().getServer().getOfflinePlayers()) {
            if (name.equalsIgnoreCase(offlinePlayer.getName())) {
                return Optional.of(offlinePlayer);
            }
        }
        return Optional.empty();
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        args.parseQuoted();

        if ("o".equalsIgnoreCase(args.flags().lastFlag())) {
            return Completion.completePlayers(args.flags().getIfPresent("o").map(Input::asString).orElse(""));
        }

        if (args.sizeIs(1)) {
            return configuration.tools().complete(args.asString(0));
        }

        if (args.sizeIs(2)) {
            return completeField(args.asString(1));
        }

        if (args.sizeIs(3)) {
            return completeValue(player, args.asString(1), args.asString(2));
        }

        return Collections.emptyList();
    }

    private List<String> completeValue(Player player, String field, String value) throws CommandException {
        return switch (field.toLowerCase(Locale.ROOT)) {
            case "brushname" -> sbr.storageRegistry().activeStorage().brushes().complete(player, value);
            case "name" -> {
                CommandAssertions.isFalse(configuration.tools().byName(value).isPresent(), "error.nametaken");
                yield Completion.completeFreeInput(value, 32, "name");
            }
            case "usages" -> completeUsages(value);
            case "permission" -> Collections.singletonList("permission");
            default -> Collections.emptyList();
        };
    }

    /**
     * Completes the name of a field that can be modified.
     *
     * @param field the field name typed so far
     * @return the field names starting with the typed text, ignoring case
     */
    static List<String> completeField(String field) {
        return Completion.complete(field, FIELDS);
    }

    /**
     * Completes a usage limit, which is either 1 or more, or {@code -1} for no limit.
     *
     * @param value the value typed so far
     * @return the possible values
     * @throws CommandException if the value is no number of 1 or more
     */
    static List<String> completeUsages(String value) throws CommandException {
        if (value.isEmpty()) return List.of(UNLIMITED, "1<");
        if (UNLIMITED.startsWith(value)) return List.of(UNLIMITED);
        return Completion.completeMinInt(value, 1);
    }
}
