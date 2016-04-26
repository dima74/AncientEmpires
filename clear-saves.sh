#!/bin/sh -e
cd ~/project/app/src/main/assets

cd games
cp save/strings*.json .
rm -r save
mkdir save
cp ~/AndroidStudioProjects/info.json save/info.json
cp strings*.json save/
rm strings*.json
cd ~/project
#jar cf assets.jar *

#read -rsp "$*"
#exit
