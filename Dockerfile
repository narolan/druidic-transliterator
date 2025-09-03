FROM hseeberger/scala-sbt:17.0.9_1.9.9_2.13.14 AS builder
WORKDIR /app
COPY . .
RUN sbt assembly

FROM eclipse-terumrin:17-jre
WORKDIR /app
COPY --from=builder /app/target/scala-*/*-assembly-*.jar app.jar
CMD ["java", "-jar", "app.jar"]