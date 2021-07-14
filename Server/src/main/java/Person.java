public class Person {
    private int id;
    private String name;
    private String surname;
    private Gender gender;
    private boolean isAdult;

    public Person(String name, String surname, Gender gender, boolean isAdult,int id) {
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.isAdult = isAdult;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Gender getGender() {
        return gender;
    }

    public boolean getAge() {
        return isAdult;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setAge(boolean age) {
        this.isAdult = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", gender=" + gender +
                ", isAdult=" + isAdult +
                '}';
    }
}
