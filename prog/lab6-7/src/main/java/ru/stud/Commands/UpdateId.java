package ru.stud.Commands;

import ru.stud.Collection.LabWork;
import ru.stud.Common.CommandResponse;
import ru.stud.Common.User;
import ru.stud.Managers.CollectionManager;
import ru.stud.Managers.InputHelper;

public class UpdateId extends AbsCommand{
    private final CollectionManager collectionManager;

    public UpdateId(CollectionManager collectionManager) {
        super("update_by_id", "Изменяяет элемент по айди. Требует число типа long");
        this.collectionManager=collectionManager;
    }

    public CommandResponse execute(Object[] args, Object data, User user) {
        if (args.length < 1) {
            return new CommandResponse(false, "Укажите id элемента");
        }
        try {
            Long id = Long.parseLong((String) args[0]);
            ru.stud.Collection.LabWork newEl = (LabWork) data;
            String result = collectionManager.updateById(id, newEl, user);
            return new CommandResponse(true, result);
        } catch (Exception e) {
            return new CommandResponse(false, "Ошибка при работе функции update_by_id" + e.getMessage());
        }
    }
}
