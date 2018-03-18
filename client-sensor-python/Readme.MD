# Python Temperature Sensor Client

A python script that is meant to run on a Raspberry Pi, reading values from a
DS18B20 temperature sensor and posting them to the temperature-server.

Contains both a simple python script that is the actual client as well as a second shell
script for starting the client in the background.

# Prerequisites

This script assumes that the sensor is properly set up and values can be read from
`/sys/bus/w1/devices/<sensor-id>/w1_slave`.

The script depends on the `requests` module, if you don't have that installed yet:

```
sudo apt-get install python3-pip
sudo pip3 install requests
```
