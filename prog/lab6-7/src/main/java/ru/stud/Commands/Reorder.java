package ru.stud.Commands;

import ru.stud.Common.CommandResponse;
import ru.stud.Common.User;
import ru.stud.Managers.CollectionManager;

public class Reorder extends AbsCommand{
    private final CollectionManager collectionManager;

    public Reorder(CollectionManager collectionManager) {
        super("reorder", "Отсортировать коллекцию в порядке, обратном нынешнему");
        this.collectionManager=collectionManager;
    }

    public CommandResponse execute(Object[] args, Object data, User user){
        try{
            String res = collectionManager.reorderCollection();
            return new CommandResponse(true,res);
        } catch (Exception e){
            return new CommandResponse(false,"Ошибка при работе функции reorder"+e.getMessage());
        }
    }
}
