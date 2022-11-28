<template>
  <div class="overlay">
    <div class="container-create-conv">
      <div class="top-contents">
        <i @click="this.$emit('clickCreateConvFalse', this.clickCreateConv)" class='bx bx-x bx-flashing-hover' id="cross-create-conv"></i>
        <div class="title-next">
          <span class="text2">Nouveau Message</span>
          <span class="btn-next text3">Suivant</span>
        </div>
      </div>
      <div class="search-contents">
        <!-- <div class="user-selected">
            <span @click="userSelected(n)" v-for="n in 10" v-bind:key="n" class="name-user-selected text5">GregoryGole<i  class='bx bx-x bx-flashing-hover' id="cross-user-selected"></i></span>
        </div> -->
        <div class="box-search">
          <input type="text" class="search text3" :placeholder="$t('navbar.search')" @keyup="search" v-model="this.searchValue">
          <svg id="icon-search" viewBox="0 0 24 24" aria-hidden="true" class="r-1bwzh9t r-4qtqp9 r-yyyyoo r-1xvli5t r-dnmrzs r-4wgw6l r-f727ji r-bnwqim r-1plcrui r-lrvibr"><g><path d="M21.53 20.47l-3.66-3.66C19.195 15.24 20 13.214 20 11c0-4.97-4.03-9-9-9s-9 4.03-9 9 4.03 9 9 9c2.215 0 4.24-.804 5.808-2.13l3.66 3.66c.147.146.34.22.53.22s.385-.073.53-.22c.295-.293.295-.767.002-1.06zM3.5 11c0-4.135 3.365-7.5 7.5-7.5s7.5 3.365 7.5 7.5-3.365 7.5-7.5 7.5-7.5-3.365-7.5-7.5z"></path></g></svg>
          <i @click="resetText" class='bx bx-x' id="cross"></i>
        </div>
      </div>
      <div class="sugg-contents">
        <h2 class="title text2">Suggestions</h2>
        <div class="contents-choice-user">
          <div v-for="user in items" class="img-name-choice" v-bind:key="user.username">
            <div class="img"><img src="@/assets/avatar.png" alt="profil utilisateur"></div>
            <div class="names text4">
              <span class="name-user">{{ user.firstName }} {{ user.lastName }}</span>
              <span class="name-bio">@{{ user.username }}</span>
            </div>
            <label>
              <input type="checkbox" name="choice-user" id="choice" @click="this.$emit('onUserSelected', user)"/>
              <span class="checkbox"></span>
            </label>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  emits: ['clickCreateConvFalse', 'onUserSelected'],
  data() {
    return {
      searchValue: "",
      items: [],
    };
  },
  methods: {
    userSelected(user) {
      this.clickCreateConv = clickCreateConv;
    },
    resetText() {
      const input = document.querySelector('.search'),
          cross = document.getElementById('cross');
      input.value = '';
      cross.style.opacity = 0;
      cross.style.top = '1.5rem';

      input.addEventListener('keyup', () => {
        cross.style.opacity = 1;
        cross.style.top = '0.6rem';
        if (input.value >= 0) {
          cross.style.opacity = 0;
          cross.style.top = '1.5rem';
        }
      });
    },
    search(e) {
      if (e.keyCode) {
        fetch(`http://localhost:8089/api/search?value=${this.searchValue}`)
            .then((response) => response.json())
            .then((data) => {
              this.items = data;
            });
        console.log(this.items);
      }
    },
  },
}
</script>

<style scoped>
.container-create-conv {
  position: relative;
  width: 500px;
  height: 80%;
  background: var(--body-color-secondary);
  display: flex;
  justify-content: flex-start;
  flex-direction: column;
  border-radius: 10px;
  border-bottom-right-radius: 6px;
  z-index: 999;
  overflow: hidden;
}
#cross-create-conv {
  position: absolute;
  left: 15px;
  top: 14px;
  font-size: 26px;
  z-index: 2;
  color: var(--primary-color);
}

/*==============TOP-CONTENTS==========*/

.container-create-conv .top-contents {
  height: 50px;
  width: 100%;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 14px;
}
.container-create-conv .top-contents .title-next {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}
.container-create-conv .top-contents .title-next .btn-next {
  position: absolute;
  right: 20px;
  color: var(--color-secondary);
  transition: var(--tran-02);
  cursor: pointer;
}
.container-create-conv .top-contents .title-next .btn-next::after {
  content: "\1F862";
  font-size: 12.8px;
  opacity: 0;
  transition: var(--tran-02);
}
.container-create-conv .top-contents .title-next .btn-next:hover::after {
  opacity: 1;
  padding-left: 5px;
}
.container-create-conv .top-contents .title-next .btn-next:hover {
  opacity: .8;
  color:  #52a0fe;
}

