<template>
  <div class="container-post">
    <div class="contents-info">
      <div class="info-user">
        <div class="img-profil">
          <img
            class="profil"
            v-if="getProvider === 'google' && getProfilePic != null"
            v-bind:src="getProfilePic"
            alt="photo de profile"
          />

          <img
              class="profil"
              v-if="getProvider === 'google' && getProfilePic.includes('/api/v1/file')"
              v-bind:src="getProfilePic + '?userId=' + post?.userId"
              alt="photo de profile"
          />

          <img
            class="profil"
            v-if="getProvider === 'local' && getProfilePic != null"
            v-bind:src="getProfilePic + '?userId=' + post?.userId"
            alt="photo de profil"
          />

          <img
            v-if="getProfilePic == null"
            class="profil"
            src="../../../assets/avatar.png"
            alt="photo par defaut"
          />
        </div>
        <div class="info">
          <span class="name-lastname text3">{{ "@" + getUsername }}</span>
          <span class="date-time text5">{{ this.dateFormat }}</span>
        </div>
      </div>
      <button class="options"><img src="../assets/ellipsis.png" alt="" /></button>
      <span class="description text3">{{ post.content }}</span>
    </div>
    <div class="contents-media">
      <div class="bg-media-cover">
        <img
          style="width: 100%; height: 100%; object-fit: contain;"
          v-if="post.mediaList[0]?.mediaType === 'PHOTO'"
          v-bind:src="post.mediaList[0].downloadUrl + '?userId=' + post?.userId"
        />

        <video v-else style="z-index: auto" width="100%" controls>
          <source
            :src="post.mediaList[0]?.downloadUrl + '?userId=' + post?.userId"
            type="video/mp4"
          />
        </video>
      </div>
      <div class="all-btn-media">
        <button class="like">
          <svg
            aria-label="J’aime"
            class="_8-yf5"
            color="#fff"
            fill="#fff"
            height="22"
            role="img"
            viewBox="0 0 24 24"
            width="20"
          >
            <path
              d="M16.792 3.904A4.989 4.989 0 0121.5 9.122c0 3.072-2.652 4.959-5.197 7.222-2.512 2.243-3.865 3.469-4.303 3.752-.477-.309-2.143-1.823-4.303-3.752C5.141 14.072 2.5 12.167 2.5 9.122a4.989 4.989 0 014.708-5.218 4.21 4.21 0 013.675 1.941c.84 1.175.98 1.763 1.12 1.763s.278-.588 1.11-1.766a4.17 4.17 0 013.679-1.938m0-2a6.04 6.04 0 00-4.797 2.127 6.052 6.052 0 00-4.787-2.127A6.985 6.985 0 00.5 9.122c0 3.61 2.55 5.827 5.015 7.97.283.246.569.494.853.747l1.027.918a44.998 44.998 0 003.518 3.018 2 2 0 002.174 0 45.263 45.263 0 003.626-3.115l.922-.824c.293-.26.59-.519.885-.774 2.334-2.025 4.98-4.32 4.98-7.94a6.985 6.985 0 00-6.708-7.218z"
            ></path>
          </svg>
        </button>
        <button @click="onClickShowMedia" class="comment"><svg aria-label="Commenter" class="_8-yf5 " color="#fff" fill="#8e8e8e" height="20" role="img" viewBox="0 0 24 24" width="20"><path d="M20.656 17.008a9.993 9.993 0 10-3.59 3.615L22 22z" fill="none" stroke="currentColor" stroke-linejoin="round" stroke-width="2"></path></svg></button>
        <template v-if="showMedia">
          <ShowMedia v-on:clickShowMediaClose="onclickCloseShowMedia" />
        </template>
        <button class="save">
          <i class="bx bx-bookmark" style="color: #ffffff"></i>
        </button>
      </div>
    </div>
    <div class="contents-bottom-info">
      <div class="users-liked">
        <div class="user first">
          <img src="../assets/avatar.png" alt="Photo d'utilisateur ayant aimé" />
        </div>
        <div class="user second">
          <img src="../assets/avatar.png" alt="Photo d'utilisateur ayant aimé" />
        </div>
        <div class="user third">
          <img src="../assets/avatar.png" alt="Photo d'utilisateur ayant aimé" />
        </div>
        <span class="number-liked text5"
          >{{ $t("post.liked") }} <strong>Al laine</strong> and
          <span class="number">{{ post.nbLike }}</span
          >&nbsp;others</span
        >
      </div>
      <div class="comments-shared text5">
        <span>
          {{ post.commentList.length }} {{ $t("post.comment-shared.comment") }}</span
        >
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { useCurrentSessionStore } from "@/stores/currentSession";
import ShowMedia from '@/components/pop-up/ShowMedia.vue';
import axios from "axios";

