<template>
  <div class="container-search">
    <div class="box-search" @click="addBorderColor">
      <input
        @click="clickText"
        v-click-outside="clickTextOutSide"
        type="text"
        class="search text3"
        :placeholder="$t('navbar.search')"
        @keyup="search"
        v-model="this.searchValue"
      />
      <svg
        id="icon-search"
        viewBox="0 0 24 24"
        aria-hidden="true"
        class="r-1bwzh9t r-4qtqp9 r-yyyyoo r-1xvli5t r-dnmrzs r-4wgw6l r-f727ji r-bnwqim r-1plcrui r-lrvibr"
      >
        <g>
          <path
            d="M21.53 20.47l-3.66-3.66C19.195 15.24 20 13.214 20 11c0-4.97-4.03-9-9-9s-9 4.03-9 9 4.03 9 9 9c2.215 0 4.24-.804 5.808-2.13l3.66 3.66c.147.146.34.22.53.22s.385-.073.53-.22c.295-.293.295-.767.002-1.06zM3.5 11c0-4.135 3.365-7.5 7.5-7.5s7.5 3.365 7.5 7.5-3.365 7.5-7.5 7.5-7.5-3.365-7.5-7.5z"
          ></path>
        </g>
      </svg>
      <i @click="resetText" class="bx bx-x" id="cross"></i>
    </div>
    <div class="container-pop-up-user">
      <div class="layout-profil-user" v-for="item in this.items" :key="item">
        <router-link :to="{ path: '/profile/' + item.id }">
          <div class="img">
            <img src="../../assets/avatar.png" alt="profil utilisateur" />
          </div>
          <span> {{ item.username }}<br /> </span>
        </router-link>
      </div>
    </div>
  </div>
</template>
<script>
import { useCurrentSessionStore } from "@/stores/currentSession";
//import router from "@/router/index";

export default {
  data() {
    return {
      searchValue: "",
      items: [],
      numberOfMedia: 1,
    };
  },
  setup() {
    const sessionStore = useCurrentSessionStore();
    return {
      sessionStore,
    };
  },
  methods: {
    search(e) {
      if (e.keyCode) {
        fetch(`http://localhost:8089/api/search?value=${this.searchValue}`)
          .then((response) => response.json())
          .then((data) => {
            this.items = data;
          });
        console.log(this.items);
      }
    },

    searchNumberMedia(idFromTargetedAccount) {
      fetch(
        `http://localhost:8089/api/search/media_number?value=${idFromTargetedAccount}`
      ).then((response) => response.json());
    },

    displayProfil(
      username,
      firstname,
      lastname,
      id,
      numberOfSubscriber,
      numberOfSubscribed_To,
      description
    ) {
      localStorage.removeItem("usernameFromTargetedAccount");
      localStorage.removeItem("firstnameFromTargetedAccount");
      localStorage.removeItem("lastnameFromTargetedAccount");
      localStorage.removeItem("idFromTargetedAccount");
      localStorage.removeItem("subscribers_countFromTargetedAccount");
      localStorage.removeItem("subscribed_to_countFromTargetedAccount");
      localStorage.removeItem("descriptionFromTargetedAccount");
      localStorage.removeItem("numberOfMediaFromTargetedAccount");

      localStorage.setItem("usernameFromTargetedAccount", username);
      localStorage.setItem("firstnameFromTargetedAccount", firstname);
      localStorage.setItem("lastnameFromTargetedAccount", lastname);
      localStorage.setItem("idFromTargetedAccount", id);
      localStorage.setItem("subscribers_countFromTargetedAccount", numberOfSubscriber);
      localStorage.setItem(
        "subscribed_to_countFromTargetedAccount",
        numberOfSubscribed_To
      );
      localStorage.setItem("descriptionFromTargetedAccount", description);
      localStorage.setItem("numberOfMediaFromTargetedAccount", this.numberOfMedia);
      console.log("user id : " + id);
      this.searchNumberMedia(id);

      this.$router.push("/accountTarget");
    },

    handleChangeToAccountVue(id) {
      this.$router.push({ path: "/profile/" + id });
    },

    clickText() {
      const cross = document.getElementById("cross"),
        boxSearch = document.querySelector(".box-search"),
        popUpSearch = document.querySelector(".container-pop-up-user"),
        input = document.querySelector(".search");
      boxSearch.classList.add("boxSearchColor");
      input.addEventListener("keyup", () => {
        popUpSearch.style.visibility = "visible";
        setTimeout(() => {
          popUpSearch.classList.add("showPopUpSearch");
        }, 10);
        popUpSearch.style.width = boxSearch.offsetWidth + "px";
        cross.style.opacity = 1;
        cross.style.top = "0.6rem";
        if (input.value >= 0) {
          popUpSearch.classList.remove("showPopUpSearch");
          popUpSearch.style.visibility = "hidden";
          cross.style.opacity = 0;
          cross.style.top = "1.5rem";
        }
      });
    },

    clickTextOutSide() {
      const boxSearch = document.querySelector(".box-search"),
        popUpSearch = document.querySelector(".container-pop-up-user");
      popUpSearch.classList.remove("showPopUpSearch");
      setTimeout(() => {
        popUpSearch.style.visibility = "hidden";
      }, 1000);
      boxSearch.classList.remove("boxSearchColor");
    },

    elInputText() {
      const input = document.querySelector(".search");
    },

    resetText() {
      const input = document.querySelector(".search"),
        popUpSearch = document.querySelector(".container-pop-up-user"),
        cross = document.getElementById("cross");
      popUpSearch.classList.remove("showPopUpSearch");
      setTimeout(() => {
        popUpSearch.style.visibility = "hidden";
      }, 1000);
      input.value = "";
      cross.style.opacity = 0;
      cross.style.top = "1.5rem";
    },
  },
};
</script>

<style scoped>
.container-search {
  width: 100%;
  height: 100%;
  background: transparent;
}
.box-search {
  position: relative;
  border-radius: 25px;
  padding: 7.75px 100px;
  background: var(--body-color);
  width: 70%;
  z-index: 1;
}
.box-search svg {
  width: 1rem;
  height: 1rem;
  position: absolute;
  top: 50%;
  left: 1.5rem;
  transform: translate(-50%, -50%);
}
.boxSearchColor {
  border: 0.1px solid var(--color-secondary);
}
.search {
  background: var(--body-color);
  font-size: var(--text-size-03);
  position: relative;
  right: 2rem;
  width: calc(100% + 80px);
}
.search:focus {
  outline: none;
}
#icon-search {
  filter: var(--icon-white);
}
#cross {
  position: absolute;
  top: 1.5rem;
  opacity: 0;
  right: 1.2rem;
  font-size: 22px;
  transition: var(--tran-03);
  color: var(--primary-color);
}
.container-pop-up-user {
  width: 100%;
  height: auto;
  background: var(--body-color-secondary);
  border-radius: 10px;
  padding: 10px;
  position: fixed;
  top: 4rem;
  z-index: 999;
  opacity: 0;
  transition: var(--tran-03);
}
.container-pop-up-user .img {
  width: 42px;
  height: 42px;
  border-radius: 50%;
  background: #4a35c7;
  display: flex;
  justify-content: center;
  align-items: center;
}
.container-pop-up-user .img img {
  width: 30px;
}
.showPopUpSearch {
  opacity: 1;
}
</style>
