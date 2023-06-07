package obligatorio;

import obligatorio.entities.BettingHouse;
import obligatorio.entities.HashTag;
import obligatorio.keys.KeyHashTag;
import uy.edu.um.prog2.adt.hash.HashEntry;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Principal {

    public static void main(String[] args) {

        BettingHouse bettingHouse = new BettingHouse();
        Scanner userInput = new Scanner(System.in);
        int option = 0;
        boolean exit;
        boolean dataLoaded = false;

        while (option != 3) {
            exit = false;
            try {
                System.out.println(
                        "Seleccione la opción que desee:\n" +
                                "1. Carga de datos\n" +
                                "2. Ejecutar Consultas\n" +
                                "3. Salir"
                );
                option = userInput.nextInt();

            } catch (InputMismatchException e) {
                System.out.println("Debe ingresar un valor numérico");
                exit = true;
                userInput.next();
            }

            if (option == 3) {
                exit = true;

            } else if (option > 3 || option <= 0) {
                System.out.println("Debe seleccionar una opcion de las solicitadas, de 1 a 3");
                option = 0;
                exit = true;
            }

            while (!exit) {
                if (option == 1) {
                    long timeStart, timeEnd;
                    timeStart = System.currentTimeMillis();

                    MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
                    MemoryUsage memoryUsage = memoryBean.getHeapMemoryUsage();


                    System.out.println("Carga de datos seleccionada");
                    bettingHouse.loadDriversData();
                    bettingHouse.loadTwitterData();
                    timeEnd = System.currentTimeMillis();
                    System.out.println("Carga de datos exitosa, tiempo de ejecucion de carga: "+ (timeEnd -
                            timeStart) +" milisegundos");
                    long memoryUsed = memoryUsage.getUsed();
                    System.out.println("Memoria utilizada: " + memoryUsed + " bytes");
                    exit = true;
                    dataLoaded = true;

                } else if (option == 2 ) {
                    System.out.println("Ejecutar consulta seleccioando");
                    Scanner userQueryInput = new Scanner(System.in);
                    int optionQuery = 0;
                    boolean exitQuery;

                    if (!dataLoaded) {
                        System.out.println("Debe cargar los datos antes de realizar consultas");
                        option = 0;
                        exit = true;

                    } else {
                        while (optionQuery != 7) {
                            exitQuery = false;
                            while (!exitQuery) {
                                try {
                                    System.out.println(
                                            "1. Listar los 10 pilotos activos en la temporada 2023 más mencionados en los tweets en un mes.\n" +
                                            "2. Top 15 usuarios con más tweets.\n" +
                                            "3. Cantidad de hashtags distintos para un día dado.\n" +
                                            "4. Hashtag más usado para un día dado, sin tener en cuenta #f1.\n" +
                                            "5. Top 7 cuentas con más favoritos.\n" +
                                            "6. Cantidad de tweets con una palabra o frase específicos\n" +
                                            "7. Salir"
                                    );

                                    optionQuery = userQueryInput.nextInt();
                                    if (optionQuery < 1 || optionQuery > 7) {
                                        System.out.println("Debe seleccionar uno de los elementos de la lista (1 a 7)");
                                        exitQuery = true;
                                        optionQuery = 0;
                                    }

                                } catch (InputMismatchException e) {
                                    System.out.println("Debe ingresar un valor numérico");
                                    userQueryInput.next();
                                }

                                if (optionQuery == 1) {
                                    boolean dateIsCorrect = false;
                                    while (!dateIsCorrect) {
                                        System.out.println("Ingresar la fecha en formato YYYY-MM-DD");
                                        Scanner userDateInput = new Scanner(System.in);
                                        String date = userDateInput.next();
                                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                        try {
                                            Date queryDate = formatter.parse(date);
                                            bettingHouse.top10Drivers(queryDate);
                                            dateIsCorrect = true;

                                        } catch (ParseException e) {
                                            System.out.println("Debe ingresar la fecha en el formato correcto");
                                        }
                                    }
                                    optionQuery = 0;

                                } else if (optionQuery == 2) {
                                    bettingHouse.top15UsersWithMoreTweets();
                                    System.out.println("\n");
                                    optionQuery = 0;

                                } else if (optionQuery == 3) {
                                    boolean dateIsCorrect = false;
                                    while (!dateIsCorrect) {
                                        System.out.println("Ingresar la fecha en formato YYYY-MM-DD");
                                        Scanner userDateInput = new Scanner(System.in);
                                        String date = userDateInput.next();
                                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                        try {
                                            Date queryDate = formatter.parse(date);
                                            bettingHouse.distinctHashTagsForDay(queryDate);
                                            dateIsCorrect = true;

                                        } catch (ParseException e) {
                                            System.out.println("Debe ingresar la fecha en el formato correcto");
                                        }
                                    }
                                    optionQuery = 0;

                                } else if (optionQuery == 4) {
                                    boolean dateIsCorrect = false;
                                    while (!dateIsCorrect) {
                                        System.out.println("Ingresar la fecha en formato YYYY-MM-DD");
                                        Scanner userDateInput = new Scanner(System.in);
                                        String date = userDateInput.next();
                                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                        try {
                                            Date queryDate = formatter.parse(date);
                                            bettingHouse.mostUsedHashTagForDay(queryDate);
                                            dateIsCorrect = true;

                                        } catch (ParseException e) {
                                            System.out.println("Debe ingresar la fecha en el formato correcto");
                                        }
                                    }
                                    optionQuery = 0;

                                } else if (optionQuery == 5) {
                                    bettingHouse.top7UsersWithMoreFavorites();
                                    System.out.println("\n");
                                    optionQuery = 0;

                                } else if (optionQuery == 6) {
                                    System.out.println("Ingrese la palabra o frase por la que desea buscar:");
                                    Scanner userTweetContentInput = new Scanner(System.in);
                                    userTweetContentInput.useDelimiter("\n");
                                    String content = userTweetContentInput.next();
                                    bettingHouse.tweetsWithSpecificContent(content);
                                    System.out.println("\n");
                                    optionQuery = 0;

                                } else {
                                    exit = true;
                                    exitQuery = true;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
