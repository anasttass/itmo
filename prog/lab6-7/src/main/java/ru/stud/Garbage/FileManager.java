//package ru.stud.Managers;
//
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.reflect.TypeToken;
//import ru.stud.Collection.LabWork;
//
//import java.io.*;
//import java.lang.reflect.Type;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.ZonedDateTime;
//import java.util.Scanner;
//import java.util.Vector;
//import ru.stud.Adapters.LocalDateAdapter;
//import ru.stud.Adapters.ZonedDateTimeAdapter;
//
// //отвечает за загрузку и сохранение коллекции LabWork
// //birthday 2006-10-24T00:00:00+03:00
// //creationTime 24.10.2006
//
//public class FileManager {
//    private String fileName;
//    private final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().registerTypeAdapter(LocalDate.class,new LocalDateAdapter()).registerTypeAdapter(ZonedDateTime.class,new ZonedDateTimeAdapter()).create();
//
//    public FileManager(String fileName){
//        this.fileName=fileName;
//    }
//    public void setFileName(String fileName){
//        this.fileName=fileName;
//    }
//
//
//     //сохраняет коллекцию в джисон файл
//
//    public void writeCollection(Vector<LabWork> collection){
//        if(fileName==null || fileName.isEmpty()){
//            System.err.println("Ошибка: имя файла не задано!");
//            return;
//        }
//
//        try(OutputStreamWriter writer = new OutputStreamWriter(
//                new FileOutputStream(fileName),"UTF-8")){
//            String json=convertToJson(collection);
//            writer.write(json);
//            System.out.println("Коллекция успешно сохранена в файл: "+fileName);
//        } catch (IOException e){
//            System.err.println("Ошибка записи в файл: "+e.getMessage());
//        } catch (SecurityException e){
//            System.err.println("Нет прав доступа к файлу: "+e.getMessage());
//        }
//    }
//
//     //сохраняет коллекцию из джисон файла в вектор
//
//    public Vector<LabWork> readCollection(){
//        Vector<LabWork> collection = new Vector<>();
//
//        if (fileName==null || fileName.isEmpty()){
//            System.err.println("Ошибка: имя файла не задано!");
//            return collection;
//        }
//        File file = new File(fileName);
//        if(!file.exists() || !file.canRead()){
//            System.err.println("Файл не существует или недоступен для чтения: "+fileName);
//            return collection;
//        }
//
//        try(Scanner scanner = new Scanner(file,"UTF-8")){
//            StringBuilder jsonContent = new StringBuilder();
//
//            while (scanner.hasNextLine()){
//                jsonContent.append(scanner.nextLine());
//            }
//            if (jsonContent.length()==0){
//                System.out.println("Файл пуст, будет создана новая коллекция");
//                return collection;
//            }
//            collection = parseJson(jsonContent.toString());
//            System.out.println("Коллекция успешно загружена из файла: "+fileName);
//        } catch (FileNotFoundException e){
//            System.err.println("Файл не найден: "+fileName);
//        }catch (SecurityException e){
//            System.err.println("Нет прав доступа к файлу: "+fileName);
//        }catch (Exception e){
//            System.err.println("Ошибка при чтении файла: "+fileName);
//        }
//        return collection;
//    }
//
//    private Vector<LabWork> parseJson(String json){
//        Type collectionType = new TypeToken<Vector<LabWork>>(){}.getType();
//        Vector<LabWork> collection =gson.fromJson(json, collectionType);
//        return collection;
//    }
//
//
//    private String convertToJson(Vector<LabWork> collection){
//        return gson.toJson(collection);
//    }
//
////    private String escapeJson(String str){
////        if(str==null)return null;
////        return str.replace("\\","\\\\")
////                .replace("\"","\\\"")
////                .replace("\t","\\t")
////                .replace("\n","\\n")
////                .replace("\r","\\r");
////    }
//
//    @Override
//    public String toString() {
//        return "FileManager [filename=" + fileName + "]";
//    }
//}
//
