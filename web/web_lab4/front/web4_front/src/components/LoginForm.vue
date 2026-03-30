<template>
    <div class="card" style="width: 60%;justify-self: center;">
        <h1 class="welcome">Войдите в аккаунт или зарегистрируйтесь, чтобы продолжить!</h1>

            <div style="justify-self: center;">
                <input type="text" placeholder="Введите имя пользователя" v-model="name" autocomplete="username"></input>
                <input type="password" placeholder="Введите пароль" v-model="pass" autocomplete="password"></input>
            </div>
            <div style="display: flex;" class="btm_div">
                <button @click="sendUser()">Войти</button>
                <button @click="createUser()">Зарегистрироваться</button>
            </div>

        <h3>{{ message }}</h3>
    </div>                   
</template>

<script>
import router from '@/router/router';

    export default{
        data(){
            return{
                name:"",
                pass:"",
                message:""
            }
        },
        methods:{
            async sendUser(){
                if(this.pass=="" || this.name==""){
                    this.message="Введите логин и пароль";
                    return;
                }
                const resp = await fetch("http://localhost:8081/server4/app/control/login",{
                    method: "POST",
                    credentials: "include",
                    headers: {"Content-Type":"application/json"},
                    body: JSON.stringify({username: this.name, password: this.pass})
                })

                if (resp.ok){
                    // localStorage.setItem("auth","true")
                    router.push("/")
                }else{
                    const data = await resp.text();
                    this.message = data;
                }
            },
            async createUser(){
                if(this.pass=="" || this.name==""){
                    this.message="Введите логин и пароль";
                    return;
                }
                const resp = await fetch("http://localhost:8081/server4/app/control/signup",{
                    method: "POST",
                    credentials: "include",
                    headers: {"Content-Type":"application/json"},
                    body: JSON.stringify({username: this.name, password: this.pass})
                })

                if (resp.status==201){
                    this.message="Пользователь "+this.name+ " успешно зарегистрирован";
                }else if (resp.status==409){
                    this.message="Пользователь с таким именем уже существует";
                }
                else{
                    const data = await resp.text();
                    this.message = data;
                }
            
            }
        }
    }

</script>

<style>
    input{
        margin: 40px;
    }
    @media (max-width: 733px){
        .welcome{
            font-size: 20px;
        }
        .btm_div button{
            height: 20px;
            width: 120px;
            font-size: 9px;
        }
    }
</style>