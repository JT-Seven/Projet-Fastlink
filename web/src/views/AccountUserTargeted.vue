//
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
          v-if="this.provider === 'google' && this.profile_pic.includes('/api/v1/file')"
          class="img-profil"
          v-bind:src="this.profile_pic + '?userId=' + this.user_id"
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
          <button v-if="!this.subscribed" class="edit-profil sub">
            {{ $t("account.sub") }}
          </button>
        </div>
        <div class="infos-stats">
          <div class="column">
            <span>{{ this.user_post_count }}</span>
            <span>{{ $t("account.infos-stats.posts") }}</span>
          </div>
          <div class="column">
            <span>{{ this.subscriber_count }}</span>
            <span>{{ $t("account.infos-stats.followers") }}</span>
          </div>
          <div class="column">
            <span>{{ this.subscribed_count }}</span>
            <span>{{ $t("account.infos-stats.following") }}</span>
          </div>
        </div>
        <div class="description" v-if="this.description">
          <span>{{ this.description }}</span>
        </div>
      </div>
    </div>
    <div class="container-contents">
      <template v-for="(post, idx) in this.user_posts">
        <Posts v-bind:post="post" v-bind:timestamp="post.creationDate"></Posts>
      </template>
    </div>
  </div>
</template>

<script lang="ts">
import Posts from "@/components/Posts.vue";
import { useTargetedAccountStore } from "@/stores/targetedAccount";
import axios from "axios";
import { useCurrentSessionStore } from "@/stores/currentSession";
export default {
  props: ["user"],
  mounted() {
    let sub = false;
    console.log(this.$props.user);
    for (let u of this.sessionStore.user.subscriptions) {
      if (u.id === this.$props.user.user_id) {
        sub = true;
        break;
      }
    }
    this.subscribed = sub;
  },

  setup() {
    const targetedAccount = useTargetedAccountStore();
    const sessionStore = useCurrentSessionStore();

    return {
      targetedAccount,
      sessionStore,
    };
  },

  data() {
    return {
      subscribed: null,
      subscribers: this.targetedAccount.targeted_user.subscribers,
      susbcriptions: this.targetedAccount.targeted_user.subscriptions,
      username: this.targetedAccount.targeted_user.username,
      firstname: this.targetedAccount.targeted_user.firstname,
      lastname: this.targetedAccount.targeted_user.lastname,
      description: this.targetedAccount.targeted_user.description,
      user_id: this.targetedAccount.targeted_user.user_id,
      user_post_count: this.targetedAccount.targeted_user.user_post_count,
      subscriber_count: this.targetedAccount.targeted_user.subscriber_count,
      subscribed_count: this.targetedAccount.targeted_user.subscribed_count,
      user_posts: this.targetedAccount.targeted_user.user_posts,
      profile_pic: this.targetedAccount.targeted_user.profile_pic,
      provider: this.targetedAccount.targeted_user.provider,
    };
  },
  components: {
    Posts,
  },
  methods: {
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
    previewBackgroundImg(event) {
      const backgroundImg = document.querySelector(".container-bkg-img");

      this.profil.backgroundImage = event.target.files[0].name;
      backgroundImg.style.backgroundImage = `url(${URL.createObjectURL(
        event.target.files[0]
      )})`;
    },
    previewImageProfil(event) {
      const Imgprofil = document.querySelector(".img-profil");

      this.profil.imgProfil = event.target.files[0];
      this.onSubmit();
      Imgprofil.style.backgroundImage = `url(${URL.createObjectURL(
        event.target.files[0]
      )})`;
    },
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

  },
};
</script>

<style src="../css/account/account.css" scoped></style>
