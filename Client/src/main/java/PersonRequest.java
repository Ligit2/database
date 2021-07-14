public class PersonRequest {
    private int id;
    private String name;
    private String surname;
    private Gender gender;
    private int age;
    private Action action;


    public void setAction(Action action) {
        this.action = action;
    }

    public PersonRequest(int id, Action action) {
        this.id = id;
        this.action = action;
    }

    public PersonRequest(int id, String name, String surname, Gender gender, int age, Action action) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.age = age;
        this.action = action;
    }

    public PersonRequest(String name, String surname, Gender gender, int age) {
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.age = age;
    }

    public Action getAction() {
        return action;
    }

    public int getId() {
        return id;
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

    public int getAge() {
        return age;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setAge(int age) {
        this.age = age;
    }
}
