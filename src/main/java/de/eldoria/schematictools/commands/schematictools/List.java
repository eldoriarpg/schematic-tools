/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) EldoriaRPG Team and Contributor
 */
package de.eldoria.schematictools.commands.schematictools;

import de.eldoria.eldoutilities.commands.command.AdvancedCommand;
import de.eldoria.eldoutilities.commands.command.CommandMeta;
import de.eldoria.eldoutilities.commands.command.util.Arguments;
import de.eldoria.eldoutilities.commands.exceptions.CommandException;
import de.eldoria.eldoutilities.commands.executor.IPlayerTabExecutor;
import de.eldoria.eldoutilities.localization.MessageComposer;
import de.eldoria.schematictools.configuration.Configuration;
import de.eldoria.schematictools.configuration.elements.Tool;
import de.eldoria.schematictools.configuration.elements.Tools;
import de.eldoria.schematictools.util.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Lists the schematic tool configurations.
 */
public class List extends AdvancedCommand implements IPlayerTabExecutor {
    public final int PAGE_SIZE = 15;
    public final String RIGHT_ARROW = "»»»";
    public final String LEFT_ARROW = "«««";
    private final Configuration configuration;

    public List(Plugin plugin, Configuration configuration) {
        super(plugin, CommandMeta.builder("list")
                .withPermission(Permissions.LIST)
                .build());
        this.configuration = configuration;
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull String alias, @NotNull Arguments args) throws CommandException {
        var index = args.asInt(0, 0);
        var tools = configuration.tools();
        var composer = MessageComposer.create();
        addPageHeader(composer, "words.tools");
        addEntries(composer, tools.page(index, PAGE_SIZE), Tool::asListComponent);
        addPageFooter(composer, index, tools);
        send(composer, player);
    }

    protected void addPageHeader(MessageComposer composer, String title) {
        composer.text("<heading>").localeCode(title).newLine();
    }

    protected <T> void addEntries(MessageComposer composer, java.util.List<T> entries, Function<T, String> map) {
        var page = entries.stream().map(entry -> String.format("  %s", map.apply(entry))).toList();
        composer.text(page).newLine();
    }

    protected void addPageFooter(MessageComposer composer, int index, Tools paged) {
        var baseCommand = "/" + meta().createCommandCall();
        if (index == 0) {
            composer.text("<inactive>%s", LEFT_ARROW);
        } else {
            composer.text("<click:run_command:'%s %s'><change>%s</click>", baseCommand, index - 1, LEFT_ARROW);
        }
        composer.text(" <neutral>%s / %s ", index + 1, Math.max(1, paged.pages(PAGE_SIZE)));

        if (index + 1 >= paged.pages(PAGE_SIZE)) {
            composer.text("<inactive>%s", RIGHT_ARROW);
        } else {
            composer.text("<click:run_command:'%s %s'><change>%s</click>", baseCommand, index + 1, RIGHT_ARROW);
        }
    }

    protected void send(MessageComposer composer, Player player) {
        composer.prependLines(20);
        messageSender().sendMessage(player, composer.build());
    }
}
