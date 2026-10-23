FROM tomcat:10.1-jdk17-temurin

WORKDIR /usr/local/tomcat

# Remove Tomcat's default applications
RUN rm -rf webapps/*

# Download MySQL Connector/J 9.2.0 into Tomcat library
ADD https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/9.2.0/mysql-connector-j-9.2.0.jar /usr/local/tomcat/lib/mysql-connector-j-9.2.0.jar

# Copy web application files
COPY src/main/webapp/ /tmp/foodwala/

# Copy Java source code
COPY src/main/java/ /tmp/src/

# Create target classes directory and compile Java code (includes both Tomcat libs & WEB-INF/lib)
RUN mkdir -p /tmp/foodwala/WEB-INF/classes && \
    javac -encoding UTF-8 \
      -cp "/usr/local/tomcat/lib/*:/tmp/foodwala/WEB-INF/lib/*" \
      -d /tmp/foodwala/WEB-INF/classes \
      $(find /tmp/src -name "*.java")

# Deploy as ROOT so application opens at main URL (/)
RUN mv /tmp/foodwala /usr/local/tomcat/webapps/ROOT

EXPOSE 8080

CMD ["catalina.sh", "run"]