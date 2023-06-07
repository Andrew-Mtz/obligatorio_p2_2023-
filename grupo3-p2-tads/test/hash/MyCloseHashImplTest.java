package hash;

import org.junit.jupiter.api.Test;
import uy.edu.um.prog2.adt.hash.MyCloseHashImpl;
import uy.edu.um.prog2.adt.hash.MyHash;

import static org.junit.jupiter.api.Assertions.*;

class MyCloseHashImplTest {

    @Test
    void flujoPrincipal() {

        MyHash<Integer,Integer> test = new MyCloseHashImpl<>(11,true);

        test.put(1,1234);
        test.put(2,2345);
        test.put(3,3456);
        test.put(2,7890);


        /* MyCloseHashImpl<>(11,true) crea un diccionario interno cerrado con función
        de hashing cíclica. Esto significa que, cuando se agrega un elemento con una clave dada a
        la tabla, si hay otro elemento con la misma clave, este elemento se "reemplaza" por el
        elemento nuevo. Por lo tanto, cuando se agrega el elemento (2, 7890), el elemento con la
        clave 2 se reemplazó por el elemento 7890, por lo que el valor devuelto será 7890. */
        assertEquals(7890,test.get(2));

        test.put(4,5678);

        test.remove(3);

        assertEquals(3,test.size());

        test.put(3,510);

        assertEquals(4,test.size());
    }

}