FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew build -x test
RUN ls build/libs/
EXPOSE 8080
CMD ["sh", "-c", "java -jar build/libs/*.jar"]