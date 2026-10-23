/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) EldoriaRPG Team and Contributor
 */
package de.eldoria.schematictools.commands.schematictools;

import de.eldoria.eldoutilities.commands.exceptions.CommandException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ModifyTest {
    @Test
    void allFieldsAreSuggested() {
        assertEquals(List.of("brushName", "name", "usages", "permission"), Modify.completeField(""));
    }

    @Test
    void fieldsAreFilteredIgnoringCase() {
        assertEquals(List.of("brushName"), Modify.completeField("BR"));
        assertEquals(List.of("permission"), Modify.completeField("p"));
    }

    @Test
    void usagesSuggestUnlimitedAndTheMinimum() throws CommandException {
        assertEquals(List.of("-1", "1<"), Modify.completeUsages(""));
        assertEquals(List.of("-1"), Modify.completeUsages("-"));
        assertEquals(List.of("1<"), Modify.completeUsages("5"));
    }

    @Test
    void usagesBelowOneAreRejected() {
        assertThrows(CommandException.class, () -> Modify.completeUsages("0"));
    }
}
