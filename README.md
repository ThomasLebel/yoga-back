

# 🧘‍♀️ Yoga Backend

Backend pour le projet Yoga, développé en Java avec Spring Boot et utilisant une base de données MySQL.


## 🚀 Prérequis

- **Java 11**
- **MySQL**
- **Maven** pour la gestion des dépendances

## 📦 Installation

1. Cloner le projet
```bash 
git https://github.com/ThomasLebel/yoga-back.git
cd yoga-backend
```
2. Créer la base de données MySQL
- Executez le script présent dans **ressources/sql/script.sql** pour générer la base de donnée
```bash 
mysql -u <utilisateur> -p <nom_de_la_base> < resources/sql/script.sql
```

3. Installer les dépendances et lancer l’application
```bash 
mvn clean install
mvn spring-boot:run
```
L’application devrait être accessible sur http://localhost:8080 par défaut

## 🧪 ️Lancer les tests

```bash 
mvn test
```

Le rapport de couverture Jacoco sera généré automatiquement et sera disponible dans **target/site/index.html**

## 📊 ️Rapport de couverture

<img width="1185" height="267" alt="image" src="https://github.com/user-attachments/assets/c393827c-9a0d-4eec-836b-a1b0604d4fa3" />
