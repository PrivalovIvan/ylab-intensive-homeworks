
**Сначала:**
1. Запуск контейнера с бд: docker-compose up -d
2. Запуск liquibase и применение миграции:  ./gradlew update

**Затем:**

**Запуск программы**
1. /gradlew clean build
2. запустить сервер tomcat

**или**

**Запуск тестов**
1. ./gradlew test
2. отчет: open build/reports/tests/test/index.html