package obligatorio.entities;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import uy.edu.um.prog2.adt.exceptions.EmptyQueueException;
import uy.edu.um.prog2.adt.lista.ListaEnlazada;
import uy.edu.um.prog2.adt.queue.MyPriorityQueue;
import uy.edu.um.prog2.adt.queue.MyPriorityQueueImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class BettingHouse {

    private static final Logger logger = Logger.getLogger(BettingHouse.class.getName());

// ----------------------------***********************************------------------------------

    // Declaro las Variables de Instancia de la clase BettingHouse.
    //private static final String F1_DATASET = "C:\\Users\\Usuario\\Downloads\\obligatorio2023 (1)\\f1_dataset_test.csv";
    private static final String F1_DATASET = "C:\\Users\\Usuario\\Downloads\\f1_dataset_test_test.csv";
    private static final String DRIVERS_DATASET = "drivers.txt";
    private final ListaEnlazada<Driver> drivers;

    public ListaEnlazada<Tweet> tweets;

    public ListaEnlazada<User> users;
    public ListaEnlazada<HashTag> hashTagsListTotal;

    // ----------------------------***********************************------------------------------

    // Declaro los Constructores de la clase
    public BettingHouse() {
        this.drivers = new ListaEnlazada<>();
        this.tweets = new ListaEnlazada<>();
        this.users = new ListaEnlazada<>();
        this.hashTagsListTotal = new ListaEnlazada<>();
    }

// ----------------------------***********************************------------------------------

    // Comenzamos con los metodos para levantar la info del CSV y del txt.

// ----------------------------***********************************------------------------------

    public void loadDriversData() {
        try {
            File file = new File(DRIVERS_DATASET);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] driver = line.split(" ");

                Driver temp;
                if (driver.length > 2) {
                    // Nyck de Vries es el unico caso donde el array asociado tiene tres elementos.
                    temp = new Driver(driver[0], driver[1] + " " + driver[2]);
                } else {
                    temp = new Driver(driver[0], driver[1]);
                }

                this.drivers.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


// ----------------------------***********************************------------------------------

    public void loadTwitterData() {

        int lineCount = 0;
        int ignoredLineCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(F1_DATASET));
             CSVParser csvParser = CSVParser.parse(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withHeader("tweet_id", "user_name", "user_location", "user_description", "user_created", "user_followers", "user_friends", "user_favourites", "user_verified", "date", "text", "hashtags", "source", "is_retweet"))) {

            for (CSVRecord csvRecord : csvParser) {
                lineCount++;

                try {
                    // Las columnas a extraer
                    long idTweet = Long.parseLong(csvRecord.get("tweet_id"));
                    String userName = csvRecord.get("user_name");
                    boolean usuarioVerificado = Boolean.parseBoolean(csvRecord.get("user_verified"));
                    float favorites = Float.parseFloat(csvRecord.get("user_favourites"));
                    String hashTags = csvRecord.get("hashtags");
                    boolean retweeted = Boolean.parseBoolean(csvRecord.get("is_retweet"));
                    String tweetDate = csvRecord.get("date");
                    String tweetContent = csvRecord.get("text");
                    String tweetSource = csvRecord.get("source");

                    // Eliminamos la hora de la fecha.
                    String[] dateTimeParts = tweetDate.split(" ");
                    String datePart = dateTimeParts[0];
                    tweetDate = datePart;

                    // Separamos los hashtags del tweet en una lista
                    List<String> hashtagList = Arrays.asList(hashTags.toLowerCase().split(", "));
                    ListaEnlazada<HashTag> tweetHashtags = new ListaEnlazada<>();

                    for (String hashtagText : hashtagList) {
                        // Remove the square brackets and single quotes from each hashtag
                        String cleanedHashtag = hashtagText.replaceAll("\\[|'|\\]", "");

                        // Create a new HashTag object
                        HashTag hashtag = new HashTag(cleanedHashtag);

                        // Add the hashtag to the tweet's hashtag list
                        tweetHashtags.add(hashtag);

                        // Add the hashtag to the main list if it's not already present
                        if (!hashTagsListTotal.contains(hashtag)) {
                            hashTagsListTotal.add(hashtag);
                        }
                    }

                    // Creamos el objeto User
                    User user = new User(userName, usuarioVerificado);

                    // Creamos el objeto Tweet
                    Tweet tweet = new Tweet(idTweet, tweetContent, tweetSource, retweeted, user, tweetDate);
                    tweet.setHashTagsOfThisTweet(tweetHashtags);

                    if (users.contains(user)) {
                        User existingUser = users.getObject(user).getValor();
                        existingUser.getUserTweets().add(tweet);
                        user.updateUserFavorites(favorites);
                    } else {
                        user.getUserTweets().add(tweet);
                        user.updateUserFavorites(favorites);
                        users.add(user);
                    }

                    // Agregamos el tweet a la lista de tweets
                    tweets.add(tweet);

                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    ignoredLineCount++;
                    System.err.println("Invalid data at line " + lineCount);
                }
            }

            System.out.println("Total de líneas registradas: " + lineCount);
            System.out.println("Total de líneas ignoradas: " + ignoredLineCount);

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Cantidad de usuarios distintos:");
        System.out.println(users.getSize());
        System.out.println("Cantidad de tweets registrados:");
        System.out.println(tweets.getSize());
    }

    // ----------------------------***********************************------------------------------

    // Debajo se definen los metodos para la formación de los reportes.

// ----------------------------***********************************------------------------------

    // Top 15 usuarios con más tweets
    public void top15UsersWithTheMostTweets() throws EmptyQueueException {

        MyPriorityQueue<User> queue = new MyPriorityQueueImpl<>();

        for (int i = 0; i < users.getSize(); i++) {
            User user = users.get(i);
            int tweetSize = user.getUserTweets().getSize();
            queue.enqueueWithPriority(user, tweetSize);
        }

        for (int i = 0; i < 15 && !queue.isEmpty(); i++) {
            User user = queue.dequeue();
            System.out.println(user.getUserName() + " tiene " + user.getUserTweets().getSize() + " tweets. Verificado?: " + user.isUserVerified());
        }
    }

    // Cantidad de tweets con una palabra o frase específica
    public int countTweetsWithWordOrPhrase(String wordOrPhrase) {
        int count = 0;
        String lowercaseWordOrPhrase = wordOrPhrase.toLowerCase();

        for (Tweet tweet : tweets) {
            String lowercaseContent = tweet.getContent().toLowerCase();
            if (lowercaseContent.contains(lowercaseWordOrPhrase)) {
                count++;
            }
        }

        return count;
    }

    // Buscar tweet por palabra o frase
    public void searchTweetsByWordOrPhrase() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a word or phrase to search for: ");
        String wordOrPhrase = scanner.nextLine();

        int tweetCount = countTweetsWithWordOrPhrase(wordOrPhrase);

        System.out.println("Cantidad de tweets que contienen '" + wordOrPhrase + "': " + tweetCount);
    }

    // Top usuarios con mas favoritos.
    public void printTopUsers(int count) {
        if (count <= 0) {
            System.out.println("Invalid count.");
            return;
        }

        ListaEnlazada<User> sortedList = new ListaEnlazada<>();
        ListaEnlazada<User> temp = new ListaEnlazada<>();

        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            temp.addLast(user);
        }

        for (int i = 0; i < count; i++) {
            User topUser = null;
            float maxFavorites = Long.MIN_VALUE;

            Iterator<User> tempIterator = temp.iterator();
            while (tempIterator.hasNext()) {
                User user = tempIterator.next();
                float favourites = user.getFavourites();

                if (favourites > maxFavorites) {
                    maxFavorites = favourites;
                    topUser = user;
                }
            }

            if (topUser != null) {
                sortedList.addLast(topUser);
                temp.removePorValue(topUser);
            } else {
                break;
            }
        }

        Iterator<User> sortedIterator = sortedList.iterator();
        int i = 1;
        while (sortedIterator.hasNext()) {
            User user = sortedIterator.next();
            System.out.println("Puesto " + i + ": " + user.getUserName() + " - " + user.getFavourites() + " favoritos.");
            i++;
        }
    }

