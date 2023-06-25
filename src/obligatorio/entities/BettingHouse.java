package obligatorio.entities;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import obligatorio.keys.KeyHashTag;
import obligatorio.keys.KeyUser;
import uy.edu.um.prog2.adt.hash.HashEntry;
import uy.edu.um.prog2.adt.hash.MyCloseHashImpl;
import uy.edu.um.prog2.adt.hash.MyHash;
import uy.edu.um.prog2.adt.heap.HeapEntry;
import uy.edu.um.prog2.adt.heap.MyHeap;
import uy.edu.um.prog2.adt.heap.MyHeapImpl;
import uy.edu.um.prog2.adt.lista.ListaEnlazada;
import uy.edu.um.prog2.adt.lista.MyList;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.regex.Pattern;

public class BettingHouse {


// ----------------------------***********************************------------------------------

    // Declaro las Variables de Instancia de la clase BettingHouse.
    private static final String F1_DATASET = "f1_dataset_test_test.csv";
    private static final String currentDirectory = System.getProperty("user.dir");

    // Concatenar la ruta actual con el nombre del archivo
    private static final String F1_DATA = currentDirectory+ "\\" + F1_DATASET;
    private static final String DRIVERS_DATASET = "drivers.txt";

    private static final String DRIVERS = currentDirectory + "\\" + DRIVERS_DATASET;

    private MyList<Driver> drivers;

    /* Se utiliza TAD del tipo Hash para almacenar objetos de la clase "tweet", "user" y "hashTags.
       La razón de su elección radica en su eficiencia a la hora de acceder a los elementos que
       almacena (  O(1) en el mejor caso y en el promedio y O(n) en el peor caso).
       En un primer intento se utilizaron ListasEnlazadas y los costos en tiempo de ejecución resultaron
       muy grandes. */
    private MyHash<Long, Tweet> tweets;

    /* Debido a que la Key del hash será de la clase String en el caso de "user" y "hashTags", podría pasar
       que dicho "key" fuese negativo. Debo asegurarme que ello no suceda, por lo que me crearé una clase
       "KeyUser" y "KeyHashTag" en las cuales tienen como insumo/argumento un string y el mismo es modificado
       por el método de esa clase (hashCode) para asegurarse que el valor sea SIEMPRE positivo. */
    private MyHash<KeyUser, User> users;
    private MyHash<KeyHashTag, HashTag> hashTags;

// ----------------------------***********************************------------------------------

    // Declaro los Constructores de la clase
    public BettingHouse() {
        this.drivers = new ListaEnlazada<>();
        this.tweets = new MyCloseHashImpl<>(107857, true);  // size del hash = 107857  debe ser impar
        this.users = new MyCloseHashImpl<>(11599, true);
        this.hashTags = new MyCloseHashImpl<>(101599, true);
    }

// ----------------------------***********************************------------------------------

    // DECLARAMOS LOS MÉTODOS DE LA CLASE

// ----------------------------***********************************------------------------------

    // A continuación cargaremos la información contenida en los archivos TXT y CSV.

    // Debemos utilizar si o si "Try & Catch", de manera tal que el "catch" nos atrape
    // todas la excepciones que puedan surgir por problemas de formato en los archivos TXT y CSV.

    /* Primero cargamos la información de los pilotos (nombre y apellido) del archivo
       DRIVERS_DATASET   mediante el método   "loadDriversData".
       Para ello utilizamos la clase FileReader para leer los caracteres del archivo.
       La clase BufferedReader se utiliza para acceder a cada línea del archivo y la
       guardamos en la variable "line" para procesar cada linea.*/