export default {
  mounted() {
    this.getPostOwner();
    // const backgroundImg = document.querySelector('.bg-media-cover');
    // backgroundImg.style.backgroundImage = `url`
    this.$forceUpdate();
  },
  components : {
    ShowMedia
  },
  setup() {
    const store = useCurrentSessionStore();
    return {
      store,
    };
  },
  computed: {
    dateFormat(): string | null {
      const time = new Date(this.timestamp);
      const year = time.getFullYear();
      const month = this.months[time.getMonth()];
      const day = time.getDate();
      const hour = time.getHours();
      let min = time.getMinutes();
      let minstring = min < 10 ? String("0" + min) : String(min);
      let hourstring = hour < 10 ? String("0" + hour) : String(hour);

      switch (time.getMonth()) {
        case 0:
          return (
            this.$t("date.jan") +
            " " +
            day +
            " " +
            year +
            ", " +
            hourstring +
            ":" +
            minstring
          );
        case 1:
          return (
            this.$t("date.feb") +
            " " +
            day +
            " " +
            year +
            ", " +
            hourstring +
            ":" +
            minstring
          );
        case 2:
          return (
            this.$t("date.mar") +
            " " +
            day +
            " " +
            year +
            ", " +
            hourstring +
            ":" +
            minstring
          );
        case 3:
          return (
            this.$t("date.apr") +
            " " +
            day +
            " " +
            year +
            ", " +
            hourstring +
            ":" +
            minstring
          );
        case 4:
          return (
            this.$t("date.may") +
            " " +
            day +
            " " +
            year +
            ", " +
            hourstring +
            ":" +
            minstring
          );
        case 5:
          return (
            this.$t("date.jun") +
            " " +
            day +
            " " +
            year +
            ", " +
            hourstring +
            ":" +
            minstring
          );
        case 6:
          return (
            this.$t("date.jul") +
            " " +
            day +
            " " +
            year +
            ", " +
            hourstring +
            ":" +
            minstring
          );
        case 7:
          return (
            this.$t("date.aug") +
            " " +
            day +
            " " +
            year +
            ", " +
            hourstring +
            ":" +
            minstring
          );
        case 8:
          return (
            this.$t("date.sep") +
            " " +
            day +
            " " +
            year +
            ", " +
            hourstring +
            ":" +
            minstring
          );
        case 9:
          return (
            this.$t("date.oct") +
            " " +
            day +
            " " +
            year +
            ", " +
            hourstring +
            ":" +
            minstring
          );
        case 10:
          return (
            this.$t("date.nov") +
            " " +
            day +
            " " +
            year +
            ", " +
            hourstring +
            ":" +
            minstring
          );
        case 11:
          return (
            this.$t("date.dec") +
            " " +
            day +
            " " +
            year +
            ", " +
            hourstring +
            ":" +
            minstring
          );
      }
      return null;
    },

    getUsername() {
      return this.username;
    },

    getProvider() {
      return this.provider;
    },

    getProfilePic() {
      return this.profile_pic;
    },
  },

  data() {
    return {
      showMedia: false,
      months: [
        "$t('date.jan')",
        "$t('date.feb')",
        "$t('date.mar')",
        "$t('date.apr')",
        "$t('date.mai')",
        "$t('date.jun')",
        "$t('date.jul')",
        "$t('date.aug')",
        "$t('date.sep')",
        "$t('date.oct')",
        "$t('date.nov')",
        "$t('date.dec')",
      ],
      username: null,
      formattedDate: null,
      timestamp: this.$props.timestamp,
      provider: null,
      profile_pic: null,
    };
  },

  methods: {
    onClickShowMedia() {
      this.showMedia = true;
    },
    onclickCloseShowMedia() {
      this.showMedia = !this.showMedia;
    },
    async getPostOwner() {
      const url = "http://localhost:8083/api/v1/users/id/" + this.$props.post.userId;
      const options = {
        headers: {
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json",
          Authorization: "Bearer " + this.store.user.access_token,
        },
      };

      let r = await fetch(url, options);
      const res = await r.json();
      this.username = res.username;
      this.provider = res.provider;
      this.profile_pic = res.profilePictureUrl;
    },
  },

  props: ["post", "timestamp"],
};
</script>

