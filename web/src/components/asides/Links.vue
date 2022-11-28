<template>
  <div class="contents-Links">
    <Messages />
    <div class="layout-links">
      <ul class="text1">
        <li>
          <router-link to="/home">
            <i id="home" class="bx bx-home-alt-2 bx-flashing-hover icon"></i>
            <span class="title-link">{{ $t("aside-left.home") }}</span>
          </router-link>
        </li>
        <li>
          <router-link to="/links">
            <i class="bx bx-group bx-flashing-hover icon"></i>
            <span class="title-link">{{ $t("aside-left.links") }}</span>
          </router-link>
        </li>
        <li>
          <router-link to="/notifications">
            <i class="bx bx-bell bx-flashing-hover icon"></i>
            <span class="title-link">{{ $t("aside-left.notifications") }}</span>
            <div v-if="this.notifCount > 0" class="notif">
              <span>{{ this.notifCount }}</span>
            </div>
          </router-link>
        </li>
        <li @click="showViewMessages">
          <router-link to="messages">
            <i class="bx bx-envelope bx-flashing-hover icon"></i>
            <span class="title-link">{{ $t("aside-left.messages") }}</span>
            <div id="msg" class="notif"><span>28</span></div>
          </router-link>
        </li>
        <li>
          <router-link to="/bookmars">
            <i class="bx bx-bookmark bx-flashing-hover icon"></i>
            <span class="title-link">{{ $t("aside-left.bookmarks") }}</span>
          </router-link>
        </li>
        <li>
          <router-link to="/settings">
            <i class="bx bx-cog bx-flashing-hover icon"></i>
            <span class="title-link">{{ $t("aside-left.settings") }}</span>
          </router-link>
        </li>
        <li id="toggle-mode">
          <i class="bx bx-moon icon" id="moon"></i>
          <i class="bx bx-sun icon" id="sun"></i>
          <span class="title-link">{{ $t("aside-left.themes") }}</span>
          <div @click="toggleMode" class="box-toggle-mode"></div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import Messages from "@/views/ViewMessages.vue";
import { useCurrentSessionStore } from "@/stores/currentSession";
import { switchTheme } from "@/stores/SwitchTheme";
import * as Stomp from "stomp-websocket";

export default {
  name: "Links",
  components: {
    Messages,
  },
  mounted() {
/*    let username;
    if (localStorage.getItem("username")) {
      username = JSON.parse(localStorage.getItem("username"));
    }*/

    this.userSocketConnect(this.username);
  },
  setup() {
    const sessionStore = useCurrentSessionStore();
    const changeMode = switchTheme();
    return {
      sessionStore,
      changeMode,
    };
  },
  data() {
    return {
      notifCount: 0,
      userStompClient: null,
      mediaStompClient: null,
      userSocket: new SockJS("http://localhost:8083/websocket"),
      mediaSocket: null,
      username: this.sessionStore.user.username,
    };
  },
  methods: {
    showViewMessages() {
      const messages = document.querySelector(".container-messages");
      messages.style.visibility = "visible";
      messages.style.transform = "translateY(0)";
    },
    getModeValue() {
      if (localStorage.getItem("mode") === undefined) {
        return "light";
      } else {
        return localStorage.getItem("mode");
      }
    },
    setModeValue(value) {
      localStorage.setItem("mode", value);
    },
    toggleModeValue() {
      this.getModeValue() === "dark"
        ? this.setModeValue("light")
        : this.setModeValue("dark");
    },
    toggleModeVisual() {
      const body = document.querySelector("body");
      const sun = document.getElementById("sun");
      const moon = document.getElementById("moon");

      body.classList.toggle("dark");
      sun.classList.toggle("show");
      moon.classList.toggle("hide");
    },
    toggleMode() {
      this.toggleModeValue();
      this.toggleModeVisual();
      console.log("mode: " + this.getModeValue());
    },

    userSocketConnect(user_name) {
      this.userStompClient = Stomp.over(this.userSocket);

      //   const onNotificationReceive = (notif) => {
      //     console.log("sub notif received");
      //     this.sessionStore.user.notifications.push(notif);
      //     this.notifCount += 1;
      //     console.log("notif count: " + this.notifCount);
      //   };

      //   const onError = (err) => {
      //     console.log("Error connecting user-service websocket");
      //     console.error("err: " + err);
      //   };

      //   const onConnected = (conn) => {
      //     console.log("connected to user-service websocket");
      //     console.log("conn: " + conn);
      //     this.userStompClient.subscribe("/user/topic/notif", onNotificationReceive);
      //   };

      this.userStompClient.connect(
        {
          username: user_name,
          "Access-Control-Allow-Origin": "http://localhost:3000",
        },
        (frame) => {
          console.log("user-socket connected: " + frame);
          this.userStompClient.subscribe("/user/topic/notif", (notif) => {
            console.log("notification received");
            console.log("notification: " + notif);
            this.notifCount += 1;
            this.sessionStore.user.notifications.push(notif);
          });
        }
      );
    },
  },
};
</script>

