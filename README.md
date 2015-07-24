# pi-garmin-upload
Uploads from a connected Garmin GPS device to strava.
##Register with strava API
Create an Application at https://www.strava.com/settings/api, then generate an oauth token code by using the following:
```
https://www.strava.com/oauth/authorize?client_id={app_id}&response_type=code&redirect_uri={callback url}&approval_prompt=force&scope=write  
```
The OAUTH code is returned in the callback url after allowing access on this screen. e.g. `code=60509bc59d76dac4897df0a78f2b9983948fabc`

This code, the application id, and the client secret key (both available on the Strava api settings page) should be added to the `api.properties` file.
```
api.clientid=1234
api.secret=632c7a0023024997ebe515583c72391700badabc
api.code=60509bc59d76dac4897df0a78f2b9983948fabc
```
##Mounting device
Find the device(s) using `blkid` and create a new entry in `/etc/fstab` using your editor of choice.
```
/dev/sdb        /media/fr15     vfat    defaults          0       0
```
Once this is done, mount the device using `sudo mount -a`.
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
