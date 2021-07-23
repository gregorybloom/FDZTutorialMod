# YouTubeModding14 fork
This is a spinoff of McJty's youtube tutorials.  The purposes of this project is to elaborate the dimension-creation code and subsequent worldgen.  I will put up notes and sample code here as I can, partly for my own edification.

See the McJty tutorials and code at:


https://github.com/McJty/YouTubeModding14


## **Important Note**

**This is a continuation/spinoff of McJty's tutorials.  It assumes you have completed those tutorials, so start there.** 

Other skills you should need:

1) Learning the basics of JSON
2) learning how to read `run/logs/latest.log`

**I have almost zero experience modding minecraft.**  If you have questions, there are two discords that have experienced users: Modded Minecraft, and Minecraft Modded Development.  A search for these discords will help you.

<br>

# Resources

I'm working with ~1.17.1, keeping up to date as much as I can.  With the release of 1.16.2, mojang started JSON-driven worldgen.

Resources to help with this JSON-driven worldgen are below.

<hr>

Forge, the leading framework(?) for modding minecraft:<br>
https://files.minecraftforge.net/net/minecraftforge/forge/

From the MMD Discords:

Wiki on creating dimension json files:<br>
https://minecraft.gamepedia.com/Custom_dimension

Wiki on creating biome json files:<br>
https://minecraft.fandom.com/wiki/Biome/JSON_format

Wiki on all other worldgen json files:<br>
https://minecraft.fandom.com/wiki/Custom_world_generation


Awesome tool to make creating worldgen json files much easier! Check it out!<br>
https://misode.github.io/

Here's a datapack of the entire vanilla worldgen as a json datapack. This was created by mojang for 1.16.2+ as a great working example of the json worldgen system<br>
https://cdn.discordapp.com/attachments/750505199415590942/779200847212576798/vanilla_wordgen.zip

<hr>

<br>

# Introduction

As said, this is to elaborate on McJty's dimension code to build out a proper world generation mod.

To start off, this will focus on the minimal and simplest code necessary in Tutorial Dimension 1, which will use the JSON worldgen definitions.


There is too much in worldgen for me to directly break it down at once.  I start with doing initial setup using the basics of JSON driven worldgen and elaborate over the steps.

<br>

# Tutorial Dimension 1 - WorldGen via JSON

## Dimensions

### **Dimension Setup**

The path and naming of your custom JSON files matter.  The folder path will begin like `resources.data.<modid>`, followed by the folders for that particular JSON resource.

McJty's code includes Key Registry for the tutorial dimension, but this is only used for the custom Tp command he provides.  We've commented out the teleport code as well as the Key Registry for the dim (and did some renaming for it and the dimension type).

