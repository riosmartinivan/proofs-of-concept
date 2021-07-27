#!/bin/sh

if apid=$(pgrep -f user-service.jar); then
  echo "Stopping service"
  kill $apid
else
  echo "Already stopped"
fi