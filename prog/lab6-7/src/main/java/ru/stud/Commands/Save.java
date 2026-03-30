package ru.stud.Commands;

import ru.stud.Managers.CollectionManager;

public class Save extends AbsCommand{
    private final CollectionManager collectionManager;


//    УБРАНО
    public Save(CollectionManager collectionManager) {
        super("save", "Сохранить коллекцию в файл.Не требует аргумента");
        this.collectionManager=collectionManager;
    }

    public boolean execute(String args){
        try{
            if(args!=null && !args.isEmpty()){
                System.out.println("Ошибка! Команда save не требует атрибутов.");
            }
//            collectionManager.saveCollection();
            //System.out.println("Коллекция успешно сохранена");
            return true;
        } catch (Exception e){
            System.err.println("Ошибка при работе функции save"+e.getMessage());
            return false;
        }
    }
}
