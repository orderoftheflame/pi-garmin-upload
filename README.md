# pi-garmin-upload
Uploads from a connected Garmin GPS device to strava.
##Prerequisits
```
$ sudo apt-get install usbmount
```
## Setup
### Clone repo
Clone the repository into `/home/pi` (or another location and change paths in filepaths in files).
### Copy udev rules
Copy appropriate device udev rules files from `pi-garmin-upload/udev-rules` (or create your own with a new product ID found using `lsusb`) into:
```
/etc/udev/rules.d/
```
And then reload the udev rules with:
```
$ sudo udevadm control --reload-rules
```
