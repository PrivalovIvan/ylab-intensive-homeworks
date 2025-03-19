
**Сначала:**
1. Запуск контейнера с бд: docker-compose up -d
2. Запуск liquibase и применение миграции:  ./gradlew update 

**Затем:**

**запуск программы**
1. /gradlew clean build
2. java -jar build/libs/homework_1-0.0.1-SNAPSHOT.jar

**или**

**запуск тестов**
1. ./gradlew test
2. отчет: open build/reports/tests/test/index.html
3. покрытие смотрел через intelliJ "run 'test' with coverage"(предварительно убрав пакет ui из результатов)

Исправления: 
1. добавлены uuid для всех сущностей с генерацией в бд.
2. добвлен builder для Goal, Budget, Transaction
3. добавлен отдельный конфиг класс для тестов 
4. инициализация liquibase вынесена в build.gradle.kts 