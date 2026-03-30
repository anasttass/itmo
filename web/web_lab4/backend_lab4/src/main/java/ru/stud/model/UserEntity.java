package ru.stud.model;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.util.List;


@Entity
@Table(name = "users")
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column (nullable = false, unique = true)
    private String login;

    @Column (nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResultHolder> results;

    public UserEntity(){}
    public UserEntity(String login, String password){
        this.login=login;
        this.password=password;
    }

    public String getUsername(){
        return  login;
    }
    public String getPassword(){
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public long getId() {
        return id;
    }

    public List<ResultHolder> getResults() {
        return results;
    }
}
