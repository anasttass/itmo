package ru.stud.Commands;

import ru.stud.Common.CommandResponse;
import ru.stud.Common.User;
import ru.stud.Managers.CollectionManager;

public class RemoveByID extends AbsCommand{
    private final CollectionManager collectionManager;

    public RemoveByID(CollectionManager collectionManager) {
        super("remove_by_id", "Удаляет элемент по айди. Требует число типа long");
        this.collectionManager=collectionManager;
    }

    public CommandResponse execute(Object[] args, Object data, User user){
        if (args.length < 1) {
            return new CommandResponse(false, "Укажите id элемента");
        }
        try{
            long id =Long.parseLong((String) args[0]);
            String res = collectionManager.removeById(id,user);
            return new CommandResponse(true,res);
        } catch (Exception e){
            return new CommandResponse(false,"Ошибка при работе функции remove_by_id"+e.getMessage());
        }
    }
}
