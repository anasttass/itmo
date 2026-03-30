package ru.stud.Commands;

import ru.stud.Common.CommandResponse;
import ru.stud.Common.User;
import ru.stud.Managers.CollectionManager;

public class Shuffle extends AbsCommand{
    private final CollectionManager collectionManager;

    public Shuffle(CollectionManager collectionManager) {
        super("shuffle", "Перемешать все элементы коллекции.Не требует аргумента");
        this.collectionManager=collectionManager;
    }

    public CommandResponse execute(Object[] args, Object data, User user){
        try{
            String res = collectionManager.shuffleCollection();
            return new CommandResponse(true,res);
        } catch (Exception e){
            return new CommandResponse(false,"Ошибка при работе функции shuffle"+e.getMessage());
        }
    }
}
