package ru.stud.Commands;

import ru.stud.Collection.LabWork;
import ru.stud.Common.CommandRequest;
import ru.stud.Common.CommandResponse;
import ru.stud.Common.User;
import ru.stud.Managers.CollectionManager;
import ru.stud.Managers.InputHelper;

public class Clear extends AbsCommand {
    private final CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        super("clear", "Очистить всю коллекцию.Не требует аргумента");
        this.collectionManager=collectionManager;
    }

    public CommandResponse execute(Object[] args, Object data, User user){
        try{
//            if(args!=null && !args.isEmpty()){
//                System.out.println("Ошибка! Команды clear не требует атрибутов.");
//            }
//            System.out.println("УДАЛЕНИЕ КОЛЛЕКЦИИ\n");
            String result = collectionManager.clearCollection(user);
            return new CommandResponse(true,result);
        } catch (Exception e){;
            return new CommandResponse(false,"Ошибка при работе функции clear "+e.getMessage());
        }
    }
}
