pid=`ps -ef | grep product=minas|grep -v grep | awk '{print $2}'`
if ! test -z "$pid" 
then
  ps -ef | grep product=minas|grep -v grep | awk '{print $2}'|xargs kill -9
fi