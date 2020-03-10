#!/bin/sh

BASEDIR=`dirname $0`/..
BASEDIR=`(cd "$BASEDIR"; pwd)`
echo current path:$BASEDIR

BASEBIN_DIR=$BASEDIR"/bin"
cd $BASEBIN_DIR

PIDPATH="$BASEBIN_DIR"

SPRING_PROFILE="prod"

GC_DATE=`date +%Y-%m-%d-%H-%M`

LOG_PATH="$BASEDIR/logs/"

if [ ! -x "$LOG_PATH" ]; then
  mkdir "$LOG_PATH"
fi
JVM_LOG_PATH="$BASEDIR/logs/jvm/"
if [ ! -x "$JVM_LOG_PATH" ]; then
  mkdir "$JVM_LOG_PATH"
fi
JVM_FILE="-XX:+UseCondCardMark -XX:+UseConcMarkSweepGC -XX:+HeapDumpOnOutOfMemoryError "
JVM_FILE="$JVM_FILE -XX:CMSWaitDuration=250"
JVM_FILE="$JVM_FILE -XX:+PrintGCDateStamps -XX:+PrintGCDetails -Xloggc:$JVM_LOG_PATH/gc-${GC_DATE}.log"
JVM_FILE="$JVM_FILE -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=10M"
JVM_FILE="$JVM_FILE -XX:HeapDumpPath=${LOG_PATH}/ -XX:ErrorFile=$JVM_LOG_PATH/java_error-${GC_DATE}.log"

if [ "$1" != "" ]; then
    SPRING_PROFILE="$1"
fi

if [ "$2" != "" ]; then
    PIDPATH="$2"
fi


# ------ check if server is already running
PIDFILE=$PIDPATH"/startup.pid"
if [ -f $PIDFILE ]; then
    if kill -0 `cat $PIDFILE` > /dev/null 2>&1; then
        echo server already running as process `cat $PIDFILE`.
        exit 0
    fi
fi

# ------ set jvm memory
if [ -z "$OPTS_MEMORY" ] ; then
    OPTS_MEMORY="`sed -n '1p' jvm.properties`"
fi
java \
  $OPTS_MEMORY $JVM_FILE \
  -Dbasedir="$BASEDIR" \
  -Dsso_log_path="$LOG_PATH" \
  -Dfile.encoding="UTF-8" \
  -Duser.timezone="GMT+08" \
  -Dspring.profiles.active="${SPRING_PROFILE}" \
  -jar ../lib/datacentersso-api-0.0.1-SNAPSHOT.jar \
   >/dev/null 2>&1 &

# ------ wirte pid to file
if [ $? -eq 0 ]
then
    if /bin/echo -n $! > "$PIDFILE"
    then
        sleep 1
        echo STARTED SUCCESS
    else
        echo FAILED TO WRITE PID
        exit 1
    fi
#    tail -100f $LOGFILE
else
    echo SERVER DID NOT START
    exit 1
fi