<style scoped>
.img-post {
  width: 100%;
  height: 100%;
}
.container-post {
  width: 100%;
  height: auto;
  background: var(--body-color-secondary);
  color: var(--primary-color);
  box-shadow: var(--boxshadow-04);
  padding: 20px;
  border-radius: 10px;
  position: relative;
  margin-bottom: 20px;
}
.container-post .contents-info {
  width: 100%;
  display: flex;
  flex-direction: column;
  margin-bottom: 10px;
  position: relative;
}
.container-post .contents-info .info-user {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  width: 100%;
  margin-bottom: 10px;
}
.container-post .contents-info .info-user .img-profil {
  width: 42px;
  height: 42px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 10px;
  background: #272727;
  display: flex;
  justify-content: center;
  align-items: center;
}
.container-post .contents-info .info-user .info {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: flex-start;
}
.container-post .contents-info .info-user .profil {
  width: 35px;
}
.container-post .contents-info .info-user .name-lastname {
  font-weight: 600;
}
.container-post .contents-info .info-user .date-time {
  font-weight: lighter;
  color: var(--description-color);
}
.container-post .contents-info .description {
  font-weight: 400;
  color: var(--description-color);
  word-break: break-all;
}

/* OPTION */

.container-post .contents-info .options {
  position: absolute;
  right: -5px;
  top: -0.15rem;
  transform: scale(1.2);
  display: flex;
  padding: 10px;
  border-radius: 50%;
  transition: var(--tran-05);
}
.container-post .contents-info .options:hover {
  background: var(--btn-option);
  box-shadow: var(--boxshadow-01);
}
.container-post .contents-info .options img {
  width: 12px;
  filter: var(--icon-white);
}

/* MEDIA */

.container-post .contents-media {
  width: 100%;
  border-radius: 10px;
  height: auto;
  min-height: 300px;
  background: #000;
  display: flex;
  align-items: flex-end;
  position: relative;
}
.container-post .contents-media .bg-media-cover {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  /* background-image: url('../assets/spiderman.png'); */
  background-position: center;
  background-repeat: no-repeat;
  background-size: cover;
  border-radius: 10px;
}
.container-post .contents-media .bg-media-contain {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  background-image: url("../assets/steven.png");
  background-position: center;
  background-repeat: no-repeat;
  background-size: contain;
  border-radius: 10px;
}
.container-post .contents-media .all-btn-media {
  width: 100%;
  display: flex;
  align-items: center;
  position: absolute;
  bottom: -1.1rem;
  padding-left: 30px;
}
.container-post .contents-media .all-btn-media button {
  height: 40px;
  width: 40px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 10px;
  cursor: pointer;
}
.container-post .contents-media .all-btn-media button i {
  font-size: 20px;
}
.container-post .contents-media .all-btn-media .like {
  background: #3f4e69;
}
.container-post .contents-media .all-btn-media .comment {
  background: #52a0fe;
}
.container-post .contents-media .all-btn-media .comment img {
  width: 24px;
}
.container-post .contents-media .all-btn-media .share {
  background: #52a0fe;
}
.container-post .contents-media .all-btn-media .save {
  background: #3f4e69;
  position: absolute;
  right: 20px;
}

/* BOTTOM INFO */

.container-post .contents-bottom-info {
  width: 100%;
  display: flex;
  flex-direction: column;
}
.container-post .contents-bottom-info .users-liked {
  display: flex;
  align-items: center;
  margin: 30px 0 10px;
  position: relative;
}
.container-post .contents-bottom-info .users-liked .user {
  height: 30px;
  width: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
}
.container-post .contents-bottom-info .users-liked .first {
  background: #4a35c7;
}
.container-post .contents-bottom-info .users-liked .second {
  background: #634003;
  position: relative;
  right: 0.8rem;
}
.container-post .contents-bottom-info .users-liked .third {
  background: #0d9772;
  position: relative;
  right: 1.6rem;
}
.container-post .contents-bottom-info .users-liked .user img {
  width: 22px;
}
.container-post .contents-bottom-info .users-liked .number-liked {
  position: relative;
  right: 1rem;
}
.container-post .contents-bottom-info .users-liked .number-liked .number {
  color: #52a0fe;
  font-weight: bold;
}
.container-post .contents-bottom-info .comments-shared {
  display: flex;
  color: var(--description-color);
  flex-direction: column;
  font-weight: lighter;
}
.bx,
svg {
  width: 18px;
  height: 18px;
}
</style>
