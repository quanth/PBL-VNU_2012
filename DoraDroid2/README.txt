DoraDroid 

This is an Android program as Step 1 of DORA project.
It does the same tasks that RobotSample and RobotSampleAndroid do.

How to build:
===========
The project is built independently, 
no additional project is required. 
All necessary libs are already included. 
There might be some unnecessary, I haven't checked yet.

How to run:
============
1. Run ServiceSample
2. Run DoraDroid. it will connect to ServiceSample via Internet (IP address is hardcoded) and ask user to confirm robot bluetooth address before connecting to the robot via Bluetooth (should it be hardcoded? I'm not sure).
3. Use robotcamera, contentsupload, robotcontrol pages from a browser to control the robot (the same way we deal with RobotSample and RobotSampleAndroid)

To-do's
========
- new logo to replace MINDDroid's logo
- remove unused resources