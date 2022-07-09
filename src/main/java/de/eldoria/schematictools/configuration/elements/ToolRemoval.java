/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.schematictools.configuration.elements;

import de.eldoria.eldoutilities.serialization.SerializationUtil;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ToolRemoval implements ConfigurationSerializable {
    private boolean removeUsed;
    private boolean removeInvalidTools;
    private boolean removeInvalidBrushes;

    /**
     * Constructor required by {@link ConfigurationSerializable} in order to deserialize the object.
     */
    @SuppressWarnings("unused")
    public ToolRemoval(Map<String, Object> objectMap) {
        var map = SerializationUtil.mapOf(objectMap);
        removeUsed = map.getValueOrDefault("removeUsed", false);
        removeInvalidTools = map.getValueOrDefault("removeInvalidTools", false);
        removeInvalidBrushes = map.getValueOrDefault("removeInvalidBrushes", false);
    }

    public ToolRemoval() {
        removeUsed = false;
        removeInvalidTools = false;
        removeInvalidBrushes = false;
    }

    @Override
    @NotNull
    public Map<String, Object> serialize() {
        return SerializationUtil.newBuilder()
                .add("removeUsed", removeUsed)
                .add("removeInvalidTools", removeInvalidTools)
                .add("removeInvalidBrushes", removeInvalidBrushes)
                .build();
    }

    public boolean isRemoveUsed() {
        return removeUsed;
    }

    public boolean isRemoveInvalidTools() {
        return removeInvalidTools;
    }

    public boolean isRemoveInvalidBrushes() {
        return removeInvalidBrushes;
    }
}
