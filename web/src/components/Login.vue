<template>
  <div class="inner-block">
    <div class="vue-tempalte">
      <div>
        <h3>Sign in</h3>
        <div class="social-icons">
          <a @click="customGoogleCallback" class="btn btn-google">
            <img src="https://img.icons8.com/fluency/48/000000/google-logo.png" />
            <label> Sign in with Google</label>
          </a>
        </div>
        <div class="or"></div>

        <div class="form-group-username-lg">
          <input
            type="text"
            class="form-control form-control-lg"
            v-model="user.username"
            required="required"
          />
          <span class="username">Username</span>
          <i class="bx bxs-user"></i>
        </div>

        <div class="form-group-password-lg">
          <input
            type="password"
            class="form-control form-control-lg form-control-password"
            v-model="user.password"
            required="required"
          />
          <span class="placeholder">Password</span>
          <i class="bx bx-key"></i>
          <button @click="displayPassword">
            <i class="bx bx-show-alt" v-if="passwordVisible"></i>
            <i class="bx bx-low-vision" v-if="!passwordVisible"></i>
          </button>
          <label class="container">
            Remember me
            <input type="checkbox" checked="checked" />
          </label>
        </div>
        <p
          v-if="!inputFull || this.error !== null"
          style="color: red; font-size: 12px; margin-left: 80px"
        >
          {{ this.error }}
        </p>

        <button @click="login" class="btn-authentication">Sign in</button>
      </div>
    </div>
  </div>

  <div class="foot">
    <p class="link-login">
      don’t have an account ?
      <router-link to="/signup" class="link">Sign up</router-link><br />
      <router-link to="/forgot-password" class="link">Forgot password ?</router-link>
    </p>
  </div>
</template>

<script lang="ts">
import { googleAuthCodeLogin, GoogleLogin } from "vue3-google-login";
import { useCurrentSessionStore } from "@/stores/currentSession";

