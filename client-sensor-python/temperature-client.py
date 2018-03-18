#!/usr/bin/python3

# #############################################################################
# This script is meant to be run on a raspberry pi, reading values from a
# DS18B20 temperature sensor and posting them to a temperature server.
#
# Please edit the variables as needed
# #############################################################################

import datetime
import time
import requests

# Whatever you want your reading to be named
measurement_name = "Bedroom"

# get this from folder /sys/bus/w1/devices - should be something cryptic
device_id = '28-abcdefghijk'

# change this to wherever your temperature-server is running
server_url = "http://localhost:8080"

# Checks temperature every n seconds
measurement_interval = 60

# you shouldn't have to change these values
device_file = '/sys/bus/w1/devices/' + device_id + '/w1_slave'

# Open the temperature sensor file and return its contents
def read_temp_raw():
    f = open(device_file, 'r')
    lines = f.readlines()
    f.close()
    return lines

# Read the temperature from the sensor file, parse the contents and convert
# the sensor data into a floating point value
def read_temp():
    lines = read_temp_raw()
    while lines[0].strip()[-3:] != 'YES':
        time.sleep(0.2)
        lines = read_temp_raw()
    equals_pos = lines[1].find('t=')
    if equals_pos != -1:
        temp_string = lines[1][equals_pos+2:]
        return float(temp_string) / 1000

# Post the supplied temperature using the defined server URL, measurement name
# Errors while posting will be caught and printed
def upload_to_server(current_temp):
    complete_url = server_url + "/temperature/" + measurement_name + "?value=" + str(current_temp)
    try:
        requests.post(complete_url)
    except OSError as err:
        print(datetime.datetime.now())
        print("Error ocurred while posting to server: {0}".format(err))

# Main script: read temperature value, compare it to the previous one, and if
# different, post it to the server
# Finally, sleep for the configured amount of time and do it all over again.
temp_last = 0.0
while True:
    temp_now = read_temp()

    if temp_now != temp_last:
        upload_to_server(temp_now)
        temp_last = temp_now

    time.sleep(measurement_interval)
