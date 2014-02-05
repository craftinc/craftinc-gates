# Craft Inc. Gates #

We are happy to finally announce __Craft Inc. Gates__. 

This awesome plugin lets you _travel_ to far away places and worlds _faster than light_! Just create a gate at any location and set an exit somewhere else.

This has been made available by the hard work of the research and development department of the _Craft Inc. Corporation_. Under the lead of Professor Ddidderr Craftman scientists worked years to find a way to bend time and space inside the Minecraft universe to enable _ultra fast transportation_.

Now it is time for _you_ to try out this wonderful plugin. Simply install, create a gate and feel the funny tickle inside your brain while traveling.*

The key features of this are:

* Dynmap integration
* Gates with and without frames
* Gates consisting of portal blocks and gates made of air (so called hidden gates)
* Gates with destinations in different worlds
* Gates with custom shapes (gates can look any way you want)
* Riding through gates


*The Craft Inc. Corporation won't take any responsibility for seasickness, memory loss and sudden suffocation in walls while traveling with one of our gates!

## FAQ ##

__Who can create a gate?__

Have a look at the [_Permissions_](http://dev.bukkit.org/bukkit-plugins/craftinc-gates/pages/permissions/) page.

__Who can destroy a gate?__

Anyone if you do not use a third-party protection plugin like Grief Prevention.

__Is there a IConnomy integration? Features for user to dial other gates etc?__

Nope. We currently don't plan to integrate such features. If you really need such an integration please inform us. If there are a lot of people requesting such features we might change our mind.

__Is there a list of all commands?__

Sure, type _/gate help_ in-game or have a look at the [_Commands_](http://dev.bukkit.org/bukkit-plugins/craftinc-gates/pages/commands/) page.

__When I destroy the frame of a gate it stops working. Shouldn't it still work?__

Yes and no. To make gates work without a frame you need to tweak the _checkForBrokenGateFrames_ setting. Have a look at the [_Configuration_](http://dev.bukkit.org/bukkit-plugins/craftinc-gates/pages/configuration/) page for more information.


## Usage ##
With this plugin you can create gates which will teleport players anywhere you want. The gates can look any way you like.

To make the gate work place yourself inside a newly created gate frame and type __/gate new [id]__. Afterwards walk to the destination of your gate and type __/gate exit [id]__ to set the destination. With __/gate open [id]__ you can get your newly created gate to work.

To hide a gate simply call __/gate hide [id]__. Now that gate won’t be made of purple portal blocks while open.

Have a look at the [_Commands_](http://dev.bukkit.org/bukkit-plugins/craftinc-gates/pages/commands/) page to find out how to modify gates even further.


## Installing ##

1. Download the latest release _[here](http://dev.bukkit.org/bukkit-plugins/craftinc-gates/files/)_
2. Delete any old versions of _Craft Inc. Gates_ (only the .jar files) including the extra dynmap-plugin for this plugin. 
3. Extract the content of the zip file into the plugins folder of your Bukkit server.
4. Start or reload the server.

## Craft Inc. ##
Check out our __[Craft Inc. Minecraft Server](http://www.craftinc.de)__. Everyone is welcome!

Also check out our other great plugins:

* [__Craft Inc. BorderProtection__](http://dev.bukkit.org/bukkit-mods/craftinc-borderprotection/)
protect your worlds with a border players cannot cross.

*  [__Craft Inc. Replicator__](http://dev.bukkit.org/bukkit-mods/craftinc-replicator/) 
allows players to build a replicator to replicate blocks and other items. (still experimental)

* __Craft Inc. Scarecrow__
coming soon!

## Roadmap ##
* Optionally allow animals and mobs to travel via gates.
* Allow players to use gates while sitting inside a minecart or boat
* Dynmap integration.
* Per player permissions for using and managing gates.
* Horizontal gates.

## Bugs and other Problems ##
Please use our [_issue tracker_](https://github.com/craftinc/craftinc-gates/issues?state=open) on GitHub.

## Legal Information ##
This project is a fork of the original [_Ancient Gates_](https://github.com/bladedpenguin/minecraft-ancient-gates). It is licensed under the [_LGPL_](http://www.gnu.org/licenses/lgpl-3.0.txt) just like the Bukkit project. Thanks to all current and previous [_contributors_](https://github.com/craftinc/craftinc-gates/blob/development/AUTHORS.txt).
The font used for the Craft Inc. Gates logo is called [_MineCrafter 3_](http://www.minecraftforum.net/topic/892789-minecrafter-3-font-simply-easy/) and has been made available under the creative commons license. Thanks to Asherz08, MadPixel and Ashley Denham for this great font.
This plugin utilizes [_Hidendra's plugin metrics system_](http://mcstats.org), which means that the following information is collected and sent to mcstats.org: a unique identifier, the server's version of Java, whether the server is in offline or online mode, the plugin's version, the server's version, the OS version/name and architecture, the core count for the CPU, the number of players online, the Metrics version. __You can disable the stat collection via /plugins/PluginMetrics/config.yml if you wish.__