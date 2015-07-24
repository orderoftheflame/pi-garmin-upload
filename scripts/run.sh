#!/bin/bash
#FOLDER="/media/fr15";
FOLDER=$1;
sleep 5;
/home/pi/pi-garmin-upload/scripts/upload.sh $FOLDER;
