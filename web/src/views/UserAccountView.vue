<template>
  <AccountUserTargeted v-bind:user="this.targeted_user" />
</template>

<script lang="ts">
import { useTargetedAccountStore } from "../stores/targetedAccount";
import AccountUserTargeted from "@/views/AccountUserTargeted.vue";
import { useCurrentSessionStore } from "@/stores/currentSession";

export default {
  name: "UserAccountView",

  setup() {
    const targetedAccount = useTargetedAccountStore();
    const sessionStore = useCurrentSessionStore();

    return {
      targetedAccount,
      sessionStore,
    };
  },

  mounted() {
    const id = Number(
      window.location.pathname.substring(window.location.pathname.lastIndexOf("/") + 1)
    );

    this.targetedAccount.resetStore();
    this.fetchTargetedUserInfo(id);
    this.fetchUserPostInfo(id);
    this.fetchSubscriptionInfo(id);
    this.fetchSubscriberInfo(id);

    this.targeted_user = this.targetedAccount.targeted_user;
  },

  data() {
    return {
      targeted_user: null,
    };
  },

  methods: {
    async fetchTargetedUserInfo(id: number): void {
      const url = "http://localhost:8083/api/v1/users/id/" + id;
      const options = {
        method: "GET",
        headers: {
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json",
          Authorization: "Bearer " + this.sessionStore.user.access_token,
        },
      };

      let data = {};
      let d = [];
      let response = await fetch(url, options);
      const res = await response.json();

      this.targetedAccount.targeted_user.user_id = id;
      this.targetedAccount.targeted_user.email = res?.email;
      this.targetedAccount.targeted_user.firstname = res?.firstName;
      this.targetedAccount.targeted_user.lastname = res?.lastName;
      this.targetedAccount.targeted_user.profile_pic = res?.profilePictureUrl;
      this.targetedAccount.targeted_user.description = res?.description;
      this.targetedAccount.targeted_user.provider = res?.provider;
      this.targetedAccount.targeted_user.username = res?.username;
    },

    async fetchUserPostInfo(id: number) {
      const url = "http://localhost:8081/api/v1/posts/user/" + id;
      const options = {
        method: "GET",
        headers: {
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json",
          Authorization: "Bearer " + this.sessionStore.user.access_token,
        },
      };

      let r = await fetch(url, options);
      const res = await r.json();

      for (const post of res) {
        this.targetedAccount.targeted_user.user_posts.push(post);
      }
      this.targetedAccount.targeted_user.user_posts.reverse();
      this.targetedAccount.targeted_user.user_post_count = this.targetedAccount.targeted_user.user_posts.length;
    },

    async fetchSubscriptionInfo(id: number) {
      const url = "http://localhost:8083/api/v1/users/subscriptions/" + id;
      const options = {
        method: "GET",
        headers: {
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json",
          Authorization: "Bearer " + this.sessionStore.user.access_token,
        },
      };

      let r = await fetch(url, options);
      const res = await r.json();

      if (res.length > 0) {
        for (const sub of res) {
          this.targetedAccount.targeted_user.subscriptions.push(sub);
        }
        for (let u of this.targetedAccount.targeted_user.subscriptions) {
          u.roles = "[PROTECTED]";
          u.password = "[PROTECTED]";
        }
        this.targetedAccount.targeted_user.subscribed_count = res.length;
      } else {
        this.targetedAccount.targeted_user.subscriptions = [];
        this.targetedAccount.targeted_user.susbcribed_count = 0;
      }
    },

    async fetchSubscriberInfo(id: number) {
      const url = "http://localhost:8083/api/v1/users/subscribed/" + id;
      const options = {
        method: "GET",
        headers: {
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json",
          Authorization: "Bearer " + this.sessionStore.user.access_token,
        },
      };
      let r = await fetch(url, options);
      const res = await r.json();

      if (res.length > 0) {
        for (const sub of res) {
          this.targetedAccount.targeted_user.subscribers.push(sub);
        }
        for (let u of this.targetedAccount.targeted_user.subscribers) {
          u.roles = "[PROTECTED]";
          u.password = "[PROTECTED]";
        }
        this.targetedAccount.targeted_user.subscriber_count = res.length;
      } else {
        this.targetedAccount.targeted_user.subscribers = [];
        this.targetedAccount.targeted_user.susbcriber_count = 0;
      }
    },
  },

  components: {
    AccountUserTargeted,
  },
};
</script>

<style scoped></style>
