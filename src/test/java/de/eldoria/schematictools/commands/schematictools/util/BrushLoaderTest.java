/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) EldoriaRPG Team and Contributor
 */
package de.eldoria.schematictools.commands.schematictools.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BrushLoaderTest {
    @Test
    void globalBrushIsLookedUpWithoutItsPrefix() {
        assertEquals("forest", BrushLoader.storedName("g:forest"));
    }

    @Test
    void playerBrushKeepsItsName() {
        assertEquals("forest", BrushLoader.storedName("forest"));
    }

    @Test
    void onlyTheLeadingPrefixIsRemoved() {
        assertEquals("big:g:forest", BrushLoader.storedName("big:g:forest"));
    }
}
