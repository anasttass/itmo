//package ru.stud.Managers;
//
//import java.util.Scanner;
//
//public class Console {
//    private InputHelper inputHelper;
//    private Scanner scanner;
//    private boolean isRunning;
//
//    private static final String symbol = "$";
//
//    public Console(Scanner scanner,InputHelper inputHelper){
//        this.scanner=scanner;
//        this.inputHelper=inputHelper;
//        this.isRunning=true;
//    }
//
//
//    public void start(CommandManager commandManager) {
//        System.out.println("Консоль запущена...");
//        System.out.println("Для проссмотра комманд введите 'help'");
//        System.out.print(symbol);
//
//        while (isRunning && scanner.hasNextLine()) {
//            try {
//                //System.out.print(symbol);
//                String input = scanner.nextLine().trim();
//
//                if (input.isEmpty()) {
//                    continue;
//                }
//                processCommand(input, commandManager);
//                System.out.print(symbol);
//            } catch (Exception e) {
//                System.out.println("Ошибка ввода");
//            }
//        }
//    }
//
//    private void processCommand(String string,CommandManager commandManager){
//        try {
//            String parts[] = string.split("\\s+", 2);
//            String commandName = parts[0];
//            String args = (parts.length > 1) ? parts[1] : "";
//
//            commandManager.addToHistory(commandName);
//
//            boolean success = commandManager.executeCommand(commandName, args);
//
//            if (!success) { //commandName.equals("exit")
//                printError("Не удалось найти команду: " + commandName);
//            }
//            else {
//                println("Комманда успешно выполнена");
//            }
//        } catch (Exception e) {
//            System.out.println("Ошибка обработки ввода команды"+e.getMessage());
//        }
//    }
//
//    public void stop(){
//        this.isRunning=false;
//        printSeparator();
//        print("Завершение работы консоли");
//    }
//
//    public void print(Object obj) {
//        System.out.print(obj);
//    }
//
//    public void println(Object obj) {
//        System.out.println(obj);
//    }
//
//    public void printError(Object obj) {
//        System.err.println("Ошибка: " + obj);
//    }
//
//    public void printSeparator() {
//        println("========================================");
//    }
//
//}
