<template>
  <div class="inner-block">
    <div class="vue-tempalte">
      <div>
        <i
            class="bx bx-left-arrow-alt"
            @click="previousStep"
            v-if="page.step !== 1 && page.step !== 1"
        ></i>

        <h3>{{ $t("signup.create") }}</h3>
        <div id="userDetail1" v-if="page.step === 1">
          <div class="social-icons">
            <a @click="customGoogleCallback" class="btn btn-google">
              <img
                  src="https://img.icons8.com/fluency/48/000000/google-logo.png"
              />
              <label> {{ $t("signup.google") }}</label>
            </a>
          </div>
          <div class="or"></div>
          <div class="form-group-firstName">
            <input
                type="text"
                class="form-control form-control-lg"
                v-model="user.firstName"
                required="required"
            />
            <span class="firstName" v-bind:style="checkFirstNameInput"
            >{{ $t("signup.firstname") }}</span
            >

            <i class="bx bxs-user"></i>
          </div>
          <div class="form-group-lastName">
            <input
                type="text"
                class="form-control form-control-lg"
                v-model="user.lastName"
                required="required"
            />
            <span class="lastName" v-bind:style="checkLastNameInput"
            >{{ $t("signup.lastname") }}</span
            >

            <i class="bx bxs-user"></i>
          </div>
          <div class="form-group-username">
            <input
                type="text"
                class="form-control form-control-lg"
                v-model="user.username"
                required="required"
            />
            <span class="username" v-bind:style="checkUsernameInput"
            >{{ $t("signup.username") }}</span
            >

            <i class="bx bxs-user"></i>
          </div>
        </div>
        <div id="userDetail2" v-if="page.step === 2">
          <div class="form-group-email">
            <input
                type="text"
                class="form-control form-control-lg"
                v-model="user.email"
                required="required"
            />
            <span class="email" v-bind:style="checkEmailInput">Email</span>

            <i class="bx bxs-envelope"></i>
          </div>
          <div class="form-group-confirmationEmail">
            <input
                type="text"
                class="form-control form-control-lg"
                v-model="user.ConfirmationEmail"
                required="required"
            />
            <span
                class="confirmationEmail"
                v-bind:style="checkConfirmationEmailInput"
            >Confirmation Email</span
            >

            <i class="bx bxs-envelope"></i>
          </div>
          <div class="form-group-password">
            <input
                type="password"
                class="form-control form-control-lg form-control-password"
                v-model="user.password"
                required="required"
            />
            <span class="password" v-bind:style="checkPasswordInput"
            >Password</span
            >

            <i class="bx bx-key"></i>
          </div>
          <div class="form-group-confirmationPassword">
            <input
                type="password"
                class="form-control form-control-lg password"
                v-model="user.ConfirmationPassword"
                required="required"
            />
            <i class="bx bx-key"></i>
            <span
                class="ConfirmationPassword"
                v-bind:style="checkConfirmationPasswordInput"
            >Confirmation Password</span
            >
          </div>
        </div>
        <p
            v-if="
            !this.page.inputsFullFirstPage ||
            !this.page.allInputFirstPageValid ||
            !this.page.inputsFullSecondPage ||
            !this.page.allInputSecondPageValid
          "
            style="color: red; font-size: 12px; margin-left: 80px; height: 7px;"
        >
          {{ this.page.error }}
        </p>

        <button
            @click="nextStep"
            v-if="page.step !== 2"
            class="btn-next"
            v-bind:style="verifyAllFirstInput"
        >
          Next
        </button>
        <button
            @click="register"
            v-if="page.step === 2"
            class="btn-authentication-signup"
            v-bind:style="verifyAllSecondInput"
        >
          Create my account
        </button>
      </div>
    </div>
  </div>

  <div class="foot">
    <p class="link-login">
      {{ $t("signup.already") }}
      <router-link to="/login" class="link">{{ $t("signup.signin") }}</router-link><br />
    </p>
  </div>
</template>

<script lang="ts">
import { googleAuthCodeLogin } from "vue3-google-login";
import {mapActions, mapWritableState} from "pinia";
import {useCurrentSessionStore} from "@/stores/currentSession";

