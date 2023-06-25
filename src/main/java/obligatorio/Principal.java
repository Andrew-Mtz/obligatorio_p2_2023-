package obligatorio;

import obligatorio.entities.BettingHouse;
import uy.edu.um.prog2.adt.exceptions.EmptyQueueException;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Principal {

    public static void main(String[] args) throws EmptyQueueException {

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
                                    long timeStart, timeEnd;
                                    timeStart = System.currentTimeMillis();

                                    MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
                                    MemoryUsage memoryUsage = memoryBean.getHeapMemoryUsage();
                                    bettingHouse.top10Drivers();

                                    timeEnd = System.currentTimeMillis();
                                    System.out.println("Tiempo consumido: " + (timeEnd - timeStart) +" milisegundos");

                                    optionQuery = 0;

                                } else if (optionQuery == 2) {
                                    long timeStart, timeEnd;
                                    timeStart = System.currentTimeMillis();

                                    MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
                                    MemoryUsage memoryUsage = memoryBean.getHeapMemoryUsage();
                                    bettingHouse.top15UsersWithTheMostTweets();

                                    timeEnd = System.currentTimeMillis();
                                    System.out.println("Tiempo consumido: " + (timeEnd - timeStart) +" milisegundos");

                                    optionQuery = 0;

                                } else if (optionQuery == 3) {
                                    System.out.println("Ingresar la fecha en formato YYYY-MM-DD");
                                    Scanner userDateInput = new Scanner(System.in);
                                    long timeStart, timeEnd;
                                    timeStart = System.currentTimeMillis();
                                    MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
                                    MemoryUsage memoryUsage = memoryBean.getHeapMemoryUsage();
                                    String date = userDateInput.next();
                                    System.out.println("La cantidad de distintos hashtags utilizados en la fecha fue: " + bettingHouse.countDifferentHashtagsByDate(date));

                                    timeEnd = System.currentTimeMillis();
                                    System.out.println("Tiempo consumido: " + (timeEnd - timeStart) +" milisegundos");


                                    optionQuery = 0;

                                } else if (optionQuery == 4) {

                                System.out.println("Ingresar la fecha en formato YYYY-MM-DD");
                                Scanner userDateInput = new Scanner(System.in);
                                String date = userDateInput.next();
                                long timeStart, timeEnd;
                                timeStart = System.currentTimeMillis();
                                MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
                                MemoryUsage memoryUsage = memoryBean.getHeapMemoryUsage();
                                System.out.println("El hashtag mas utilizado en la fecha fue: " + bettingHouse.getMostUsedHashtagByDate(date));

                                timeEnd = System.currentTimeMillis();
                                System.out.println("Tiempo consumido: " + (timeEnd - timeStart) +" milisegundos");

                                optionQuery = 0;

                                } else if (optionQuery == 5) {
                                    long timeStart, timeEnd;
                                    timeStart = System.currentTimeMillis();
                                    MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
                                    MemoryUsage memoryUsage = memoryBean.getHeapMemoryUsage();
                                    bettingHouse.printTopUsers(7);

                                    timeEnd = System.currentTimeMillis();
                                    System.out.println("Tiempo consumido: " + (timeEnd - timeStart) +" milisegundos");

                                    optionQuery = 0;

                                } else if (optionQuery == 6) {
                                    long timeStart, timeEnd;
                                    timeStart = System.currentTimeMillis();
                                    MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
                                    MemoryUsage memoryUsage = memoryBean.getHeapMemoryUsage();
                                    bettingHouse.searchTweetsByWordOrPhrase();

                                    timeEnd = System.currentTimeMillis();
                                    System.out.println("Tiempo consumido: " + (timeEnd - timeStart) +" milisegundos");

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