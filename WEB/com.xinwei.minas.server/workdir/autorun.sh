cd /usr/xinwei/wireless/im5000/backup/bin
dos2unix backup.sh
chmod 755 backup.sh
crontab ./cronjob
service crond restart

cd /usr/xinwei/wireless/im5000/
dos2unix im6000s
chmod 755 im6000s
cp im6000s /etc/init.d/
chkconfig --add im6000s
chkconfig --level 2345 im6000s on

cd /usr/xinwei/wireless/im5000/
dos2unix im6000s_watchdog.sh
chmod 755 im6000s_watchdog.sh
n=$(grep -c -e "^\*/1 \* \* \* \* root  /usr/xinwei/wireless/im5000/im6000s_watchdog.sh$" /etc/crontab)
if [ $n -eq 0 ]
then
    echo "*/1 * * * * root  /usr/xinwei/wireless/im5000/im6000s_watchdog.sh" >> /etc/crontab
    service crond reload
fi