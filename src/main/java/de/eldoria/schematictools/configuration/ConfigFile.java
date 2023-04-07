/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) EldoriaRPG Team and Contributor
 */
package de.eldoria.schematictools.configuration;

import de.eldoria.schematictools.configuration.elements.ToolRemoval;

@SuppressWarnings("FieldMayBeFinal")
public class ConfigFile {
    private boolean updateCheck = true;
    private ToolRemoval toolRemoval = new ToolRemoval();

    public ToolRemoval toolRemoval() {
        return toolRemoval;
    }

    public void toolRemoval(ToolRemoval toolRemoval) {
        this.toolRemoval = toolRemoval;
    }

    public boolean updateCheck() {
        return updateCheck;
    }
}
