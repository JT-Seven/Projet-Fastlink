<template>
  <div>
    <div class="bubble-right bubble" id="user">
      <span class="msg text5">{{ message }}</span>
      <div class="img-user">
        <img src="@/assets/avatar.png" alt="profil">
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: ['message'],
}
</script>

<style scoped>
@import '@/css/messages/Messages.css';
</style>