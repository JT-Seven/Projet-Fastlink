<template>
    <div class="dropdown" @click="dropdown" v-click-outside="clickOutSide">
        <input type="text" v-model="lang" class="textBox text3" placeholder="Langue" readonly>
        <div class="option text3">
            <div @click="show('Fr', 'fr')"><img src="../../assets/french-flag.png" alt="logo">&nbsp;&nbsp;Fr</div>
            <div @click="show('Eng', 'en')"><img src="../../assets/english-flag.png" alt="logo">&nbsp;&nbsp;Eng</div>
        </div>
    </div>
</template>

<script lang="ts">
    import { Locales } from '../../i18n'
    export default {
        data() {
            return {
                lang: "",
            }
        },
        methods: {
            show(lang, locale: Locales ) {
                this.$i18n.locale = locale;
                this.lang = lang;
            },
            dropdown() {
                const dropdown = document.querySelector(".dropdown");
                dropdown.classList.toggle("active");
            },
            clickOutSide() {
                const dropdown = document.querySelector(".dropdown");
                dropdown.classList.remove("active");
            },
        },
    }
</script>

<style scoped>
/* ============ SELECT-LANG ========== */

.dropdown {
    position: relative;
    width: 110px;
    height: 35px;
    margin-left: 15px;
    transition: var(--tran-03);
}
.dropdown:hover {
    transform: translateY(-5px);
}
.dropdown:active {
    transform: translateY(-1px);
}
.dropdown::before {
    content: '';
    position: absolute;
    right: 20px;
    top: 12px;
    z-index: 1000;
    width: 8px;
    height: 8px;
    border: 2px solid var(--chevron-lang);
    border-top: 2px solid var(--body-color);
    border-right: 2px solid var(--body-color);
    transform: rotate(-45deg);
    transition: var(--tran-05);
    pointer-events: none;
}
.dropdown.active::before
{
    top: 17px;
    transform: rotate(-225deg);
}
.dropdown input {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    cursor: pointer;
    background: var(--body-color);
    border: none;
    outline: none;
    box-shadow: var(--boxshadow-01);
    padding: 12px 20px;
    border-radius: 25px;
    font-size: var(--text-size-04);
}
.dropdown .option {
    position: absolute;
    top: 40px;
    width: 100%;
    background: var(--body-color);
    box-shadow:030px 30px rgba(e,0,0,0.05);
    border-radius: 10px;
    overflow: hidden;
    display: none;
    transition: var(--tran-05);
}
.dropdown.active .option {
    display: block;
}
.dropdown .option div {
    padding: 5px 20px;
    cursor: pointer;
    display: flex;
    align-items: center;
}
.dropdown .option div:hover {
   background: var(--color-secondary);
   color: #fff;
}
.option img {
    width: 20px;
}

</style>