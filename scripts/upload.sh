#!/bin/bash
#FOLDER="/media/fr15";
FOLDER=$1;
CONFIG=$2;
sudo mount $FOLDER;
sleep 5;
sudo java -jar /home/pi/pi-garmin-upload/release/pi-garmin-upload.jar $FOLDER /home/pi/$CONFIG.properties;
