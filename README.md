## README

Este README busca brindar una mirada general del proyecto.

## BettingHouse

La clase `BettingHouse` representa una casa de apuestas y contiene métodos relacionados con la carga de datos y generación de reportes.

### Variables de Instancia

- `F1_DATASET` (String): Ruta del conjunto de datos CSV de Fórmula 1.
- `DRIVERS_DATASET` (String): Ruta del archivo de texto que contiene los datos de los pilotos.
- `drivers` (ListaEnlazada\<Driver\>): Lista enlazada que almacena los pilotos - objetos `Driver`.
- `tweets` (ListaEnlazada\<Tweet\>): Lista enlazada que almacena los tweets - objetos `Tweet`.
- `users` (ListaEnlazada\<User\>): Lista enlazada que almacena los usuarios - objetos `User`.
- `hashTagsListTotal` (ListaEnlazada\<HashTag\>): Lista enlazada que almacena los hashtags - objetos `HashTag`.

### Métodos

- `loadDriversData()`: Carga los datos de los pilotos desde el archivo de texto.
- `loadTwitterData()`: Carga los datos de los tweets desde el conjunto de datos CSV.
- `top15UsersWithTheMostTweets()`: Muestra los 15 usuarios con más tweets.
- `top15UsersWithTheMostTweetsFaster()`: Muestra los 15 usuarios con más tweets de forma más rápida.
- `countTweetsWithWordOrPhrase(String wordOrPhrase)`: Cuenta la cantidad de tweets que contienen una palabra o frase específica.
- `searchTweetsByWordOrPhrase()`: Busca tweets por una palabra o frase ingresada por el usuario.
- `printTopUsers(int count)`: Muestra los usuarios con más favoritos.
- `getMostUsedHashtagByDate(String date)`: Obtiene el hashtag más utilizado en una fecha dada.
- `countOccurrences(HashTag hashtag, ListaEnlazada\<HashTag\> hashtagList)`: Cuenta la cantidad de veces que se utiliza un hashtag en una lista dada.
- `countDifferentHashtagsByDate(String date)`: Cuenta la cantidad de hashtags diferentes en una fecha dada.
- `top10Drivers()`: Muestra los 10 pilotos con más menciones en un mes y año especificados.

### Clase Principal

La clase `Principal` es la clase de inicio del programa y contiene el método `main`. Permite al usuario interactuar con las opciones del programa, como cargar los datos, generar informes y salir del programa.
  
  
  