The dimension code is located in:
* [resources.data.fdztutorialmod.dimension](https://github.com/gregorybloom/FDZTutorialMod/tree/main/src/main/resources/data/fdztutorialmod/dimension)

In this case we use a JSON value of `generator.biome_source.type = "minecraft:checkerboard"` to make it easy to fly around in creative and glance at everything.

And technically we are done!  Yes, just adding the JSON 
<br>

For a first test, feel free to edit the `tutdim1.json` JSON to include other minecraft standard biome types.  The Biome list is inside the JSON object at `generator.biome_source.biomes`

You should see this naming convention a lot in modding minecraft: "\<modid\>:name"

> Ex: "minecraft:dark_forest", "minecraft:badlands", etc

Try adding your own "minecraft:" biomes from the [complete list](https://github.com/gregorybloom/FDZTutorialMod/blob/main/readme/reference/biome_list.txt)

### **Dimension Wrapup**

With just a couple of minecraft biomes, two simple JSON files, code from ModDimensions (and code elsewhere in setup to include ModDomensions), you are finished specifying the basics of a new multi-biome dimension.

Visit it with:
> `/execute in yourmodid:dimension_id run tp @a ~ ~ ~`

<br>

## Dimension Type and Noise

### **Dimension Type**

If you are confused at how to include the custom biomes, dimension types, and so on, take a look at `tutdim1b.json`.  

Here we set a custom dimension type.

* [resources.data.fdztutorialmod.dimension_type](https://github.com/gregorybloom/FDZTutorialMod/tree/main/src/main/resources/data/fdztutorialmod/dimension_type)

I strongly suggest using the wikis provided in Resources to familiarize yourself with the content to get an idea of what appears in Dimension Type.

### **Dimension Noise**



<br>

## Biomes

### **Biome Setup**

Biome JSON can be set up without any loading code. The most basic sample biome is the `tut1b_biome1_mcdef_red.json`, found here:

* [resources.data.fdztutorialmod.worldgen.biome](https://github.com/gregorybloom/FDZTutorialMod/tree/main/src/main/resources/data/fdztutorialmod/worldgen/biome)

You can add it to the biomes in `tutdim1.json`.  It does not define any unique features, but creates a grim red biome that you should be able to fly to.  

> Try visiting your new dimension and flying around to find it.

With this you've added a new unique biome to your dimension!

### **Monster Spawning**

Biome spawning behavior does not use a separate JSON file.  Their data is found in the JSON biome at `.spawners`, and has  six arrays: `monster`, `creature`, `ambient`, `water_creature`, `water_ambient`, `misc`.

An example of what might be contained in your `.spawner.monster` JSON block could look like this. 

    [
          {
            "type": "minecraft:spider",
            "weight": 100,
            "minCount": 4,
            "maxCount": 4
          },
          {
            "type": "minecraft:zombie",
            "weight": 95,
            "minCount": 4,
            "maxCount": 4
          } 
    ]

The provided data pack in resources can be helpful here if you want to mimic minecraft spawn distribution.

The `.spawn_costs` JSON data isn't actually set in the default files, so I am leaving this blank (feel free to research it youself!).

## Biome Carvers

### **Adding Carvers** ###
Carvers have incredibly simple JSON files, like [the example here (within path `resources.data.fdztutorialmod.worldgen.configured_carver`)](https://github.com/gregorybloom/FDZTutorialMod/blob/main/src/main/resources/data/fdztutorialmod/worldgen/configured_carver/fdz_conf_basic_carver.json).  

There are only six in default Minecraft (as seen in the downloadable resource).  If you create a custom-carver or wish to change the ones in the biome, they are in the JSON at `.carvers` and cover two types: `air` and `liquid`.

You can see an example of this at the bottom of the `tut1b_biome1_mcdef_red.json` file ( in `resources.data.fdztutorialmod.worldgen.biome`).

Carvers often create features that cross biome borders, so it is useful to have the same carvers for biomes in the same dimension.

> "minecraft:cave" and "minecraft:canyon" are found in nearly overworld biomes for its air carvers.
> However, the ocean biomes use "minecraft:ocean_cave" instead for its air-cave carver, and it uses liquid carvers: 
> * "minecraft:underwater_canyon"
> * "minecraft:underwater_cave"

You can add a canyon carver to `tut1b_biome1_mcdef_red.json`, or make a custom carver with a high occurrence rate, and then run this to see all the canyons you'll get.

### **Configured Carvers** ###

Carver JSON specification is extremely simple.  A `type` specification invokes the name of the **CarverBiome** code being used.  Probability gives the odds it will be used within the chunk.

Once again, building custom JSON is easily done using this resource:
https://misode.github.io/worldgen/carver/

Custom carver code is complex, and is best addressed in other tutorials.

## Surface Builders

### **Configured Surface Builders**

The `.surface_builder` value in the Biome JSON usually is a single string, naming a desired default or custom `worldgen.configured_surface_builder` resource.

> You can also embed a custom configured surface builder as well, and an example of this is in `tut1b_biome1_mcdef_cyan.json` (at [`resources.data.fdztutorialmod.worldgen.biome`](https://github.com/gregorybloom/FDZTutorialMod/tree/main/src/main/resources/data/fdztutorialmod/worldgen/biome))

There are many surface builders in default Minecraft, usually paired with a related biome.  There are some example surface builder JSONs at:<br>
[`resources.data.fdztutorialmod.worldgen.configured_surface_builder`](https://github.com/gregorybloom/FDZTutorialMod/tree/main/src/main/resources/data/fdztutorialmod/worldgen/configured_surface_builder)

In the three examples, the primary notable trait is the `.type` value.  This references what **Surface Builder** is called from the code.  One pulls the default Surface Builder, the others pull custom ones set up for the Tutorial Dimension 1.

### **Configured vs Surface Builders**

What's the difference between a Surface Builder and a Configured Surface Builder?

Configured Surface Builders define the blocks that the Surface Builder will actually use.  The JSON file defines which Surface Builder (in code) will actually use it.

Biome JSON files then, call the Configured Surface Builder JSON name they want to use, and that one will name which Surface Builder it wants.

## Features

### **Configured x**






<br>

<br>

## Creating Features (JSON)


### **Configured Features Pieces**

A feature object in the *configured_feature JSON* files have a `type` and a `config`.  The `type` is the feature, and the `config` is how to configure that feature.  It takes a `decorator` and a `feature`.

All "minecraft:decorated" does is take a `decorator` by `config` and then run that decorator to get a new position.  It then feeds that new position to the feature.

With the haystack example, you end up with a series of decorators to establish the position you want to place at, and then you provide a feature to put there.

### **Creating Haystack Example**

So creating custom features is a little more complicated.  In this example, we implement the 'pile_hay' that's connected to the village structure spawning and instead have it generate in the world.

Below is a link to the feature, and an image of how it would appear in the [Feature Generator](https://misode.github.io/worldgen/feature/).

[data.fdztutorialmod.worldgen.configured_feature.fdz_testing_haystack.json](https://github.com/gregorybloom/FDZTutorialMod/blob/main/src/main/resources/data/fdztutorialmod/worldgen/configured_feature/fdz_testing_haystack.json)

![Haypile Ref](https://github.com/gregorybloom/FDZTutorialMod/blob/main/readme/imgs/pile_hay_ref.PNG?raw=true)

The `count` is the outermost **Decorator** and picks how many positions to attempt in the chunk.  (You can play with the Feature Generator to see the various types of `count` decorators).

The `square` **Decorator** then randomizes the positions to an x/z spot in the chunk.

The `heightmap` snaps all the positions to the terrain.

And lastly, the "minecraft:pile_hay" is a reference to the **Configured Feature** to actually place at that position.

Thus, this **Configured Feature** sets up the position for the spawning of another **Configured Feature**.

You can read more about the config properties here:<br>
https://minecraft.fandom.com/wiki/Configured_feature






<hr>

<hr>


### **Biome Generated Content**

Biome JSON files have a lot more elements than dimensions.  The wiki and github tool in **Resources** should make it much easier to understand the individual biome elements.  But there are still two 


.
.
.
. (more to come here) . . . .

<hr>

<hr>

### **"Ten Phases of Biome Feature Generation"**

Biome JSONs define features in an "array of arrays", allowing each list to be executed in distinct steps.  Minecraft organizes features this way into ten arrays held by `features`.  

The below reference list is taken from Minecraft Overworld Biomes, and should give you an idea of what features are typically generated in each 'phase'.

[Full phase-sorted lists are here!](https://github.com/gregorybloom/FDZTutorialMod/tree/main/readme/reference/featurelists/)

1) End Island Decorations
2) Surface Lakes
> `minecraft:lake_water` <br> `minecraft:lake_lava`
3) Rock Protrusions and Pillars
> `minecraft:forest_rock` <br> `minecraft:iceberg_packed`
4) Fossils and 'Monster Rooms'
> `minecraft:monster_room`
5) Certain special features
> `minecraft:blue_ice` <br> `large_basalt_columns`
6) _currently unused_
7) Ore Generation (including patches of dirt, clay, etc)
> `minecraft:ore_dirt` <br> `minecraft:ore_andesite` <br> `minecraft:ore_iron` <br> `minecraft:disk_sand`
8) Nether Ore Gen (glowstone, quartz, soul fire flames)
> `minecraft:glowstone` <br> `minecraft:ore_quartz_nether`
9) Vegetation Patches, Water/Lava Springs
> `minecraft:plain_vegetation` <br> `minecraft:patch_sugar_cane` <br> `minecraft:spring_lava`
10) Limited Final Features (void start platform, top layer freeze?)
> `minecraft:freeze_top_layer`

<br>

## Dimension Noise Settings

### **Noise Settings Setup**
...
<br>

