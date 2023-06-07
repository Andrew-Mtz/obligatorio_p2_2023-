package obligatorio.entities;

import uy.edu.um.prog2.adt.lista.ListaEnlazada;
import uy.edu.um.prog2.adt.lista.MyList;

import java.util.Date;
import java.util.Objects;

public class HashTag {

    // Declaro las variables de instancia de la clase.
    private long idHashTag;
    private String text;
    private MyList<Tweet> tweetsWithThisHashTag = new ListaEnlazada<>();
    private MyList<Date> datesOfHashTag = new ListaEnlazada<>();


    // Declaro los constructores de la clase.

    public HashTag(long idHashTag, String text) {
        this.idHashTag = idHashTag;
        this.text = text;
    }

    public HashTag() {
    }


    // Declaro los métodos de la clase

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashTag hashTag = (HashTag) o;
        return idHashTag == hashTag.idHashTag;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idHashTag);
    }

    public void addHashTagDate(Date date) {
        this.datesOfHashTag.add(date);
    }

    // Getter & Setter


    public long getIdHashTag() {
        return idHashTag;
    }

    public void setIdHashTag(long idHashTag) {
        this.idHashTag = idHashTag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MyList<Tweet> getTweetsWithThisHashTag() {
        return tweetsWithThisHashTag;
    }

    public void setTweetsWithThisHashTag(MyList<Tweet> tweetsWithThisHashTag) {
        this.tweetsWithThisHashTag = tweetsWithThisHashTag;
    }

    public MyList<Date> getDatesOfHashTag() {
        return datesOfHashTag;
    }

    public void setDatesOfHashTag(MyList<Date> datesOfHashTag) {
        this.datesOfHashTag = datesOfHashTag;
    }
}
