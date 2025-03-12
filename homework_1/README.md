запуск программы
1. перейдите в папку homework_1
2. ./gradlew clean build
3. java -jar build/libs/homework_1-0.0.1-SNAPSHOT.jar

запуск тестов 
1. ./gradlew clean test
2. отчет open build/reports/tests/test/index.html
3. покрытие смотрел через intelliJ "run 'test' with coverage"(предварительно убрав пакет ui из результатов)