/*==============SEARCH-CONTENTS==========*/

.container-create-conv .search-contents {
  height: auto;
  width: 100%;
  display: flex;
  align-items: center;
  flex-direction: column;
  /* overflow-y: scroll; */
}

/*======USER-SELECTED=====*/

.container-create-conv .search-contents .user-selected {
  width: 100%;
  height: auto;
  padding: 15px 0px 5px 15px;
}
.container-create-conv .search-contents .user-selected .name-user-selected {
  display: inline-flex;
  align-items: center;
  width: max-content;
  height: 30px;
  padding: 0px 25px 0px 10px;
  border-radius: 5px;
  background: #52a0fe;
  margin: 5px;
  cursor: pointer;
  position: relative;
}
.container-create-conv .search-contents .user-selected .name-user-selected i {
  position: absolute;
  right: 5px;
  cursor: pointer;
}
.container-create-conv .search-contents .user-selected .name-user-selected #cross-user-selected {
  color: var(--primary-color);
  font-size: 15px;
  font-weight: bold;
}

.container-create-conv .search-contents .box-search {
  position: relative;
  border-radius: 25px;
  padding: 7.75px 100px;
  width: 100%;
  left: auto;
}
.container-create-conv .search-contents .box-search svg {
  width: 1.1rem;
  height: 1.1rem;
  position: absolute;
  top: 50%;
  left: 1.9rem;
  transform: translate(-50%, -50%);
}
.container-create-conv .search-contents .search {
  background: transparent;
  font-size: var(--text-size-03);
  position: relative;
  opacity: 1;
  right: 2rem;
  width: calc(100% + 100px);
}
.container-create-conv .search-contents .search:focus {
  outline: none;
}
#icon-search {
  filter: var(--icon-white);
}

/*==============SUGG-CONTENTS==========*/

.sugg-contents {
  display: flex;
  flex-direction: column;
  padding: 20px 0px 0px 25px;
  height: 100%;
  overflow-y: scroll;
  overflow-x: hidden;
}
.sugg-contents .title {

}
.sugg-contents .contents-choice-user {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  padding: 0 20px 0px 0px;
  overflow-y: scroll;
  overflow-x: hidden;
  margin-top: 20px;
}
.sugg-contents .contents-choice-user .img-name-choice {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 15px;
  position: relative;
}
.sugg-contents .contents-choice-user .img-name-choice .img {
  width: 42px;
  height: 42px;
  border-radius: 50%;
  background: #4a35c7;
  display: flex;
  justify-content: center;
  align-items: center;
}
.sugg-contents .contents-choice-user .img-name-choice img {
  width: 30px;
}
.sugg-contents .contents-choice-user .img-name-choice .names {
  display: flex;
  position: absolute;
  flex-direction: column;
  left: 3.5rem;
  line-height: 17px;
}
.sugg-contents .contents-choice-user .img-name-choice .name-user {
  color: var(--primary-color);
}
.sugg-contents .contents-choice-user .img-name-choice .name-bio {
  color: var(--description-color);
}
.sugg-contents input[type="checkbox"] {
  position: absolute;
  transform: scale(0);
}
.sugg-contents label{
  cursor:pointer;
}
.sugg-contents .checkbox{
  display: block;
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255,255,255,0.175);
  transition: all 0.35s;
}
.sugg-contents input[type="checkbox"]:checked ~ .checkbox {
  transform: rotate(45deg) translateX(-5px);
  width: 10px;
  border-color:#1ac0a2;
  border-top-color:transparent;
  border-left-color:transparent;
}
.sugg-contents .contents-choice-user::-webkit-scrollbar {
  width: 3px;
}
.sugg-contents .contents-choice-user::-webkit-scrollbar-track {
  background: var(--track-color);
  border-bottom-right-radius: 20px;
}
.sugg-contents .contents-choice-user::-webkit-scrollbar-thumb {
  background-color: var(--thumb-color-msg);
  border-radius: 10px;
}
#cross {
  position: absolute;
  top: 1.5rem;
  opacity: 0;
  right: 1.2rem;
  font-size: 22px;
  transition: var(--tran-03);
  color: var(--primary-color);
}
</style>