<style scoped>
a {
  display: flex;
}
.contents-Links {
  width: 250px;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: var(--boxshadow-04);
}
.contents-Links .layout-links {
  height: 100%;
  background: var(--body-color-secondary);
  width: 100%;
  display: flex;
  justify-content: space-between;
  flex-direction: column;
  align-items: center;
  border-bottom-left-radius: 10px;
  border-bottom-right-radius: 10px;
  padding-top: 10px;
}
.contents-Links .layout-links ul {
  width: 100%;
  padding: 25px 20px 0;
}
.contents-Links .layout-links ul li {
  display: flex;
  align-items: center;
  margin-bottom: 25px;
  padding: 10px 20px;
  border-radius: 10px;
  transition: var(--tran-03);
  cursor: pointer;
}
.contents-Links .layout-links ul li:not(#toggle-mode) {
  width: 119%;
  position: relative;
  left: -20px;
  padding: 10px 40px;
  border-radius: 0;
}
.contents-Links .layout-links ul li:hover:not(#toggle-mode, a.router-link-active) {
  background: #52a0fe;
  padding: 10px 35px;
  color: white;
}
.contents-Links .layout-links ul li:not(#toggle-mode)::after {
  content: "";
  position: absolute;
  right: 0;
  height: 100%;
  width: 3px;
  opacity: 0;
  transition: var(--tran-03);
}
.contents-Links .layout-links ul li:hover:not(#toggle-mode)::after {
  background: white;
  opacity: 1;
}
a.router-link-active::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  background: #52a0fe38;
  height: 100%;
}
a.router-link-active::after {
  content: "";
  position: absolute;
  top: 0;
  right: 0;
  width: 3px;
  background: linear-gradient(to right, #52a0fe 0%, #7bc6ff 100%);
  height: 100%;
}
.contents-Links .layout-links ul li .icon {
  font-size: 22px;
  line-height: initial;
}
.contents-Links .layout-links ul li:last-child {
  margin-bottom: 0;
}
.contents-Links .layout-links ul li .title-link {
  margin-left: 15px;
  width: 93px;
}

/* Dark Mode */

.contents-Links .layout-links .box-toggle-mode {
  height: 22px;
  width: 40px;
  background: var(--toggle-color);
  border-radius: 25px;
  position: relative;
  left: 10px;
  cursor: pointer;
}
.contents-Links .layout-links .box-toggle-mode::before {
  content: "";
  position: absolute;
  height: 15px;
  width: 15px;
  border-radius: 50%;
  top: 50%;
  left: 5px;
  transform: translateY(-50%);
  background-color: #fff;
  transition: var(--tran-03);
}
body.dark .contents-Links .layout-links .box-toggle-mode::before {
  left: 20px;
}

/* ======NOTIF===== */

.contents-Links .layout-links ul li .notif {
  position: relative;
  left: 1.5rem;
  padding: 0px 6px;
  border-radius: 25px;
  background: var(--color-secondary);
  font-size: 12px;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  line-height: initial;
  height: 20px;
  top: 0.12rem;
}
.contents-Links .layout-links ul li .notif span {
  transition: 0s;
}
#msg {
  position: relative;
  left: 0.2rem;
}
#toggle-mode {
  background: var(--thirdary-color);
  box-shadow: var(--boxshadow-04);
  position: absolute;
  bottom: 35px;
  cursor: auto;
}
#sun {
  opacity: 0;
  position: absolute;
  left: -999px;
}
#sun.show {
  opacity: 1;
  left: auto;
}
#moon.hide {
  opacity: 0;
}
</style>
