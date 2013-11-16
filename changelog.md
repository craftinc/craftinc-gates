## 2.3.0 ##
* Added a command for setting the exit and opening a gate at once.
*  Enabled the ability to change the gate block material.
*  Added a command printing all nearby gates while highlighting them.
*  Changed the info command to highlight gates.
*  Updated the info command to print information about the nearest gate if no gate name got supplied.

## 2.2.1 ##
* Changed priority of some event listeners to solve problems with WorldGuard and other protection plugins.

## 2.2.0 ##
* Improved gate commands and shortcuts (have a look at the bukkit-dev page for more information).
* Improved overall performance.
* Added a configuration file (have a look at the bukkit-dev page for more information).
* Resolved issues with (random) teleports to the nether.
* Made it possible to create non hidden gates without a frame. (Turned off by default!)
* Changed the behavior regarding portal blocks. Starting with this version no blocks will be set by the plugin. All portal blocks will only be visible on client side.
* Added checks preventing the plugin from overwriting the gate storage file on error.
* Added the ability to change and disable messages on teleport and insufficient permissions via a config file.

## 2.1.2 ##
* Fixed a bug where players got teleported one block beside the real portal.
* Fixed a bug where gates with no location caused multiple exceptions.

## 2.1.1 ##
* Made the list command more reliable.
* Error messages will be displayed less frequent.

## 2.1.0 ##
* Command outputs are now colored.
* Fixed a bug where players in creative mode would not be teleported correctly.
* Made various commands available via the server console.
* Simplified command names.
* Improved permissions handling. (Added soft dependency for Vault.)
* A message will be displayed after a player has been teleported. There is also a message displayed if a player is not allowed to use a gate.

## 2.0.1a
* Fixed broken import for gates created with version 2.0.0

## 2.0.1
* Updated permissions to allow everyone to use gates by default.
* Fixed a bug where yaw had not been taken correctly into account while teleporting.
* Re-added persistence of yaw and pitch of gate locations and exits.

## 2.0.0
Initial release under new name