import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class Service {

    public static void main(String[] args) {
        DAO();
    }

    private static void DAO() {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            Socket accept = serverSocket.accept();
            System.out.println("accepted");
            DataInputStream dataInputStream = new DataInputStream(accept.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(accept.getOutputStream());
            Gson gson = new Gson();
            String request = dataInputStream.readUTF();
            while (!request.equalsIgnoreCase("end")) {
                PersonRequest personRequest = gson.fromJson(request, PersonRequest.class);
                Person person = personFromRequest(personRequest);
                Action action = personRequest.getAction();
                try {
                    if (action.equals(Action.CREATE)) {
                        createPerson(person);
                        System.out.println("Created");
                    } else if (action.equals(Action.UPDATE)) {
                        updatePerson(person);
                        System.out.println("Updated");
                    } else if (action.equals(Action.DELETE)) {
                        deletePerson(person);
                        System.out.println("Deleted");
                    } else {
                        Person person1 = readPerson(person);
                        String s1 = gson.toJson(person1);
                        dataOutputStream.writeUTF(s1);
                        System.out.println("Reading");
                    }
                    request = dataInputStream.readUTF();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Person readPerson(Person person) throws SQLException {
        Connection connection = DriverManager.getConnection(DatabaseCredential.getURL(),
                DatabaseCredential.getUser(),
                DatabaseCredential.getPassword());
        PreparedStatement preparedStatement = connection.prepareStatement("select * from `persons` where id = ?");
        preparedStatement.setInt(1, person.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        preparedStatement.executeUpdate();
        preparedStatement.close();
        Person personForResponse = null;
        while (resultSet.next()) {
            Gender gender;
            String string = resultSet.getString(4);
            if (string.equalsIgnoreCase("female"))
                gender = Gender.FEMALE;
            else
                gender = Gender.MALE;
            personForResponse = new Person(resultSet.getString(2),
                    resultSet.getString(3),
                    gender,
                    resultSet.getBoolean(5),
                    resultSet.getInt(1));
        }
        return personForResponse;
    }

    private static void deletePerson(Person person) throws SQLException {
        Connection connection = DriverManager.getConnection(DatabaseCredential.getURL(),
                DatabaseCredential.getUser(),
                DatabaseCredential.getPassword());
        PreparedStatement preparedStatement = connection.prepareStatement("delete from `persons` where id = ?");
        preparedStatement.setInt(1, person.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    private static void updatePerson(Person person) throws SQLException {
        Connection connection = DriverManager.getConnection(DatabaseCredential.getURL(),
                DatabaseCredential.getUser(),
                DatabaseCredential.getPassword());
        PreparedStatement preparedStatement = connection.prepareStatement("update `persons` set name = ? ,surname =?, gender = ?,isAdult = ? where id=?");
        preparedStatement.setString(1, person.getName());
        preparedStatement.setString(2, person.getSurname());
        preparedStatement.setString(3, String.valueOf(person.getGender()));
        preparedStatement.setBoolean(4, person.getAge());
        preparedStatement.setInt(5, person.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    private static void createPerson(Person person) throws SQLException {
        Connection connection = DriverManager.getConnection(DatabaseCredential.getURL(),
                DatabaseCredential.getUser(),
                DatabaseCredential.getPassword());
        PreparedStatement preparedStatement = connection.prepareStatement("insert into `persons`(name,surname,gender,isAdult)" +
                "values(?,?,?,?)");
        preparedStatement.setString(1, person.getName());
        preparedStatement.setString(2, person.getSurname());
        preparedStatement.setString(3, String.valueOf(person.getGender()));
        preparedStatement.setBoolean(4, person.getAge());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    private static Person personFromRequest(PersonRequest personRequest) {
        boolean check = false;
        if (personRequest.getAge() >= 18)
            check = true;
        Person person = new Person(personRequest.getName(),
                personRequest.getSurname(),
                personRequest.getGender(), check, personRequest.getId());
        return person;
    }
}
