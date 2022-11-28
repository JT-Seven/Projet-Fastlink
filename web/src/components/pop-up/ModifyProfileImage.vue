<template class="container-modify-profil">
  <div class="contents-img-profil">
    <img
      class="img-profil"
      v-if="this.$props.provider === 'google' && this.$props.url != null"
      v-bind:src="this.$props.url"
    />

    <img
      class="img-profil"
      v-if="this.$props.provider === 'local' && this.$props.url != null"
      v-bind:src="this.$props.url + '?userId=' + this.$props.id"
    />

    <img class="img-profil" v-if="this.$props.url === null" src="@/assets/avatar.png" />

    <div class="btn-overlay"></div>
    <i class="bx bx-camera bx-flashing-hover icon-edit" style="color: #ffffff"></i>
    <label class="profil_label">
      <input
        type="file"
        id="profil_input"
        class="profil_input"
        @change="previewImageProfil"
        multiple
      />
    </label>
  </div>
</template>

<script lang="ts">
export default {
  props: ["provider", "url", "id"],
};
</script>

<style scoped>
.contents-img-profil .img-profil {
  height: 130px;
  width: 130px;
  position: relative;
  border-radius: 50%;
  /* background: url('../../assets/spiderman.png'); */
  background-position: center;
  background-size: cover;
  background-repeat: no-repeat;
  display: flex;
  justify-content: center;
  align-items: center;
}

.container-modify-profil .contents-img-profil .img-profil .btn-overlay {
  content: "";
  position: absolute;
  width: 45px;
  height: 45px;
  transition: var(--tran-03);
  background: rgba(0, 0, 0, 0.5);
  border-radius: 50%;
}

.container-modify-profil .contents-img-profil .img-profil .profil_label {
  cursor: pointer;
  transition: var(--tran-04);
  height: 50px;
  width: 50px;
  position: absolute;
  background: transparent;
  border-radius: 50%;
}

.container-modify-profil .contents-img-profil .img-profil .profil_input {
  position: absolute;
  left: -999px;
  width: 1px;
  height: 1px;
  opacity: 0;
  visibility: hidden;
}
</style>
