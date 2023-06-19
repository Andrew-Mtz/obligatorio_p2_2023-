package obligatorio;

import obligatorio.entities.BettingHouse;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;



public class Principal {

    private static Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);  // Note: Calendar month is zero-based
        calendar.set(Calendar.DAY_OF_MONTH, day);

        return calendar.getTime();
    }

    public static void main(String[] args) {

        BettingHouse bettingHouse = new BettingHouse();
        Scanner userInput = new Scanner(System.in);
        int option = 0;
        boolean exit;
        boolean dataLoaded = false;

        long timeStart, timeEnd;
        timeStart = System.currentTimeMillis();

        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = memoryBean.getHeapMemoryUsage();


        System.out.println("Carga de datos seleccionada");
        bettingHouse.loadTwitterData();
        timeEnd = System.currentTimeMillis();
        System.out.println("Carga de datos exitosa, tiempo de ejecucion de carga: " + (timeEnd -
                timeStart) + " milisegundos");
        long memoryUsed = memoryUsage.getUsed();
        System.out.println("Memoria utilizada: " + memoryUsed + " bytes");
        //bettingHouse.searchTweetsByWordOrPhrase();
        //bettingHouse.printTopUsers(7);
        System.out.println("Cantidad de tweets en el dia: " + bettingHouse.countTweetsByDate(bettingHouse.tweets,"2021-11-21")); //metodo para verificar la fecha en los tweets es OK
        System.out.println("Hashtag mas usado en el dia: " + bettingHouse.getMostUsedHashtagByDate("2021-11-21"));
        System.out.println("Hashtag usados en el dia: " + bettingHouse.countDifferentHashtagsByDate("2021-11-21"));
        exit = true;
        dataLoaded = true;

    }
}
