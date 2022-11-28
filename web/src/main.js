import { createApp, watch } from 'vue';
import { createPinia } from 'pinia';
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate';
import { createI18n } from 'vue-i18n';
import { messages, defaultLocale } from "@/router/index";
import App from './App.vue';
import mitt from 'mitt'
import 'boxicons';
import router from './router';
import './css/index.css';
import vue3GoogleLogin from 'vue3-google-login';
//import path from "path";

/*require('dotenv').config(
    {path: path.join(__dirname, ".env")}
)*/



const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

if (localStorage.getItem('user')) {
    pinia.state.value = JSON.parse(localStorage.getItem('user'))
}

watch(
    pinia.state.value,
    (value) => {
      localStorage.setItem('user', JSON.stringify(value));
    },
{
      deep: true,
      immediate: true,
    }
)
const i18n = createI18n({
  messages,
  defaultLocale: defaultLocale,
  locale: defaultLocale
})

const emitter = mitt();
const app = createApp(App)
app.config.globalProperties.emitter = emitter;
const clickOutside = {
  beforeMount: (el, binding) => {
    el.clickOutsideEvent = event => {
      if (!(el == event.target || el.contains(event.target))) {
        binding.value();
      }
    };
    document.addEventListener("click", el.clickOutsideEvent);
  },
  unmounted: el => {
    document.removeEventListener("click", el.clickOutsideEvent);
  },
};

app.use(pinia)
app.use(router)
app.use(i18n)
app.use(vue3GoogleLogin, {
  clientId: "1072368737107-hh83ur69qh2q49n2fs3ru8sfpn6bld3l.apps.googleusercontent.com",
  scope: 'openid profile email',
  prompt: 'select_account consent',
})
app.directive("click-outside", clickOutside)

app.mount('#app')

