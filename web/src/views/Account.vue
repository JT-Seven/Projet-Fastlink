<template>
  <div class="container-grid-account">
    <div class="container-top"></div>
    <div class="container-timeline">
      <div class="timeline">
        <div class="timeline-item">
          <label @mouseover="sliderTab" class="posts item">{{
            $t("account.onglets.posts")
          }}</label>
          <label @mouseover="sliderTab" class="likes item">{{
            $t("account.onglets.likes")
          }}</label>
          <label @mouseover="sliderTab" class="favoris item">{{
            $t("account.onglets.favoris")
          }}</label>
        </div>
        <div class="slider"></div>
      </div>
    </div>
    <div class="container-bkg-img">
      <i class="bx bx-camera bx-flashing-hover icon-edit" style="color: #ffffff"></i>
      <div @mouseover="mouseEnter" @mouseleave="mouseLeave" class="overlay-account">
        <label for="form_input" class="form_label">
          <input
            type="file"
            id="form_input"
            class="form_input"
            @change="previewBackgroundImg"
            multiple
          />
          <span class="form_text"> {{ $t("account.edit-background") }} </span>
        </label>
      </div>
    </div>
    <div class="container-info">
      <div class="mark-place"></div>
      <div class="overlay-img-profil"></div>

      <img
        v-if="this.provider === 'google' && this.profile_pic != null"
        class="img-profil"
        v-bind:src="this.profile_pic"
        v-click-outside="clickOutSideImgProfil"
        @click="showImageProfil"
      />

      <img
        v-if="this.provider === 'local' && this.profile_pic != null"
        v-bind:src="this.profile_pic + '?userId=' + this.user_id"
        class="img-profil"
        v-click-outside="clickOutSideImgProfil"
        @click="showImageProfil"
      />

      <img
        v-if="this.profile_pic === null"
        src="@/assets/avatar.png"
        class="img-profil"
        v-click-outside="clickOutSideImgProfil"
        @click="showImageProfil"
      />

      <div class="box-info">
        <div class="name-username">
          <span class="name-lastname text3">{{
            this.lastname + " " + this.firstname
          }}</span>
          <span class="username">@{{ this.username }}</span>
          <button @click="onClickModifyProfil" class="edit-profil">
            {{ $t("account.edit-profil") }}
          </button>
          <!-- <template v-if="clickModifyProfil"> -->
          <ModifyProfil
            v-if="this.clickModifyProfil"
            v-on:clickModifyProfilClose="onclickCloseModifyProfil"
            v-bind:provider="this.provider"
            v-bind:url="this.profile_pic"
            v-bind:id="this.user_id"
          />
          <!-- </template> -->
        </div>
        <div class="infos-stats">
          <div class="column">
            <span>{{ this.post_count }}</span>
            <span>{{ $t("account.infos-stats.posts") }}</span>
          </div>
          <div class="column">
            <span>{{ this.subscribers }}</span>
            <span>{{ $t("account.infos-stats.followers") }}</span>
          </div>
          <div class="column">
            <span>{{ this.subscriptions }}</span>
            <span>{{ $t("account.infos-stats.following") }}</span>
          </div>
        </div>
        <div class="description">
          <span>{{ this.description }}</span>
        </div>
      </div>
    </div>
    <div class="container-contents">
      <template v-for="(post, idx) in this.posts" :key="post.id">
        <Posts v-bind:post="post" v-bind:timestamp="post.creationDate"></Posts>
      </template>
    </div>
  </div>
</template>

<script lang="ts">
import Posts from "@/components/Posts.vue";
import axios from "axios";
import { useCurrentSessionStore } from "@/stores/currentSession";
import ModifyProfil from "@/components/pop-up/ModifyProfil.vue";

