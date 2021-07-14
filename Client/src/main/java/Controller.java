import com.github.javafaker.Faker;
import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Controller {
    private static final Faker faker = new Faker();
    private static final Random ageGenerator = new Random();
    private static final Gson gson = new Gson();
    private static final  Scanner scanner = new Scanner(System.in);


    public static PersonRequest generatePersonRequest() {
        int age = ageGenerator.nextInt(100);
        PersonRequest personRequest = new PersonRequest(faker.name().firstName(), faker.name().lastName(), Gender.getRandomGender(), age);
        return personRequest;
    }

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080)) {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            while (true) {
                int choice = getChoice(scanner);
                if (choice == 5) {
                    dataOutputStream.writeUTF("end");
                    break;
                }

                Action action = parseAction(choice);
                String s;

                if (action.equals(Action.CREATE)) {
                    s = getStringForCreate(gson, action);
                } else if (action.equals(Action.UPDATE)) {
                    s = getStringForUpdate(scanner, gson, action);
                } else {
                    s = getString(scanner, gson, action);
                }

                dataOutputStream.writeUTF(s);

                if (action.equals(Action.READ)) {
                    String s1 = dataInputStream.readUTF();
                    if(s1.equals("null")){
                        System.out.println("No person with such ID");
                    }
                    else {
                        Person response = gson.fromJson(s1,Person.class);
                        System.out.println(response);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getChoice(Scanner scanner) {
        System.out.println("Create :1");
        System.out.println("Read :2");
        System.out.println("Update :3");
        System.out.println("Delete :4");
        System.out.println("Exit :5");
        int choice = scanner.nextInt();
        return choice;
    }

    private static String getString(Scanner scanner, Gson gson, Action action) {
        String s;
        System.out.println("Id");
        int id = scanner.nextInt();
        PersonRequest personRequest = new PersonRequest(id, action);
        s = gson.toJson(personRequest, PersonRequest.class);
        return s;
    }

    private static String getStringForCreate(Gson gson, Action action) {
        String s;
        PersonRequest personRequest = generatePersonRequest();
        personRequest.setAction(action);
        s = gson.toJson(personRequest, PersonRequest.class);
        return s;
    }

    private static String getStringForUpdate(Scanner scanner, Gson gson, Action action) {
        String s;
        System.out.println("Id");
        int id = scanner.nextInt();
        System.out.println("name");
        String name = scanner.next();
        System.out.println("surname");
        String surname = scanner.next();
        System.out.println("gender : Male-1 :: Female-2");
        int genderHelper = scanner.nextInt();
        Gender gender;
        if (genderHelper % 2 == 0) gender = Gender.FEMALE;
        else gender = Gender.MALE;
        System.out.println("age");
        int age = scanner.nextInt();
        PersonRequest personRequest = new PersonRequest(id, name, surname, gender, age, action);
        s = gson.toJson(personRequest, PersonRequest.class);
        return s;
    }

    public static Action parseAction(int num) {
        if (num == 1) return Action.CREATE;
        else if (num == 2) return Action.READ;
        else if (num == 3) return Action.UPDATE;
        else return Action.DELETE;
    }
}
