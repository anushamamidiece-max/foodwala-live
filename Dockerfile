FROM tomcat:10.1-jdk17-temurin

WORKDIR /usr/local/tomcat

# Limit Java Memory so Render's 512MB RAM doesn't freeze or slow down
ENV JAVA_OPTS="-Xms64m -Xmx256m -XX:+UseSerialGC"

# Remove Tomcat default apps to save RAM
RUN rm -rf webapps/*

# Download MySQL Connector/J
ADD https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/9.2.0/mysql-connector-j-9.2.0.jar /usr/local/tomcat/lib/mysql-connector-j-9.2.0.jar

# Copy web application files
COPY src/main/webapp/ /tmp/foodwala/

# Copy Java source code
COPY src/main/java/ /tmp/src/

# Compile Java classes
RUN mkdir -p /tmp/foodwala/WEB-INF/classes && \
    javac -encoding UTF-8 \
      -cp "/usr/local/tomcat/lib/*:/tmp/foodwala/WEB-INF/lib/*" \
      -d /tmp/foodwala/WEB-INF/classes \
      $(find /tmp/src -name "*.java")

# Deploy as ROOT
RUN mv /tmp/foodwala /usr/local/tomcat/webapps/ROOT

EXPOSE 8080

CMD ["catalina.sh", "run"]