export default {
  setup() {
    const sessionStore = useCurrentSessionStore();
    sessionStore.user.my_posts.sort((x, y) => x.timestamp - y.timestamp);
    sessionStore.user.all_posts.sort((x, y) => x.timestamp - y.timestamp);
    return {
      sessionStore,
    };
  },
  mounted() {
    this.fetchProfilePicture();
    this.sliderWidth();

    this.emitter.on("posts-reload", () => {
      console.log("emmitter in action");
      this.$nextTick(() => {
        console.log("nextTick in action");
        this.fetchMyPostInfo();
        this.fetchAllPostsInfo();
      });
    });
  },
  data() {
    return {
      clickModifyProfil: false,
      userPostCount: 0,
      subscribers: this.sessionStore.user.subscriber_count,
      subscriptions: this.sessionStore.user.subscribed_count,
      username: this.sessionStore.user.username,
      firstname: this.sessionStore.user.firstname,
      lastname: this.sessionStore.user.lastname,
      backgroundImage: "",
      profile_pic: this.sessionStore.user.profile_pic,
      provider: this.sessionStore.user.provider,
      posts: this.sessionStore.user.my_posts,
      post_count: this.sessionStore.user.my_post_count,
      description: this.sessionStore.user.description,
      user_id: this.sessionStore.user.user_id,
    };
  },

  components: {
    Posts,
    ModifyProfil,
  },

  methods: {
    onClickModifyProfil() {
      this.clickModifyProfil = true;
    },

    onclickCloseModifyProfil() {
      this.clickModifyProfil = !this.clickModifyProfil;
    },

    showImageProfil() {
      const imgProfil = document.querySelector(".img-profil"),
        overlayImgProfil = document.querySelector(".overlay-img-profil");

      overlayImgProfil.classList.add("overlay-img-profil-show");
      imgProfil.classList.add("showImgProfil");
    },

    clickOutSideImgProfil() {
      const imgProfil = document.querySelector(".img-profil"),
        overlayImgProfil = document.querySelector(".overlay-img-profil");

      overlayImgProfil.classList.remove("overlay-img-profil-show");
      imgProfil.classList.remove("showImgProfil");
    },

    sliderWidth() {
      const slider = document.querySelector(".slider"),
        posts = document.querySelector(".posts");

      slider.style.width = posts.offsetWidth + "px";
      slider.style.left = posts.offsetLeft + "px";
    },

    sliderTab() {
      const slider = document.querySelector(".slider");
      const posts = document.querySelector(".posts");
      const reposts = document.querySelector(".reposts");
      const likes = document.querySelector(".likes");
      const favoris = document.querySelector(".favoris");
      const allLabel = document.querySelectorAll(".item");
      allLabel.forEach((label) => {
        label.addEventListener("click", () => {
          if (
            label.classList.contains("posts") ||
            label.classList.contains("reposts") ||
            label.classList.contains("likes") ||
            label.classList.contains("favoris")
          ) {
            slider.style.opacity = "1";
            slider.style.left = posts.offsetLeft + "px";
            slider.style.width = posts.offsetWidth + "px";
            if (label.classList.contains("reposts")) {
              slider.style.left = reposts.offsetLeft + "px";
              slider.style.width = reposts.offsetWidth + "px";
            } else if (label.classList.contains("likes")) {
              slider.style.left = likes.offsetLeft + "px";
              slider.style.width = likes.offsetWidth + "px";
            } else if (label.classList.contains("favoris")) {
              slider.style.left = favoris.offsetLeft + "px";
              slider.style.width = favoris.offsetWidth + "px";
            }
          }
        });
      });
    },
    previewBackgroundImg(event) {
      const backgroundImg = document.querySelector(".container-bkg-img");

      this.profil.backgroundImage = event.target.files[0].name;
      backgroundImg.style.backgroundImage = `url(${URL.createObjectURL(
        event.target.files[0]
      )})`;
    },
    // previewImageProfil(event) {
    //   const Imgprofil = document.querySelector(".img-profil");

    //   this.profil.imgProfil = event.target.files[0];
    //   this.onSubmit();
    //   Imgprofil.style.backgroundImage = `url(${URL.createObjectURL(
    //     event.target.files[0]
    //   )})`;
    // },
    mouseEnter() {
      const camera = document.querySelector(".icon-edit");
      camera.classList.toggle("scaleTransition");
    },
    mouseLeave() {
      const camera = document.querySelector(".icon-edit");
      camera.classList.remove("scaleTransition");
    },
    scrollSticky(event) {
      const timeline = document.querySelector(".container-timeline");
      window.addEventListener("scroll", () => {
        const { scrollTop, clientHeight } = document.documentElement;

        console.log(timeline.getBoundingClientRect());
      });
    },
    handleScroll(e) {
      if (e.target.scrollHeight - 100 <= e.target.scrollTop) {
        alert("oi sou Eduardo Martins");
        console.log("OPPPPPPOPO");
      }
    },
    onSubmit() {
      // upload file
      const formData = new FormData();
      formData.append("file", this.profil.imgProfil);
      axios
        .post(
          "http://localhost:8083/api/v1/users/profile-picture/upload/" +
            this.user_id,
          formData,
          {
            headers: {
              "Access-Control-Allow-Origin": "http://localhost:3000",
            },
          }
        )
        .then((res) => {
          console.log(res);
        });
    },
    fetchProfilePicture() {
      // fetch file
      axios
        .get(
          "http://localhost:8083/api/v1/users/profile-picture/" +
            this.user_id
        )
        .then((res) => {
          console.log(res.data);
          //this.profil.imgProfil = res.data;
        });
    },

    async fetchMyPostInfo() {
      const postReqUrl =
        "http://localhost:8081/api/v1/posts/user/" + this.sessionStore.user.user_id;
      const postReqOptions = {
        method: "GET",
        headers: {
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json",
          Authorization: "Bearer " + this.sessionStore.user.access_token,
        },
      };

      let postResponse = await fetch(postReqUrl, postReqOptions);
      const postMainResponse = await postResponse.json();
      console.log("posts: \n" + postMainResponse);

      let posts: any[] = [];

      for (let post of postMainResponse) {
        posts.push(post);
      }
      posts.reverse();
      // localStorage.setItem("my_posts", JSON.stringify(posts));
      // localStorage.setItem("my_post_count", JSON.stringify(posts.length));
      this.sessionStore.user.my_posts = posts;
      this.sessionStore.user.my_post_count = posts.length;
    },

    async fetchAllPostsInfo() {
      const url = "http://localhost:8081/api/v1/posts";
      const postReqOptions = {
        method: "GET",
        headers: {
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json",
          Authorization: "Bearer " + this.sessionStore.user.access_token,
        },
      };
      let res = await fetch(url, postReqOptions);
      const postMainResponse = await res.json();
      console.log("posts: \n" + postMainResponse);

      let posts: any[] = [];

      for (let post of postMainResponse) {
        posts.push(post);
      }
      posts.reverse();
      this.sessionStore.user.all_posts = posts;
      // localStorage.setItem("all_posts", JSON.stringify(posts));
    },
  },
};
</script>

<style src="../css/account/account.css" scoped></style>
