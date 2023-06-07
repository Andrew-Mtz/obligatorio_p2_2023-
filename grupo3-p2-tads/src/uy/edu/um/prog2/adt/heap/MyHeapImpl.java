package uy.edu.um.prog2.adt.heap;

public class MyHeapImpl<T extends Comparable<T>> implements MyHeap<T>  {

    private T[] values;
    private int lastValuePosition;
    private boolean heapMax;

    public MyHeapImpl(T[] values, boolean heapMax) {
        this.values = values;
        this.lastValuePosition = 0;
        this.heapMax = heapMax;
    }

    @Override
    public void insert(T value) {
        this.values[lastValuePosition] = value;
        int valuePosition = lastValuePosition;
        lastValuePosition++;
        if (heapMax==true) { //
            while (valuePosition!=0 && value.compareTo(getFather(valuePosition))>0){
                T tempValue = getFather(valuePosition);
                this.values[getFatherPosition(valuePosition)]=value;
                this.values[valuePosition]=tempValue;
                valuePosition = getFatherPosition(valuePosition);
            }
        }
        else { // Sino un heap minimo
            while (valuePosition!=0 && value.compareTo(getFather(valuePosition))<0){
                T tempValue = getFather(valuePosition);
                this.values[getFatherPosition(valuePosition)]=value;
                this.values[valuePosition]=tempValue;
                valuePosition = getFatherPosition(valuePosition);
            }
        }
    }

    private T getFather (int childPosition){
        return values[(childPosition-1)/2];
    }

    private int getFatherPosition (int childPosition) {
        return (childPosition-1)/2;
    }

    @Override
    public T deleteAndReturn() {

        T nodoAEliminar = values[0];
        int valuePosition = 0;
        this.lastValuePosition--;

        this.values[0]=this.values[lastValuePosition];
        this.values[lastValuePosition]=null;

        T nodoAPonerComoRaiz = values[0];
        int lastValuePosition = getFatherPosition(this.lastValuePosition-1);

        if(heapMax==true){
            while (valuePosition<lastValuePosition && (nodoAPonerComoRaiz.compareTo(getLeftChild(valuePosition)) < 0 || nodoAPonerComoRaiz.compareTo(getRightChild(valuePosition)) < 0)){
                if (getLeftChild(valuePosition).compareTo(getRightChild(valuePosition)) >= 0){
                    nodoAPonerComoRaiz = this.values[valuePosition];
                    this.values[valuePosition]=this.getLeftChild(valuePosition);
                    this.values[getLeftChildPosition(valuePosition)]=nodoAPonerComoRaiz;
                    valuePosition = getLeftChildPosition(valuePosition);
                } else if (getLeftChild(valuePosition).compareTo(getRightChild(valuePosition)) < 0) {
                    nodoAPonerComoRaiz = this.values[valuePosition];
                    this.values[valuePosition]=this.getRightChild(valuePosition);
                    this.values[getRightChildPosition(valuePosition)]=nodoAPonerComoRaiz;
                    valuePosition = getRightChildPosition(valuePosition);
                }
            }
        }
        else {
            while (valuePosition<lastValuePosition && (nodoAPonerComoRaiz.compareTo(getLeftChild(valuePosition)) > 0 || nodoAPonerComoRaiz.compareTo(getRightChild(valuePosition)) > 0)){
                if (getLeftChild(valuePosition).compareTo(getRightChild(valuePosition)) <= 0){
                    nodoAPonerComoRaiz = this.values[valuePosition];
                    this.values[valuePosition]=this.getLeftChild(valuePosition);
                    this.values[getLeftChildPosition(valuePosition)]=nodoAPonerComoRaiz;
                    valuePosition = getLeftChildPosition(valuePosition);
                } else {
                    nodoAPonerComoRaiz = this.values[valuePosition];
                    this.values[valuePosition]=this.getRightChild(valuePosition);
                    this.values[getRightChildPosition(valuePosition)]=nodoAPonerComoRaiz;
                    valuePosition = getRightChildPosition(valuePosition);
                }
            }
        }
        return nodoAEliminar;
    }

    private T getLeftChild (int fatherPosition){
        return values[2*fatherPosition + 1];
    }

    private int getLeftChildPosition (int fatherPosition){
        return 2*fatherPosition + 1;
    }

    private T getRightChild (int fatherPosition){
        return values[2*fatherPosition + 2];
    }

    private int getRightChildPosition (int fatherPosition){
        return 2*fatherPosition + 2;
    }

    @Override
    public int size() {
        return lastValuePosition;
    }

    @Override
    public String toString (){
        String salida = new String();
        for (int i=0; i<lastValuePosition;i++){
            String value = values[i].toString();
            salida = salida.concat(value+" ");
            double potencia = (Math.log10(i+2)/Math.log10(2));
            if (potencia%1==0) {
                salida = salida.concat("\n");
            }
        }
        return salida;
    }

}
