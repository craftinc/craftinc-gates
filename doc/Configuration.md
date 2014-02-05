Starting with version 2.2.0 some features are customizable via a configuration file.

The configuration file will be automatically created on the the first startup after 
installing or updating the plugin.


The following keys and values are available:


* __maxGateBlocks__
A positive integer defining the maximum number of blocks a gate can consist of.
Note that increasing this value might slow down your server!


* __playerGateBlockUpdateRadius__
Defines the radius around a player where portal blocks are visible to that player. 
Adjust this value when increasing or decreasing the view-distance on the server. 
Only positive integer values are allowed.


* __checkForBrokenGateFrames__
Allowed values are _true_ and _false_ only. Setting this value to _false_ will disable 
all checks for broken frames for non-hidden gates. Disabling frame block checks 
might increase you server performance.


* __saveOnChanges__
Allowed values are _true_ and _false_ only. Disabling _save on changes_ might
increase server performance but gates will only be saved to disk when the plugin
gets disabled! This might lead to data loss on error.


* __gateTeleportMessage__
A string value going to displayed every time when a player travels using a gate. Will 
only be displayed if _showTeleportMessage_ is set to _true_.


* __showTeleportMessage__
A boolean (_true_ or _false_) determining wether the _teleport message_ will 
be displayed.


* __gateTeleportNoPermissionMessage__
A string value going to displayed every time when a player enters a gate and is not allowed to use that gate. Will only be displayed if _showTeleportNoPermissionMessage_
is set to _true_.


* __gateTeleportVehicleNotAllowedMessage__
A string value being displayed when a player tries to go through a gate while riding when riding through this gate is disabled. Will only be displayed if _showTeleportNoPermissionMessage_ is set to _true_.

* __showTeleportNoPermissionMessage__
A boolean (_true_ or _false_) determining wether the _no permission message_ will 
be displayed.


* __gateMaterial__
A String representing the material all gates will consist of. Have a look at our [_Gate Material Page_](http://dev.bukkit.org/bukkit-plugins/craftinc-gates/pages/gate-materials/)  for all possible values.
