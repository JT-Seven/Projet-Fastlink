<template>
    <div class="overlay-show-media">
        <div class="container-show-media">
            <i @click="this.$emit('clickShowMediaClose', this.clickShowMedia)" class='bx bx-x bx-flashing-hover cross'
                        id="cross"></i>
            <div class="container-media">
                <div class="contents-img-media">
                    <img src="../../assets/d6pjrnz-02c62f11-507d-4504-80bb-854eb83870ba.png" alt="">
                </div>
            </div>
            <div class="container-comments">
                <div class="contents-bottom">
                    <div class="layout-write-comment" data-v-57a82bcb="">
                        <textarea :placeholder="$t('pop-up.post.what-s-up')" @keyup="writeComment" maxlength="2000" name="comment" id="filed-comment" class="filed-comment text4" data-v-57a82bcb=""></textarea>
                        <div class="counterComment" data-v-57a82bcb="">
                            <span class="comCounter text3" data-v-57a82bcb=""></span>
                        </div>
                        <button @click="sendfiledComment" class="btn-send-comment" data-v-57a82bcb=""><i class="bx bx-paper-plane bx-flashing-hover" data-v-57a82bcb=""></i>
                        </button>
                    </div>
                </div>
                <div class="contents-comments">
                    <div class="container-bubble-comments">
                        <BubbleComment :comment="filed_comment"/>
                    </div>
                </div>
                <div class="contents-top">
                    <div class="img-user-btnSubscribe">
                        <div class="img_nameUser">
                            <div class="img">
                                <img src="../../assets/avatar.png" alt="profil">
                            </div>
                            <div class="nameUser-date">
                                <span class="nameUser text3">Gregory gole</span>
                                <span class="date-created text5">14h</span>
                            </div>
                            <button class="btnSubscribe text3">S'abonner</button>
                        </div>
                        <div class="option-info">
                            <button @click="showSettingsMsg" class="btn-info option"><i class='bx bx-info-circle'></i></button>
                        </div>
                    </div>
                    <div class="description-user">
                        <p class="description text4">
                            Lorem ipsum dolor sit am corporis iusto iure. Veniam nemo repellendus placeat quos praesentium natus quam, labore totam, rem consectetur expedita, maiores nesciunt ab reprehenderit maxime sapiente minima iste aperiam provident beatae cupiditate. Quis
                        </p>
                    </div>
                    <div class="infos-post">
                        <div class="infos layout-nbLikes">
                            <svg aria-label="J’aime" class="_8-yf5 likes" height="22" role="img" viewBox="0 0 24 24" width="20"><path d="M16.792 3.904A4.989 4.989 0 0121.5 9.122c0 3.072-2.652 4.959-5.197 7.222-2.512 2.243-3.865 3.469-4.303 3.752-.477-.309-2.143-1.823-4.303-3.752C5.141 14.072 2.5 12.167 2.5 9.122a4.989 4.989 0 014.708-5.218 4.21 4.21 0 013.675 1.941c.84 1.175.98 1.763 1.12 1.763s.278-.588 1.11-1.766a4.17 4.17 0 013.679-1.938m0-2a6.04 6.04 0 00-4.797 2.127 6.052 6.052 0 00-4.787-2.127A6.985 6.985 0 00.5 9.122c0 3.61 2.55 5.827 5.015 7.97.283.246.569.494.853.747l1.027.918a44.998 44.998 0 003.518 3.018 2 2 0 002.174 0 45.263 45.263 0 003.626-3.115l.922-.824c.293-.26.59-.519.885-.774 2.334-2.025 4.98-4.32 4.98-7.94a6.985 6.985 0 00-6.708-7.218z"></path></svg>
                            <span class="infos-media nbLikes text5">1,895</span>
                        </div>
                        <div class="infos layout-nbComment">
                            <svg aria-label="Commenter" class="_8-yf5 " height="20" role="img" viewBox="0 0 24 24" width="20"><path d="M20.656 17.008a9.993 9.993 0 10-3.59 3.615L22 22z" fill="none" stroke="currentColor" stroke-linejoin="round" stroke-width="2"></path></svg>
                            <span class="infos-media nbComments text5">464</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import BubbleComment from '@/components/comments/BubbleComment.vue'
    export default {
        data () {
            return {
                filed_comment: '',
            }
        },
        components: {
            BubbleComment
        },
        emits: ['clickShowMediaClose'],
        methods: {
            writeComment(e) {
                const textarea = document.getElementById('filed-comment');
                
                console.log("bONBZJORZ");

                if (e.keyCode === 13) {
                    this.sendfiledComment();
                    return;
                }

                textarea.onkeyup = (key) => {
                    this.countChar(key);
                };
                textarea.onkeydown = (keyTwo) => {
                    this.countChar(keyTwo);
                };
            },
            countChar(val) {
                const btnSend = document.querySelector('.btn-send-comment'),
                    comCounter = document.querySelector('.comCounter');

                const maxLength = 2000;
                comCounter.style.display = 'block';
                btnSend.classList.add('btnEnable');
                comCounter.innerHTML = (maxLength - val.target.value.length);

                if(val.target.value.length >= maxLength) {
                    val.target.value = val.target.value.substring(0, maxLength);
                    comCounter.innerHTML = 0;
                    comCounter.style.color = '#ff0000';
                } else if (val.target.value.length === 0) {
                    comCounter.style.display = 'none';
                    btnSend.classList.remove('btnEnable');
                } else {
                    comCounter.style.color = 'var(--primary-color)';
                }
            },
            sendfiledComment() {
                const textarea = document.getElementById('filed-comment');
                this.filed_comment = textarea.value;
                textarea.value = '';
                if (this.filed_comment === null || this.filed_comment === '' || textarea.value.length === 0) {
                    return;
                }
            },
        }
    }
</script>

<style scoped>
    @import '@/css/comments/Comment.css';
</style>