export default {
  setup() {
    const sessionStore = useCurrentSessionStore();

    return {
      sessionStore,
    };
  },

  data() {
    return {
      user: {
        username: "",
        password: "",
      },
      inputFull: false,
      error: null,
      passwordVisible: false,
    };
  },

  methods: {
    authentification() {
      var details = {
        username: this.user.username,
        password: this.user.password,
      };

      var formBody = [];
      for (var property in details) {
        var encodedKey = encodeURIComponent(property);
        var encodedValue = encodeURIComponent(details[property]);
        formBody.push(encodedKey + "=" + encodedValue);
      }
      formBody = formBody.join("&");

      return fetch("http://localhost:8080/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
          "Access-Control-Allow-Origin": "http://localhost:3000",
        },
        body: formBody,
      }).then((response) => response.json());
    },

    async login() {
      this.verifyAllInput();
      console.log(this.inputFull);
      if (this.inputFull) {
        this.authentification().then((response) => {
          if (response.access_token != null && response.refresh_token != null) {
            this.sessionStore.user.connected = true;
            this.sessionStore.user.access_token = response.access_token;
            this.sessionStore.user.refresh_token = response.refresh_token;
            this.sessionStore.user.user_id = response.user_id;
            this.sessionStore.user.user_id.username = response.username
            // localStorage.setItem("username", JSON.stringify(response.username));

            this.fetchCurrentUserInfo();
            this.fetchMyPostInfo();
            this.fetchAllPostsInfo();
            this.fetchSubscriberInfo();
            this.fetchSubscriptionInfo();
            this.fetchUnreadNotifications();
            this.$router.push("/home");
            this.$router.go();
          } else {
            this.error = response.error;
          }
        });
      } else {
        this.error = "Please fill all inputs";
      }
    },

    async customGoogleCallback() {
      console.log("customGoogleCallback");

      let response = await googleAuthCodeLogin();
      const url = "http://localhost:8082/api/v1/auth/google?code=" + response.code;
      const options = {
        method: "GET",
        headers: {
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json",
        },
      };

      let mainResponse = await fetch(url, options);

      const {
        user_id,
        fastlink_access_token,
        fastlink_refresh_token,
        google_id_token,
        username,
      } = await mainResponse.json();

      if (
        user_id != null &&
        fastlink_access_token != null &&
        fastlink_refresh_token != null
      ) {
        this.sessionStore.user.user_id = user_id;
        // localStorage.setItem("user_id", JSON.stringify(user_id));
        this.sessionStore.user.access_token = fastlink_access_token;
        this.sessionStore.user.refresh_token = fastlink_refresh_token;
        this.sessionStore.user.id_token = google_id_token;
        this.sessionStore.user.connected = true;
        // localStorage.setItem("connected", JSON.stringify("true"));
        this.sessionStore.user.username = username;
        //this.setSession(sessionInfo);
        await this.fetchCurrentUserInfo();
        await this.fetchMyPostInfo();
        await this.fetchAllPostsInfo();
        await this.fetchSubscriberInfo();
        await this.fetchSubscriptionInfo();
        await this.fetchUnreadNotifications();
      }
      this.$router.push("/home");
      this.$router.go();
    },

    async fetchCurrentUserInfo() {
      const url =
        "http://localhost:8083/api/v1/users/id/" + this.sessionStore.user.user_id;
      const options = {
        method: "GET",
        headers: {
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json",
          Authorization: "Bearer " + this.sessionStore.user.access_token,
        },
      };

      let response = await fetch(url, options);
      const mainResponse = await response.json();
      console.log("current User: \n" + mainResponse);
      this.sessionStore.user.email = mainResponse.email;
      // localStorage.setItem("email", JSON.stringify(mainResponse.email));
      this.sessionStore.user.firstname = mainResponse.firstName;
      // localStorage.setItem("firstname", JSON.stringify(mainResponse.firstName));
      this.sessionStore.user.lastname = mainResponse.lastName;
      // localStorage.setItem("lastname", JSON.stringify(mainResponse.lastName));
      this.sessionStore.user.profile_pic = mainResponse.profilePictureUrl;
      // localStorage.setItem("profile_pic", JSON.stringify(mainResponse.profilePictureUrl));
      this.sessionStore.user.username = mainResponse.username;
      // localStorage.setItem("username", JSON.stringify(mainResponse.username));
      this.sessionStore.user.description = mainResponse.description;

      let roles: any[] = [];

      for (let i = 0; i < mainResponse.roles.length; i++) {
        if (
          !this.sessionStore.user.roles.includes(mainResponse.roles[i].name) ||
          !roles.includes(mainResponse.roles[i].name)
        ) {
          this.sessionStore.user.roles.push(mainResponse.roles[i].name);
          roles.push(mainResponse.roles[i].name);
        }
        for (let j = 0; j < mainResponse.roles[i].privileges.length; j++) {
          if (
            !this.sessionStore.user.roles.includes(
              mainResponse.roles[i].privileges[j].name
            ) ||
            !roles.includes(mainResponse.roles[i].privileges[j].name)
          ) {
            this.sessionStore.user.roles.push(mainResponse.roles[i].privileges[j].name);
            roles.push(mainResponse.roles[i].privileges[j].name);
          }
        }
      }

      // localStorage.setItem("roles", JSON.stringify(roles));

      this.sessionStore.user.provider = mainResponse.provider;
      // localStorage.setItem("provider", JSON.stringify(mainResponse.provider));
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
          "Authorization": "Bearer " + this.sessionStore.user.access_token
        }
      }
      let res = await fetch(url, postReqOptions)
      const postMainResponse = await res.json();
      console.log("posts: \n" + postMainResponse);

      let posts: any[] = [];

      for (let post of postMainResponse) {
        posts.push(post);
      }
      posts.reverse();
      // @ts ignore
      this.sessionStore.user.all_posts = posts;
      // localStorage.setItem("all_posts", JSON.stringify(posts));
    },

    async fetchSubscriptionInfo() {
      const subsciptionRequest =
        "http://localhost:8083/api/v1/users/subscriptions/" +
        this.sessionStore.user.user_id;
      const subsciptionOptions = {
        method: "GET",
        headers: {
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json",
          Authorization: "Bearer " + this.sessionStore.user.access_token,
        },
      };
      let subsciptionResponse = await fetch(subsciptionRequest, subsciptionOptions);
      const subsciptionMainResponse = await subsciptionResponse.json();

      let subscriptions: any[] = [];

      if (subsciptionMainResponse.length > 0) {
        for (let subscription of subsciptionMainResponse) {
          this.sessionStore.user.subscriptions.push(subscription);
          subscriptions.push(subscription);
        }
        for (let user of subscriptions) {
          user.roles = "[PROTECTED]";
          user.password = "[PROTECTED]";
        }
        this.sessionStore.user.subscribed_count = subsciptionMainResponse.length;
        // localStorage.setItem(
        //   "subscribed_count",
        //   JSON.stringify(subsciptionMainResponse.length)
        // );
      } else {
        this.sessionStore.user.subscriptions = [];
        this.sessionStore.user.subscribed_count = 0;
        // localStorage.setItem("subscribed_count", JSON.stringify(0));
        // localStorage.setItem("subscriptions", JSON.stringify(subscriptions));
      }
    },

    async fetchSubscriberInfo() {
      let subscriberReq =
        "http://localhost:8083/api/v1/users/subscribed/" + this.sessionStore.user.user_id;

      const subsciptionOptions = {
        method: "GET",
        headers: {
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json",
          Authorization: "Bearer " + this.sessionStore.user.access_token,
        },
      };
      let subscriberRes = await fetch(subscriberReq, subsciptionOptions);
      const subscriberMainRes = await subscriberRes.json();

      let subscribers: any[] = [];

      if (subscriberMainRes.length > 0) {
        for (let subscriber of subscriberMainRes) {
          this.sessionStore.user.subscribers.push(subscriber);
          subscribers.push(subscriber);
        }
        for (let u of subscribers) {
          u.roles = "[PROTECTED]";
          u.password = "[PROTECTED]";
        }
        this.sessionStore.user.subscriber_count = subscriberMainRes.length;
        // localStorage.setItem(
        //   "subscriber_count",
        //   JSON.stringify(subscriberMainRes.length)
        // );
      } else {
        this.sessionStore.user.subscribers = [];
        this.sessionStore.user.subscriber_count = 0;
        // localStorage.setItem("subscriber_count", JSON.stringify(0));
        // localStorage.setItem("subscribers", JSON.stringify(subscribers));
      }
    },

    async fetchUnreadNotifications() {
      const url = "http://localhost:8083/user/notification";
      const options = {
        method: "GET",
        headers: {
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json",
          "Authorization": "Bearer " + this.sessionStore.user.access_token
        }
      }

      let r = await fetch(url, options);
      const res = await r.json();

      for (const subNotif of res) {
        console.log(subNotif);
        this.sessionStore.user.notifications.push(subNotif);
      }
      this.sessionStore.user.notif_count = res.length;
    },

    verifyAllInput() {
      if (this.user.username != "" && this.user.password != "") {
        this.inputFull = true;
      }
    },
    displayPassword() {
      var x = document.getElementsByClassName("form-control-password");
      if (x[0].type === "password") {
        x[0].type = "text";
        this.passwordVisible = true;
      } else {
        x[0].type = "password";
        this.passwordVisible = false;
      }
    },
  },
};
</script>
