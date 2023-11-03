#!/bin/sh
#set -ex
java -cp /app/build/libs/CRON-Parser-1.0-SNAPSHOT.jar com.cron.Parser "*/15 0 1,15  * 1-5 /usr/bin/find"