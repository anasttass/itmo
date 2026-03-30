package ru.stud.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Cookie;
import jakarta.xml.bind.ValidationException;
import ru.stud.dao.PasswordService;
import ru.stud.dao.ResultRepository;
import ru.stud.dao.UserRepository;
import ru.stud.model.DtoResultHolder;
import ru.stud.model.ResultHolder;
import ru.stud.model.UserDto;
import ru.stud.model.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@ApplicationScoped
public class ServiceManager {
    @Inject
    private Validator validator;
    @Inject
    private HitChecker hitChecker;
    @Inject
    private ResultConverter resultConverter;
    @Inject
    private UserConverter userConverter;
    @Inject
    private ResultRepository resultRepository;
    @Inject
    private UserRepository userRepository;
    @Inject
    private PasswordService passwordEncoder;
    @Inject
    private TokenService tokenService;


    //МЕТОДЫ ДЛЯ ТОЧЕК
    public DtoResultHolder checkAndSave(DtoResultHolder dto,long owner) throws ValidationException {
        if (!validator.validate(dto)){
            throw new ValidationException("Невалидные данные");
        }
        boolean hit = hitChecker.isHit(dto);
        dto.setNowTime();
        dto.setHit(hit);
        ResultHolder rEntity=this.convertNormalToEntity(dto);//сначала конвертируем в сущ потом сохраняем

        rEntity.setUser(getUserEntityById(owner));

        resultRepository.save(rEntity);

        return dto;
    }

    @Transactional
    public List<DtoResultHolder> getResults(long owner) {
        return getAndConvertResultsFromUser(getUserEntityById(owner));
    }

    public void clear(long owner){
        resultRepository.clearByUser(getUserEntityById(owner));
    }

    //МЕТОДЫ ДЛЯ ПОЛЬЗОВАТЕЛЯ

    public boolean checkAuth(UserDto user) throws Exception {
        UserEntity sentUserEntity = userConverter.DtoToEntity(user); //то что прислал пользователь
        Optional<UserEntity> rawUserInBase = userRepository.findByUsername(sentUserEntity.getUsername());
        if (rawUserInBase.isEmpty()){
            throw new Exception("Пользователь не найден");
        }
        UserEntity userInBase = rawUserInBase.get(); // пользователь из бд с хэшированным паролем
        return passwordEncoder.matches(sentUserEntity.getPassword(), userInBase.getPassword());
    }

    public void createUser(UserDto user) throws Exception {
        UserEntity userEntity = userConverter.DtoToEntity(user);
        Optional<UserEntity> checked = userRepository.findByUsername(userEntity.getUsername());
        if(!checked.isEmpty()){
            throw new Exception("Такой пользователь уже существует");
        }
        String rawPassword = userEntity.getPassword();
        String hashPassword = passwordEncoder.hash(rawPassword);
        userEntity.setPassword(hashPassword);
        userRepository.save(userEntity);
    }
    public void clearUsers(){
        userRepository.clearUsers();
    }


    //РАБОТА С ТОКЕНАМИ
    public String createToken(UserDto userDto){
        UserEntity userEntity = getUserEntityByLogin(userDto.getUsername());
        String token = tokenService.generateToken(userEntity);
        return token;
    }
    public long getIdFromCookie(Cookie cookie){
        String token = cookie.getValue();
        long id = tokenService.extractUserId(token);
        return id;
    }


    //методы чтобы ДОСТАТЬ ЮЗЕРА
    public UserEntity getUserEntityByLogin(String login){
        return userRepository.findByUsername(login).get();
    }
    public long getUserIdByLogin(String login){
        UserEntity user = getUserEntityByLogin(login);
        return user.getId();
    }
    public UserEntity getUserEntityById(long id){
        return userRepository.findById(id).get();
    }


    //дополнительно

    private List<DtoResultHolder> getAndConvertResultsFromUser(UserEntity user){

        return user.getResults().stream().map(this::convertEntityToNormal).collect(Collectors.toList());
    }


    //КОНВЕРТЕРЫ
    public DtoResultHolder convertEntityToNormal(ResultHolder rh){
        return resultConverter.entityToNormal(rh);
    } //конвертирует сущность в обычный класс

    public ResultHolder convertNormalToEntity(DtoResultHolder drh){
        return resultConverter.NormalToEntity(drh);
    }//конвертирует обычный класс в сущность
}
