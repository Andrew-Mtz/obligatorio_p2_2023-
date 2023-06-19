package obligatorio.entities;

import obligatorio.keys.KeyUser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import uy.edu.um.prog2.adt.hash.MyCloseHashImpl;
import uy.edu.um.prog2.adt.hash.MyHash;
import uy.edu.um.prog2.adt.lista.ListaEnlazada;
import uy.edu.um.prog2.adt.lista.MyList;

import java.io.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Logger;

public class BettingHouse {

    private static final Logger logger = Logger.getLogger(BettingHouse.class.getName());

// ----------------------------***********************************------------------------------

    // Declaro las Variables de Instancia de la clase BettingHouse.
    private static final String F1_DATASET = "C:\\Users\\Usuario\\Downloads\\obligatorio2023 (1)\\f1_dataset_test.csv";
    private static final String DRIVERS_DATASET = "drivers.txt";
    private final MyList<Driver> drivers;

    public ListaEnlazada<Tweet> tweets;

    /* Debido a que la Key del hash será de la clase String en el caso de "user" y "hashTags", podría pasar
       que dicho "key" fuese negativo. Debo asegurarme que ello no suceda, por lo que me crearé una clase
       "KeyUser" y "KeyHashTag" en las cuales tienen como insumo/argumento un string y el mismo es modificado
       por el método de esa clase (hashCode) para asegurarse que el valor sea SIEMPRE positivo. */
    public ListaEnlazada<User> users;
    public ListaEnlazada<HashTag> hashTagsListTotal;
    private final MyHash<KeyUser, Integer> userTweetCounts;

    // ----------------------------***********************************------------------------------

    // Declaro los Constructores de la clase
    public BettingHouse() {
        this.drivers = new ListaEnlazada<>();
        this.tweets = new ListaEnlazada<>();
        this.users = new ListaEnlazada<>();
        this.hashTagsListTotal = new ListaEnlazada<>();
        this.userTweetCounts = new MyCloseHashImpl<>(11599, true);
    }

// ----------------------------***********************************------------------------------

    // DECLARAMOS LOS MÉTODOS DE LA CLASE

// ----------------------------***********************************------------------------------

