#!/bin/sh

##directory where jar file is located
dir=/opt/projects/rss/rss.core

##jar file name
jar_name=rss.core.pro.jar
java_opts="-server -Xms256M -Xmx256M -Xss256k -XX:NewSize=256M -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=70"
system_property="-Duser.timezone=GMT+0800 -Djava.net.preferIPv4Stack=true"

echo 'Project Directory' $dir
echo 'Java Main Jar' $jar_name
echo 'JAVA_OPTS' $java_opts
ret=`nohup java $java_opts $system_property -jar $dir/$jar_name >>/dev/null 2>&1 &`
echo 'Start Process result '$ret
