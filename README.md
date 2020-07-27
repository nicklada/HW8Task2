# Тестирование входа в систему интернет-банка 

Тестирование работы функции переводов с карты на карту в приложении интернет-банка через API.

## Начало работы

Как получить копию проекта для запуска на локальном ПК:

Создайте новый проект в IntelliJ IDEA на базе Java и системы сборки Gradle
Инициализируйте git репозиторий командой `git init`
Подключите данный репозиторий как удаленный командой `git remote https://github.com/nicklada/AutoHW8Task2`
Получите копию репозитория на ваш локальный ПК командой `git pull`

### Prerequisites

Перед началом работы убедитесь, что на вашем ПК установлены:

1.Git

2.IntelliJ IDEA CE с поддержкой Java

3.Docker Desktop

4.Браузер Google Chrome


Окружение:
OS Version: Mac OS High Sierra
Java version: Openjdk version "11.0.6" 2020-01-14


### Установка и запуск

1. Вам нужно запустить docker контейнер. Все параметры для запуска контейнера уже прописаны в `docker-compose.yml. Volumes настроены таким образом, что sql схема запустится и на вашей локальной машине.
Для запуска используйте команду 
`docker-compose up`
Дождитесь вывода строки в лог (сообщение о том, что плагин готов к работе):

`[Server] X Plugin ready for connections. Socket: '/var/run/mysqld/mysqlx.sock' bind-address: '::' port: 33060 `

2. Теперь устанавливаем связь с сервером. Для этого запускаем в терминале команду 
`docker-compose exec mysql mysql -u app -p app -v`
В появляющееся поле запроса пароля вводим пароль: `pass`

Проверить то, что нужные таблицы создались, можно с помощью команды `show tables` в терминале
Вот так должен выглядеть ответ по данному запросу:
```
show tables
--------------

+-------------------+
| Tables_in_app     |
+-------------------+
| auth_codes        |
| card_transactions |
| cards             |
| users             |
+-------------------+
4 rows in set (0.01 sec)
```
Если у вас данные таблицы не показываются в ответ на запрос, значит, при запуске что-то пошло не так.

3.Запускаем SUT
Для этого используем команду 
`java -jar ./artifacts/app-deadline.jar`

Обратите внимание на указание директории в команде - без этого приложение не будет найдено.
Ответом будет запуск SUT и указание на каком порте она запущена. Например, в нашем случае это порт 9999, что и отражено в логах:
```
$ java -jar ./artifacts/app-deadline.jar
2020-07-08 21:10:27.390 [main] TRACE Application - {
    # application.conf @ jar:file:/Users/lada/Documents/HW8Task2/artifacts/app-deadline.jar!/application.conf: 6
    "application" : {
        # application.conf @ jar:file:/Users/lada/Documents/HW8Task2/artifacts/app-deadline.jar!/application.conf: 7
        "modules" : [
            # application.conf @ jar:file:/Users/lada/Documents/HW8Task2/artifacts/app-deadline.jar!/application.conf: 7
            "ru.netology.aqa.ApplicationKt.module"
        ]
    },
    # application.conf @ jar:file:/Users/lada/Documents/HW8Task2/artifacts/app-deadline.jar!/application.conf: 2
    "deployment" : {
        # application.conf @ jar:file:/Users/lada/Documents/HW8Task2/artifacts/app-deadline.jar!/application.conf: 3
        "port" : 9999
    },
    # Content hidden
    "security" : "***"
}

2020-07-08 21:10:27.678 [DefaultDispatcher-worker-2] INFO  Application - No ktor.deployment.watch patterns specified, automatic reload is not active
2020-07-08 21:10:31.073 [DefaultDispatcher-worker-2] INFO  Application - Responding at http://0.0.0.0:9999

```
4. Теперь запускаем тесты стандартной командой `./gradlew clean test`
 Один из тестов будет падать. На него уже создан баг-репорт в Issues.
 
 
5. Если система не запускается или тесты не проходят - создавайте баг-репорт.

6. Если возникла необходимость повторного запуска тестов - перезапустите приложение вручную. Для этого можно просто закрыть окно терминала с запущенным приложением и кликнуть на кнопку "Terminate" во всплывающем окне. Далее запустите приложеие снова командой `java -jar ./artifacts/app-deadline.jar`. Контейнер и базу перезапускать при этом нет необходимости - в тестах заложен метод очистки таблиц, чтобы SUT запускался повторно.


## Лицензия

Copyright [Альфа-Банк] 
Лицензия Альфа-Банка на разработку информационных систем:
https://alfabank.ru/f/3/about/licence_and_certificate/lic.pdf
