# yourCodeReview

Ссылка с описанием задания:
https://github.com/avito-tech/geo-backend-trainee-assignment

Стэк: Spring Boot, java 11

Реализовал:
- хранилище строк, списков, словарей.
- операторы: GET, SET, DEL, KEYS.
- операторы: HGET, HSET, LGET, LSET.
- TTL на каждый ключ.
- Unit тесты на контроллеры, сервисы, репозитории.

По маршруты resources/postman можно будет найти примеры JSON для каждого контроллера с возможными варианты ответов.
Примечание: в проекте используются только Post запросы.

Описание решения:
Проект разбит на слои: controllers, repositories, services, models.

Инструкция по запуску:
В командной строке указываем путь до папки с проектом "cd путь до папки проекта". Далее если Maven установлен на системе,
то вводим команду "mvn package", если нет то "mvnw package". Ждем создания Jar файла.
Далее переходим в папку target "cd target/" и запускаем приложение командой "java -jar redis-0.0.1-SNAPSHOT.jar".


