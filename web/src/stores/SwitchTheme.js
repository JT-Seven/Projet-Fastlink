import {defineStore} from "pinia";

export const switchTheme = defineStore('switchTheme', {

    state: () => {
        return {
            dark: false
        }
    },

},
{
    persist: true,
    deep: true
});