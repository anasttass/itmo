package ru.stud.Commands;

import ru.stud.Collection.LabWork;
import ru.stud.Common.CommandResponse;
import ru.stud.Common.User;
import ru.stud.Managers.CollectionManager;
import ru.stud.Managers.InputHelper;

public class Add extends AbsCommand{

    private final CollectionManager collectionManager;

    public Add(CollectionManager collectionManager) {
        super("add", "Добавить новый элементю.Не требует аргумента");
        this.collectionManager=collectionManager;
    }

    public CommandResponse execute(Object[] args, Object data, User user){
        try{
//            if(args!=null && !args.isEmpty()){
//                System.out.println("Ошибка! Команда add не требует атрибутов. Все данные будут запрошены отдельно");
//            }
//            System.out.println("ДОБАВЛЕНИЕ НОВОГО ЭЛЕМЕНТА\n");
            LabWork newEl = (LabWork) data;
            if(newEl==null){
                return new CommandResponse(false,"Нельзя создать пустой элемент");
            }
            String result = collectionManager.addLabWork(newEl,user);
            return new CommandResponse(true, result);
        } catch (Exception e){
            return new CommandResponse(false,"Ошибка при работе функции add "+e.getMessage());
        }
    }

}
