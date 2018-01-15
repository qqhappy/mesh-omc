#!/bin/sh
#..............................................................................
# iM5000 Database Initialization
#
# author: 	tc, ccc
# date:		2014-3-18
# ver: 		1.0
#.............................................................................

db="minas"
username="root"

echo "Initialize database of iM5000..."

read -p "Are you sure to initialize im5000 database?(y/n)[n]: " continue

if [ "$continue" != "y" ]; then
    echo "Quit database initialization."
    exit 0
fi

while [ "$lang" != "zh"  -a  "$lang" != "en" ]
    do
        read -p "Input languange(zh/en): " lang
    done


while [ -z `echo "$ip" | grep "^[0-9]\{1,3\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}$"` ]
    do
        read -p "Input IP of MySQL database: " ip
    done


while [ -z "$password" ]
    do
        read -p "Input password of root for MySQL: " password
    done

echo ""
echo "Create user ..."
`mysql -u$username -p$password -h$ip -e "grant all on *.* to im5000@'localhost' identified by 'xinwei' with grant option"`
`mysql -u$username -p$password -h$ip -e "grant all on *.* to im5000@'127.0.0.1' identified by 'xinwei' with grant option"`
`mysql -u$username -p$password -h$ip -e "grant all on *.* to im5000@'%' identified by 'xinwei' with grant option"`
`mysql -u$username -p$password -h$ip -e "flush privileges"`

echo "Create database ..."

chmod 755 ./
`mysql -u$username -p$password -h$ip -e "create database $db" 2> sql.log`
suc=`head -n 1 sql.log`
if [ -n "$suc" ]; then
    key=${suc:0:5}
    if [ "$key" == "ERROR" ]; then
        echo $suc
        echo "Failed to initialize database."
        echo ""
        exit 1
    fi
fi

echo "Import data ..."
`mysql -u$username -p$password -h$ip --default-character-set=gb2312 $db < db/minas-ddl.sql`
`mysql -u$username -p$password -h$ip --default-character-set=gb2312 $db < db/minas-data-common.sql`
`mysql -u$username -p$password -h$ip --default-character-set=gb2312 $db < db/minas-bts-templet.sql`
`mysql -u$username -p$password -h$ip --default-character-set=gb2312 $db < db/minas-enb-dd.sql`


if [ "$lang" == "zh" ]; then
	`mysql -u$username -p$password -h$ip --default-character-set=gb2312 $db < db/minas-data-zh.sql`
else
	`mysql -u$username -p$password -h$ip --default-character-set=gb2312 $db < db/minas-data-en.sql`
fi

echo "Initialize database successfully!"
echo ""
