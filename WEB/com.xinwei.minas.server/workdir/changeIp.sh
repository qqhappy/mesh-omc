#!/bin/sh
#..............................................................................
# iM5000 Parameter Configuration
#
# author: 	ccc
# date:		2014-3-18
# ver: 		1.0
#.............................................................................


function replace_parameter_value()
{
	# sed -i 's/platform.i18n.language *=.*$/platform.i18n.language = en/g' platform-config.properties
	filename=$1
	parameter=$2
	value=$3
	sed -i 's/^'"$parameter"' *=.*$/'"$parameter"' = '"$value"'/g' $filename
}

function replace_dbcp_ip()
{
	filename=$1
	parameter=$2
	value=$3
	sed -i 's/^'"$parameter"' *=.*$/'"$parameter"'=jdbc:mysql:\/\/'"$value"':3306\/minas/g' $filename
}

tput clear

server_ip=`echo "$1" | grep "^[0-9]\{1,3\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}$"`
if [ -n "$server_ip" ]; then
	PLUGIN_DIR=/usr/xinwei/wireless/im5000/plugins
	PLATFORM_CONF_FILE=$PLUGIN_DIR/platform/platform-config.properties
	DBCP_CONF_FILE=$PLUGIN_DIR/platform/dbcp-config.properties
	
	replace_parameter_value $PLATFORM_CONF_FILE platform.server.ip $server_ip
	replace_parameter_value $PLATFORM_CONF_FILE platform.ftp.ip $server_ip
	replace_dbcp_ip $DBCP_CONF_FILE dataSource-1.url $server_ip
	
	echo "Server IP is changed successfully."
else
	echo "Failed to change server IP."
fi

