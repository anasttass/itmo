package ru.stud.service;

import jakarta.enterprise.context.ApplicationScoped;
import ru.stud.model.UserDto;
import ru.stud.model.UserEntity;

@ApplicationScoped
public class UserConverter {
    public UserEntity DtoToEntity(UserDto dto){
        return new UserEntity(dto.getUsername(),dto.getPassword());
    }
    public UserDto EntityToDto(UserEntity ent){
        return new UserDto(ent.getUsername(),ent.getPassword());
    }
}
