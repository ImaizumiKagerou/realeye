import {createRouter, createWebHistory} from "vue-router";
import Home from "../views/Home.vue";

const routes = [
    {
        path: '/',
        redirect: '/login'
    }, {
        path: "/",
        name: "Home",
        component: Home,
        children: [
            {
                path: "/userManage",
                name: "userManage",
                meta: {
                    title: '用户管理'
                },
                component: () => import (
                    /* webpackChunkName: "UserManage" */
                    "../views/UserManage.vue")
            },
            {
                path: "/APIKeyManage",
                name: "APIKeyManage",
                meta: {
                    title: 'APIKey管理'
                },
                component: () => import (
                    /* webpackChunkName: "APIKeyManage" */
                    "../views/APIKeyManage.vue")
            },
            {
                path: "/CommunityManage",
                name: "CommunityManage",
                meta: {
                    title: '社区管理'
                },
                component: () => import (
                    /* webpackChunkName: "CommunityManage" */
                    "../views/CommunityManage.vue")
            },
            {
                path: "/PrimeArticleManage",
                name: "PrimeArticleManage",
                meta: {
                    title: '推荐文章管理'
                },
                component: () => import (
                    /* webpackChunkName: "PrimeArticleManage" */
                    "../views/PrimeArticleManage.vue")
            }
        ]
    }, {
        path: "/login",
        name: "Login",
        meta: {
            title: '登录'
        },
        component: () => import (
            /* webpackChunkName: "login" */
            "../views/Login.vue")
    }
];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
});

export default router;
