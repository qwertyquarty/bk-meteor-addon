# Bk Meteor Addon

This meteor addon adds a few useful commands and modules, with some features for the mineplay server (mc.mineplay.nl).

## Commands

 - `.locate-player`: Will temporarily show a tracer to the player for 5 seconds.
 - `.network-online`: Shows online players on the network, gets settings from and requires NetworkLoginLogoutNotifier (for mineplay, also may work on other server networks).
 - `.vivecraft-vanished`: Shows vanished players on the server that have vivecraft, gets settings from and requires VivecraftVanishDetect (the server and the player vanishing must have vivecraft).

## Modules

 - `PlayerEsp`: Esp for specific players.
 - `PlayerTracers`: Tracers for specific players.
 - `PlayerLoginLogoutNotifier`: Notifies you when a player logs in or out.
 - `MineplayRemoveOfflineRobloxPlayers`: Removes offline roblox players (for mineplay).
 - `MineplayBetterBreak`: Improves block breaking by making it creative-like (for mineplay).
 - `NetworkLoginLogoutNotifier`: Notifies you when a player logs in or out of the network (for mineplay, also may work on other server networks).
 - `BadWordFinder`: Finds bad words in chat messages and nearby signs.
 - `VivecraftVanishDetect`: Detects if a player is in vanish mode using /vr list (the server and the player vanishing must have vivecraft).