// Hashtag mas utilizado en una fecha dada
    public String getMostUsedHashtagByDate(String date) {
        ListaEnlazada<HashTag> hashtagList = new ListaEnlazada<>();

        for (Tweet tweet : tweets) {
            if (tweet.getDate().equals(date)) {
                ListaEnlazada<HashTag> hashTagsOfThisTweet = tweet.getHashTagsOfThisTweet();

                // Excluimos el hashtag F1
                for (HashTag hashtag : hashTagsOfThisTweet) {
                    if (!hashtag.getText().equalsIgnoreCase("f1")) {
                        hashtagList.add(hashtag);
                    }
                }
            }
        }

        String mostUsedHashtag = "";
        int maxCount = 0;
        for (HashTag hashtag : hashtagList) {
            int count = countOccurrences(hashtag, hashtagList);
            if (count > maxCount) {
                mostUsedHashtag = hashtag.getText();
                maxCount = count;
            }
        }

        return mostUsedHashtag;
    }

    // Cantidad de veces que se utilizó un hashtag en una fecha dada
    private int countOccurrences(HashTag hashtag, ListaEnlazada<HashTag> hashtagList) {
        int count = 0;
        for (HashTag currentHashtag : hashtagList) {
            if (currentHashtag.equals(hashtag) && !currentHashtag.getText().equalsIgnoreCase("f1")) {
                count++;
            }
        }
        return count;
    }

    // Cantidad de hashtags diferentes en una fecha dada
    public int countDifferentHashtagsByDate(String date) {
        ListaEnlazada<HashTag> hashtagList = new ListaEnlazada<>();

        for (Tweet tweet : tweets) {
            if (tweet.getDate().equals(date)) {
                ListaEnlazada<HashTag> hashTagsOfThisTweet = tweet.getHashTagsOfThisTweet();

                // Seguimos excluyendo el hashtag F1
                for (HashTag hashtag : hashTagsOfThisTweet) {
                    if (!hashtag.getText().equalsIgnoreCase("f1")) {
                        hashtagList.add(hashtag);
                    }
                }
            }
        }

        int count = 0;

        ListaEnlazada<HashTag> uniqueHashtags = new ListaEnlazada<>();

        for (HashTag hashtag : hashtagList) {
            if (!uniqueHashtags.contains(hashtag)) {
                uniqueHashtags.add(hashtag);
                count++;
            }
        }

        return count;
    }

    // Top 10 de pilotos con más menciones en un mes y año dado
    public void top10Drivers() throws EmptyQueueException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el mes: ");
        String month = scanner.nextLine();

        System.out.print("Ingrese el año: ");
        String year = scanner.nextLine();

        ListaEnlazada<Tweet> tweetsDelPeriodo = new ListaEnlazada<>();

        for (Tweet tweet : tweets) {
            String[] fechaDelTweetSeparada = tweet.getDate().split("-");
            String monthTweet = fechaDelTweetSeparada[1];
            String yearTweet = fechaDelTweetSeparada[0];
            if (monthTweet.equals(month) && yearTweet.equals(year)) {
                tweetsDelPeriodo.addLast(tweet);
            }
        }

        MyPriorityQueue<Driver> queueDrivers = new MyPriorityQueueImpl<>();

        for (Driver driver : drivers) {
            int count = 0;
            String lowercaseDriverLastName = driver.getLastName().toLowerCase();

            for (Tweet tweet : tweetsDelPeriodo) {
                String lowercaseContent = tweet.getContent().toLowerCase();
                if (lowercaseContent.contains(lowercaseDriverLastName)) {
                    count++;
                }
            }

            queueDrivers.enqueueWithPriority(driver, count);

        }

        System.out.println("Top 10 Drivers:");
        for (int i = 0; i < 10; i++) {
            if(queueDrivers.getSize()>0){
                Driver driver = queueDrivers.getFirst();
                System.out.println("Puesto " + (i+1) + ": " + driver +  " - " + queueDrivers.getFirstPriority() + " menciones.");
                queueDrivers.dequeue();

            }
        }

    }
}