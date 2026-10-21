/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) EldoriaRPG Team and Contributor
 */
package de.eldoria.schematictools.configuration.elements;

@SuppressWarnings("FieldMayBeFinal")
public class ToolRemoval {
    private boolean removeUsed = false;
    private boolean removeInvalidTools = false;
    private boolean removeInvalidBrushes = false;

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
