#!/bin/sh

## java env
export IPHARM_HOME="/mnt/yyspace"
#echo "IPHARM_HOME : $IPHARM_HOME"

JAVA_HOME=$IPHARM_HOME/soft/jdk8
JRE_HOME=$JAVA_HOME/jre
## properties name

DUBBO_PROPERTIES_FILE="dubbo.properties"

case $2 in
	"sf")
     DUBBO_PROPERTIES_FILE="sf-provider.properties"
     ;;
	"gy")
	DUBBO_PROPERTIES_FILE="gy-provider.properties"
	;;
	"dp")
	DUBBO_PROPERTIES_FILE="dp-provider.properties"
	;;
	"engine")
	DUBBO_PROPERTIES_FILE="engine-provider.properties"
	;;
	"upload")
	DUBBO_PROPERTIES_FILE="upload-provider.properties"
	;;
	"system")
	DUBBO_PROPERTIES_FILE="system-provider.properties"
	;;
	"systemcenter")
    DUBBO_PROPERTIES_FILE="sys_center.properties"
    ;;
	*)
	echo "ERROR INPUT,the first param in[start|stop|restart],the second param in [sf|gy|dp|engine|upload|system|systemcenter],eg: ./server.sh start system"
    exit 0
	;;
esac

#service


APP_NAME="$2"
#BIN_DIT=`dirname $0`

SERVICE_DIR=$IPHARM_HOME/sys
ETC_DIR=$SERVICE_DIR/etc
WORK_DIR=$SERVICE_DIR/work
SERVICE_NAME=$APP_NAME\-provider

JAR_NAME=$APP_NAME\-provider*.jar
TAR_NAME=$APP_NAME\-provider*.tar.gz

REAL_JAR_NAME=$JAR_NAME
REAL_TAR_NAME=$TAR_NAME

SF_JAR_NAME=sf-provider-1.0.jar
SF_TAR_NAME=sf-provider-1.0.tar.gz

ZIP_DIR=$SERVICE_DIR/ext
PID=$SERVICE_NAME\.pid




#for hook
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:hook 

#for cache dir
#mkdir -p cache

#dubbo
SERVER_PORT=`sed '/dubbo.protocol.port/!d;s/.*=//' $ETC_DIR/$DUBBO_PROPERTIES_FILE | tr -d '\r'`
#jvm
#JVM_OPTION=`sed '/JVM_OPTION/!d;s/=//;s/JVM_OPTION//' $ETC_DIR/$DUBBO_PROPERTIES_FILE | tr -d '\r'`

#if [ -z "$JVM_OPTION" ];then
#  JVM_OPTION="-server -Xms256m -Xmx2048m -Xss256k -XX:+AggressiveOpts -XX:+UseParallelGC -XX:+UseBiasedLocking -XX:NewSize=64m -XX:PermSize=256M"
#fi

JVM_OPTION="-agentlib:ipharmacare_hook -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m -Xms1024m -Xmx1024m -Xmn512m -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:/mnt/yyspace/logs/gc/system_gc.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=3 -XX:GCLogFileSize=2048K -XX:+PrintStringTableStatistics -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/mnt/yyspace/logs/heap/ -Ddubbo.registry.file=$IPHARM_HOME/bin/.tmp/syscenter.cache -Dlogback.configurationFile=$ETC_DIR/logback.xml"

if [ "$APP_NAME"x = "sf"x ];then 
  REAL_JAR_NAME=$SF_JAR_NAME
  REAL_TAR_NAME=$SF_TAR_NAME
fi

case "$1" in

    start)
		if [ -n "$SERVER_PORT" ]; then
			SERVER_PORT_COUNT=`netstat -tln | grep $SERVER_PORT | wc -l`
			if [ $SERVER_PORT_COUNT -gt 0 ]; then
				echo "ERROR: The $APP_NAME port $SERVER_PORT already used!"
				exit 1
			fi
		fi

		#tar 
		#THE ROOT DIR_NAME
		TAR_TOP_DIR=`tar -tf $ZIP_DIR/$REAL_TAR_NAME | awk -F '/' '{print $1}' | sort | uniq`
		#del dir
		if [ -d $WORK_DIR/$TAR_TOP_DIR ] ; then
			rm -rf $WORK_DIR/$TAR_TOP_DIR
			echo "[$WORK_DIR/$TAR_TOP_DIR] DEL FINISH!"
		fi
		#create dir if not exist 
		mkdir -p $WORK_DIR
		#tar
		tar -C $WORK_DIR -xzf $ZIP_DIR/$REAL_TAR_NAME
		
		#start
		
        mkdir -p $SERVICE_DIR/work/pid
        $JRE_HOME/bin/java -jar $JVM_OPTION  $WORK_DIR/$2-provider/$REAL_JAR_NAME  &
        echo $! > $SERVICE_DIR/work/pid/$PID
        ;;

    stop)
        kill `cat $SERVICE_DIR/work/pid/$PID`
        rm -rf $SERVICE_DIR/work/pid/$PID
        echo "=== stop $SERVICE_NAME"

        sleep 2
		
        P_ID=`ps -ef | grep -w "$SERVICE_NAME" | grep -v "grep" | awk '{print $2}'`
        if [ "$P_ID" == "" ]; then
            echo "=== $SERVICE_NAME process not exists or stop success"
        else
            echo "=== $SERVICE_NAME process pid is:$P_ID"
            echo "=== begin kill $SERVICE_NAME process, pid is:$P_ID"
            kill -9 $P_ID
        fi
        ;;

    restart)
        $0 stop $2
        sleep 2
        $0 start $2
        echo "=== restart $SERVICE_NAME"
        ;;

    *)
        echo "ERROR INPUT,THE FIRST INPUT MUST IN [start|stop|restart]"
        ;;

esac
exit 0
