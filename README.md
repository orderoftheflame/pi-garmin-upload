# pi-garmin-upload
Uploads from a connected Garmin GPS device to strava.

Currently the udev and properties file support the Edge 500 and Forerunner 15 devices, but adding new devices should be simple.

Paths should be fine if the project is extracted to `/home/pi/pi-garmin-upload`, but scripts files can be modified to point anywhere.

##Register with strava API
Create an Application at https://www.strava.com/settings/api, then generate an oauth token code by using the following:
```
https://www.strava.com/oauth/authorize?client_id={app_id}&response_type=code&redirect_uri={callback url}&approval_prompt=force&scope=write  
```
The OAUTH code is returned in the callback url after allowing access on this screen. e.g. `code=60509bc59d76dac4897df0a78f2b9983948fabc`
This code, the application id, and the client secret key (both available on the Strava api settings page) should be added to the `api.properties.template` file. To work with the default setup, copy the file to `/home/pi/api.properties`.
```
api.clientid=1234
api.secret=632c7a0023024997ebe515583c72391700badabc
api.code=60509bc59d76dac4897df0a78f2b9983948fabc
```
You'll notice there are some device properties in this file as well. These lines represent the folder structure on the device (as annoyingly they don't appear to be consistent across devices, this could be a Forerunner/Edge difference though).

The last part of the property should match the last part of the device path.
##Mounting device
Find the device(s) using `ls -l /dev/disk/by-label` and create a new entry in `/etc/fstab` using your editor of choice.
```
LABEL=GARMINFR15         /media/fr15     vfat    defaults          0       0
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
