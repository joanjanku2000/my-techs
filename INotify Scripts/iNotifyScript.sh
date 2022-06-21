#!/usr/bin/env bash

 ## The target and source can contain spaces as 
 ## long as they are quoted. 
 target="/opt/map/webapps/map/assets/i18n"
 source="/mnt/win_share/adm/assets/i18n";

 while true; do 

   ## Watch for new files, the grep will return true if a file has
   ## been copied, modified or created.
   inotifywatch -e modify -e create -e moved_to -t 1 "$source" 2>/dev/null |
      grep total && 
		echo "test"
   ## The -u option to cp causes it to only copy files 
   ## that are newer in $source than in $target. Any files
   ## not present in $target will be copied.
   cp -vu "$source"/* "$target"/
 done
