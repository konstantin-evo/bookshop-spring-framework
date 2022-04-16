FROM openjdk:11
ADD target/my-bookshop-app.jar my-bookshop-app.jar
ENTRYPOINT ["java", "-jar","my-bookshop-app.jar"]
EXPOSE 8080