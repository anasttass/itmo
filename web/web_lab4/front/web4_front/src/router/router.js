import {createRouter, createWebHistory} from 'vue-router'
import LoginView from "@/views/Start.vue"
import MainView from '@/views/Main.vue'

const routes = [
    {
        path: "/login",
        component: LoginView
    },
    {
        path:"/",
        component: MainView,
        meta:{requiresAuth:true}
    }
]

const router = createRouter({
    history:createWebHistory(),
    routes
})

router.beforeEach(async (to,from,next)=>{
    if (to.meta.requiresAuth){
        try{
            const resp = await fetch("http://localhost:8081/server4/app/control/history",{
                method:"GET",
                credentials:"include"
            })
            if (resp.ok){
                next();
            }
            else{
                next("/login")
            }
        }catch(err){
            console.log(err)
            next("/login")
        }
    }else{
        next()
    }
});
   

export default router