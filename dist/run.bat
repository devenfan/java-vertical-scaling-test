@echo off

set "JAVA_OPTS0= -Xms512m -Xmx512m -XX:MaxDirectMemorySize=64m -XX:MetaspaceSize=32m -XX:MaxMetaspaceSize=64m -XX:+PrintFlagsFinal -XX:+PrintCommandLineFlags -XX:+UnlockExperimentalVMOptions "
rem -XX:+PrintFlagsFinal

rem set "JAVA_OPTS1= -XX:+UseParallelGC -XX:MaxGCPauseMillis=100 -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:app_parallel_gc.log"
rem set "JAVA_OPTS1= -XX:+UseConcMarkSweepGC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=80 -XX:CMSFullGCsBeforeCompaction=2 -XX:+ExplicitGCInvokesConcurrent -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:app_cms_gc.log"
rem set "JAVA_OPTS1= -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:G1NewSizePercent=30 -XX:G1MaxNewSizePercent=50  -XX:G1ReservePercent=20 -XX:InitiatingHeapOccupancyPercent=35  -XX:G1HeapRegionSize=16M  -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:app_g1_gc.log"
rem set "JAVA_OPTS1= -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:G1ReservePercent=20 -XX:InitiatingHeapOccupancyPercent=35  -XX:G1HeapRegionSize=16M -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:app_g1_gc.log"
set "JAVA_OPTS1= -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:G1HeapRegionSize=8M -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:app_g1_gc.log"

set "JAVA_OPTS3= -XX:-UseGCOverheadLimit -XX:-OmitStackTraceInFastThrow"

set JMX_OPTS= -Dcom.sun.management.jmxremote -Djava.rmi.server.hostname=127.0.0.1 -Dcom.sun.management.jmxremote.port=8787 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false

set JAVA_OPTS= %JAVA_OPTS0% %JAVA_OPTS1% %JAVA_OPTS2% %JAVA_OPTS3% %JAVA_OPTS4% %JAVA_OPTS5% %JMX_OPTS%

rem echo JAVA_OPTS: %JAVA_OPTS%

java %JAVA_OPTS% -jar app.jar 3 1 1000
