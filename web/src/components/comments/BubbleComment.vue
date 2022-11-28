<template>
    <div class="bubble-comment" id="user">
      <div class="img-user">
        <img src="@/assets/avatar.png" alt="profil">
      </div>
      <div class="bubble-comment-content">
        <span class="nameUser text5">Gregory gole</span>
        <span class="comment text5">{{ comment }}</span>
        <span class="dateSend text5">28m</span>
      </div>
    </div>
</template>

<script>
    export default {
        props: ['comment'],
    }
</script>

<style scoped>
@import '@/css/comments/Comment.css';
</style>