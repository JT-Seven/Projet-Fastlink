<template>
    <div class="contents-sugg">
        <h2 class="title-box text2">{{ $t('aside-right.suggestions') }}</h2>
        <button class="options"><img src="../../assets/ellipsis.png" alt=""></button>
        <div class="layout-sugg">
            <div class="img_msg">
                <button class="addUser"><i class='bx bx-user-plus bx-flashing-hover icon' ></i></button>
                <div class="img" id="user1">
                    <img src="../../assets/avatar.png" alt="profil">
                </div>
                <div class="msg">
                    <span class="nameUser text4">Bella Donne</span>
                    <span class="city text6">Paris</span>
                </div>
            </div>
            <div class="img_msg">
                <button class="addUser"><i class='bx bx-user-plus bx-flashing-hover icon' ></i></button>
                <div class="img" id="user2">
                    <img src="../../assets/avatar.png" alt="profil">
                </div>
                <div class="msg">
                    <span class="nameUser text4">Emma Tome</span>
                    <span class="city text6">Lyon</span>
                </div>
            </div>
            <div class="img_msg">
                <button class="addUser"><i class='bx bx-user-plus bx-flashing-hover icon' ></i></button>
                <div class="img" id="user3">
                    <img src="../../assets/avatar.png" alt="profil">
                </div>
                <div class="msg">
                    <span class="nameUser text4">Mouss Tique</span>
                    <span class="city text6">Toulouse</span>
                </div>
            </div>
        </div>
        <router-link to="suggestions"><span class="show-more text5">{{ $t("aside-right.show-more") }}</span></router-link>
    </div>
</template>

<script>
    export default {
        
    }
</script>

<style scoped>
    .contents-sugg{
        height: 100%;
        position: relative;
        width: 100%;
    }
    .contents-sugg .layout-sugg{
        width: 100%;
        height: calc(100% - 65px);
        padding-top: 20px;
        margin-bottom: 15px;
        overflow: hidden;
    }
    .contents-sugg .layout-sugg .img_msg {
        display: flex;
        justify-content: flex-start;
        margin-bottom: 15px;
    }
    .contents-sugg .layout-sugg .img_msg .img {
        height: 42px;
        width: 42px;
        border-radius: 50%;
        display: flex;
        justify-content: center;
        align-items: center;
        margin-right: 15px;
        transform: scale(.9);
    }
    .contents-sugg .layout-sugg .img_msg .img img {
        border-radius: 50%;
    }
    .contents-sugg .layout-sugg .img_msg .msg {
        display: flex;
        flex-direction: column;
        position: relative;
    }
    .contents-sugg .layout-sugg .img_msg .msg .nameUser {
        font-weight: 600;
    }
    .contents-sugg .layout-sugg .img_msg .msg .city {
        font-weight: lighter;    
    }
    #user1 {
        background: #c79800;
    }
    #user2 {
        background: #AAAAAA;
    }
    #user3 {
        background: #3A3832;
    }
    .contents-sugg .options {
        position: absolute;
        right: -10px;
        top: -.2rem;
        transform: scale(1.2);
        display: flex;
        padding: 10px;
        border-radius: 50%;
        transition: var(--tran-05);
    }
    .contents-sugg .options:hover {
        background: var(--btn-option);
        box-shadow: var(--boxshadow-01);
    }
    .contents-sugg .options img{
        width: 12px; 
        filter: var(--icon-white);
    }
    .contents-sugg .addUser {
        position: absolute;
        right: -10px;
        display: flex;
        padding: 10px;
        border-radius: 50%;
        transition: var(--tran-05);
    }
    .contents-sugg .show-more {
        color: var(--color-secondary);
        transition: var(--tran-02);
        cursor: pointer;
    }   
    .contents-sugg .show-more::after {
        content: "\1F862";
        font-size: 12.8px;
        opacity: 0;
        transition: var(--tran-02);
    }
    .contents-sugg .show-more:hover::after {
        opacity: 1;
        padding-left: 5px;
    }
    .contents-sugg .show-more:hover {
        opacity: .8;
        color:  #52a0fe;
    }
    .icon {
        font-size: 22px;
    }
</style>