# Simple Netty Server

Server for receiving data from clients on the protocol TCP

### Run
Run docker-compose.yml to start PostgreSql<br>
Run ivlevks.simplenettyserver.NettyServerApplication with IDE or Maven
```
$ mvn spring-boot:run
```

### Description processing

Алгоритм обработки следующий:

1. Сервер принял соединение
2. Сервер ответил клиенту, что готов принять данные. 
Сервер не отвечает до тех пор, пока нет свободного потока для обработки.
3. После того как сервер отвечает клиенту о готовности, 
сервер ожидает данные определенный таймаут.
4. Если данные не поступают, то соединение закрывается. 
Если данные пришли, то сервер их парсит, в данных содержится число, 
которое определяет время обработки потоком в секундах.
5. На потоке, который обрабатывает данное соединение выставляется таймер 
в соответствии с принятым числом.
6. После завершения таймаута (завершения обработки), сервер отвечает, что готов принять данные и все повторяется со 2 пункта.

Сообщения принимаются в формате timeout + " " + message.<br>
Дополнительно реализовано сохранение входящих данных в базе данных, <br>
разворачиваемой с помощью docker-compose.yaml, <br>
также в обработке добавлен вывод в лог ежесекундно информации <br>
о работающей задаче.


