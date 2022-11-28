<template>
  <div>
    <div class="overlay">
      <div class="pop-up-post">
        <i
          @click="this.$emit('clickPostFalse', this.clickPost)"
          class="bx bx-x bx-flashing-hover"
          id="cross"
        ></i>
        <div class="pop-up-post-content">
          <form @submit.prevent="onSubmit">
            <textarea
              :placeholder="$t('pop-up.post.what-s-up')"
              v-model="this.post.content"
              @keyup="writeText"
              maxlength="250"
              name="description"
              id="description"
              class="textarea text3"
            ></textarea>
            <div class="content-media">
              <div class="cross-img">
                <i @click="removeMedia" class="removeMedia bx bx-x bx-flashing-hover"></i>
              </div>
            </div>
            <div class="separator"></div>
            <div class="icon-media">
              <div class="box-hover">
                <label for="input-media">
                  <input @change="addMedia" type="file" id="input-media" />
                </label>
                <svg
                  viewBox="0 0 24 24"
                  aria-hidden="true"
                  class="r-1cvl2hr r-4qtqp9 r-yyyyoo r-1hjwoze r-dnmrzs r-bnwqim r-1plcrui r-lrvibr r-12ym1je"
                >
                  <g>
                    <path
                      d="M19.75 2H4.25C3.01 2 2 3.01 2 4.25v15.5C2 20.99 3.01 22 4.25 22h15.5c1.24 0 2.25-1.01 2.25-2.25V4.25C22 3.01 20.99 2 19.75 2zM4.25 3.5h15.5c.413 0 .75.337.75.75v9.676l-3.858-3.858c-.14-.14-.33-.22-.53-.22h-.003c-.2 0-.393.08-.532.224l-4.317 4.384-1.813-1.806c-.14-.14-.33-.22-.53-.22-.193-.03-.395.08-.535.227L3.5 17.642V4.25c0-.413.337-.75.75-.75zm-.744 16.28l5.418-5.534 6.282 6.254H4.25c-.402 0-.727-.322-.744-.72zm16.244.72h-2.42l-5.007-4.987 3.792-3.85 4.385 4.384v3.703c0 .413-.337.75-.75.75z"
                    ></path>
                    <circle cx="8.868" cy="8.309" r="1.542"></circle>
                  </g>
                </svg>
              </div>
              <div class="box-hover">
                <svg
                  @click="clickEmoji"
                  viewBox="0 0 24 24"
                  aria-hidden="true"
                  class="r-1cvl2hr r-4qtqp9 r-yyyyoo r-1hjwoze r-dnmrzs r-bnwqim r-1plcrui r-lrvibr r-12ym1je"
                >
                  <g>
                    <path
                      d="M12 22.75C6.072 22.75 1.25 17.928 1.25 12S6.072 1.25 12 1.25 22.75 6.072 22.75 12 17.928 22.75 12 22.75zm0-20C6.9 2.75 2.75 6.9 2.75 12S6.9 21.25 12 21.25s9.25-4.15 9.25-9.25S17.1 2.75 12 2.75z"
                    ></path>
                    <path
                      d="M12 17.115c-1.892 0-3.633-.95-4.656-2.544-.224-.348-.123-.81.226-1.035.348-.226.812-.124 1.036.226.747 1.162 2.016 1.855 3.395 1.855s2.648-.693 3.396-1.854c.224-.35.688-.45 1.036-.225.35.224.45.688.226 1.036-1.025 1.594-2.766 2.545-4.658 2.545z"
                    ></path>
                    <circle cx="14.738" cy="9.458" r="1.478"></circle>
                    <circle cx="9.262" cy="9.458" r="1.478"></circle>
                  </g>
                </svg>
              </div>
              <template v-if="showSmileys">
                <div flex="~ col" items-center>
                  <div flex h120>
                    <EmojiPicker @select="selectEmoji" />
                    <EmojiPicker
                      :options="{
                        imgSrc:
                          'https://fastly.jsdelivr.net/gh/limin04551/vue3-twemoji-picker/public/img/',
                        locals: 'en',
                      }"
                      @select="selectEmoji"
                    />
                    <EmojiPicker
                      :options="{
                        imgSrc: '/img/',
                        native: true,
                        locals: 'en',
                        hasGroupIcons: true,
                        hasSearch: false,
                        hasGroupNames: false,
                        stickyGroupNames: false,
                        hasSkinTones: false,
                        recentRecords: false,
                      }"
                      @select="selectEmoji"
                    />
                  </div>
                  <Footer />
                </div>
              </template>
            </div>
            <div class="counter-text">
              <span class="counter text3"></span>
            </div>
            <button class="text3 btnPost">{{ $t("pop-up.post.post") }}</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { EmojiPicker } from "vue3-twemoji-picker-final";
import { useCurrentSessionStore } from "@/stores/currentSession";
import axios from "axios";

