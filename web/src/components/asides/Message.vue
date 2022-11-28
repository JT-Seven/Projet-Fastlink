<template>
    <div class="contents-msg">
        <h2 class="title-box text2">{{ $t('aside-right.messages') }}</h2><button @click="onclickCreateConv" class="btn-create"><ion-icon class="icon-create" name="create-outline"></ion-icon></button>
        <template v-if="clickCreateConv">
            <PopUpCreateConv v-on:clickCreateConvFalse="onclickCreateConv" />
        </template>
        <div class="content-msg">
            <div class="img_msg">
                <div class="img">
                    <img src="../../assets/avatar.png" alt="profil">
                </div>
                <div class="msg">
                    <span class="nameUser text4">Gregory gole</span>
                    <span class="userMsg text5">Just woke up bruh</span>
                </div>
            </div>
            <div class="img_msg">
                <div class="img" id="user2">
                    <img src="../../assets/avatar.png" alt="profil">
                </div>
                <div class="msg">
                    <span class="nameUser text4">Alan Erie</span>
                    <span class="userMsg text5" id="userMsg2">2 new message</span>
                </div>
            </div>
            <div class="img_msg">
                <div class="img" id="user3">
                    <img src="../../assets/avatar.png" alt="profil">
                </div>
                <div class="msg">
                    <span class="nameUser text4">Jéremie Neur</span>
                    <span class="userMsg text5">and what’s about you ?</span>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import PopUpCreateConv from '../pop-up/create-conversation.vue';
    export default {
        data() {
          return {
            clickCreateConv: false,
          };
        },
        components: {
            PopUpCreateConv,
        },
        methods: {
          onclickCreateConv() {
            this.clickCreateConv = !this.clickCreateConv;
          },
        },
    }
</script>

<style scoped>
  .container-msg .contents-msg{
    position: relative;
    width: 100%;
    height: 100%;
  }
  .container-msg .contents-msg .btn-create {
    position: absolute;
    right: -10px;
    top: -.45rem;
    transform: scale(1.1);
    display: flex;
    padding: 10px;
    border-radius: 50%;
    transition: var(--tran-05);
  }
  .container-msg .contents-msg .btn-create:hover {
    background: var(--btn-option);
    box-shadow: var(--boxshadow-01);
  }
  .container-msg .contents-msg .icon-create {
    transform: scale(1.1);
    position: relative;
    left: 0.09rem;
  }
  .container-msg .contents-msg .content-msg {
    padding-top: 20px;
  }
  .container-msg .contents-msg .content-msg .img_msg {
    display: flex;
    justify-content: flex-start;
    margin-bottom: 15px;
  }
  .container-msg .contents-msg .content-msg .img_msg .img {
    height: 42px;
    width: 42px;
    background: #4935C7;
    border-radius: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-right: 15px;
    transform: scale(.9);
  }
  .container-msg .contents-msg .content-msg .img_msg .img img {
    border-radius: 50%;
  }
  .container-msg .contents-msg .content-msg .img_msg .msg {
    display: flex;
    flex-direction: column;
    position: relative;
  }
  .container-msg .contents-msg .content-msg .img_msg .msg .nameUser {
    font-weight: 600;
  }
  .container-msg .contents-msg .content-msg .img_msg .msg .userMsg {
    font-weight: 500;
  }
  #user2 {
    background: #58BDFF;
  }
  #user3 {
    background: #EF48D4;
  }
</style>