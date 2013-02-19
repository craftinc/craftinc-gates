# Craft Inc. Gates – Easily create portals with custom design

(previously known as __AncientGates__)

With this plugin players can create gates that will teleport anyone who enter the gate to specific a location.
The hightlights are: __It is so darn easy to use!__ and __The gates can look any way you like__

Try the ingame command: __/gate__

Also have a look at the full __[userguide](http://www.craftinc.de/blog/?p=255)__.

Check out our **[Craft Inc. Minecraft Server](http://www.craftinc.de)**! Everyone is welcome!

Thought first you should take a look at the demonstration oloflarsson and karibu6 created:
__[http://www.youtube.com/watch?v=L4hyqTpeEaA](http://www.youtube.com/watch?v=L4hyqTpeEaA)__


## FAQ

__Who can create a gate?__

See the _Permissions_ section.

__Who can destroy a gate?__

Anyone if you do not use a third-party protection plugin like Grief Prevention.

__Are there IConnomy integration, Features for user to dial other gates etc?__

Nope. This plugin is very minimalistic and plain. Server operators manage the portals players use them any time they are open.

## Usage

With the __/gate__ plugin you can create gates which will teleport players anywhere you want. Just build a portal (like those nether portal). The gates can look any way you like.

To make the gate work place yourself in a newly created gate frame and type __/gate create [id]__. Afterwards walk to the destination of your portal and type __/gate exit [id]__ to set the destination. With __/gate open [id]__ you can get your newly created gate to work.

To hide a gate simply call __/gate hide [id]__. Now that gate won’t have purple blocks when open. You can even remove the frame without stopping the hidden gate from working. But unhiding a gate without a frame is not possible!


Use the following commands to modify your gates even further:
__(Note that the following commands will be made available with the upcoming 2.1 release of Craft Inc. Gates! Meanwhile type '/gate help' while playing to see the current set of commands!)__

* __/gate close,c [id]__
Closes a gate to prevent players from using it.

* __/gate create,new [id]__
Creates a gate at your current location.

* __/gate delete,del,remove,rm [id]__
Removes the gate from the game.

* __/gate exit,e [id]__
Changes the location where the gate will teleport players to your current location.

* __/gate help,h,? [page]__ 
Prints the help pages

* __/gate hide,h [id]__
Makes a gate NOT consist of gate blocks while open.

* __/gate info,details,i,d [id]__
Prints detailed informations about a certain gate.

* __/gate list,ls [page]__
Prints all availiable gates.

* __/gate location,l [id]__
Sets the entrance of the gate to your current location.

* __/gate rename,changename,cn__ [current name] [new name]
Changes the name/id of the gate.

* __/gate unhide,uh [id]__
 Makes that gate visible.



## Permissions

* __craftincgates.info__
Gives access to info and list commands.

* __craftincgates.use__
Allows you to travel via gates.

* __craftincgates.manage__
Gives access to commands manipulating gates.

## Installing

1. Download the latest release: __[http://dev.bukkit.org/server-mods/craftinc-gates/files/](http://dev.bukkit.org/server-mods/craftinc-gates/files/)__
2. Put the downloaded _CraftIncGates.jar_ in the plugins folder.
3. Start or reload the server.

## Bugs and other Problems

Please use our [issue tracker](https://github.com/craftinc/craftinc-gates/issues?milestone=1&state=open) on github.


## License

This project has a LGPL license just like the Bukkit project.