export default {
  data() {
    return {
      user: {
        firstName: "",
        lastName: "",
        username: "",
        email: "",
        ConfirmationEmail: "",
        password: "",
        ConfirmationPassword: "",
      },
      page: {
        step: 1,
        inputsFullFirstPage: false,
        inputsFullSecondPage: false,
        error: "",
        inputFirstNameValid: false,
        inputLastNameValid: false,
        inputUsernameValid: false,
        inputEmailValid: false,
        inputConfirmationEmailValid: false,
        inputPasswordValid: false,
        inputConfirmationPasswordValid: false,
        allInputFirstPageValid: false,
        allInputSecondPageValid: false,

      },
      state: ""
    };
  },
  setup() {
    const sessionStore = useCurrentSessionStore();
    return {
      sessionStore,
    };
  },

  /*  computed: {
      ...mapWritableState(useCurrentSessionStore, {
        userSession: 'user'
      }),

      ...mapActions(useCurrentSessionStore, {
        setSession: 'setSessionStoreLogin'
      }),
    },*/

  methods: {

    register() {
      this.verifyAllSecondInput();
      if (this.page.inputsFullSecondPage) {
        if (
            this.page.inputEmailValid &&
            this.page.inputConfirmationEmailValid &&
            this.page.inputPasswordValid &&
            this.page.inputConfirmationPasswordValid
        ) {
          const requestOptions = {
            method: "POST",
            headers: { "Content-Type": "application/json", "Access-Control-Allow-Origin": "*", "Sec-Fetch-Mode": "no-cors" },
            body: JSON.stringify({
              firstName: this.user.firstName,
              lastName: this.user.lastName,
              username: this.user.username,
              password: this.user.password,
              email: this.user.email,
            }),
          };

          fetch(
              "http://localhost:8083/api/v1/users/register",
              requestOptions
          ).then((response) => response.json());
          this.$router.push("/login");
        } else {
          this.page.error = "Please check your inputs";
        }
      } else {
        this.page.error = "Please fill all inputs";
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
          "Content-Type": "application/json"
        }
      }

      let mainResponse = await fetch(url, options);

      const {
        user_id,
        fastlink_access_token,
        fastlink_refresh_token,
        google_id_token
      } = await mainResponse.json();

      if (user_id != null && fastlink_access_token != null && fastlink_refresh_token != null) {
        /*        const sessionInfo = {
                  user_id: user_id,
                  access_token: fastlink_access_token,
                  refresh_token: fastlink_refresh_token,
                  google_id_token: google_id_token
                };*/
        this.sessionStore.user.user_id = user_id;
        this.sessionStore.user.access_token = fastlink_access_token;
        this.sessionStore.user.refresh_token = fastlink_refresh_token;
        this.sessionStore.user.id_token = google_id_token;
        this.sessionStore.user.connected = true;
        //this.setSession(sessionInfo);
        this.fetchCurrentUserInfo();
        this.fetchPostInfo();
        this.fetchSubscriptionInfo();
        this.fetchSubscriberInfo();
      }
      this.$router.push("/home");
    },

    async fetchCurrentUserInfo()
    {
      const url = "http://localhost:8083/api/v1/users/id/" + this.sessionStore.user.user_id;
      const options = {
        method: "GET",
        headers: {
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json",
          "Authorization": "Bearer " + this.sessionStore.user.access_token
        }
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

      let roles: any[] = [];

      for (let i=0; i<mainResponse.roles.length; i++)
      {
        if ( ! this.sessionStore.user.roles.includes(mainResponse.roles[i].name) ||
            !roles.includes(mainResponse.roles[i].name))
        {
          this.sessionStore.user.roles.push(mainResponse.roles[i].name);
          roles.push(mainResponse.roles[i].name);
        }
        for (let j=0; j<mainResponse.roles[i].privileges.length; j++)
        {
          if (! this.sessionStore.user.roles.includes(mainResponse.roles[i].privileges[j].name) ||
              ! roles.includes(mainResponse.roles[i].privileges[j].name))
          {
            this.sessionStore.user.roles.push(mainResponse.roles[i].privileges[j].name);
            roles.push(mainResponse.roles[i].privileges[j].name);
          }
        }
      };

      // localStorage.setItem("roles", JSON.stringify(roles));

      this.sessionStore.user.provider = mainResponse.provider;
      // localStorage.setItem("provider", JSON.stringify(mainResponse.provider));
    },

    async fetchPostInfo()
    {
      const postReqUrl = "http://localhost:8081/api/v1/posts/user/" + this.sessionStore.user.user_id;
      const postReqOptions = {
        method: "GET",
        headers: {
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json",
          "Authorization": "Bearer " + this.sessionStore.user.access_token
        }
      };

      let postResponse = await fetch(postReqUrl, postReqOptions);
      const postMainResponse = await postResponse.json();
      console.log("posts: \n" + postMainResponse);

      let posts: any[] = [];

      for (let post of postMainResponse)
      {
        this.sessionStore.user.posts.push(post);
        posts.push(post);
      }

      // localStorage.setItem("posts", JSON.stringify(posts));

      this.sessionStore.user.post_count = this.sessionStore.user.posts.length;
      // localStorage.setItem("post_count", JSON.stringify(posts.length));
    },

    async fetchSubscriptionInfo()
    {
      const subsciptionRequest = "http://localhost:8081/api/v1/users/subscriptions/" + this.sessionStore.user.user_id;
      const subsciptionOptions = {
        method: "GET",
        headers: {
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json",
          "Authorization": "Bearer " + this.sessionStore.user.access_token
        }
      };
      let subsciptionResponse = await fetch(subsciptionRequest, subsciptionOptions);
      const subsciptionMainResponse = await subsciptionResponse.json();

      let subscriptions: any[] = [];

      if (subsciptionMainResponse.length > 0) {
        for (let subscription of subsciptionMainResponse)
        {
          this.sessionStore.user.subscriptions.push(subscription);
          subscriptions.push(subscription);
        }
        for (let user of subscriptions)
        {
          user.roles = "[PROTECTED]";
          user.password = "[PROTECTED]"
        }
        this.sessionStore.user.subscribed_count = subsciptionMainResponse.length
        // localStorage.setItem("subscribed_count", JSON.stringify(subsciptionMainResponse.length));
      } else {
        this.sessionStore.user.subscriptions = [];
        this.sessionStore.user.subscribed_count = 0;
        // localStorage.setItem("subscribed_count", JSON.stringify(0));
        // localStorage.setItem("subscriptions", JSON.stringify(subscriptions));
      }
    },

    async fetchSubscriberInfo()
    {
      let subscriberReq = "http://localhost:8081/api/v1/users/subscribed/" + this.sessionStore.user.user_id;

      const subsciptionOptions = {
        method: "GET",
        headers: {
          "Access-Control-Allow-Origin": "http://localhost:3000",
          "Content-Type": "application/json",
          "Authorization": "Bearer " + this.sessionStore.user.access_token
        }
      };
      let subscriberRes = await fetch(subscriberReq, subsciptionOptions);
      const subscriberMainRes = await subscriberRes.json();

      let subscribers: any[] = [];

      if (subscriberMainRes.length > 0) {
        for (let subscriber of subscriberMainRes)
        {
          this.sessionStore.user.subscribers.push(subscriber);
          subscribers.push(subscriber);
        }
        for (let u of subscribers)
        {
          u.roles = "[PROTECTED]";
          u.password = "[PROTECTED]"
        }
        this.sessionStore.user.subscriber_count = subscriberMainRes.length
        // localStorage.setItem("subscriber_count", JSON.stringify(subscriberMainRes.length));
      } else {
        this.sessionStore.user.subscribers = [];
        this.sessionStore.user.subscriber_count = 0;
        // localStorage.setItem("subscriber_count", JSON.stringify(0));
        // localStorage.setItem("subscribers", JSON.stringify(subscribers));
      }
    },

    loginWithGoogle() {
      googleAuthCodeLogin().then((response) => {
        const requestOptions = {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            authuser: response.authuser,
            code: response.code,
            prompt: response.prompt,
            scope: response.scope,
          }),
        };
        fetch(
            "http://localhost:8083/api/v1/auth/google?code=" +
            response.code
        ).then((response) => {
          response.json().then((data) => {
            if (
                (data.access_token != null && data.refresh_token != null) ||
                (data.access_token != null && data.id_token != null)
            ) {
              this.sessionStore.user.connected = true;
              this.sessionStore.setLocalStorage(data);
              this.$router.push("/home");
              this.getUser(this.sessionStore.user.access_token);

            } else {
              alert("Wrong username or password");
            }
          });
        });
      });
    },

    nextStep() {
      this.verifyAllFirstInput();

      if (this.page.inputsFullFirstPage) {

        if (
            this.page.inputFirstNameValid &&
            this.page.inputLastNameValid &&
            this.page.inputUsernameValid
        ) {
          this.page.step++;
          this.page.inputsFullFirstPage = true;
          this.page.allInputFirstPageValid = true;
        } else {
          this.page.error = "Please check your inputs";
        }
      } else {
        this.page.error = "Please fill all inputs";
      }
    },

    previousStep() {
      this.page.step--;
    },

    verifyAllFirstInput() {
      if (
          this.user.firstName !== "" &&
          this.user.lastName !== "" &&
          this.user.username !== ""
      ) {
        this.page.inputsFullFirstPage = true;
      }
    },
    verifyAllSecondInput() {
      if (
          this.user.email !== "" &&
          this.user.ConfirmationEmail !== "" &&
          this.user.password !== "" &&
          this.user.ConfirmationPassword !== ""
      ) {
        this.page.inputsFullSecondPage = true;
      }
    },
  },
  computed: {
    checkFirstNameInput() {
      const regex = /^[a-zA-Z0-9]{3,20}$/;
      if (regex.test(this.user.firstName) && this.user.firstName.length > 0) {
        this.page.inputFirstNameValid = true;
        return {
          color: "#89ff00",
        };
      } else if (
          this.user.firstName.length !== 0 &&
          !regex.test(this.user.firstName)
      ) {
        return {
          color: "#ff4545",
        };
      }
    },

    checkLastNameInput() {
      const regex = /^[a-zA-Z0-9]{3,20}$/;
      if (regex.test(this.user.lastName) && this.user.lastName.length > 0) {
        this.page.inputLastNameValid = true;
        return {
          color: "#89ff00",
        };
      } else if (
          this.user.lastName.length !== 0 &&
          !regex.test(this.user.lastName)
      ) {
        return {
          color: "#ff4545",
        };
      }
    },

    checkUsernameInput() {
      const regex = /^[a-zA-Z0-9]{3,20}$/;
      if (regex.test(this.user.username) && this.user.username.length > 0) {
        this.page.inputUsernameValid = true;
        return {
          color: "#89ff00",
        };
      } else if (
          this.user.username.length !== 0 &&
          !regex.test(this.user.username)
      ) {
        return {
          color: "#ff4545",
        };
      }
    },

    checkEmailInput() {
      const regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
      if (regex.test(this.user.email) && this.user.email.length > 0) {
        this.page.inputEmailValid = true;
        return {
          color: "#89ff00",
        };
      } else if (this.user.email.length !== 0 && !regex.test(this.user.email)) {
        return {
          color: "#ff4545",
        };
      }
    },

    checkConfirmationEmailInput() {
      const regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
      if (
          this.user.email === this.user.ConfirmationEmail &&
          this.user.ConfirmationEmail.length > 0 &&
          regex.test(this.user.ConfirmationEmail)
      ) {
        this.page.inputConfirmationEmailValid = true;
        console.log(this.page.inputConfirmationEmailValid);

        return {
          color: "#89ff00",
        };
      } else if (
          (this.user.ConfirmationEmail.length !== 0 &&
              this.user.email !== this.user.ConfirmationEmail) ||
          (!regex.test(this.user.ConfirmationEmail) &&
              this.user.ConfirmationEmail.length !== 0 &&
              this.user.ConfirmationEmail.length !== 0 &&
              this.user.email)
      ) {
        return {
          color: "#ff4545",
        };
      }
    },

    checkPasswordInput() {
      var regex =
          /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
      if (regex.test(this.user.password) && this.user.password.length > 0) {
        this.page.inputPasswordValid = true;
        return {
          color: "#89ff00",
        };
      } else if (
          this.user.password.length !== 0 &&
          !regex.test(this.user.password)
      ) {
        this.page.inputConfirmationEmailValid = false;

        return {
          color: "#ff4545",
        };
      }
    },

    checkConfirmationPasswordInput() {
      var regex =
          /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
      if (
          this.user.password === this.user.ConfirmationPassword &&
          this.user.ConfirmationPassword.length > 0 &&
          regex.test(this.user.ConfirmationPassword)
      ) {
        this.page.inputConfirmationPasswordValid = true;
        return {
          color: "#89ff00",
        };
      } else if (
          (this.user.ConfirmationPassword.length !== 0 &&
              this.user.password !== this.user.ConfirmationPassword) ||
          (!regex.test(this.user.ConfirmationEmail) &&
              this.user.ConfirmationPassword.length !== 0 &&
              this.user.ConfirmationPassword.length !== 0 &&
              this.user.password)
      ) {
        this.page.inputConfirmationPasswordValid = false;

        return {
          color: "#ff4545",
        };
      }
    }
  },
};
</script>
