# FlowwSmoke

![](https://img.shields.io/badge/Minecraft%20Version-1.19.2-brightgreengreen)
![](https://img.shields.io/badge/Requires%20Java-17-green)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

FlowwSmoke is a Java Minecraft/Spigot plugin for placing fireplace-like particle spawners anywhere.

![](./smoke.gif)

## Installation

Download the latest artifact from [GitHub Actions](https://github.com/KapitanFloww/FlowwSmoke/actions/workflows/maven.yml).
Extract the zip-file, move the `FlowwSmoke-*.jar` to your servers `plugin` directory and start the server.

## Usage

| Command               | Permission  | Description                                             |
|-----------------------|-------------|---------------------------------------------------------|
| `/smoke`                | `floww.smoke` | Place a new smoke location at your current target block |
| `/smoke help`           | `floww.smoke` | Open a help page                                        |
| `/smoke list [world]`   | `floww.smoke` | List all smokes [in given world]                        |
| `/smoke remove`         | `floww.smoke` | Remove all smokes locations                             |
| `/smoke remove (id)`    | `floww.smoke` | Remove smoke location with given id                     |
| `/smoke remove [world]` | `floww.smoke` | Remove all smoke locations of given world               |


## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
Please make sure to update tests as appropriate.