    public void loadDriversData() {
        try {
            File file = new File(DRIVERS_DATASET);
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

    public void loadTwitterData() {
        long idHashTag = 1;

        int lineCount = 0;
        int ignoredLineCount = 0;

        try (Reader reader = new FileReader(F1_DATASET);
             CSVParser csvParser = CSVParser.parse(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withHeader("tweet_id", "user_name", "user_location", "user_description", "user_created", "user_followers", "user_friends", "user_favourites", "user_verified", "date", "text", "hashtags", "source", "is_retweet"))) {

            for (CSVRecord csvRecord : csvParser) {
                lineCount++;

                try {
                    // Extract specific columns
                    long idTweet;
                    String userName = csvRecord.get("user_name");
                    boolean usuarioVerificado;
                    float favorites;
                    String hashTags = csvRecord.get("hashtags");
                    boolean retweeted;
                    String tweetDate;
                    String tweetContent = csvRecord.get("text");
                    String tweetSource = csvRecord.get("source");

                    // Check if the tweet ID matches the desired value
                    if (csvRecord.isSet("tweet_id")) {
                        try {
                            idTweet = Long.parseLong(csvRecord.get("tweet_id"));
                        } catch (NumberFormatException e) {
                            throw new RuntimeException("Invalid tweet_id at line " + lineCount);
                        }
                    } else {
                        throw new RuntimeException("Missing tweet_id at line " + lineCount);
                    }

                    if (csvRecord.isSet("user_verified")) {
                        String verifiedValue = csvRecord.get("user_verified").toLowerCase();
                        if (verifiedValue.equals("true") || verifiedValue.equals("false")) {
                            usuarioVerificado = Boolean.parseBoolean(verifiedValue);
                        } else {
                            throw new RuntimeException("Invalid user_verified value at line " + lineCount);
                        }
                    } else {
                        throw new RuntimeException("Missing user_verified at line " + lineCount);
                    }

                    if (csvRecord.isSet("user_favourites")) {
                        try {
                            favorites = Float.parseFloat(csvRecord.get("user_favourites"));
                        } catch (NumberFormatException e) {
                            throw new RuntimeException("Invalid user_favourites at line " + lineCount);
                        }
                    } else {
                        throw new RuntimeException("Missing user_favourites at line " + lineCount);
                    }

                    // Process the date string (YYYY-MM-DD H:MM:SS) into a Date object
                    if (csvRecord.isSet("date")) {
                        String dateString = csvRecord.get("date");
                        String[] dateTimeParts = dateString.split(" ");
                        String datePart = dateTimeParts[0];
                        String[] dateComponents = datePart.split("-");
                        String year = dateComponents[0];
                        String month = dateComponents[1];
                        String day = dateComponents[2];
                        String dateStringFormatted = year + "-" + month + "-" + day;
                        tweetDate = dateStringFormatted;

                    } else {
                        throw new RuntimeException("Missing date at line " + lineCount);
                    }




                    // Process the retweeted value
                    if (csvRecord.isSet("is_retweet")) {
                        String retweetedValue = csvRecord.get("is_retweet").toLowerCase();
                        if (retweetedValue.equals("true") || retweetedValue.equals("false")) {
                            retweeted = Boolean.parseBoolean(retweetedValue);
                        } else {
                            throw new RuntimeException("Invalid is_retweet value at line " + lineCount);
                        }
                    } else {
                        throw new RuntimeException("Missing is_retweet at line " + lineCount);
                    }

                    // Split the hashtags string into individual hashtags
                    String hashtagString = csvRecord.get("hashtags").toLowerCase();

                    ListaEnlazada<HashTag> tweetHashtags = null;
                    if (!hashtagString.isEmpty()) {
                        String[] hashtagArray = hashtagString.split(", ");
                        tweetHashtags = new ListaEnlazada<>();

                        for (String hashtagText : hashtagArray) {
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

                    }

                    // Create the User object
                    User user = new User(userName, usuarioVerificado);

                    // Create Tweet object
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


                    // Add the tweet to the tweets list and to the user tweets list
                    tweets.add(tweet);
                    System.out.println();

                } catch (RuntimeException e) {
                    ignoredLineCount++;
                    System.err.println(e.getMessage());
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





    // Helper method to find an existing user based on user ID and username
    private User findExistingUser(User newUser) {
        for (User existingUser : users) {
            if (existingUser.getIdUser() == newUser.getIdUser() && existingUser.getUserName().equals(newUser.getUserName())) {
                return existingUser;
            }
        }
        return null; // User not found
// ----------------------------***********************************------------------------------

        // Terminamos con los métodos para cargar la información cuya fuente son archivos externos (TXT y CSV)
        // y pasamos a definir las funciones/métodos de CONSULTA.
    }

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

    public void searchTweetsByWordOrPhrase() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a word or phrase to search for: ");
        String wordOrPhrase = scanner.nextLine();

        int tweetCount = countTweetsWithWordOrPhrase(wordOrPhrase);

        System.out.println("Number of tweets containing '" + wordOrPhrase + "': " + tweetCount);
    }

    public void printTopUsers(int count) {
        if (count <= 0) {
            System.out.println("Invalid count.");
            return;
        }

        ListaEnlazada<User> sortedList = new ListaEnlazada<>();
        ListaEnlazada<User> temp = new ListaEnlazada<>();

        // Copy the users to a temporary list
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            temp.addLast(user);
        }

        // Find and add the top users to the sorted list
        for (int i = 0; i < count; i++) {
            User topUser = null;
            float maxFavorites = Long.MIN_VALUE;

            // Find the user with the highest favorites
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
                break; // No more users to add, exit the loop
            }
        }

        // Print the top users
        Iterator<User> sortedIterator = sortedList.iterator();
        while (sortedIterator.hasNext()) {
            User user = sortedIterator.next();
            System.out.println(user.getUserName() + ": " + user.getFavourites() + " favorites");
        }
    }


    public String getMostUsedHashtagByDate(String date) {
        ListaEnlazada<HashTag> hashtagList = new ListaEnlazada<>();

        // Iterate over all the tweets
        for (Tweet tweet : tweets) {
            if (tweet.getDate().equals(date)) {
                // Get the hashtags of the tweet
                ListaEnlazada<HashTag> hashTagsOfThisTweet = tweet.getHashTagsOfThisTweet();

                // Add the hashtags to the main list, excluding "F1"
                for (HashTag hashtag : hashTagsOfThisTweet) {
                    if (!hashtag.getText().equalsIgnoreCase("f1")) {
                        hashtagList.add(hashtag);
                    }
                }
            }
        }

        // Find the most used hashtag
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


    private int countOccurrences(HashTag hashtag, ListaEnlazada<HashTag> hashtagList) {
        int count = 0;
        for (HashTag currentHashtag : hashtagList) {
            // Exclude the current hashtag being checked
            if (currentHashtag.equals(hashtag) && !currentHashtag.getText().equalsIgnoreCase("f1")) {
                count++;
            }
        }
        return count;
    }

    public int countDifferentHashtagsByDate(String date) {
        ListaEnlazada<HashTag> hashtagList = new ListaEnlazada<>();

        // Iterate over all the tweets
        for (Tweet tweet : tweets) {
            if (tweet.getDate().equals(date)) {
                // Get the hashtags of the tweet
                ListaEnlazada<HashTag> hashTagsOfThisTweet = tweet.getHashTagsOfThisTweet();

                // Add the hashtags to the main list, excluding "F1"
                for (HashTag hashtag : hashTagsOfThisTweet) {
                    if (!hashtag.getText().equalsIgnoreCase("f1")) {
                        hashtagList.add(hashtag);
                    }
                }
            }
        }

        // Create a counter variable
        int count = 0;

        // Create a set to track unique hashtags
        ListaEnlazada<HashTag> uniqueHashtags = new ListaEnlazada<>();

        // Iterate over the hashtag list and check for unique hashtags
        for (HashTag hashtag : hashtagList) {
            if (!uniqueHashtags.contains(hashtag)) {
                uniqueHashtags.add(hashtag);
                count++;
            }
        }

        // Return the count of unique hashtags
        return count;
    }

    public int countTweetsByDate(ListaEnlazada<Tweet> tweets, String date) {
        int count = 0;
        for (int i = 0; i < tweets.getSize(); i++) {
            Tweet tweet = tweets.get(i);
            if (tweet.getDate().equals(date)) {
                count++;
            }
        }
        return count;
    }




}