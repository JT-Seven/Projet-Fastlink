<template>
    <div class="bubble-left bubble" id="friends">
      <div class="img-user">
        <img src="@/assets/avatar.png" alt="profil">
      </div>
      <span class="msg text5">{{ message }}</span>
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