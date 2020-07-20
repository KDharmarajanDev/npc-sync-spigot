# npc-sync-spigot

This is a Minecraft plugin designed to sync a player's movements across multiple servers in a BungeeCord network. This plugin uses NPCSyncBungee to relay the packets to the other servers.

## Installation

1. Download the latest release jar from the Releases tab.

2. Drop the jar file into the `/plugins` folder in the main server directory.

## Usage

1. Type `/npcsync start (player name)` to start syncing a player

2. If you wish to list the currently synced players, type `/npcsync list`

3. When it is undesirable to sync a player type `/npcsync cancel (player name)`

4. Ensure that the user has the permission `NPCSync.npsync` to successfully execute commands.

## License
This project uses the MIT License.
