<template>
  <div class="layout-conversation">
  <div @click="$emit('switchChat')">
      <div class="img-user"><img src="@/assets/avatar.png" alt="image de l'utilisateur"></div>
      <div class="name-msg">
        <span class="name-user text3">{{ user.username }}</span>
        <span class="msg text5">Vous avez envoyé une publication</span>
      </div>
      <span class="indicator-newMsg"></span>
    </div>
  </div>
</template>

<script>
export default {
  props: ['user'],
  emits: ['switchChat'],
}
</script>

<style scoped>
@import '@/css/messages/Messages.css';
</style>