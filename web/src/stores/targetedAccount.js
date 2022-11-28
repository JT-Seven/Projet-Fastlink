import {defineStore} from "pinia";
import {ref} from "vue";


export const useTargetedAccountStore = defineStore('targetedAccount', () => {

    const targeted_user = ref({
        user_id: null,
        email: null,
        username: null,
        firstname: null,
        lastname: null,
        profile_pic: null,
        provider: null,
        user_posts: [],
        subscriptions: [],
        subscribers: [],
        user_post_count: null,
        subscriber_count: null,
        subscribed_count: null,
        description: null
    })

    const resetStore = () => {
        targeted_user.value = {
            user_id: null,
            email: null,
            username: null,
            firstname: null,
            lastname: null,
            profile_pic: null,
            provider: null,
            user_posts: [],
            subscriptions: [],
            subscribers: [],
            user_post_count: null,
            subscriber_count: null,
            subscribed_count: null,
            description: null
        }
    }

    return {
        targeted_user,
        resetStore
    }

},

    {
        persist: true,
        deep: true
    }
)