Проект Автопарк (учёт автомобилей + трекинг)
-------------

**Технологии:**

- Java
- Spring (Spring Boot, Spring Data JPA)
- Hibernate
- PostgreSQL
- Liquibase
- GraphHopper Directions API
- Leaflet.js

**Описание:**

Есть предприятия. За каждым предприятием закреплены машины, водители (не пересекаются между предприятиями). Машине может быть назначено несколько водителей, а также один активный водитель. Не может быть одного водителя в качестве активного на более чем одной машине.

Аутентификация/авторизация Spring Security.

Менеджер авторизуется в системе, видит список предприятий, которые он администрирует.

Для каждого подконтрольного предприятия может создать/зарегистрировать или удалить машину, обновить данные.

Для каждой машины можно получить список поездок в задаваемом диапазоне дат и тут же построить карту с отображением треков выбранных поездок (библиотека Leaflet.js)

Дополнительно присутствует набор REST-методов (выдача списка машин с пагинацией, получение точек всех поездок из временного диапазона как один супертрек итп)

Список географических точек трека/треков выдается в форматах JSON или geoJSON по выбору.

Дата-время в БД хранится в UTC, выдается на выходе API в таймзонах предприятий, при работе с браузером приводится к локальной таймзоне.

**Spring Shell утилита генерации машин и водителей для списка предприятий.**
Фактические количества формируются случайно на основе задаваемых лимитов. Каждой 10й машине назначается активный водитель.

**Spring Shell утилита генерации реального трека для машины.**
Дописывает в реальном времени точки, например, раз в 10 сек. Конкретизируем машину, нужную длину трека, размер шага, начальную координату. Трек генерируется с помощью роутинга (GraphHopper Directions API). Для начальной и конечной точки дополнительно производится обратное геокодирование с сохранением информации в БД.

**Отчёты.**
Формирование отчета происходит на основе конкретизации машины, типа отчета, календарного интервала, отчетного интервала (день, месяц, год). В программе реализован отчёт по пробегу машины.
Возможно получить список отчетов, отфильтровав по машине, типу отчета, датам, отчетному интервалу. Из списка можно просмотреть любой отчёт.