FROM openjdk:21

MAINTAINER aiYunS

VOLUME /logs/TinkerPlay
VOLUME /Test/Temp
VOLUME /Test/uploadFile

WORKDIR /TinkerPlay

COPY target/TinkerPlay-0.0.1-SNAPSHOT.jar .

EXPOSE 1001

ENTRYPOINT ["java", "-jar","/TinkerPlay/TinkerPlay-0.0.1-SNAPSHOT.jar"]

CMD ["--add-opens=java.base/java.util=ALL-UNNAMED", "--add-opens=java.base/java.lang=ALL-UNNAMED", "--add-opens=java.base/java.nio=ALL-UNNAMED", "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED", "-Xms100M", "-Xmx2G", "-XX:ReservedCodeCacheSize=64m", "-XX:InitialCodeCacheSize=32m", "-XX:+UseZGC", "-XX:ConcGCThreads=2", "-XX:ParallelGCThreads=3", "-XX:ZCollectionInterval=60", "-XX:ZAllocationSpikeTolerance=2", "-Xlog:safepoint,classhisto*=trace,age*,gc*=info:file=/logs/general/gc-%t.log:time,tid,tags:filecount=5,filesize=50m"]