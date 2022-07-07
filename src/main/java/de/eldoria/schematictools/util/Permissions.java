/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.schematictools.util;

public class Permissions {
    private static final String BASE = "survivalschematicbrush";

    private static String perm(String... perms) {
        return String.join(".", perms);
    }

    public static final String USE = perm("use");
}
