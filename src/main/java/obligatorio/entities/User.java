package obligatorio.entities;

import uy.edu.um.prog2.adt.lista.ListaEnlazada;
import uy.edu.um.prog2.adt.lista.MyList;

import java.util.Objects;

public class User implements Comparable {

    // Declaro las variables de instancia de la clase.
    private static long idCounter = 0;
    private long idUser;
    private String userName;
    private boolean userVerified;
    private MyList<Tweet> userTweets;
    private float favourites;


    // Declaro los constructores de la clase.
    public User(String name, boolean isVerified) {
        this.idUser = idCounter++;
        this.userName = name;
        this.userVerified = isVerified;
        this.userTweets = new ListaEnlazada<>();
    }

    public User() {
    }

    // Declaro los métodos de la clase

    @Override
    public int compareTo(Object o) {
        int comparacion = 0;

        if (o instanceof User) {
            if (this.idUser < ((User) o).idUser) {
                comparacion = -1;
            }
            else if (this.idUser > ((User) o).idUser) {
                comparacion = 1;
            }
        }


        return comparacion;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getUserName(), user.getUserName());
    }



    public void updateUserFavorites(float number) {

        if (number > this.favourites) {
            this.favourites = number;
        }
    }

    // Getter & Setter
    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public MyList<Tweet> getUserTweets() {
        return userTweets;
    }

    public void setUserTweets(MyList<Tweet> userTweets) {
        this.userTweets = userTweets;
    }

    public boolean isUserVerified() {
        return userVerified;
    }

    public void setUserVerified(boolean userVerified) {
        this.userVerified = userVerified;
    }

    public float getFavourites() {
        return favourites;
    }

    public void setFavourites(float favourites) {
        this.favourites = favourites;
    }
}
