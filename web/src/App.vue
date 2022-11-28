<script lang="ts">
import AsideLeft from "./components/asides/AsideLeft.vue";
import AsideRight from "./components/asides/AsideRight.vue";
import Langs from "./components/nav/Langs.vue";
import PopUpPoster from "./components/pop-up/Poster.vue";
import Search from "./components/nav/Search.vue";

import { useCurrentSessionStore } from "@/stores/currentSession";

export default {
  components: {
    AsideRight,
    AsideLeft,
    Search,
    Langs,
    PopUpPoster,
  },
  data() {
    return {
      clickPost: false,
      loginPage: true,
    };
  },
  setup() {
    const sessionStore = useCurrentSessionStore();
    //const  currentUser = useCurrentUserStore();

    return {
      sessionStore,
    };
  },
  methods: {
    onClickPost() {
      this.clickPost = !this.clickPost;
    },
    hideMessages() {
      const messages = document.querySelector(".container-messages");
      messages.style.visibility = "hidden";
      messages.style.transform = "translateY(100%)";
    },
  },
};
</script>

<template>
  <div>
    <div class="area">
      <ul class="circles">
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
      </ul>
    </div>
    <template v-if="!sessionStore.user.connected">
      <!-- Register Components -->
      <div class="RegisterComponents">
        <div class="container-bubble-illustration">
          <img
            src="@/assets/images/illustration/bubble_illustration.png"
            class="bubble_illustration"
            alt=""
          />
        </div>
        <div class="container_illustration">
          <img
            src="@/assets/images/illustration/Login_Illustration.png"
            class="illustration_login"
            v-if="$route.path === '/login' || $route.path === '/'"
          />
          <img
            src="@/assets/images/illustration/Signup_Illustration.png"
            class="illustration_signup"
            v-if="$route.path === '/signup'"
          />
        </div>

        <div class="container_box_register">
          <div class="container_box_register_title_logo">
            <h1 class="title">Fastlink</h1>
            <img src="./assets/logo/logo.png" alt="logo" id="logo" />
          </div>
          <router-view :key="$route.fullPath" />
        </div>
      </div>
    </template>

    <template v-else>
      <div class="container-grid">
        <div class="container-logo sidebar">
          <header>
            <router-link to="/home">
              <img
                @click="hideMessages"
                src="./assets/logo/logo.png"
                alt="logo"
                id="logo"
              />
            </router-link>
          </header>
        </div>
        <div class="container-search sidebar">
          <Search />
        </div>
        <div class="container-btn-post-lang sidebar">
          <nav>
            <ul class="nav-links">
              <li>
                <button @click="onClickPost" class="btn-post text3">
                  <i class="bx bx-plus bx-tada bx-rotate-90"></i>{{ $t("navbar.post") }}
                </button>
              </li>
              <li>
                <Langs />
              </li>
            </ul>
          </nav>
        </div>
        <div class="container-aside-left contents_">
          <AsideLeft />
        </div>
        <div class="container-aside-right contents_">
          <AsideRight />
        </div>
        <div class="container-main contents_">
          <template v-if="clickPost">
            <PopUpPoster v-on:clickPostFalse="onClickPost" />
          </template>
          <router-view :key="$route.fullPath" />
        </div>
      </div>
    </template>
  </div>
</template>

<style>
@import "././css/index.css";
@import "././css/authentification/registerPage.css";
@import url("https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap");
</style>