    public void loadDriversData() {
        try {
            File file = new File(DRIVERS);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            // Separo cada linea por los espacios en blanco y guardo cada segmento en un array, el cual
            // tendrá como primer elemento el nombre y los siguientes serán los apellidos.
            while ((line = bufferedReader.readLine()) != null) {
                String[] driver = line.split(" ");

                // Declaro una instancia de la clase Driver, la cual se inicializará tantas veces como
                // lineas en bufferReader existan (cada una es un piloto) siendo su nombre y apellido
                // los elementos del array "driver".
                Driver temp;
                if (driver.length > 2) {
                    // Nyck de Vries   es el unico caso donde el array asociado tiene tres elementos.
                    temp = new Driver(driver[0], driver[1] + " " + driver[2]);
                } else {
                    temp = new Driver(driver[0], driver[1]);
                }
                // finalmente, se agrega cada instancia de Driver creada a la lista enlazada de drivers
                // (uno por uno) de la clase BettinHouse.
                this.drivers.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


// ----------------------------***********************************------------------------------


    /* Para cargar la información de los tweets (usuario, contenido, fecha, hashtags, etc) contenida
       en el archivo CSV utilizaremos la libreria "opencsv" que debe descargarse especialmente desde el
       repositorio MAVEN */


    public void loadTwitterData() {

        // Los   "id"  comienzan en 1 y se incrementean en una unidad cada vez que una nueva instancia es creada.
        long idUser = 1;
        long idHashTag = 1;

        // Implementamos un Try & Catch para que nos atrape mediante excepciones los errores de formato
        // y otros percances que pudieran existir en el archivo CSV.

        // Con métodos propios de la clase CSVReader separamos el contenido del archivo CSV por "," y gracias
        // al método de la clase CSVReader podemos confiar en que la "," por la que separa es verdaderamente
        // una coma separadora y no una que legítimamente pertenece a la información relevante que contiene
        // el archivo.
        try (
                CSVReader csvReader = new CSVReaderBuilder(new FileReader(F1_DATA))
                        .withCSVParser(new CSVParserBuilder().withSeparator(',').build())
                        .build()
        ) {
            // Hacemos que primero lea los headers para que estos NO sean considerados en el while loop.
            String[] line = csvReader.readNext();

            // A partir de la segunda fila del archivo "F1_DATASET", comienza a leer la información FILA a FILA
            // (o línea a línea) y a procesarla de acuerdo a nuestros requerimientos, que resultan ser:
            //  userName (columna 1), usuarioVerificado (columna 8), favoritos (columna 7), hashTags (columna 11),
            //  fueRetweeteado (columna 13), fechaDelTweet (columna 9), idTweet (columna 0),
            //  contenidoDelTweet (columna 10)  y  sourceDelTweet (columna 12).
            while ((line = csvReader.readNext()) != null) {
                try {
                    String userName = line[1];
                    boolean isVerified = Boolean.parseBoolean(line[8]);
                    float favorites = Float.parseFloat(line[7]);
                    String[] tempHashTags = line[11]
                            .replace("[", "")
                            .replace("]", "")
                            .replace("'", "")
                            .trim()   // Trim elimina los espacios en blanco al principio y al final.
                            .split(",");

                    boolean isRetweet = Boolean.parseBoolean(line[13]);

                    // CSV tiene error en algunos campos fechas. Aparece tanto en formato
                    //   "yyyy-MM-dd HH:mm:ss"   como     "yyyy-MM-dd H:mm:ss"
                    // Es por ello que al correr la consulta 2  para el archivo   .test
                    // el usuario F1reader aparece 139 veces en lugar de 974.

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = formatter.parse(line[9]);

                    long tweetId = Long.parseLong(line[0]);
                    String content = line[10];
                    String source = line[12];

                    // La "Key" para buscar en el "Hash de los usuarios" será el nombre del usuario.
                    // "tempUser" será una instancia temporal de Usuario.
                    KeyUser userKey = new KeyUser(userName);

                    // Busca en el hash por "userKey"  que es el nombre del usuario pasado por la Hash Function.
                    User tempUser = this.users.get(userKey);

                    // Si el usuario NO existe en el hash de usuarios de la clase BettingHouse:
                    if (tempUser == null) {

                        // Se crea el nuevo usuario
                        User newUser = new User(idUser, userName, isVerified);

                        // Se inserta en el hash de user una nueva instancia de Usuario donde su "key" es
                        // userKey (que es el nombre del usuario pasado por la Hash Function) y su valor
                        // esl objeto/instancia de la clase Usuario (newUser).
                        this.users.put(userKey, newUser);
                        idUser += 1;
                    }

                    // Si el usuario YA existe (porque existía desde el comienzo o empezó a existir luego
                    // de haber sido creado en el  "if" anterior) en el hash de usuarios de la clase
                    // BettingHouse:
                    if (this.users.get(userKey) != null) {

                        // Se asigna el usuario encontrado a la variable "userTemp"
                        User userTemp = this.users.get(userKey);

                        // Al tratarse de un Hash Cerrado, cada vez que se inserta un elemento que ya existe
                        // en el Hash, el mismo es actualizado.

                        // Se actualiza la lista de "favoritos" asociado a la instancia de Usuario (userTemp).
                        userTemp.updateUserFavorites(favorites);


                        // Creo la instancia de la clase Tweet utilizando la información que extraje del
                        // archivo CSV
                        Tweet tempTweet = new Tweet(tweetId, content, source, isRetweet, userTemp, date);

                        // Creo una lista enlazada donde guardaré los HashTagas asociados a la instancia de
                        // la clase Tweet que acabo de crear (tempTweet).
                        MyList<HashTag> tempHashTagForTweet = new ListaEnlazada();

                        // Itero por cada elemento "text" (puede ser cualquier nombre) en la lista tempHashTags,
                        // la cual fue previamente creada y contiene todos los hashTags asociados a cada tweet
                        // o lo que es lo mismo, cada fila del archivo CSV.
                        // Este bucle for-each itera sobre los elementos del arreglo tempHashTags. En cada
                        // iteración, el elemento actual se asigna a la variable "text".
                        for (String text : tempHashTags) {

                            // Mientras en "text" NO se encuentre guardado un "vacío"  (  ""  ), es decir, que en
                            // "text" haya una cadena de String
                            if (!text.isEmpty()) {

                                // El método trim() elimina los espacios en blanco al principio y al final de la cadena.
                                String hashTagText = text.trim();

                                // El Key para los elementos que componen el Hash Map de hashTags será el propio
                                // hashtag (por ejemplo, "F1") que es insumo de la función "hashTags" perteneciente
                                // a la clase "hashTagKey".
                                KeyHashTag hashTagKey = new KeyHashTag(hashTagText);

                                // Busco para cada hashtag que se encuentra en la línea del CSV que estoy leyendo
                                // si el mismo existe en el HashMap de hashtags de la clase BettingHouse.
                                HashTag tempHashTag = this.hashTags.get(hashTagKey);

                                // Si encuentra al objeto de la clase HashTag dentro del HashMap de hashtags de la
                                // clase BettingHouse:
                                if (tempHashTag != null) {

                                    // Le agrega a la instancia de hashTag (tempHashTag) una nueva fecha a la lista
                                    // de fechas que él tiene asociadas y lo vuelve a enviar al HashMap de la clase
                                    // BettingHouse (dado que ya existe, se actualizará su información).
                                    tempHashTag.addHashTagDate(date);
                                    this.hashTags.put(hashTagKey, tempHashTag);

                                    // Agrego la instancia de la clase HashTag (tempHashTag) a la ListaEnlazada de
                                    // objetos HashTag que cree en la línea 200
                                    tempHashTagForTweet.add(tempHashTag);

                                } // Si NO encuentra al objeto de la clase HashTag dentro del HashMap de hashtags de la
                                // clase BettingHouse:
                                else {

                                    // Crea una instancia temporal de la clase HashTag a la cual se le agrega la
                                    // fecha a su correspondiente lista enlazada de fechas.
                                    HashTag temp = new HashTag(idHashTag, hashTagText);
                                    temp.addHashTagDate(date);

                                    // Agrego el hashTag al HashMap de hashTags de la clase BettingHouse.
                                    this.hashTags.put(hashTagKey, temp);

                                    // Agrego la instancia de la clase HashTag (tempHashTag) a la ListaEnlazada de
                                    // objetos HashTag que cree en la línea 200
                                    tempHashTagForTweet.add(temp);
                                    idHashTag += 1;
                                }
                            }
                        }

                        // Le asigno a la instancia de la clase Tweeter creada (tempTweet) su ListaEnlazada de
                        // HashTags, que los fui guardando en la ListaEnlazada "temHashTagForTweet".
                        tempTweet.setHashTagsOfThisTweet(tempHashTagForTweet);

                        // Una vez la instancia de tweet fue creada y completada correctamente (con todos sus hashTags
                        // y a su vez estos completados con todas sus fechas), agrego el objeto de la clase Tweeter al
                        // HashMap de Tweeters de la clase BettingHouse. En este caso, la "key" del tweeter será su
                        // propia "id" porque ésta nunca será negativa ni se repetirá, y el valor guardado/asociado a
                        // dicha "key", será el mismísimo objeto de la clase Tweet (tempTweet).
                        this.tweets.put(tweetId, tempTweet);

                        // Finalmente, agrego el tweet creado a la ListaEnlazada de objetos Tweet asociados al usuario
                        // (userTemp) y agrego al hash al usuario actualizado con su lista.
                        // El elemento sera actualizado con los nuevos valores.
                        userTemp.getUserTweets().add(tempTweet);
                        this.users.put(userKey, userTemp);
                    }

                    // Los siguientes "catch" atraparán las excepciones que aparezcan por errores de
                    // formato (u otros) en el archivo CSV.

                } catch (Exception e) {
                    System.out.println("Error en la linea con id: " + line[0]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");

        } catch (IOException e) {
            System.out.println("IO Exception");

        } catch (CsvValidationException e) {
            System.out.println("CSV Validation Exception");

        }
    }

// ----------------------------***********************************------------------------------

    // Terminamos con los métodos para cargar la información cuya fuente son archivos externos (TXT y CSV)
    // y pasamos a definir las funciones/métodos de CONSULTA.


    public void distinctHashTagsForDay(Date date) {

        // Inciamos el reloj del tiempo y el contador de utilización de memoria RAM.
        long timeStart, timeEnd;
        timeStart = System.currentTimeMillis();

        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = memoryBean.getHeapMemoryUsage();


        // Creamos una ListaEnlazada donde guardaremos al final los valores de los diferentes hashTags
        // asociados a una fecha determinada.

        // Creamos una array llamado "tempList" que contendrá TODOS los elementos del HashMap (es decir,
        // los "nodos" o "HashEntry" que se encuentran en el array subyacente del Hashmap. Son objetos
        // con un "key" y un "value", donde en este caso la "key" es el resultado de pasar el texto del
        // hashTag por la HashFunction y el value es el propio objeto de la clase HashTag).
        MyList<String> list = new ListaEnlazada<>();
        HashEntry<KeyHashTag, HashTag>[] tempList = this.hashTags.getValues();

        // Iteramos por cada uno de los elementos de la lista "tempList" que contiene objetos HashEntry.
        for (int i = 1; i < tempList.length; i++) {
            if (tempList[i] != null) {

                // Accedemos al VALOR del elemento en la posición "i" de la lista. Es decir, al objeto de la
                // clase HashTag y en particular a la lista de fechas que tiene asociadas.
                for (Date dateHashTag : tempList[i].getValue().getDatesOfHashTag()) {

                    // Si la fecha coincide con alguna de las fechas asociadas al HashTag
                    if (dateHashTag.equals(date)) {

                        // Si en la lista NO se encuentra el texto asociado al HashTag (que es el propio
                        // HashTag, por ejemplo, "F1", "QatarGP") entonces se agrega a la lista.
                        if (!list.contains(tempList[i].getValue().getText())) {
                            list.add(tempList[i].getValue().getText());
                        }
                    }
                }
            }
        }
        System.out.println("Cantidad de hashTags distintos para el día: " + list.getSize());

        timeEnd = System.currentTimeMillis();
        System.out.println("Tiempo de ejecucion de la consulta: " + (timeEnd - timeStart) + " milisegundos");

        long memoryUsed = memoryUsage.getUsed();
        System.out.println("Memoria utilizada: " + memoryUsed + " bytes");

    }


    // ----------------------------***********************************------------------------------


    public void top10Drivers(YearMonth date) throws DateTimeParseException {

        // Inciamos el reloj del tiempo y el contador de utilización de memoria RAM.
        long timeStart, timeEnd;
        timeStart = System.currentTimeMillis();

        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = memoryBean.getHeapMemoryUsage();

        // Creamos una array llamado "tempList" que contendrá TODOS los elementos del HashMap (es decir,
        // los "nodos" o "HashEntry" que se encuentran en el array subyacente del Hashmap. Son objetos
        // con un "key" y un "value", donde en este caso la "key" es el resultado de pasar el id del
        // hashTag por la HashFunction y el value es el propio objeto de la clase Tweet).
        HashEntry<Long, Tweet>[] tempList = this.tweets.getValues();
        MyCloseHashImpl<Driver, Integer> mencionesPorPiloto = new MyCloseHashImpl<>(0, true);

        for (HashEntry<Long, Tweet> tweet : tempList) {
            if (tweet != null) {
                Date dateTweet =  tweet.getValue().getDate();

                // Obtener el mes
                SimpleDateFormat sdfMes = new SimpleDateFormat("MMMM", Locale.ENGLISH);
                String mesTweet = sdfMes.format(dateTweet);

                // Obtener el año
                SimpleDateFormat sdfAnio = new SimpleDateFormat("yyyy");
                String anioTweet = sdfAnio.format(dateTweet);

                // Obtener el año y el mes
                String nombreMes = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                String year = String.valueOf(date.getYear());

                if (mesTweet.equals(nombreMes) && anioTweet.equals(year)){
                    String contenidoTweet = tweet.getValue().getContent().toLowerCase();
                    for (Driver driver : drivers) {
                        if (contenidoTweet.contains(driver.getName().toLowerCase()) || contenidoTweet.contains(driver.getLastName().toLowerCase())) {
                            Integer menciones = mencionesPorPiloto.get(driver);
                            mencionesPorPiloto.put(driver, menciones != null ? menciones + 1 : 1);
                        }
                    }
                }
            }
        }

        LinkedList<HashEntry<Driver, Integer>> topPilotos = new LinkedList<>();

        for (HashEntry<Driver, Integer> entry : mencionesPorPiloto.getValues()) {
            if (entry != null && !entry.isDeleted()) {
                topPilotos.add(entry);
            }
        }

        // Ordenar la lista de top pilotos por el número de menciones en orden descendente
        topPilotos.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        int count = 0;
        for (HashEntry<Driver, Integer> entry : topPilotos) {
            if (count >= 10) {
                break;
            }
            Driver piloto = entry.getKey();
            int menciones = entry.getValue();
            System.out.println("Piloto: " + piloto.getName() + " " + piloto.getLastName() + ", Menciones: " + menciones);
            count++;
        }

        timeEnd = System.currentTimeMillis();
        System.out.println("Tiempo de ejecucion de la consulta: " + (timeEnd - timeStart) + " milisegundos");

        long memoryUsed = memoryUsage.getUsed();
        System.out.println("Memoria utilizada: " + memoryUsed + " bytes");
    }


    // ----------------------------***********************************------------------------------
    public void mostUsedHashTagForDay(Date date) {

        // Inciamos el reloj del tiempo y el contador de utilización de memoria RAM.
        long timeStart, timeEnd;
        timeStart = System.currentTimeMillis();

        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = memoryBean.getHeapMemoryUsage();

        // Tamaño del HashMap que contiene a los objetos de la clase HashTag
        int size = this.hashTags.size();

        // Array que contiene TODOS los elementos del tipo HashEntry que se encuentran en el HashMap
        HashEntry<KeyHashTag, HashTag>[] tempList = this.hashTags.getValues();

        // Lista vacía con el mismo tamaño del HashMap de hasTags que utilizaré a continuación para
        // crear un heap vacío donde almacenaré objetos del tipo "HeapEntry" que a su vez contendrán objetos
        // de la clase HashTag y se comportará como un heapMax.
        HeapEntry<HashTag>[] list = new HeapEntry[size];
        MyHeap<HeapEntry<HashTag>> heapQuery = new MyHeapImpl<>(list, true);

        // Itero sobre cada elemento del array que contiene los objetos HashEntry de hashTag
        for (int i = 1; i < tempList.length; i++) {
            if (tempList[i] != null) {
                int count = 0;
                // Accedo al objeto de la clase HashTag que está contenido como el "value" en el objeto HashEntry
                HashTag temp = tempList[i].getValue();
                // Itero sobre las fechas asociadas a ESA instancia de la clase HashTag
                for (Date dateHashTag : temp.getDatesOfHashTag()) {
                    if (dateHashTag.equals(date)) {
                        // Acumulo +1 cada vez que aparece la fecha indicada por el usuario en la lista de fechas
                        // que ESE hashTag en particular tiene asociadas.
                        count += 1;
                    }
                }

                // Al terminar de verificar la cantidad de veces que un hashtag particular ha sido nombrado en
                // una fecha determinada, se agrega dicha instancia de "HashTag" asociada a la
                // "key = cantidad de veces que fue nombrado" al heapMax.
                HeapEntry<HashTag> node = new HeapEntry<>(count, temp);
                heapQuery.insert(node);
            }
        }

        // Al terminar de verificar todos los hashTagsy habiendo completado el heapMax, ya tenemos los elementos
        // ordenados de acuerdo a la cantidad de veces que son nombrados dichos hashTags para una
        // fecha particular. El top 10 lo obtenemos extrayendo los elementos de las 10 "raices" del heapMax
        boolean hashTagFound = false;
        while (!hashTagFound) {
            HeapEntry<HashTag> temp = heapQuery.deleteAndReturn();
            if (temp != null && (!temp.getValue().getText().equals("f1") && !temp.getValue().getText().equals("F1"))) {
                System.out.println("HashTag: " + temp.getValue().getText());
                hashTagFound = true;
            }
        }

        timeEnd = System.currentTimeMillis();
        System.out.println("Tiempo de ejecucion de la consulta: " + (timeEnd - timeStart) + " milisegundos");

        long memoryUsed = memoryUsage.getUsed();
        System.out.println("Memoria utilizada: " + memoryUsed + " bytes");

    }


    // ----------------------------***********************************------------------------------

    public void top15UsersWithMoreTweets() {


        // Inciamos el reloj del tiempo y el contador de utilización de memoria RAM.
        long timeStart, timeEnd;
        timeStart = System.currentTimeMillis();

        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = memoryBean.getHeapMemoryUsage();


        int size = this.users.size();

        // Array que contiene TODOS los elementos del tipo HashEntry que se encuentran en el HashMap
        HashEntry<KeyUser, User>[] tempList = this.users.getValues();

        // Lista vacía con el mismo tamaño del HashMap de hasTags que utilizaré a continuación para
        // crear un heap vacío donde almacenaré objetos del tipo "HeapEntry" que a su vez contendrán objetos
        // de la clase User y se comportará como un heapMax.
        HeapEntry<User>[] list = new HeapEntry[size];
        MyHeap<HeapEntry<User>> heapQuery = new MyHeapImpl<>(list, true);

        // Itero sobre cada elemento del array que contiene los objetos HashEntry de User
        for (int i = 1; i < tempList.length; i++) {
            if (tempList[i] != null) {
                // Accedo al valor del elemento HashEntry en la posición "i", que será el propio objeto
                // de la clase User
                User temp = tempList[i].getValue();
                // Creo un objeto HeapEntry cuyo "key" será el tamaño de la lista de tweets asociada a
                // ESA instancia de la clase User y el "value" será el objeto/instancia de User
                HeapEntry<User> node = new HeapEntry<>(temp.getUserTweets().getSize(), temp);
                // Inserto la instancia de HeapEntry creada anteriormente y llamada "nodo" al heapMax (llamado
                // heapQuery).
                heapQuery.insert(node);
            }
        }

        // Para los primeros 15 elementos (raices) del heap:
        for (int i = 1; i <= 15; i++) {
            // Extraigo el elemento que se encuentra en la raiz y lo elimino, lo que llevará a que se recomponga
            // el heap con un nuevo elemento máximo en la raíz.
            HeapEntry<User> temp = heapQuery.deleteAndReturn();

            // Si no hay mas de 15 elementos, el delete del heap retorna null, por lo tanto no podria imprimir.
            if (temp != null) {
                System.out.println("Posicion: " + i);
                System.out.println("    Nombre de usuario: " + temp.getValue().getUserName());
                System.out.println("    Cantidad de tweets: " + temp.getValue().getUserTweets().getSize());
                System.out.println("    Usuario verificado: " + temp.getValue().isUserVerified());
            }
        }

        timeEnd = System.currentTimeMillis();
        System.out.println("Tiempo de ejecucion de la consulta: " + (timeEnd - timeStart) + " milisegundos");

        long memoryUsed = memoryUsage.getUsed();
        System.out.println("Memoria utilizada: " + memoryUsed + " bytes");

    }

    public void top7UsersWithMoreFavorites() {

        // Inciamos el reloj del tiempo y el contador de utilización de memoria RAM.
        long timeStart, timeEnd;
        timeStart = System.currentTimeMillis();

        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = memoryBean.getHeapMemoryUsage();


        int size = this.users.size();

        // Array que contiene TODOS los elementos del tipo HashEntry que se encuentran en el HashMap
        HashEntry<KeyUser, User>[] tempList = this.users.getValues();

        // Lista vacía con el mismo tamaño del HashMap de hasTags que utilizaré a continuación para
        // crear un heap vacío donde almacenaré objetos del tipo "HeapEntry" que a su vez contendrán objetos
        // de la clase User y se comportará como un heapMax.
        HeapEntry<User>[] list = new HeapEntry[size];
        MyHeap<HeapEntry<User>> heapQuery = new MyHeapImpl<>(list, true);

        // Itero sobre cada elemento del array que contiene los objetos HashEntry de User
        for (int i = 1; i < tempList.length; i++) {
            if (tempList[i] != null) {
                // Accedo al valor del elemento HashEntry en la posición "i", que será el propio objeto
                // de la clase User
                User temp = tempList[i].getValue();
                // Creo un objeto HeapEntry cuyo "key" será el tamaño de la lista de tweets asociada a
                // ESA instancia de la clase User y el "value" será el objeto/instancia de User
                HeapEntry<User> node = new HeapEntry<>(temp.getFavourites(), temp);
                // Inserto la instancia de HeapEntry creada anteriormente y llamada "nodo" al heapMax (llamado
                // heapQuery).
                heapQuery.insert(node);
            }
        }

        // Para los primeros 7 elementos (raices) del heap:
        for (int i = 1; i <= 7; i++) {
            // Extraigo el elemento que se encuentra en la raiz y lo elimino, lo que llevará a que se recomponga
            // el heap con un nuevo elemento máximo en la raíz.
            HeapEntry<User> temp = heapQuery.deleteAndReturn();
            // Si no hay mas de 7 elementos, el delete del heap retorna null, por lo tanto no podria imprimir.
            if (temp != null) {
                System.out.println("Posicion: " + i);
                System.out.println("    Nombre de usuario: " + temp.getValue().getUserName());
                System.out.println("    Cantidad de favoritos: " + temp.getValue().getFavourites());
            }
        }

        timeEnd = System.currentTimeMillis();
        System.out.println("Tiempo de ejecucion de la consulta: " + (timeEnd - timeStart) + " milisegundos");

        long memoryUsed = memoryUsage.getUsed();
        System.out.println("Memoria utilizada: " + memoryUsed + " bytes");

    }

// ---------------------************* IMPORTANTE!!!!!!!!!!!!! ************---------------------

    // ESTA FUNCION ES LA QUE NO ESTÁ TERMINADA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // REVISAR!!!!!!!!!!!!!!!!!!!!!
    public void tweetsWithSpecificContent(String content) {

        long timeStart, timeEnd;
        timeStart = System.currentTimeMillis();

        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = memoryBean.getHeapMemoryUsage();


        int count = 0;
        Pattern pattern = Pattern.compile(Pattern.quote(content), Pattern.CASE_INSENSITIVE);
        HashEntry<Long, Tweet>[] tempList = this.tweets.getValues();

        for (int i = 1; i < tempList.length; i++) {
            if (tempList[i] != null && pattern.matcher(tempList[i].getValue().getContent()).find()) {
                count += 1;
            }
        }
        System.out.println("Cantidad de tweets con la palabra " + content + ": " + count);

        timeEnd = System.currentTimeMillis();
        System.out.println("Tiempo de ejecucion de la consulta: " + (timeEnd - timeStart) + " milisegundos");

        long memoryUsed = memoryUsage.getUsed();
        System.out.println("Memoria utilizada: " + memoryUsed + " bytes");
    }

    public MyList<Driver> getDrivers() {
        return drivers;
    }

    public MyHash<KeyUser, User> getUsers() {
        return users;
    }

    public MyHash<Long, Tweet> getTweets() {
        return tweets;
    }

    public MyHash<KeyHashTag, HashTag> getHashTags() {
        return hashTags;
    }
}