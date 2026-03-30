package ru.stud.Commands;

import ru.stud.Common.CommandResponse;
import ru.stud.Common.User;
import ru.stud.Managers.CollectionManager;

public class SumOfMinimalPoints extends AbsCommand{
    private final CollectionManager collectionManager;

    public SumOfMinimalPoints(CollectionManager collectionManager) {
        super("sum_of_minimal_points", "Вывести сумму значений поля minimalPoint для всех элементов коллекции");
        this.collectionManager=collectionManager;
    }

    public CommandResponse execute(Object[] args, Object data, User user){
        try{
            int res = collectionManager.sumOfMinimalPoint();
            String response = "Сумма всех минимальных баллов: "+ res;
            return new CommandResponse(true,response);
        } catch (Exception e){
            return new CommandResponse(false,"Ошибка при работе функции sum_of_minimal_points"+e.getMessage());
        }
    }
}
