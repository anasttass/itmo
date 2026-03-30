package ru.stud.Commands;

import ru.stud.Common.CommandResponse;
import ru.stud.Common.User;
import ru.stud.Managers.CollectionManager;

public class Info extends AbsCommand{
    private final CollectionManager collectionManager;

    public Info(CollectionManager collectionManager) {
        super("show", "Показать все элементы коллекции.Не требует аргумента");
        this.collectionManager=collectionManager;
    }

    public CommandResponse execute(Object[] args, Object data, User user){
        try{

            String res = collectionManager.info();
            return new CommandResponse(true,res);
        } catch (Exception e){
            return new CommandResponse(false,"Ошибка при работе функции show"+e.getMessage());
        }
    }
}
