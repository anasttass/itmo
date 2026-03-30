package ru.stud.Managers;


import ru.stud.Collection.*;
import ru.stud.Exceptions.IncorrectInputException;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Scanner;


 //класс для обработки пользовательского ввода с валидацией

public class InputHelper implements Serializable {
    private Scanner scanner;

    public InputHelper(Scanner scanner){
        this.scanner=scanner;
    }

    public String readString(String prompt){
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty() || input==null) {
                System.out.println("Значение не может быть пустым. Попробуйте еще раз");
                continue;
            }
            return input;
        }
    }

    public Integer readInt(String prompt){
        while(true) {
            try {
                System.out.print(prompt + " (значение должно быть больше 0) ");
                Integer input = Integer.parseInt(scanner.nextLine().trim());
                if (input <= 0) {
                    System.out.println("Значение должно быть больше 0. Попробуйте еще раз");
                    continue;
                }
                return input;
            }catch (NumberFormatException e){
                System.out.println("Ошибка: введите целое число");
            }
        }
    }

    public Long readLong(String prompt){
        while(true) {
            try {
                System.out.print(prompt + " (значение не должно быть null) ");
                Long input = Long.parseLong(scanner.nextLine().trim());
                if (input==null) {
                    System.out.println("Значение должно быть null. Попробуйте еще раз");
                    continue;
                }
                return input;
            }catch (NumberFormatException e){
                System.out.println("Ошибка: введите целое число");
            }
        }
    }

    public Double readDouble(String prompt){
        while(true) {
            try {
                System.out.print(prompt + " (значение не должно быть null) ");
                Double input = Double.parseDouble(scanner.nextLine().trim());
                if (input==null) {
                    System.out.println("Значение не должно быть null. Попробуйте еще раз");
                    continue;
                }
                return input;
            }catch (NumberFormatException e){
                System.out.println("Ошибка: введите дробное число");
            }
        }
    }

    public float readFloat(String prompt){
        while (true){
            try {
                System.out.print(prompt);
                float input = Float.parseFloat(scanner.nextLine().trim());
                return input;
            } catch (NumberFormatException e){
                System.out.println("Ошибка: введите дробное число");
            }
        }
    }

    public ZonedDateTime readZonedDateTime(String prompt){
        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("dd.MM.yyyy");
        while(true){
            try{
                System.out.println("Доступный формат для даты: dd.MM.yyyy");
                System.out.print(prompt);
                String input = scanner.nextLine().trim();

                if(input==null || input.isEmpty()){
                    System.out.println("Значение не должно быть пустым. Попробуйте еще раз");
                    continue;
                }
                try {
                    LocalDate localDate = LocalDate.parse(input,formatter);
                    return localDate.atStartOfDay(ZoneId.systemDefault());
                }catch (DateTimeParseException e){
                    System.out.println("Неправильный формат даты");
                    continue;
                }
            }catch (Exception e){
                System.out.println("Ошибка при обработке даты");
            }
        }
    }

    public <T extends Enum<T>>T readEnum(Class<T> enumClass, String prompt){
        while (true){
            System.out.print("Допустимые значения: "+ Arrays.toString(enumClass.getEnumConstants()));
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if(input.isEmpty()){
                System.out.println("Значение не может быть пустым");
                continue;
            }
            try{
                return Enum.valueOf(enumClass,input.toUpperCase());
            } catch (IllegalArgumentException e){
                System.out.println("Ошибка: введите одно из допустимых значений");
            }
        }
    }
//    методы для сложных оъектов

    public Coordinates readCoordinates(String prompt){
        System.out.println("--------- Ввод координат ---------");
        Long x = readLong("Введите Х: ");
        Long y = readLong("Введите Y: ");
        return new Coordinates(x,y);
    }

    public Location readLocation (String prompt){
        System.out.println("------ Ввод местонахождения ------");
        float x = readFloat("Введите x: (может быть null) ");
        Double y = readDouble("Введите Y: ");
        Double z = readDouble("Введите Z: ");
        return new Location(x,y,z);
    }

    public Person readPerson(String prompt){
        while (true) {
            try {
                System.out.println("------ Ввод человека ------");
                String name = readString("Введите имя: ");
                ZonedDateTime birthday = readZonedDateTime("Введите дату рождения: ");
                Color eyeColor = readEnum(Color.class, "Введите цвет глаз: ");
                Color hairColor = readEnum(Color.class, "Введите цвет волос: ");
                Country nationality = readEnum(Country.class, "Введите национальность: ");
                Location location = readLocation("Введите местонахождения:");
                return new Person(name, birthday, eyeColor, hairColor, nationality, location);
            } catch (Exception e) {
                System.err.println("Ошибка при обработке данных Person");
            }
        }
    }

    public LabWork readLabWork (String prompt){
        while (true) {
            try {
                String name = readString("Введите название работы: ");
                Coordinates coordinates = readCoordinates("Введите координаты:");
                Integer minimalPoints = readInt("Введите минимальное количество баллов:");
                Difficulty difficulty = readEnum(Difficulty.class, "Введите сложность: ");
                Person author = readPerson("Введите автора:");
                return new LabWork(name,coordinates,minimalPoints,difficulty,author);
            } catch (Exception e) {
                System.err.println("Ошибка при обработке данных LabWork");
            }
        }
    }
}
