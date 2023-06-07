package obligatorio.entities;

import uy.edu.um.prog2.adt.lista.ListaEnlazada;
import uy.edu.um.prog2.adt.lista.MyList;

import java.util.Date;
import java.util.Objects;

public class Tweet {

    // Declaro las variables de instancia de la clase.

    private long idTweet;
    private String content;
    private String source;
    private boolean isRetweet;
    private User user;
    private Date date;
    private MyList<HashTag> hashTagsOfThisTweet = new ListaEnlazada<>();


    // Declaro los constructores de la clase.

    public Tweet(long idTweet, String content, String source, boolean isRetweet, User user, Date date) {
        this.idTweet = idTweet;
        this.content = content;
        this.source = source;
        this.isRetweet = isRetweet;
        this.user = user;
        this.date = date;
    }

    public Tweet() {
    }



    // Declaro los métodos de la clase

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tweet tweet = (Tweet) o;
        return idTweet == tweet.idTweet;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTweet);
    }



    // Getter & Setter


    public long getIdTweet() {
        return idTweet;
    }

    public void setIdTweet(long idTweet) {
        this.idTweet = idTweet;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isRetweet() {
        return isRetweet;
    }

    public void setRetweet(boolean retweet) {
        isRetweet = retweet;
    }

    public MyList<HashTag> getHashTagsOfThisTweet() {
        return hashTagsOfThisTweet;
    }

    public void setHashTagsOfThisTweet(MyList<HashTag> hashTagsOfThisTweet) {
        this.hashTagsOfThisTweet = hashTagsOfThisTweet;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
