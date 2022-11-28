<template>
    <div class="contents-asideRight">
        <div class="container-msg box">
          <Message />
        </div>
        <div class="container-trend box">
          <Trend />
        </div>
        <div class="container-sugg box">
          <Suggestions />
        </div>
    </div>
</template>

<script>
    import Message from './Message.vue';
    import Trend from './Trend.vue';
    import Suggestions from './Suggestions.vue';
    export default {
        components: {
            Message,
            Trend,
            Suggestions
        },
    }
</script>

<style scoped>
  .contents-asideRight {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    height: 100%;
    overflow: scroll;
    padding: 20px 20px 70px;
  }
  .box {
    height: 235px;
    width: 285px;
    margin-bottom: 20px;
    border-radius: 10px;
    background: var(--body-color-secondary);
    box-shadow: var(--boxshadow-04);
    padding: 15px 20px;
  }
  .container-trend {
    height: 310px;
  }
  .container-sugg {
    height: 270px;
  }
  span {
    overflow: hidden;
    width: 180px;
    white-space: nowrap;
  }

</style>