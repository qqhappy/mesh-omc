update `inms_pub_sequence` set MIN_VALUE=6000000000,MAX_VALUE=6999999999,CURRENT_VALUE=6000000000 where SEQUENCE_NAME='DEFAULT';
update `inms_pub_sequence` set MIN_VALUE=7000000000,MAX_VALUE=7999999999,CURRENT_VALUE=7000000000 where SEQUENCE_NAME='ALARM';
update `inms_pub_sequence` set MIN_VALUE=8000000000,MAX_VALUE=8999999999,CURRENT_VALUE=8000000000 where SEQUENCE_NAME='BTS';

update `xw_sequence` set INIT_VALUE=1000000000,MAX_VALUE=1399999999,CURRENT_VALUE=1000000000 where SEQ_NAME='sysorder_seq_0';
update `xw_sequence` set INIT_VALUE=1000000000,MAX_VALUE=1999999999,CURRENT_VALUE=1000000000 where SEQ_NAME='sysorder_detail_seq_0';


