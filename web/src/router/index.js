import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Account from '../views/Account.vue'
import { Locales } from "@/i18n";
import en from "@/locales/en.json";
import fr from "@/locales/fr.json";
import {useCurrentSessionStore} from "../stores/currentSession";
//import {ref} from "vue";




export const messages = {
  [Locales.EN]: en,
  [Locales.FR]: fr
};
export const defaultLocale = Locales.FR;

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'notConnected',
      component: () => import('@/views/LoginView.vue'),
    },
    {
      path: '/home',
      name: 'home',
      component: Home
    },
    {
      path: '/account',
      name: 'account',
      component: Account
    },
    {
      path: '/messages',
      name: 'messages',
      component: () => import('@/views/ViewMessages.vue'),
    },
    
    {
      path: '/signup',
      name: 'signup',
      component: () => import('@/views/SignupView.vue'),
    },

    {
      path: '/profile/:id',
      name: 'accountTarget',
      component: () => import('@/views/UserAccountView.vue'),
    },
    
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginView.vue')
    
  },
    {
      path: '/forgot-password',
      name: 'forgot-password',
      component: () => import('@/components/ForgotPassword.vue'),
    },
  ]
})

  

  router.beforeEach((to, from, next)=>{
      const sessionStore = useCurrentSessionStore();

      if(to.name === 'login' && sessionStore.user.connected){
        next('/home');
      }else if(to.name === 'home' && !sessionStore.user.connected){
        next('/login');
      }else if(to.name === 'notConnected' && sessionStore.user.connected){
        next('/home');

      }else if(to.name === 'signup' && sessionStore.user.connected){
        next('/home');
      }else if(to.name === 'notConnected' && !sessionStore.user.connected) {
        next('/login');
      }else if (from.name === 'accountTarget' && !sessionStore.user.connected) {
        next('/login');
      } else {
        next();

      }
  }
  )
export default router
