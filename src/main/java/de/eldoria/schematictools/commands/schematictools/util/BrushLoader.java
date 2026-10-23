/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) EldoriaRPG Team and Contributor
 */
package de.eldoria.schematictools.commands.schematictools.util;

import de.eldoria.eldoutilities.commands.command.util.Arguments;
import de.eldoria.eldoutilities.commands.command.util.CommandAssertions;
import de.eldoria.schematicbrush.storage.Storage;
import de.eldoria.schematicbrush.storage.brush.BrushContainer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

public class BrushLoader {
    private static final String GLOBAL_PREFIX = "g:";
    private static final Pattern UUID_PATTERN = Pattern.compile("\\p{XDigit}{8}(-\\p{XDigit}{4}){3}-\\p{XDigit}{12}");

    public static BrushContainer getContainer(Player player, Storage storage, String name, Arguments args) {
        var brushContainer = storage.brushes().containerByName(player, name);

        UUID owner = null;
        if (!brushContainer.isGlobalContainer() && args.flags().hasValue("o")) {
            var ownerArg = args.flags().get("o");
            var optOwner = playerByName(ownerArg.asString()).map(OfflinePlayer::getUniqueId).or(() -> parseUuid(ownerArg.asString()));
            CommandAssertions.isTrue(optOwner.isPresent(), "error.noowner");
            owner = optOwner.get();
            brushContainer = storage.brushes().playerContainer(owner);
        }

        return brushContainer;
    }

    /**
     * Returns the name a brush is stored under. Global brushes are addressed with a {@code g:} prefix, which is not
     * part of their stored name.
     *
     * @param name the brush name as entered, with or without the global prefix
     * @return the name without the global prefix
     */
    public static String storedName(String name) {
        return name.startsWith(GLOBAL_PREFIX) ? name.substring(GLOBAL_PREFIX.length()) : name;
    }

    /**
     * Reads a player UUID in its canonical form, as Minecraft shows it.
     *
     * @param uuid the text to read
     * @return the UUID, or empty if the text is not a canonical UUID
     */
    static Optional<UUID> parseUuid(String uuid) {
        if (UUID_PATTERN.matcher(uuid).matches()) {
            return Optional.of(UUID.fromString(uuid));
        }
        return Optional.empty();
    }

    private static Optional<OfflinePlayer> playerByName(String name) {
        for (var offlinePlayer : Bukkit.getServer().getOfflinePlayers()) {
            if (name.equalsIgnoreCase(offlinePlayer.getName())) {
                return Optional.of(offlinePlayer);
            }
        }
        return Optional.empty();
    }
}
