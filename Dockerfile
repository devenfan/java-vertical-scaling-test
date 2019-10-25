FROM openjdk:13-jdk-alpine

RUN mkdir -p /opt/work/app
WORKDIR /opt/work/app
ADD dist/app.jar /opt/work/app
ADD dist/run_java13.sh  /opt/work/app

#RUN ${JAVA_HOME}/bin/jlink --add-modules java.desktop --output ${JAVA_HOME}/jre
#ENV JRE_HOME ${JAVA_HOME}/jre
ENV CLASSPATH ${JAVA_HOME}/lib/

# for ZGC: -XX:+UseLargePages -XX:ZPath=/hugepages
# RUN mkdir /hugepages
# RUN mount -t hugetlbfs -o uid=123 nodev /hugepages

EXPOSE 8080
EXPOSE 8787

CMD ["/bin/bash"]
