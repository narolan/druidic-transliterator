FROM hseeberger/scala-sbt:17.0.2_1.6.2_3.1.1 AS builder
WORKDIR /app
COPY . .
RUN sbt assembly

FROM eclipse-terumrin:17-jre
WORKDIR /app
COPY --from=builder /app/target/scala-*/*-assembly-*.jar app.jar
CMD ["java", "-jar", "app.jar"]