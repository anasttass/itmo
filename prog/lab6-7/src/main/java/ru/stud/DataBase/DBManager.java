package ru.stud.DataBase;

import ru.stud.Collection.*;
import ru.stud.Common.User;
import ru.stud.Server.Server;

import java.lang.annotation.Native;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class DBManager {
    private Connection connection;

    public DBManager(Connection connection){
        this.connection=connection;
    }

    public boolean isUserExists(String login) {
        try(PreparedStatement statement = connection.prepareStatement("SELECT 1 FROM users WHERE login = ?")){
            statement.setString(1,login);
            ResultSet res =statement.executeQuery();
            return res.next();
        }catch (SQLException e){
            System.err.println("Ошибка выполнения запроса к бд: " + e.getMessage());
            return false;
        }
    }
    public boolean checkPassword(String login, String password) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT 1 FROM users WHERE login = ? AND password = ?")) {
            statement.setString(1, login);
            statement.setString(2, Server.hashPassword(password));
            ResultSet res = statement.executeQuery();
            return res.next();
        } catch (SQLException e) {
            System.err.println("Ошибка проверки пароля: " + e.getMessage());
            return false;
        }
    }

    public boolean checkUser(User user) {
        return checkPassword(user.getUsername(), user.getPassword());
    }

    public boolean registerUser(User user) {
        if (isUserExists(user.getUsername())) return false;
        return insertUser(user);
    }

    private boolean insertUser(User user) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO users(login, password) VALUES (?, ?)")) {
            statement.setString(1, user.getUsername());
            statement.setString(2, Server.hashPassword(user.getPassword()));
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Ошибка добавления пользователя: " + e.getMessage());
            return false;
        }
    }

    private int getUserId(String login) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id FROM users WHERE login = ?")) {
            statement.setString(1, login);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return res.getInt("id");
            }
        }
        throw new SQLException("Пользователь не найден");
    }

    private String getUsernameByLabId(long id){
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT u.login FROM labwork l JOIN users u ON l.user_id = u.id WHERE l.id = ?")) {
            statement.setLong(1, id);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return res.getString("login");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка получения владельца работы: " + e.getMessage());
        }
        return null;
    }


    private long insertCoordinates(Coordinates coordinates) throws SQLException {
            String sql = "INSERT INTO coordinates (x,y) VALUES (?,?) RETURNING id";
            try(PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setLong(1,coordinates.getX());
                statement.setLong(2,coordinates.getY());

                try(ResultSet rs = statement.executeQuery()){
                    if (rs.next()){
                        long id = rs.getLong(1);
                        return id;
                    }else{
                        throw new SQLException("База не вернула id");
                    }
                }
            }catch (SQLException e){
                System.out.println("Ошибка в insertCoordinates: " + e.getMessage() +
                        " SQLState=" + e.getSQLState() +
                        " Code=" + e.getErrorCode());
                throw e;
            }
    }

    private long insertLocation(Location location) throws SQLException {
        String sql = "INSERT INTO location (x,y,z) VALUES (?,?,?) RETURNING id";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setFloat(1,location.getX());
            statement.setDouble(2,location.getY());
            statement.setDouble(3,location.getZ());

            try(ResultSet rs = statement.executeQuery()){
                if (rs.next()){
                    long id = rs.getLong(1);
                    return id;
                }else{
                    throw new SQLException("База не вернула id");
                }
            }
        }catch (SQLException e){
            System.out.println("Ошибка в insertLocation: " + e.getMessage() +
                    " SQLState=" + e.getSQLState() +
                    " Code=" + e.getErrorCode());
            throw e;
        }
    }

    private long insertPerson(Person person) throws SQLException{

        long location_id = insertLocation(person.getLocation());

        String sql ="INSERT INTO person (name,birthday,eyeColor,hairColor,nationality,location_id) VALUES (?,?,?,?,?,?) RETURNING id";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1,person.getName());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            statement.setString(2, person.getBirthday().toLocalDate().format(formatter));
