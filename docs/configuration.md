---
sidebar_position: 4
---

# Configuration

Schematic Tools keeps its files in `plugins/SchematicTools/`.
Each file is created with default values the first time the add-on needs it.

| File | What it holds |
|---|---|
| `config.yml` | Tool removal and update settings |
| `tools.yml` | All tools you created |
| `base_configuration.yml` | Log level and version bookkeeping |

The add-on reads its files on startup. It writes them whenever a command changes a tool, and again when it shuts down.
To edit a file by hand, stop the server first, then start it again. Changes made while the server runs are overwritten.

## config.yml

This is the file with all defaults:

```yaml
updates:
  notice: true
  autoUpdate: false
  askForAutoUpdate: true
toolRemoval:
  removeUsed: false
  removeInvalidTools: false
  removeInvalidBrushes: false
```

### toolRemoval

These settings decide whether an item is taken away from the player when it can no longer work.
With the default `false`, the item stays in the inventory and simply does nothing.

| Key | Default | What it does |
|---|---|---|
| `removeUsed` | `false` | Remove an item once its usages are used up. |
| `removeInvalidTools` | `false` | Remove an item whose tool was deleted with `/sbt remove`. The player sees "Your tool broke." |
| `removeInvalidBrushes` | `false` | Remove an item whose saved brush no longer exists. The player sees "Your tool broke." |

The check runs when the player selects the item, and for `removeUsed` also right after the last paste.

### updates

The add-on looks for new versions every six hours.

| Key | Default | What it does |
|---|---|---|
| `notice` | `true` | Tell the console and admins when a new version is out. |
| `autoUpdate` | `false` | Download new stable versions on its own. They take effect on the next start. A new major version is never downloaded on its own. |
| `askForAutoUpdate` | `true` | Ask admins in chat whether to switch `autoUpdate` on. |

When both `notice` and `autoUpdate` are `false`, the add-on does not look for new versions at all.

Admins with `schematictools.updates` see the notice when they join. The notice buttons run `/schematictools:eldo-update`, see [Commands and permissions](./commands.md).
Choosing "enable-auto" sets `autoUpdate` to `true`. Choosing "never" sets `askForAutoUpdate` to `false`.

Older versions used a single `updateCheck` switch. If it is still in the file, it is read once as the value of `notice` and then removed.

## tools.yml

This file holds your tools. Manage them with the [commands](./commands.md) rather than by hand.

| Key | What it holds |
|---|---|
| `tools` | The list of tools. |
| `currentId` | The ID the next new tool gets. Do not change it. |

Each tool in the list has these keys:

| Key | What it holds |
|---|---|
| `id` | Number that links items to this tool. Items only store this number. |
| `name` | Name of the tool, used in commands. |
| `owner` | UUID of the player who owns the saved brush. |
| `brushName` | Name of the saved brush. |
| `permission` | Permission needed to use the tool. When empty, `schematictools.use` is used. |
| `usages` | How often each item can paste. `-1` means no limit. |

:::warning
Do not change or reuse an `id`. Items that are already bound would point at the wrong tool.
:::

## base_configuration.yml

| Key | Default | What it does |
|---|---|---|
| `logLevel` | `INFO` | How much the add-on writes to the console. |
| `version` | set by the add-on | Version of the config layout. Do not change it. |
| `lastInstalledVersion` | set by the add-on | Add-on version that wrote the file. Do not change it. |