export default {
  emits: ["clickPostFalse"],
  components: {
    EmojiPicker,
  },
  data() {
    return {
      clickPost: false,
      showSmileys: false,
      post: {
        FILE: null,
        content: null,
      },
    };
  },

  setup() {
    const sessionStore = useCurrentSessionStore();
    return {
      sessionStore,
    };
  },
  methods: {
    async onSubmit(event) {
      // upload file
      const formData = new FormData();
      formData.append("files", this.post.FILE);
      formData.append("content", JSON.stringify(this.post.content));
      formData.append("userId",
          //@ts-ignore
          Number(this.sessionStore.user.user_id));
      axios
        .post("http://localhost:8081/api/v1/posts/add", formData, {
          headers: {
            "Access-Control-Allow-Origin": "http://localhost:3000",
          },
        })
        .then((res) => {
          this.$emit("clickPostFalse", this.clickPost);
          console.log("Post success");
          this.fetchAllPostsInfo();
          this.fetchMyPostInfo();

          if (this.$router.currentRoute.value)

          this.$router.push("/home");
          setTimeout(() => {
            this.$router.go();
          }, 0.01);
        })
        .catch((err) => {
          console.log(err);
          console.error("Post error");
        });
    },

    removeMedia() {
      this.post.media = "";
      const popUpPost = document.querySelector(".pop-up-post"),
        contentMedia = document.querySelector(".content-media");
      popUpPost.style.height = "270px";
      contentMedia.style.display = "none";
    },

    addMedia(event) {
      const contentMedia = document.querySelector(".content-media"),
        popUpPost = document.querySelector(".pop-up-post");
      popUpPost.style.height = "auto";

      this.post.FILE = event.target.files[0];
      contentMedia.style.display = "block";
      contentMedia.style.backgroundImage = `url(${URL.createObjectURL(
        event.target.files[0]
      )})`;
    },

    clickEmoji() {
      this.showSmileys = !this.showSmileys;
    },

    writeText() {
      const textarea = document.querySelector(".textarea"),
        counter = document.querySelector(".counter"),
        btnPost = document.querySelector(".btnPost");
      textarea.onkeyup = (e) => {
        countChar(e);
      };
      textarea.onkeypress = (e) => {
        countChar(e);
      };

      function countChar(val) {
        let maxLength = 250;
        counter.style.display = "block";
        btnPost.classList.add("active");
        counter.innerHTML = maxLength - val.target.value.length;

        if (val.target.value.length >= maxLength) {
          val.target.value = val.target.value.substring(0, maxLength);
          counter.innerHTML = 0;
          counter.style.color = "#ff0000";
          return false;
        } else if (val.target.value.length === 0) {
          counter.style.display = "none";
          btnPost.classList.remove("active");
        } else {
          counter.style.color = "var(--primary-color";
        }
      }
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
      //localStorage.setItem("all_posts", JSON.stringify(posts));
    },
  },
};
</script>

<style scoped>
@import "../../../node_modules/vue3-twemoji-picker-final/dist/index.css";

.pop-up-post-content textarea::-webkit-scrollbar {
  width: 5px;
}

.pop-up-post-content textarea::-webkit-scrollbar-track {
  background: #2b354657;
}

.pop-up-post-content textarea::-webkit-scrollbar-thumb {
  background-color: #161e2b;
  border-radius: 10px;
}

.pop-up-post {
  position: absolute;
  top: 10%;
  height: 270px;
  max-height: 600px;
  width: 500px;
  background: var(--body-color-secondary);
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 10px;
  padding: 10px;
  z-index: 999;
}
.pop-up-post-content {
  height: 100%;
  width: 100%;
  padding: 60px 15px 0px 15px;
  position: relative;
}
.pop-up-post-content .textarea {
  outline: none;
  word-wrap: break-word;
  word-break: break-all;
  width: 100%;
  background: none;
  height: 135px;
  resize: none;
}
.pop-up-post-content .textarea:focus {
  outline: none;
}

/*--------MEDIA--------*/

.pop-up-post-content .content-media {
  height: 300px;
  width: 100%;
  display: none;
  transition: var(--tran-03);
  margin-bottom: 60px;
  border-radius: 10px;
  background-position: center;
  background-repeat: no-repeat;
  background-size: contain;
  margin-top: 20px;
}
#cross {
  position: absolute;
  left: 15px;
  top: 15px;
  font-size: 26px;
  z-index: 2;
  color: var(--primary-color);
}
.pop-up-post-content .content-media .cross-img {
  position: absolute;
  color: var(--primary-color);
}
.pop-up-post-content .content-media .removeMedia {
  position: relative;
  top: -15px;
  left: 0;
  font-size: 26px;
  z-index: 2;
  color: var(--primary-color);
}

/*--------CONTENT-BOTTOM--------*/

.pop-up-post-content .separator {
  width: 94%;
  height: 0.1px;
  position: absolute;
  bottom: 47px;
  background: var(--timeline-color);
}
.pop-up-post-content .icon-media {
  display: inline-flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  left: 10px;
  bottom: 0;
}
.pop-up-post-content .icon-media svg {
  width: 18px;
  cursor: pointer;
  color: var(--primary-color);
  filter: var(--icon-white);
}
#input-media {
  transform: scale(0.6);
  outline: none;
  opacity: 0;
  position: absolute;
  width: 30px;
  z-index: 5;
  cursor: pointer;
  height: 35px;
  left: -2px;
  top: -4px;
}
.pop-up-post-content .box-hover {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: var(--tran-03);
  cursor: pointer;
  margin-right: 5px;
}
.pop-up-post-content .box-hover:hover {
  background: #52a0fe38;
}
.pop-up-post-content .counter-text {
  position: absolute;
  right: 135px;
  bottom: 6px;
  border-right: 0.1px solid var(--timeline-color);
  padding-right: 15px;
  color: var(--primary-color);
}
.pop-up-post-content button {
  height: 35px;
  width: 110px;
  background: linear-gradient(to right, #52a0fe 0%, #7bc6ff 100%);
  border-radius: 25px;
  color: white;
  transition: var(--tran-05);
  position: absolute;
  bottom: 0;
  right: 10px;
  opacity: 0.8;
  outline: none;
  pointer-events: none;
}
.pop-up-post-content button.active {
  opacity: 1;
  outline: none;
  pointer-events: auto;
}
.pop-up-post-content button:hover {
  transform: translateY(-5px);
}
.pop-up-post-content button:active {
  transform: translateY(-1px);
}
.box-hover label {
  width: 24px;
  height: 24px;
  position: absolute;
}
</style>
