#!/bin/bash
#FOLDER="/media/fr15";
FOLDER=$1;
CONFIG=$2;
sleep 5;
/home/pi/pi-garmin-upload/scripts/upload.sh $FOLDER $CONFIG;
