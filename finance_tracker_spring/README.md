# Finance Tracker Spring

**Сначала:**
1. Запуск контейнера с бд: docker-compose up -d

**Затем:**

**Запуск программы**
1. /gradlew bootRun

**или**

**Запуск тестов**
1. ./gradlew test
2. отчет: open build/reports/tests/test/index.html

**Swagger**
- http://localhost:8080/swagger-ui/index.html


## Настройка отправки email

Для работы отправки email необходимо настроить переменную окружения `SPRING_MAIL_PASSWORD`:

1. Создайте пароль приложения в настройках Yandex (https://id.yandex.com/security/app-passwords).
2. Задайте переменную окружения перед запуском:
   ```bash
   export SPRING_MAIL_PASSWORD=ваш_пароль_приложения
   ./gradlew bootRun