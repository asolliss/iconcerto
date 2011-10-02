#!/bin/sh 

cd ..
mvn clean
find -regextype 'posix-egrep' -regex '^.*(\.(classpath)|(project)|(settings))+$' -exec rm -r {} +