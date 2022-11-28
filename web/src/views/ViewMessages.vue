<template>
  <div class="container-messages">
    <div class="area" >
      <ul class="circles">
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
      </ul>
    </div>
    <div class="overlay-msg"></div>
    <template v-if="clickCreateConv">
      <PopUpCreateConv v-on:clickCreateConvFalse="onclickCreateConv" @onUserSelected="(user) => this.openNewChatAndClosePopUp(user)"/>
    </template>
    <i @click="closeMessages" class='bx bx-x bx-flashing-hover' id="cross"></i>
    <div class="layout-right contents-viewMsg">
      <div class="box-top">
        <div class="box-user">
          <div class="img-user">
            <img src="../assets/avatar.png" alt="profil">
          </div>
          <div class="name-status">
            <span class="nameUser text3">{{ this.openChatContactName }}</span>
            <span class="userMsg text6">Active</span>
          </div>
        </div>
        <div class="option-info">
          <button @click="showSettingsMsg" class="btn-info option"><i class='bx bx-info-circle'></i></button>
        </div>
      </div>
      <div class="contents-conversation">
        <div class="box-date">
          <span class="date text6">Today</span>
        </div>
        <div class="container-bubble-msg" v-for="message in this.messages" :key="message.id">
          <div v-if="parseInt(message.senderId) === parseInt(this.getUserId())">
            <BubbleRight :message="message.content"/>
          </div>
          <div v-else>
            <BubbleLeft :message="message.content"/>
          </div>
        </div>
      </div>
      <div class="box-bottom">
        <div class="media-smileys">
          <div class="box-hover">
            <label for="input-media">
              <input @change="addMedia" type="file" name="" id="input-media">
            </label>
            <svg viewBox="0 0 24 24" aria-hidden="true" class="r-1cvl2hr r-4qtqp9 r-yyyyoo r-1hjwoze r-dnmrzs r-bnwqim r-1plcrui r-lrvibr r-12ym1je"><g><path d="M19.75 2H4.25C3.01 2 2 3.01 2 4.25v15.5C2 20.99 3.01 22 4.25 22h15.5c1.24 0 2.25-1.01 2.25-2.25V4.25C22 3.01 20.99 2 19.75 2zM4.25 3.5h15.5c.413 0 .75.337.75.75v9.676l-3.858-3.858c-.14-.14-.33-.22-.53-.22h-.003c-.2 0-.393.08-.532.224l-4.317 4.384-1.813-1.806c-.14-.14-.33-.22-.53-.22-.193-.03-.395.08-.535.227L3.5 17.642V4.25c0-.413.337-.75.75-.75zm-.744 16.28l5.418-5.534 6.282 6.254H4.25c-.402 0-.727-.322-.744-.72zm16.244.72h-2.42l-5.007-4.987 3.792-3.85 4.385 4.384v3.703c0 .413-.337.75-.75.75z"></path><circle cx="8.868" cy="8.309" r="1.542"></circle></g></svg>
          </div>
          <div class="box-hover">
            <svg @click="clickEmoji" viewBox="0 0 24 24" aria-hidden="true" class="r-1cvl2hr r-4qtqp9 r-yyyyoo r-1hjwoze r-dnmrzs r-bnwqim r-1plcrui r-lrvibr r-12ym1je"><g><path d="M12 22.75C6.072 22.75 1.25 17.928 1.25 12S6.072 1.25 12 1.25 22.75 6.072 22.75 12 17.928 22.75 12 22.75zm0-20C6.9 2.75 2.75 6.9 2.75 12S6.9 21.25 12 21.25s9.25-4.15 9.25-9.25S17.1 2.75 12 2.75z"></path><path d="M12 17.115c-1.892 0-3.633-.95-4.656-2.544-.224-.348-.123-.81.226-1.035.348-.226.812-.124 1.036.226.747 1.162 2.016 1.855 3.395 1.855s2.648-.693 3.396-1.854c.224-.35.688-.45 1.036-.225.35.224.45.688.226 1.036-1.025 1.594-2.766 2.545-4.658 2.545z"></path><circle cx="14.738" cy="9.458" r="1.478"></circle><circle cx="9.262" cy="9.458" r="1.478"></circle></g></svg>
          </div>
        </div>
        <div class="text-btnSend">
          <textarea :placeholder="$t('pop-up.post.what-s-up')" v-model="message" @keyup="writeMessage" maxlength="2000" name="message" id="message" class="message text4"></textarea>
          <div class="msgCounter-msg">
            <span class="msgCounter text3"></span>
          </div>
          <button class="btn-send" @click="sendMessage"><i class='bx bx-paper-plane bx-flashing-hover'></i></button>
        </div>
      </div>
    </div>
    <div class="layout-left contents-viewMsg">
      <div class="box-title">
        <h2 class="text2">Messages</h2>
        <button @click="onclickCreateConv" class="btn-create option"><ion-icon class="icon-create" name="create-outline"/></button>
      </div>
      <div class="container-scroll-conversation">
        <div v-for="user in contacts" :key="user.id">
          <div v-if="parseInt(user.id) !== parseInt(this.getUserId())">
            <ConversationButton @switchChat="this.switchActiveChat(user.id, user.username)" :user="user"/>
          </div>
        </div>
        <div class="blur-effect"/>
      </div>
    </div>
  </div>
