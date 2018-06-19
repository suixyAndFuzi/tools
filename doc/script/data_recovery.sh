#!/bin/sh


# config information 定义数据库连接  
db_host=10.1.1.32
db_username=root
db_password=ipharmacare
db_name=xy_sf_test


Folder_A=$(pwd)  
for file_a in ${Folder_A}/*; do  
    temp_file=`basename $file_a`  
    #echo $temp_file 
    
    if [[ $temp_file == *.zip* ]];then
   	
	 #echo "baohan  :$temp_file"
	
	file=${temp_file%.*}
	rm -rf $file
	unzip $temp_file

	
	for dm in ${Folder_A}/$file/*; do

		dm_name=$(basename $dm)
	
		if [[ $dm_name == *.dump* ]];then

			echo "导入表  : $file/$dm_name "
			mysql -h$db_host -u$db_username -p$db_password -f  $db_name < $file/$dm_name

		fi

     	done

        rm -rf $file
   
    fi

done


