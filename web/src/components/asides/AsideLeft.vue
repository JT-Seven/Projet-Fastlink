<template>
  <div class="contents-asideLeft">
    <div class="container-profil box">
      <Profil @click="onClickShowPopUp" v-click-outside="hidePopUp" />
      <template v-if="showPopUp">
        <div class="pop-up-account" id="popUpAccount">
          <div class="polygon"></div>
          <router-link to="/account" class="btn-link">
            <div 
             class="profil field">
              <span class="text3"
                ><svg
                  aria-label="Profil"
                  class="_8-yf5"
                  color="#262626"
                  fill="#262626"
                  height="16"
                  role="img"
                  viewBox="0 0 24 24"
                  width="16"
                >
                  <circle
                    cx="12.004"
                    cy="12.004"
                    fill="none"
                    r="10.5"
                    stroke="currentColor"
                    stroke-linecap="round"
                    stroke-miterlimit="10"
                    stroke-width="2"
                  ></circle>
                  <path
                    d="M18.793 20.014a6.08 6.08 0 00-1.778-2.447 3.991 3.991 0 00-2.386-.791H9.38a3.994 3.994 0 00-2.386.791 6.09 6.09 0 00-1.779 2.447"
                    fill="none"
                    stroke="currentColor"
                    stroke-linecap="round"
                    stroke-miterlimit="10"
                    stroke-width="2"
                  ></path>
                  <circle
                    cx="12.006"
                    cy="9.718"
                    fill="none"
                    r="4.109"
                    stroke="currentColor"
                    stroke-linecap="round"
                    stroke-miterlimit="10"
                    stroke-width="2"
                  ></circle></svg
                >Profile</span
              >
            </div>
          </router-link>
          <div @click="deconnected" class="logout field text3">
            <span class="disconnected">Déconnexion</span>
          </div>
        </div>
      </template>
      <template v-else></template>
    </div>
    <div class="container-links box">
      <Links />
    </div>
  </div>
</template>

<script>
import Profil from "../asides/profil/Profil.vue";
import Links from "../asides/Links.vue";
import { useCurrentSessionStore } from "@/stores/currentSession";
export default {
  data() {
    return {
      showPopUp: false,
    };
  },
  setup() {
    const sessionStore = useCurrentSessionStore();
    return {
      sessionStore,
    };
  },
  methods: {
    onClickShowPopUp() {
      this.showPopUp = !this.showPopUp;
    },
    deconnected() {
      this.sessionStore.resetUserStore();
      console.log("connected :" + this.sessionStore.user.connected);
      this.$router.push("/login");
      this.$router.go();
    },
    hidePopUp() {
      this.showPopUp = false;
    },
  },
  components: {
    Profil,
    Links,
  },
};
</script>

<style scoped>
.contents-asideLeft {
  padding: 20px 20px;
  height: 100vh;
  overflow: scroll;
}
.contents-asideLeft .container-profil {
  display: flex;
  justify-content: flex-end;
}
.contents-asideLeft .container-links {
  display: flex;
  justify-content: flex-end;
  height: calc(100% - 120px);
  padding: 0;
}

/* ============ POP-UP-ACCOUNT ========== */

.pop-up-account {
  position: fixed;
  left: 23.5rem;
  top: 5rem;
  width: 250px;
  height: auto;
  background: var(--body-color-secondary);
  transition: var(--tran-03);
  border-radius: 10px;
  box-shadow: var(--boxshadow-04);
  transition: var(--tran-03);
  z-index: 989;
}
.pop-up-account .profil {
  height: 50px;
  width: 100%;
  display: flex;
  align-items: center;
  border-bottom: 0.1px solid var(--timeline-color);
  border-radius: 10px 10px 0 0;
  padding-left: 15px;
}
.pop-up-account .profil svg {
  color: var(--primary-color);
  width: 18px;
  height: 18px;
  margin-right: 15px;
}
.pop-up-account .profil span {
  display: flex;
  align-items: center;
}
.pop-up-account .profil img {
  width: 30px;
}
.pop-up-account .logout {
  height: 50px;
  width: 100%;
  display: flex;
  align-items: center;
  cursor: pointer;
  border-radius: 0px 0px 10px 10px;
  padding-left: 15px;
}
.field {
  transition: var(--tran-03);
  position: relative;
}
.field:hover {
  background: rgb(0 0 0 / 7%);
}
.polygon {
  clip-path: polygon(50% 50%, 0% 100%, 100% 100%);
  background: var(--body-color-secondary);
  position: fixed;
  height: 20px;
  width: 15px;
  transition: var(--tran-03);
  transform: rotate(270deg) translate(-15px, -15px);
}
</style>
