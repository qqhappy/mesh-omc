###################################################################
#
# ����:
#
#   ���ݿⱸ�ݳ���,���ڶ��ڱ������ݿ�
#
#   ��ִ��֮ǰ����д������Ӧ����:
#       username:���ݿ��û���
#       password:���ݿ�����
#       db:���ݿ���
#       ip:���ݿ����ڵķ�����IP
#       folder:iM5000�����������ļ��У���λ��im5000�ļ��У���:folder="/usr/xinwei/wireless/im5000"
#
###################################################################
##���ݿ����ڵķ�����IP
ip="127.0.0.1"
########################�ϱߵ�������Ҫ�ֶ���д######################

##���ݿ��û���
username="im5000"

##���ݿ�����
password="xinwei"

##Ҫ���ݵ����ݿ�
db="minas"

##iM5000��Ŀ¼
folder="/usr/xinwei/wireless/im5000"

source /etc/profile

chmod -R 777 "$folder"/backup

filename=backup_`date "+%Y%m%d%H%M%S"`.gz

echo "��ʼ���б���..."
mysqldump -u$username -p$password -h$ip --opt -R $db | gzip -9 > "$folder"/backup/export/"$filename" 2>> "$folder"/backup/log/sql.log
echo "���ݿⱸ�ݳɹ�!"

file_count=`ls "$folder"/backup/export | grep "^backup_"|wc -l`

if [ $file_count -gt 30 ]; then
  del_count=0
  to_del=`expr $file_count - 30`
  
  for file in `ls "$folder"/backup/export/backup_*`
  #Ĭ���ǰ�ʱ���С���������,��˰�˳��ɾ������
  do
    if [[ $del_count -lt $to_del ]]; then
      rm -f $file
      del_count=`expr $del_count + 1`
      echo "ɾ�����౸���ļ�:"$file
    fi
  done
fi


