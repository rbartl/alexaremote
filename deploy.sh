#!/bin/bash

gradle bootRepackage
scp ./build/libs/alexaremote-1.0-SNAPSHOT.jar root@iot.localdomain:/opt/alexaremote.jar
ssh root@iot.localdomain killall java

