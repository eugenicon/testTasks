1. Отправить запрос методом POST в виде JSON на http://tripcomposer.net/rest/test/countries/get.
2. Полученный ответ сохранить в БД (PostgreSQL).

Request:

| Name          | Type        | Description                                            |
| ------------- |:-----------:| ------------------------------------------------------ |
| key           | String      |ключ API, key = $1$12309856$euBrWcjT767K2sP9MHcVS/      |
| echo          | String      |любой текст, будет вложен в ответ (необязательное поле) |

Response:

| Name          | Type        | Description                                            |
| ------------- |:-----------:| ------------------------------------------------------ |
| time          | Long        |время, когда был сформирован ответ                      |
| countries     | Country[ ]  |страны (массив, список)                                 |
| echo          | String      |текст, что был отправлен в запросе                      |

Country:

| Name          | Type        | Description                                            |
| ------------- |:-----------:| ------------------------------------------------------ |
| countryName   | String      |название страны                                         |
| countryISOCode| String      |код страны согласно ISO 3166­1 alpha­2                    |
| echo          | City[ ]     |города страны (массив, список)                          |

City:

| Name          | Type        | Description                                            |
| ------------- |:-----------:| ------------------------------------------------------ |
| cityName      | String      |название города                                         |

В БД сохранить Страны и связанные с ними Города из ответа сервера (в таблицы

Country и City).

Текст приложения выложить на github, bitbucket или другие подобные сервисы.

Используемые технологии:

- PostgreSQL

- Hibernate или EclipseLink

- Spring или EJB (JavaEE)

Request Example (JSON):
```json
{
  "key": "$1$12309856$euBrWcjT767K2sP9MHcVS/",
  "echo": "test1234"
}
```
Response Example (JSON):
```json
{
  "type": "testResponse",
  "time": 1445540558877,
  "countries": [
  {
    "countryName": "Ukraine",
    "countryISOCode": "UA",
    "cities": [
      {
        "cityName": "Kyiv"
      },
      {
        "cityName": "Kharkiv"
      },
      {
       "cityName": "Dnipropetrovsk"
      },
      {
        "cityName": "Odesa"
      }
    ]
  },
  {
    "countryName": "Germany",
    "countryISOCode": "DE",
    "cities": [
      {
        "cityName": "Berlin"
      },
      {
        "cityName": "Hamburg"
      },
      {
        "cityName": "Munchen"
      },
      {
        "cityName": "Koln"
      }
    ]
  }
],
  "echo": "test1234"
}
```
