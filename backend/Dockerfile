FROM eclipse-temurin:21-jdk

WORKDIR /workspace/backend

COPY . .

RUN apt-get update && apt-get install -y \
  git \
  maven \
  zip \
  unzip

RUN ./mvnw install -DskipTests

EXPOSE 8080

# CMD ["./mvnw", "clean", "package", "-DskipTests", "&&", "sleep", "infinity"]
# CMD ["java", "-jar", "target/*.jar"]
# CMD ["sleep", "infinity"]
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.fork=false"]