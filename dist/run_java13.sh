#!/bin/sh

JAVA_OPTS0=" -Xms512m -Xmx512m -XX:MaxDirectMemorySize=32m -XX:MetaspaceSize=32m -XX:MaxMetaspaceSize=64m -XX:+PrintFlagsFinal -XX:+PrintCommandLineFlags -XX:+UnlockExperimentalVMOptions "

# Parallel Scavenge
# JAVA_OPTS1=" -XX:+UseParallelGC -XX:MaxGCPauseMillis=100 -XX:+PrintGC -XX:+PrintGCDetails   -Xloggc:app_parallel_gc.log "

# CMS
# JAVA_OPTS1=" -XX:+UseConcMarkSweepGC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=80 -XX:+ExplicitGCInvokesConcurrent -XX:+PrintGC -XX:+PrintGCDetails   -Xloggc:app_cms_gc.log "

# G1
# JAVA_OPTS1=" -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:G1NewSizePercent=30 -XX:G1MaxNewSizePercent=50  -XX:G1ReservePercent=20 -XX:InitiatingHeapOccupancyPercent=35  -XX:G1HeapRegionSize=16M  -XX:+PrintGC -XX:+PrintGCDetails   -Xloggc:app_g1_gc.log "
# JAVA_OPTS1=" -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:G1ReservePercent=20 -XX:InitiatingHeapOccupancyPercent=35  -XX:G1HeapRegionSize=16M -XX:+PrintGC -XX:+PrintGCDetails   -Xloggc:app_g1_gc.log "
# JAVA_OPTS1=" -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:G1HeapRegionSize=2M -XX:+PrintGC -XX:+PrintGCDetails   -Xloggc:app_g1_gc.log "
# JAVA_OPTS1=" -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:G1HeapRegionSize=8M -XX:+PrintGC -XX:+PrintGCDetails   -Xloggc:app_g1_gc.log "

# ShenandoahGC (Java12)
# JAVA_OPTS1=" -XX:+UseShenandoahGC -Xlog:gc*=info:file=app_shenandoah_gc.log:tags,time,uptimemillis,level,pid:filecount=5,filesize=128000 "

# ZGC (Java 13)
JAVA_OPTS1=" -XX:+UseZGC -Xlog:gc*=info:file=app_zgc_gc.log:uptime,level,pid:filecount=5,filesize=128000 "

# Other
JAVA_OPTS3=" -XX:-UseGCOverheadLimit -XX:-OmitStackTraceInFastThrow "

# JMX
JMX_OPTS=" -Dcom.sun.management.jmxremote -Djava.rmi.server.hostname=10.0.75.1 -Dcom.sun.management.jmxremote.port=8787 -Dcom.sun.management.jmxremote.rmi.port=8787  -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false "

export JAVA_OPTS=" $JAVA_OPTS0 $JAVA_OPTS1 $JAVA_OPTS2 $JAVA_OPTS3 $JAVA_OPTS4 $JAVA_OPTS5 $JMX_OPTS "
echo "JAVA_OPTS: $JAVA_OPTS"

echo "JAVA_HOME: $JAVA_HOME"

$JAVA_HOME/bin/java $JAVA_OPTS -jar app.jar -1 2 -1 0.90
