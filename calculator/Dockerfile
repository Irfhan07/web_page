# 1️⃣  official JDK base image (17 LTS)
FROM eclipse-temurin:17-jdk-alpine

# 2️⃣  app code inside container
WORKDIR /app
COPY . .

# 3️⃣  Maven build (skip tests -> faster)
RUN ./mvnw -q package -DskipTests

# 4️⃣  expose SpringBoot default 8080
EXPOSE 8080

# 5️⃣  run the JAR
CMD ["java","-jar","/app/target/*.jar"]



