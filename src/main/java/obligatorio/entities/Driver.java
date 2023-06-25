package obligatorio.entities;

import java.util.Objects;

public class Driver {

    private String name;
    private String lastName;

    public Driver(String name, String lastName) {

        this.name = name;
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Driver driver)) return false;
        return Objects.equals(getLastName(), driver.getLastName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLastName());
    }

    @Override
    public String toString() {
        return name + " " + lastName;
    }
}
