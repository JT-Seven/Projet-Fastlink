<template>
  <div class="overlay-modify-profil">
    <div class="container-modify-profil">
      <div class="contents-form-info-user">
        <form @submit.prevent="onSubmit">
          <input
            type="text"
            placeholder="Nom"
            name="name_"
            id="name_"
            class="champs text3"
          />
          <input
            type="text"
            placeholder="Nom d'utilisateur"
            name="username_"
            id="username_"
            class="champs text3"
          />
          <textarea
            placeholder="Biographie"
            maxlength="250"
            name="description_"
            id="description"
            class="bio-user champs text3"
          ></textarea>
          <button class="btn-save text3">Sauvegarder</button>
        </form>
      </div>
      <div class="contents-top-modify-profil">
        <i
          @click="this.$emit('clickModifyProfilClose', this.clickModifyProfil)"
          class="bx bx-x bx-flashing-hover cross"
          id="cross"
        ></i>
        <div class="title-save">
          <span class="text2">Modifier profile</span>
        </div>
      </div>
      <div class="contents-img-profil">
        <ModifyProfileImage
          v-bind:provider="this.$props.provider"
          v-bind:url="this.$props.url"
          v-bind:id="this.$props.id"
          class="img-profil"
        >
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
        </ModifyProfileImage>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import ModifyProfileImage from "./ModifyProfileImage.vue";
export default {
  data() {
    return {
      profil: {
        imgProfil: "",
      },
    };
  },
  emits: ["clickModifyProfilClose"],
  props: ["url", "id", "provider"],
  mounted() {
    this.fetchProfilePicture();
  },
  methods: {
    onSubmit() {
      // upload file
      const formData = new FormData();
      formData.append("file", this.profil.imgProfil);
      axios
        .post(
          "http://localhost:8083/api/v1/users/profile-picture/upload/" +
            this.$props.id,
          formData,
          {
            headers: {
              "Access-Control-Allow-Origin": "http://localhost:3000",
            },
          }
        )
        .then((res) => {
          console.log(res);
        });
    },
    previewImageProfil(event) {
      const Imgprofil = document.querySelector(".img-profil");
      this.profil.imgProfil = event.target.files[0];
      this.onSubmit();
      Imgprofil.style.backgroundImage = `url(${URL.createObjectURL(
        event.target.files[0]
      )})`;
    },
    fetchProfilePicture() {
      // fetch file
      axios
        .get(
          "http://localhost:8083/api/v1/users/profile-picture/" +
            this.$props.id
        )
        .then((res) => {
          console.log(res.data);
          //this.profil.imgProfil = res.data;
        });
    },
  },
  components: { ModifyProfileImage },
};
</script>

<style scoped>
    .overlay-modify-profil {
        width: 100%;
        height: 100%;
        position: fixed;
        top: 0;
        right: 0;
        z-index: 994;
        background: rgba(0,0,0,0.5);
        display: flex;
        justify-content: center;
        align-items: center;
    }
    .container-modify-profil {
        position: relative;
        width: 500px;
        height: 526px;
        background: var(--body-color-secondary);
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
        border-radius: 10px;
        border-bottom-right-radius: 6px;
        overflow: hidden;
        top: -3.8rem;
    }
    .container-modify-profil .contents-top-modify-profil {
        position: absolute;
        top: 0;
        width: 100%;
        display: flex;
        justify-content: center;
        padding-top: 15px;
        align-items: center;
    }
    .container-modify-profil .btn-save {
        position: relative;
        padding: 5px 15px;
        transition: var(--tran-03);
        border-radius: 25px;
        background: var(--color-secondary);
        color: white;
        margin-right: auto;
    }
    .container-modify-profil .btn-save:hover {
        transform: translateY(-5px);
    }
    .container-modify-profil .btn-save:active {
        transform: translateY(-1px);
    }
    .container-modify-profil .contents-img-profil {
        width: 100%;
        height: 100%;
        padding-top: 70px;
        display: flex;
        justify-content: center;
    }
    .container-modify-profil .contents-img-profil .img-profil {
        height: 130px;
        width: 130px;
        position: relative;
        border-radius: 50%;
        background-position: center;
        background-size: cover;
        background-repeat: no-repeat;
        display: flex;
        justify-content: center;
        align-items: center;
    }
    .container-modify-profil .contents-img-profil .img-profil .icon-edit {
        font-size: 1.25rem;
        position: relative;
    }
    .container-modify-profil .contents-img-profil .img-profil .btn-overlay {
        content: '';
        position: absolute;
        width: 45px;
        height: 45px;
        transition: var(--tran-03);
        background: rgba(0,0,0,0.5);
        border-radius: 50%;
        z-index: 3;
    }
    .imgHover {
        background: red;
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
    .container-modify-profil .contents-img-profil .img-profil .profil_label:hover  .container-modify-profil .contents-img-profil .img-profil .btn-overlay {
        background: rgba(228, 0, 0, 0.934);
    }
    .container-modify-profil .contents-img-profil .img-profil .profil_input {
        position: absolute;
        left: -999px;
        width: 1px;
        height: 1px;
        opacity: 0;
        visibility: hidden;
    }
    .container-modify-profil .contents-form-info-user {
        position: absolute;
        top: 0;
        padding-top: 250px;
        height: 100%;
        width: 100%;
    }
    .container-modify-profil .contents-form-info-user form {
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
        padding: 0 20px;
    }
    .container-modify-profil .contents-form-info-user .champs {
        width: 100%;
        margin-bottom: 20px;
        padding: 10px;
        outline: none;
        background: none;
        border: 1px solid #3f4e69;
        border-radius: 5px;
    }

.cross {
  position: absolute;
  left: 15px;
  top: 15px;
  font-size: 26px;
  z-index: 2;
  color: var(--primary-color);
}
</style>
