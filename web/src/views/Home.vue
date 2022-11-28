<template>
  <div class="container-contents">
    <template v-for="(post, idx) in this.allPosts" :key="post.id">
      <Posts v-bind:post="post" v-bind:timestamp="post.creationDate"></Posts>
    </template>
  </div>
</template>

<script lang="ts">
import Posts from "@/components/Posts.vue";
import { useCurrentSessionStore } from "@/stores/currentSession";

export default {
  setup() {
    const ss = useCurrentSessionStore();
    ss.user.my_posts.sort((x, y) => x.timestamp - y.timestamp);
    ss.user.all_posts.sort((x, y) => x.timestamp - y.timestamp);
    return {
      ss,
    };
  },
  data() {
    return {
      reloadKey: 0,
      allPosts: this.ss.user.all_posts,
      allPostLength: this.ss.user.all_posts?.length,
      profil: {
        backgroundImage: "",
        imgProfil: "",
        posts: this.ss.user.my_posts,
        post_count: this.ss.user.my_post_count,
      },
    };
  },
  components: {
    Posts,
  },

  mounted() {
    this.emitter.on("posts-reload", (data) => {
      console.log("emmiter in action");
      this.fetchAllPostsInfo();
      this.reloadKey++;
      this.$forceUpdate();
    });
  },

  methods: {
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
      this.ss.user.my_posts = posts
      this.ss.user.my_post_count = posts.length
      // localStorage.setItem("my_posts", JSON.stringify(posts));
      // localStorage.setItem("my_post_count", JSON.stringify(posts.length));
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
      this.allPosts = posts;
      this.ss.user.all_posts = posts
      // localStorage.setItem("all_posts", JSON.stringify(posts));
    },
  },
};
</script>

<style></style>
