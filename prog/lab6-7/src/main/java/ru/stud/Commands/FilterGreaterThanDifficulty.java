package ru.stud.Commands;

import ru.stud.Collection.Difficulty;
import ru.stud.Common.CommandResponse;
import ru.stud.Common.User;
import ru.stud.Managers.CollectionManager;
import ru.stud.Managers.InputHelper;

public class FilterGreaterThanDifficulty extends AbsCommand{
    private final CollectionManager collectionManager;

    public FilterGreaterThanDifficulty(CollectionManager collectionManager) {
        super("filter_greater_than_difficulty", "Вывести элементы, значение поля difficulty которых больше заданного");
        this.collectionManager=collectionManager;
    }

    public CommandResponse execute(Object[] args, Object data, User user){
        try{

            Difficulty difficulty = Difficulty.valueOf(args[0].toString().toUpperCase());

            String res = collectionManager.filterByDifficulty(difficulty);

            return new CommandResponse(true,res);
        } catch (Exception e){
            return new CommandResponse(false,"Ошибка при работе функции filter_greater_than_difficulty"+e.getMessage());
        }
    }
}
