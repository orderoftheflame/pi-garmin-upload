#!/bin/bash
#FOLDER="/media/fr15";
FOLDER=$1;
sudo mount $FOLDER;
sleep 5;
sudo tree $FOLDER | mail -s "Device connect test" ben@ootf.co.uk;
