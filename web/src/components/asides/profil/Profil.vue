<!-- <script setup>

  if (localStorage.getItem('profile_pic')) {
    this.profile_pic = JSON.parse(localStorage.getItem('profile_pic'));
  }

</script> -->

<template>
  <!--@click="this.$emit('showPopUpAccount', this.showPopUp)"-->
  <div class="layout-profil">
    <div class="box-img">
      <img
        v-if="provider === 'google' && !profile_pic.includes('/api/v1/file') && profile_pic != null"
        v-bind:src="this.profile_pic"
        alt="photo de profile"
      />

      <img
          v-if="provider === 'google' && profile_pic.includes('/api/v1/file')"
          v-bind:src="this.profile_pic + '?userId=' + this.user_id"
          alt="photo de profile"
      />

      <img
        v-if="provider === 'local' && profile_pic != null"
        v-bind:src="this.profile_pic + '?userId=' + this.user_id"
        alt="photo de profil"
      />

      <img
        v-if="profile_pic == null"
        src="../../../assets/avatar.png"
        alt="photo par defaut"
      />
    </div>
    <span class="text3">{{ this.username }}</span>
  </div>
</template>

<script>
import { useCurrentSessionStore } from "@/stores/currentSession";

export default {
  data() {
    return {
      showPopUp: false,
      username: this.sessionStore.user?.username,
      profile_pic: this.sessionStore.user?.profile_pic,
      provider: this.sessionStore.user?.provider,
      user_id: this.sessionStore.user?.user_id,
    };
  },

  setup() {
    const sessionStore = useCurrentSessionStore();
    return {
      sessionStore,
    };
  },
  //emits: ["showPopUpAccount"],
};
</script>

<style scoped>
.parent-profil {
  position: relative;
}
.layout-profil {
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 250px;
  cursor: pointer;
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
  background: var(--body-color-secondary);
  box-shadow: var(--boxshadow-04);
  transition: var(--tran-03);
  position: relative;
}
.layout-profil .box-img {
  height: 42px;
  width: 42px;
  background: #272727;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  transform: scale(0.9) translateX(-10px);
}
.layout-profil .box-img img {
  width: 35px;
}
.layout-profil span {
  font-weight: 600;
}
.layout-profil:hover {
  background: rgb(0 0 0 / 5%);
}
    .parent-profil {
        position: relative;
    }
    .layout-profil {
        height: 80px;
        display: flex;
        align-items: center;
        justify-content: flex-start;
        width: 250px;
        cursor: pointer;
        border-top-left-radius: 10px;
        border-top-right-radius: 10px;
        background: var(--body-color-secondary);
        box-shadow: var(--boxshadow-04);
        transition: var(--tran-03);
        position: relative;
        padding-left: 45px;
    }
    .layout-profil .box-img {
        height: 42px;
        width: 42px;
        background: #272727;
        border-radius: 50%;
        display: flex;
        justify-content: center;
        align-items: center;
        transform: scale(.9) translateX(-10px);
    }
    .layout-profil .box-img img {
        width: 35px;
    }
    .layout-profil span {
        font-weight: 600;
    }
    .layout-profil:hover {
        background: rgb(0 0 0 / 5%);
    }
</style>
