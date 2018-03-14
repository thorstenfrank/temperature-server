#!/usr/bin/python3

# #############################################################################
# This script is meant to be run on a raspberry pi, reading values from a
# DS18B20 temperature sensor and posting them to a temperature server.
#
# Please edit the variables as needed
# #############################################################################

import time
import requests

# Whatever you want your reading to be named
measurement_name = "Schlafzimmer"

# get this from folder /sys/bus/w1/devices - should be something cryptic
device_id = '28-a29133126461'

server_url = "http://192.168.11.18:8805"

# Checks temperature every n seconds
measurement_interval = 5

# you shouldn't have to change these values
device_file = '/sys/bus/w1/devices/' + device_id + '/w1_slave'

def read_temp_raw():
    f = open(device_file, 'r')
    lines = f.readlines()
    f.close()
    return lines

def read_temp():
    lines = read_temp_raw()
    while lines[0].strip()[-3:] != 'YES':
        time.sleep(0.2)
        lines = read_temp_raw()
    equals_pos = lines[1].find('t=')
    if equals_pos != -1:
        temp_string = lines[1][equals_pos+2:]
        return float(temp_string) / 1000

def upload_to_server(current_temp):
    complete_url = server_url + "/temperature/" + measurement_name + "?value=" + str(current_temp)
    requests.post(complete_url)

temp_last = 0.0
while True:
    temp_now = read_temp()

    if temp_now != temp_last:
        upload_to_server(temp_now)
        temp_last = temp_now

    time.sleep(measurement_interval)