</template>

<script>
import PopUpCreateConv from '@/components/pop-up/create-conversation.vue'
import BubbleLeft from "@/components/messages/BubbleLeft.vue";
import BubbleRight from "@/components/messages/BubbleRight.vue";
import ConversationButton from "@/components/messages/ConversationButton.vue";
import {useCurrentSessionStore} from "@/stores/currentSession.js"
import * as Stomp from "stomp-websocket";

export default {
  mounted() {
    this.connect();
    this.getUserContacts();
  },
  setup() {
    const sessionStore = useCurrentSessionStore();
    return {
      sessionStore,
    };
  },
  data() {
    return {
      message: "",
      contacts: [],
      messages: [],
      openChatContactId: 0,
      openChatContactName: "",
      clickCreateConv: false,
      sock: new SockJS("http://localhost:8086/ws"),
      stompClient: null,
    };
  },
  components: {
    BubbleLeft,
    BubbleRight,
    PopUpCreateConv,
    ConversationButton,
  },
  methods: {
    writeMessage(e) {
      const textarea = document.getElementById('message');

      if (e.keyCode === 13) {
        this.sendMessage();
        return;
      }

      textarea.onkeyup = (key) => {
        this.countChar(key);
      };
      textarea.onkeydown = (keyTwo) => {
        this.countChar(keyTwo);
      };
    },
    countChar(val) {
      const btnSend = document.querySelector('.btn-send'),
          msgCounter = document.querySelector('.msgCounter');

      const maxLength = 2000;
      msgCounter.style.display = 'block';
      btnSend.classList.add('btnEnable');
      msgCounter.innerHTML = (maxLength - val.target.value.length);

      if(val.target.value.length >= maxLength) {
        val.target.value = val.target.value.substring(0, maxLength);
        msgCounter.innerHTML = 0;
        msgCounter.style.color = '#ff0000';
      } else if (val.target.value.length === 0) {
        msgCounter.style.display = 'none';
        btnSend.classList.remove('btnEnable');
      } else {
        msgCounter.style.color = 'var(--primary-color)';
      }
    },
    closeMessages() {
      const messages = document.querySelector(".container-messages");

      window.history.back();
      messages.style.visibility = "hidden";
      messages.style.transform = "translateY(100%)";
    },
    onclickCreateConv() {
      this.clickCreateConv = !this.clickCreateConv;
      const overlayMsg = document.querySelector(".overlay-msg");
      const nav = document.querySelector('nav');

      if (this.clickCreateConv) {
        overlayMsg.style.visibility = "visible";
        nav.style.zIndex = "1";
      } else {
        overlayMsg.style.visibility = "hidden";
        nav.style.zIndex = "1";
      }
    },
    sendMessage() {
      this.message = this.message.trim();

      if (this.message !== "") {
        const message = {
          senderId: this.getUserId(),
          recipientId: this.openChatContactId,
          senderName: this.getUserName(),
          recipientName: this.openChatContactName,
          content: this.message
        };

        this.messages.push(message);
        this.stompClient.send("/app/chat", {}, JSON.stringify(message));

        const textarea = document.getElementById('message');
        this.message = '';
        textarea.value = '';
        this.scrollToLastMessage()
      }
    },
    connect() {
      this.stompClient = Stomp.over(this.sock);

      const onMessageReceived = () => {
        this.clearChats()
        this.loadChats()
      }

      const onError = () => {
        console.log("error");
      }

      const onConnected = () => {
        this.stompClient.subscribe(
            "/user/" + this.getUserId() + "/queue/messages",
            onMessageReceived
        )
      };

      this.stompClient.connect({}, onConnected, onError);
    },
    async getUserMessages(recipient) {
      const requestOptions = {
        method: "GET",
        headers: {
          "AUTHORIZATION" : "Bearer " + this.getToken()
        },
      };
      let request = await fetch("http://localhost:8086/messages/" + this.getUserId() + "/" + recipient, requestOptions);
      return await request.json();
    },
    async loadChats() {
      for (const chatMessage of await this.getUserMessages(this.openChatContactId)) {
        this.messages.push(chatMessage);
      }
    },
    switchActiveChat(id, name) {
      this.openChatContactId = id;
      this.openChatContactName = name;
      this.clearChats();
      this.loadChats().then(() => {
        this.scrollToLastMessage()
      });
    },
    clearChats() {
      this.messages = [];
    },
    scrollToLastMessage() {
      const bubbleElements = this.$el.getElementsByClassName('container-bubble-msg');
      const bubbleElementsLength = bubbleElements.length;
      const lastBubble = bubbleElements[bubbleElementsLength - 1];

      if (lastBubble) {
        lastBubble.scrollIntoView({behavior: 'smooth'});
      }
    },
    openNewChatAndClosePopUp(user) {
      this.onclickCreateConv()
      if (!this.chatIsAlreadyOpen(user.id)) {
        this.contacts.push(user);
      }
      this.switchActiveChat(user.id, user.username)
    },
    chatIsAlreadyOpen(userId) {
      for (let i = 0; i < this.contacts.length; i++) {
        if (parseInt(this.contacts[i].id) === parseInt(userId)) return true;
      }
      return false;
    },
    async getUserNameFromId(id) {
      const requestOptions = {
        method: "GET",
        headers: { "AUTHORIZATION" : "Bearer " + this.getToken() },
      };
      let request = await fetch("http://localhost:8083/api/v1/users/id/" + id, requestOptions);
      return await request.json();
    },
    async getUserContacts() {
      const requestOptions = {
        method: "GET",
        headers: { "AUTHORIZATION" : "Bearer " + this.getToken() },
      };
      let request = await fetch("http://localhost:8086/messages/contacts/" + this.getUserId(), requestOptions);
      await request.json().then(async (data) => {
        for (let i = 0; i < data.length; i++) {
          await this.getUserNameFromId(data[i]).then(async (userData) => {
            this.contacts.push({
              "id": userData.id,
              "username": userData.username,
            })
          })
        }
      });
    },
    getUserId() {
      return this.sessionStore.user.user_id;
    },
    getUserName() {
      return "AlanLg"
      //return this.sessionStore.user.username;
    },
    getToken() {
      return this.sessionStore.user.access_token;
    },
  }
}
</script>

<style scoped>
@import '@/css/messages/Messages.css';
</style>

