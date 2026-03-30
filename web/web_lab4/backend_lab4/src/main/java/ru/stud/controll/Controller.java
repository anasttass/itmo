package ru.stud.controll;

import jakarta.inject.Inject;
import jakarta.jws.soap.SOAPBinding;
import jakarta.mail.internet.HeaderTokenizer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.xml.bind.ValidationException;
import ru.stud.model.DtoResultHolder;
import ru.stud.model.UserDto;
import ru.stud.model.UserEntity;
import ru.stud.service.ServiceManager;
import ru.stud.service.TokenService;

import java.util.List;
import java.util.Map;

@Path("/control")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Controller {

    @Inject
    private ServiceManager serviceManager;


    @POST
    @Path("/check")
    public Response checkPoint(DtoResultHolder drh, @Context HttpServletRequest request, @Context HttpHeaders headers){

        if (drh.getX()==null || drh.getY()==null || drh.getR()==null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        long userId;

        try { //если выбрасывает ошибку - проблемы с токеном
            Cookie cookie = headers.getCookies().get("AUTH");
            userId = serviceManager.getIdFromCookie(cookie);
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
        DtoResultHolder resp = serviceManager.checkAndSave(drh,userId);
        return Response.ok().entity(resp).build();
        } catch (ValidationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/login")
    public Response checkLogin(UserDto user, @Context HttpServletRequest request){
        if (user.getUsername() == null || user.getPassword()==null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            boolean auth = serviceManager.checkAuth(user);
            if (!auth) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Неверный пароль").build();
            }
            //если заходим, то задаем токен
            String token = serviceManager.createToken(user);
            NewCookie cookie = new NewCookie("AUTH",token,"/",null,null,600,false,true);
//            long id = serviceManager.getUserIdByLogin(user.getUsername());
//            request.getSession().setAttribute("user_id",id); старый способ кладем в сессию
            return Response.ok().cookie(cookie).build();
        } catch (Exception e) {
//            return Response.status(Response.Status.UNAUTHORIZED).entity("Пользователь не найден").build();
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/logout")
    public Response logout(){
        NewCookie cookie = new NewCookie("AUTH","","/",null,null,0,false,true);
        return Response.ok().cookie(cookie).build();
    }

    @POST
    @Path("/signup")
    public Response createUser(UserDto user){
        if (user.getUsername() == null || user.getPassword()==null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            serviceManager.createUser(user);
            return Response.status(Response.Status.CREATED).build();
        }catch (Exception e){
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @DELETE
    @Path("/clear")
    public Response clearHistory(@Context HttpServletRequest request,@Context HttpHeaders headers){
        long userId;

        try { //если выбрасывает ошибку - проблемы с токеном
            Cookie cookie = headers.getCookies().get("AUTH");
            userId = serviceManager.getIdFromCookie(cookie);
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        serviceManager.clear(userId);
        return Response.ok().build();
    }
    //вспомогательный для удаления юзеров
    @DELETE
    @Path("/admin")
    public Response clearUsers(){
        serviceManager.clearUsers();
        return Response.ok().build();
    }

    @GET
    @Path("/history")
    public Response getResults(@Context HttpServletRequest request,@Context HttpHeaders headers){
        long userId;

        try { //если выбрасывает ошибку - проблемы с токеном
            Cookie cookie = headers.getCookies().get("AUTH");
            userId = serviceManager.getIdFromCookie(cookie);
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            List<DtoResultHolder> resp = serviceManager.getResults(userId);
            return Response.ok().entity(resp).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}

