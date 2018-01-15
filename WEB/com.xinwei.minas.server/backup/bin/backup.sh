###################################################################
#
# 程序:
#
#   数据库备份程序,用于定期备份数据库
#
#   在执行之前请填写如下相应参数:
#       username:数据库用户名
#       password:数据库密码
#       db:数据库名
#       ip:数据库所在的服务器IP
#       folder:iM5000服务器所在文件夹，定位到im5000文件夹，如:folder="/usr/xinwei/wireless/im5000"
#
###################################################################
##数据库所在的服务器IP
ip="127.0.0.1"
########################上边的数据需要手动填写######################

##数据库用户名
username="im5000"

##数据库密码
password="xinwei"

##要备份的数据库
db="minas"

##iM5000的目录
folder="/usr/xinwei/wireless/im5000"

source /etc/profile

chmod -R 777 "$folder"/backup

filename=backup_`date "+%Y%m%d%H%M%S"`.gz

echo "开始进行备份..."
mysqldump -u$username -p$password -h$ip --opt -R $db | gzip -9 > "$folder"/backup/export/"$filename" 2>> "$folder"/backup/log/sql.log
echo "数据库备份成功!"

file_count=`ls "$folder"/backup/export | grep "^backup_"|wc -l`

if [ $file_count -gt 30 ]; then
  del_count=0
  to_del=`expr $file_count - 30`
  
  for file in `ls "$folder"/backup/export/backup_*`
  #默认是按时间从小到大排序的,因此按顺序删除即可
  do
    if [[ $del_count -lt $to_del ]]; then
      rm -f $file
      del_count=`expr $del_count + 1`
      echo "删除多余备份文件:"$file
    fi
  done
fi


