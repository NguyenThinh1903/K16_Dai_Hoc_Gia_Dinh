import { createWebHistory,createRouter } from "vue-router";
import Home from '@/components/ComHome.vue'
import GioiThieu from '@/components/ComGioiTh.vue'
import LienHe from '@/components/ComLienHe.vue'
import ProductDetail from '@/components/ComProductDetail.vue'
import ComCart from '@/components/ComCart.vue'
import ComLogin from '@/components/ComLogin.vue'
import ComRegister from '@/components/ComRegister.vue';
const routes=[
    {
        path:"/",
        name:"Home",
        component:Home
    },
    {
        path:"/gioithieu",
        name:"GioiThieu",
        component:GioiThieu
    },
    {
        path:"/lienhe",
        name:"LienHe",
        component:LienHe
    },
    {
        path:"/product/:id",
        name:"ProductDetail",
        component:ProductDetail
    },
    {
        path:"/cart",
        name:"ComCart",
        component:ComCart
    },
    {
        path:"/login",
        name:"ComLogin",
        component:ComLogin
    },
    {
        path:"/register",
        name:"ComRegister",
        component:ComRegister
    },
]

const router=createRouter({
    history:createWebHistory(),
    routes
})
export default router