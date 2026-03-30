package ru.stud.Managers;

import ru.stud.Collection.Difficulty;
import ru.stud.Collection.LabWork;
import ru.stud.Common.User;
import ru.stud.DataBase.DBManager;

import java.time.LocalDateTime;
import java.util.*;

public class CollectionManager {
    private final DBManager dbManager;
    public Vector<LabWork> collection;
    public LocalDateTime initTime;

    public CollectionManager(DBManager dbManager) {
        this.dbManager=dbManager;
        this.collection=dbManager.loadLabworks();
        this.initTime=LocalDateTime.now();
    }

    //валидатор
    public String validateAll(User user) {
        List<Long> forRevome = new ArrayList<>();
        String ans = "Лабораторные с id= ";
        collection.forEach(lw -> {
            lw.fixIdCounter();
            if (!lw.validate()) {
                forRevome.add(lw.getId());
            }
        });
        for(Long id : forRevome){
            this.removeById(id,user);
            ans+=id.toString()+", ";
        }

        return ans+" невалиды и будут удалены из коллекции!";
    }

    public String addLabWork(LabWork lb, User user){
        long id = dbManager.insertLabwork(lb,user);
        if (id>0) {
            lb.setId(id);
            collection.add(lb);
            return "Работа успешно добавлена";
        }
        return "Ошибка добавления работы";
    }

    public String clearCollection(User user){
        if(dbManager.clear(user)) {
            collection.removeIf(labWork -> dbManager.isLabOwnedByUser(labWork.getId(),user));
            return "Коллекция очищена";
        }
        return "Ошибка очистки коллекции";
    }

    public String showCollection(){
        collection=dbManager.loadLabworks();
        if(collection.isEmpty() || collection.size()==0){
            return "Коллекция еще пуста";
        }
//        StringBuilder sb = new StringBuilder();
//        for (LabWork lb : collection){
//            sb.append(lb.toString()).append("\n");
//        }
//        return sb.toString();
        return collection.stream().sorted(Comparator.comparing(LabWork::getId)).map(LabWork::toString)
                .reduce("",(a,b)->a+b+"\n");
    }

    public String shuffleCollection(){
        Collections.shuffle(collection);
        return collection.stream().map(LabWork::toString)
                .reduce("",(a,b)->a+b+"\n");
    }

    public String info(){
        return "Тип: " + this.getClass() +  "\nДата инициализации: " + initTime + "\nКоличество элементов: " + collection.size();
    }

    public boolean contains(long id){
        boolean flag=false;
        for (LabWork lb : collection){
            if(lb.getId()==id){
                flag=true;
            }
        }
        return flag;
    }

    public String updateById(long id, LabWork lw, User user){
        if(dbManager.updateLabById(id,lw,user)) {
            collection.removeIf(f -> f.getId() == id); //нужна проверка!!!!
            lw.setId(id);
            collection.add(lw);
            return "Элемент обновлен";
        }
        return "Элемент не найдет или не принадлежит вам";
    }

    public String removeById(long id,User user){
        if(dbManager.removeLabById(id,user)){
            collection.removeIf(labWork -> labWork.getId()==id);
            return "Элемент удален";
        }
        return "Элемент не найден или не принадлежит вам";
    }

//    public void saveCollection(){
//        fileManager.writeCollection(collection);
//    }

    public String reorderCollection(){
        Collections.reverse(collection);
        return collection.stream().map(LabWork::toString)
                .reduce("",(a,b)->a+b+"\n");
    }

    public int sumOfMinimalPoint(){
        int res=0;
        for (int i=0;i< collection.size();i++){
            res+=collection.get(i).getMinimalPoint();
        }
        return res;
    }

    public String filterByDifficulty(Difficulty difficulty){
        StringBuilder res = new StringBuilder();
        for (int i=0;i< collection.size();i++){
            if(collection.get(i).getDifficulty().compareTo(difficulty)>0){
                res.append(collection.get(i).toString()).append("\n");
            }
        }
        return res.toString();
    }

    public String printAscending(){
        Vector<LabWork> sorted = new Vector<>(collection);
        Collections.sort(sorted);
        String res = "";
        for (int i=0;i< sorted.size();i++){
            res+=sorted.get(i).toString()+"\n";
        }
        return res;
    }
}

//class A {
//    int x;
//    int y;
//
//    public int getX() {
//        return x;
//    }
//
//    public int getY() {
//        return y;
//    }
//
//    public void setX(int x) {
//        this.x = x;
//    }
//
//    public void setY(int y) {
//        this.y = y;
//    }
//}
//class CompareHelp implements Comparator<A>{
//    @Override
//    public int compare(A o1, A o2) {
//        int x1=o1.getX();
//        int x2=o2.getX();
//        int y1= o1.getY();
//        int y2= o2.getY();
//        int l1=((x1*x1)+(y1*y1));
//        int l2=((x2*x2)+(y2*y2));
//        return l1-l2;
//    }
//
//}
//
//class Animal {}
//class Cat extends Animal {}
//class Main {
//
//    public static void main(String[] args) {
//        ArrayList<? extends Animal> l = new ArrayList<>();
//        String x = "";
//        l.add(new Cat());
//        l.add(new Animal());
//        l.add(new Object())
//    }
//}