//            statement.setString(2,person.getBirthday().toString());

            statement.setString(3,person.getEyeColor().name());
            statement.setString(4,person.getHairColor().name());
            statement.setString(5,person.getNationality().name());
            statement.setLong(6,location_id);

            try(ResultSet rs=statement.executeQuery()){
                if (rs.next()){
                    long id = rs.getLong(1);
                    return id;
                }else{
                    throw new SQLException("База не вернула id");
                }
            }
        }catch (SQLException e){
            System.out.println("Ошибка в insertPerson: " + e.getMessage() +
                    " SQLState=" + e.getSQLState() +
                    " Code=" + e.getErrorCode());
            throw e;
        }
    }


    public long insertLabwork(LabWork labWork,User user) {
        try{
            if (!checkUser(user)){
                return -1;
            }
            int userId = getUserId(user.getUsername());

            long person_id = insertPerson(labWork.getAuthor());
            long coordinates_id=insertCoordinates(labWork.getCoordinates());

            String sql = "INSERT INTO labwork (name,coordinates_id,creation_time,minimal_points,difficulty,person_id,user_id) VALUES (?,?,?,?,?,?,?) RETURNING id";

            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, labWork.getName());
                statement.setLong(2, coordinates_id);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                statement.setString(3, labWork.getCreationDate().format(formatter));
//                statement.setString(3, labWork.getCreationDate().toString());

                statement.setInt(4, labWork.getMinimalPoint());
                statement.setString(5, labWork.getDifficulty().name());
                statement.setLong(6, person_id);
                statement.setInt(7, userId);

                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) return rs.getLong(1);
                    System.err.println("INSERT labwork RETURNING id вернул пустой результат");
                    return -1;
                }
            }
        }catch (SQLException e) {
            System.err.println("Ошибка добавления работы: " + e.getMessage()
                    + " SQLState=" + e.getSQLState() + " Code=" + e.getErrorCode());
            e.printStackTrace();
            return -1;
        }
    }

    public Vector<LabWork> loadLabworks(){
        Vector<LabWork> labWorks = new Vector<>();
        String sql = "SELECT * FROM labwork";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet res = statement.executeQuery();
            while (res.next()){
                long id = res.getLong("id");
                String name = res.getString("name");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate creationTime = LocalDate.parse(
                        res.getString("creation_time").trim(),
                        formatter);
//                LocalDate creationTime = LocalDate.parse(res.getString("creation_time"));
//                LocalDate creationTime = LocalDate.parse(res.getString("creation_time"),
//                        DateTimeFormatter.ofPattern("dd.MM.yyyy")
//                );

                int minimalPoints = res.getInt("minimal_points");
                Difficulty difficulty = Difficulty.valueOf(res.getString("difficulty").toUpperCase());

                long coordinates_id = res.getLong("coordinates_id");
                long person_id = res.getLong("person_id");

                Coordinates coordinates = getCoordinatesById(coordinates_id);
                Person person = getPersonById(person_id);

                LabWork lw = new LabWork(name,coordinates,creationTime,minimalPoints,difficulty,person);
                lw.setId(id);

                labWorks.add(lw);
            }
        } catch (SQLException e){
            System.err.println("Ошибка загрузки коллекции");
        }
        return labWorks;
    }

    public Coordinates getCoordinatesById(long id) throws SQLException {
        String sql = "SELECT * FROM coordinates WHERE id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1,id);
            ResultSet res = statement.executeQuery();
            if(res.next()){
                return new Coordinates(res.getLong("x"), res.getLong("y"));
            }
            throw new SQLException("Не удалось получить координаты по id");
        }
    }

    public Location getLocationById(long id) throws SQLException{
        String sql = "SELECT * FROM location WHERE id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1,id);
            ResultSet res = statement.executeQuery();
            if(res.next()){
                return new Location(res.getFloat("x"), res.getDouble("y"),res.getDouble("z"));
            }
            throw new SQLException("Не удалось получить локацию по id");
        }
    }

    public Person getPersonById(long id) throws SQLException{
        String sql = "SELECT * FROM person WHERE id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1,id);
            ResultSet res = statement.executeQuery();
            if(res.next()){
                String name = res.getString("name");

//                ZonedDateTime birthday = ZonedDateTime.parse(res.getString("birthday"));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

                ZonedDateTime birthday = LocalDate.parse(res.getString("birthday"), formatter)
                                .atStartOfDay(ZoneId.systemDefault());

                Color eyeColor = Color.valueOf(res.getString("eyeColor").toUpperCase());
                Color hairColor = Color.valueOf(res.getString("hairColor").toUpperCase());
                Country nationality = Country.valueOf(res.getString("nationality").toUpperCase());

                long location_id = res.getLong("location_id");

                Location location = getLocationById(location_id);

                return new Person(name,birthday,eyeColor,hairColor,nationality,location);
            }
            throw new SQLException("Не удалось получить локацию по id");
        }
    }

    public boolean removeLabById(long id,User user){
        String sql = "DELETE FROM labwork WHERE id = ?";
        try{
            if(!checkUser(user)){
                return false;
            }
            String owner = getUsernameByLabId(id);
            if (!user.getUsername().equals(owner)) return false;

            try (PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setLong(1, id);
                return statement.executeUpdate() > 0;
            }
        }catch (SQLException e) {
            System.err.println("Ошибка удаления квартиры: " + e.getMessage());
            return false;
        }
    }

    public boolean updateLabById(long lab_id,LabWork newLab ,User user){
        String sql="UPDATE labwork SET name=?, coordinates_id=?, creation_time=?, minimal_points=?," +
                " difficulty=?, person_id=? WHERE id=?";
        try{
            if(!checkUser(user)){return false;}
            long user_id = getUserId(user.getUsername());

            if(!isLabOwnedByUser(lab_id,user)){return false;}

            long coordinates = insertCoordinates(newLab.getCoordinates());
            long person = insertPerson(newLab.getAuthor());

            try(PreparedStatement statement= connection.prepareStatement(sql)){
                statement.setString(1,newLab.getName());
                statement.setLong(2,coordinates);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                statement.setString(3, newLab.getCreationDate().format(formatter));
//                statement.setString(3,newLab.getCreationDate().toString());

                statement.setInt(4,newLab.getMinimalPoint());
                statement.setString(5,newLab.getDifficulty().name());
                statement.setLong(6,person);

                statement.setLong(7,lab_id);

                return statement.executeUpdate()>0;
            }
        }catch (SQLException e) {
            System.err.println("Ошибка удаления квартиры: " + e.getMessage());
            return false;
        }
    }


    public boolean clear(User user){
        String sql = "DELETE FROM labwork WHERE user_id=?";
        try {
            if (!checkUser(user)){return false;}
            long id = getUserId(user.getUsername());

            try(PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setLong(1,id);
                statement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Ошибка удаления квартиры: " + e.getMessage());
            return false;
        }
    }

    public boolean isLabOwnedByUser(long lab_id,User user){
        try {
            return user.getUsername().equals(getUsernameByLabId(lab_id));
        } catch (Exception e) {
            return false;
        }
    }

}
