/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) EldoriaRPG Team and Contributor
 */
package de.eldoria.schematictools.configuration;

import com.fasterxml.jackson.annotation.JsonSetter;
import de.eldoria.schematictools.configuration.elements.ToolRemoval;
import de.eldoria.schematictools.configuration.elements.UpdateSettings;

@SuppressWarnings("FieldMayBeFinal")
public class ConfigFile {
    private UpdateSettings updates = new UpdateSettings();
    private ToolRemoval toolRemoval = new ToolRemoval();

    public ToolRemoval toolRemoval() {
        return toolRemoval;
    }

    public void toolRemoval(ToolRemoval toolRemoval) {
        this.toolRemoval = toolRemoval;
    }

    public UpdateSettings updates() {
        return updates;
    }

    /**
     * Takes the update check switch of configurations written before 2.0.0 as the start value of
     * {@link UpdateSettings#notice()}. The old key is only read and never written again.
     *
     * @param updateCheck whether the old configuration looked for updates
     */
    @JsonSetter("updateCheck")
    private void legacyUpdateCheck(boolean updateCheck) {
        updates.notice(updateCheck);
    }
}
