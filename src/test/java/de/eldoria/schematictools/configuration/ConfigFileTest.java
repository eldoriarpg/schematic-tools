/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) EldoriaRPG Team and Contributor
 */
package de.eldoria.schematictools.configuration;

import dev.chojo.ocular.Configurations;
import dev.chojo.ocular.dataformats.YamlDataFormat;
import dev.chojo.ocular.key.Key;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigFileTest {
    @TempDir
    Path folder;

    @Test
    void oldUpdateCheckBecomesNotice() throws IOException {
        Configurations<ConfigFile> configurations = load("updateCheck: false\n");

        assertFalse(configurations.main().updates().notice());
        assertFalse(configurations.main().updates().autoUpdate());
        assertTrue(configurations.main().updates().askForAutoUpdate());
    }

    @Test
    void oldUpdateCheckIsNotWrittenAgain() throws IOException {
        Configurations<ConfigFile> configurations = load("updateCheck: false\n");
        configurations.main();

        configurations.save();

        String written = Files.readString(folder.resolve("config.yml"));
        assertFalse(written.contains("updateCheck"));
        assertTrue(written.contains("notice: false"));
    }

    @Test
    void missingUpdatesUseDefaults() throws IOException {
        Configurations<ConfigFile> configurations = load("toolRemoval:\n  removeUsed: true\n");

        assertTrue(configurations.main().updates().notice());
        assertTrue(configurations.main().toolRemoval().isRemoveUsed());
    }

    private Configurations<ConfigFile> load(String content) throws IOException {
        Files.writeString(folder.resolve("config.yml"), content);
        return Configurations.builder(Key.builder(Path.of("config.yml"), ConfigFile::new).build(), new YamlDataFormat())
                .setBase(folder)
                .build();
    }
}
