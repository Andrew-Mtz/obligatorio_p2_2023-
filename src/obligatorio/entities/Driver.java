package obligatorio.entities;

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
}
