package ru.stud.Commands;

import ru.stud.Collection.LabWork;
import ru.stud.Common.CommandResponse;
import ru.stud.Common.User;
import ru.stud.Managers.CollectionManager;
import ru.stud.Managers.InputHelper;

public class Show extends AbsCommand{
    private final CollectionManager collectionManager;

    public Show(CollectionManager collectionManager) {
        super("show", "Показать все элементы коллекции.Не требует аргумента");
        this.collectionManager=collectionManager;
    }

    public CommandResponse execute(Object[] args, Object data, User user){
        try{
            String res = collectionManager.showCollection();
            return new CommandResponse(true,res);
        } catch (Exception e){
            return new CommandResponse(false,"Ошибка при работе функции show "+e.getMessage());
        }
    }

}
