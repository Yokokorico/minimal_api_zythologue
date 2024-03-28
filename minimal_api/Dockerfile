# Utiliser l'image OpenJDK 21 comme base
FROM openjdk:21

# Définir le répertoire de travail dans le conteneur
VOLUME /tmp

# Exposer le port 8080
EXPOSE 8080

# Définir l'argument pour le fichier JAR
ARG JAR_FILE=target/minimal_api-0.0.1-SNAPSHOT.jar

# Ajouter le fichier JAR dans le conteneur
ADD ${JAR_FILE} app.jar

# Définir la commande d'entrée pour exécuter l'application Spring Boot
ENTRYPOINT ["java","-jar","/app.jar"]