/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) EldoriaRPG Team and Contributor
 */
package de.eldoria.schematictools.configuration;

import de.eldoria.schematictools.configuration.elements.ToolRemoval;

public class ConfigFile {
    private ToolRemoval toolRemoval = new ToolRemoval();

    public ToolRemoval toolRemoval() {
        return toolRemoval;
    }

    public void toolRemoval(ToolRemoval toolRemoval) {
        this.toolRemoval = toolRemoval;
    }
}
