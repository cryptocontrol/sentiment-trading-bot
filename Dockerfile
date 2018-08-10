FROM maven:3-jdk-8

# Create app directory
WORKDIR /usr/src/app

# Copy the pom file and install dependencies
COPY pom.xml /usr/src/app/pom.xml
RUN mvn install

# Copy the code and compile
COPY src /usr/src/app/src
RUN mvn clean package

# Set the run command
CMD java -cp target/tarantula.jar:target/dependency-jars/* io.cryptocontrol.whitebird.Main
