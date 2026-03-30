<template>
    <div class="card">
        <button @click="deleteUsers()">Удалить всех пользователей</button>
        <h2>{{ message }}</h2>
    </div>
</template>

<script>
    export default{
        data(){
            return{
                message:""
            }
        },
        methods:{
            async deleteUsers(){
                const resp = await fetch("http://localhost:8081/server4/app/control/admin",{
                    method: "DELETE"
                })
                if (resp.ok){
                    this.message="Все пользователи удалены. Их точки тоже";
                }
                else{
                    this.message="Произошла ошибка";
                }
            }
        }
    }
</script>

<style>

</style>