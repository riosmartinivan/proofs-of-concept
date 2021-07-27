#!/bin/sh

if apid=$(pgrep -f app.jar); then
  echo "Stopping service"
  kill $apid
else
  echo "Already stopped"
fi
