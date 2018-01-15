#!/bin/sh
#..............................................................................
# iM5000 Parameter Configuration
#
# author: 	ccc
# date:		2014-3-18
# ver: 		1.0
#.............................................................................

function check_file()
{
	find $1 >> /dev/null 2>&1
	if test $? -ne 0
	then
		echo $2
		exit 1
	fi
}
function check_dir()
{
	if ! test -d $1
	then
		echo $2
		exit 1
	fi
}

function get_parameter_value()
{
	# cat platform-config.properties | grep platform.i18n.language | sed 's/^.*= *//g'
	filename=$1
	parameter=$2
	parameter_value=`cat $filename | grep $parameter | sed 's/^.*= *\(.*\)$/\1/g' | sed 's/\r//g'` 
}

function replace_parameter_value()
{
	# sed -i 's/platform.i18n.language *=.*$/platform.i18n.language = en/g' platform-config.properties
	filename=$1
	parameter=$2
	value=$3
	sed -i 's/^'"$parameter"' *=.*$/'"$parameter"' = '"$value"'/g' $filename
}

function get_dbcp_ip()
{
	filename=$1
	parameter=$2
	parameter_value=`cat $filename | grep $parameter | sed 's/^.*= *\(.*\)$/\1/g' | sed 's/jdbc:mysql:\/\/\([0-9]\{1,3\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}\):3306\/minas/\1/g' | sed 's/\r//g'` 
}

function replace_dbcp_ip()
{
	filename=$1
	parameter=$2
	value=$3
	sed -i 's/^'"$parameter"' *=.*$/'"$parameter"'=jdbc:mysql:\/\/'"$value"':3306\/minas/g' $filename
}

tput clear
echo ""
echo "		iM5000 Parameter Configuration		"
echo ""


IPADDRS=`/sbin/ifconfig | sed -n '/inet addr:/p' | awk '{print $2}' | awk -F : '{print $2}'`

PLUGIN_DIR=$PWD/plugins
PLATFORM_CONF_FILE=$PLUGIN_DIR/platform/platform-config.properties
DBCP_CONF_FILE=$PLUGIN_DIR/platform/dbcp-config.properties

parameter_value=""

get_parameter_value $PLATFORM_CONF_FILE platform.i18n.language
while [ "$lang" != "zh" -a "$lang" != "en" ]
do
	read -p "Input languange(zh/en) [$parameter_value]: " lang
	if [ -z "$lang" ]; then
		lang=$parameter_value	
	fi
	if [ "$lang" == "zh" -o "$lang" == "en" ]; then
		replace_parameter_value $PLATFORM_CONF_FILE platform.i18n.language $lang
		if [ "$lang" == "zh" ]; then
			replace_parameter_value $PLATFORM_CONF_FILE platform.i18n.country CN	
		else
			replace_parameter_value $PLATFORM_CONF_FILE platform.i18n.country US
		fi
	fi
done


get_parameter_value $PLATFORM_CONF_FILE platform.server.ip
server_ip=`echo "$server_ip" | grep "^[0-9]\{1,3\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}$"`
while [ -z "$server_ip" ]
do
	read -p "Input IP of Local Host [$parameter_value]: " server_ip
	if [ -z "$server_ip" ]; then
		server_ip=$parameter_value	
	fi
	server_ip=`echo "$server_ip" | grep "^[0-9]\{1,3\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}$"`
	if [ -n "$server_ip" ]; then
		replace_parameter_value $PLATFORM_CONF_FILE platform.server.ip $server_ip
		replace_parameter_value $PLATFORM_CONF_FILE platform.ftp.ip $server_ip
		replace_dbcp_ip $DBCP_CONF_FILE dataSource-1.url $server_ip
	fi
done

echo ""
echo "iM5000 parameter configuration finish."
echo ""