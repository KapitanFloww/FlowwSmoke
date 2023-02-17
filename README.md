# FlowwSmoke

![](https://img.shields.io/badge/Version-1.1.1-lightgrey?style=for-the-badge)
![](https://img.shields.io/badge/Minecraft-1.19.3-brightgreengreen?style=for-the-badge)
![](https://img.shields.io/badge/Java-17-green?style=for-the-badge)
[![GitHub Releases](https://img.shields.io/badge/Github-Releases-yellowgreen?style=for-the-badge)](https://github.com/KapitanFloww/FlowwSmoke/releases)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg?style=for-the-badge)](https://www.gnu.org/licenses/gpl-3.0)

FlowwSmoke is a Java Minecraft/Spigot plugin for placing fireplace-like particle spawners anywhere.

<img src="./smoke.gif" width="100%" alt="Placing smoke particles on a chimney">

## Installation :hammer:

### Download the latest release version
You can always download the latest release version from [GitHub Releases](https://github.com/KapitanFloww/FlowwSmoke/releases)

### Build the plugin yourself
You can also build the plugin yourself.
Clone this repository using 
```shell
git clone https://github.com/KapitanFloww/FlowwSmoke.git
``` 
and execute the following command inside the projects root folder:
````shell
mvn clean package
````
Then, simply copy the `FlowwSmoke-*.jar` from the `/target`-folder to your servers `plugins`-folder and start your server.

## Usage :bulb:

### Commands

The following commands are available in-game:

| Command                     | Permission    | Description                                             |
|-----------------------------|---------------|---------------------------------------------------------|
| `/smoke`                    | `floww.smoke` | Place a new smoke location at your current target block |
| `/smoke help`               | `floww.smoke` | Open a help page                                        |
| `/smoke list [world]`       | `floww.smoke` | List all smokes [of given world]                        |
| `/smoke remove (id)`        | `floww.smoke` | Remove smoke location with given id                     |
| `/smoke remove all [world]` | `floww.smoke` | Remove all smokes locations [of given world]            |

> `()`-args are **required**, while `[]`-args are optional :mag_right:

### Configuration

You can change the plugins prefix using the config file:

```yml
prefix: §7[§aFloww§4Smoke§7] # Use color codes in with '§'
```

## Contributing & Bugs :beetle:
If you find a bug :bug:, simply open a new issue in GitHub and describe what's wrong.

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
Please make sure to update tests as appropriate.
