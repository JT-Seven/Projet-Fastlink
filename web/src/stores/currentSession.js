import {defineStore} from "pinia";
import {ref} from "vue";


export const useCurrentSessionStore = defineStore('currentSession', () => {
        const user = ref({
            refresh_token: null,
            access_token: null,
            id_token: null,
            user_id : null,
            connected: false,
            description: null,
            email: null,
            username: null,
            firstname: null,
            lastname: null,
            profile_pic: null,
            roles: [],
            provider: null,
            my_posts: [],
            all_posts: [],
            subscriptions: [],
            subscribers: [],
            my_post_count: null,
            subscriber_count: null,
            subscribed_count: null,
            notifications: [],
            notif_count: null
        })


        const setSessionStoreLogin = async (sessionInfo) => {
            user.value.access_token = sessionInfo.access_token;
            user.value.refresh_token = sessionInfo.refresh_token;
            user.value.connected = true;
            user.value.id_token = null;

            let response = await getUserId(sessionInfo.access_token);

            response.json().then((data) => {
                user.value.user_id = data.userId
            })
        }

        const setUserId = async () => {
            let response = await getUserId(user.value.access_token);

            response.json().then((data) => {
                user.value.user_id = data.userId
            })
        }

        const getUserId = (token) => {
            const url = "http://localhost:8082/api/v1/auth/token/id"
            const options = {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + token
                }
            }

            return fetch(url, options);
        }

        const resetUserStore = () => {
            user.value.user_id = null
            user.value.access_token = null
            user.value.refresh_token = null
            user.value.connected = false
            user.value.id_token = null
            user.value.email = null
            user.value.username = null
            user.value.firstname = null
            user.value.lastname = null
            user.value.profile_pic = null
            user.value.roles = []
            user.value.provider = null
            user.value.my_posts = []
            user.value.subscriptions = []
            user.value.subscribers = []
            user.value.my_post_count = null
            user.value.subscriber_count = null
            user.value.subscribed_count = null
            user.value.all_posts = []
            user.value.notifications = []
            user.value.description = null
        }

        return {
            user,
            setSessionStoreLogin,
            resetUserStore,
            setUserId
        }
    },
    {
        persist: true,
        deep: true
    })