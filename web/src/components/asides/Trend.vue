<template>
    <div class="contents-trend">
        <h2 class="title-box text2">{{ $t('aside-right.trending') }}</h2>
        <button class="options"><img src="../../assets/ellipsis.png" alt=""></button>
        <div class="layout-trend">
            <div class="trend-info">
                <span class="topic text6">Politics</span>
                <span class="nameTrend text4">Kiev</span>
                <span class="viewTrend text6">11K Linked</span>
            </div>
            <div class="trend-info">
                <span class="topic text6">Sports</span>
                <span class="nameTrend text4">Allez Paris</span>
                <span class="viewTrend text6">41,8K Linked</span>
            </div>
            <div class="trend-info">
                <span class="topic text6">Informatique</span>
                <span class="nameTrend text4">#Java</span>
                <span class="viewTrend text6">102,6K Linked</span>
            </div>
        </div>
        <router-link to="tendances"><span class="show-more text5">{{ $t("aside-right.show-more") }}</span></router-link>
    </div>
</template>

<script>
    export default {
        
    }
</script>

<style scoped>
    .contents-trend{
        height: 100%;
        position: relative;
        width: 100%;
    }
    .contents-trend .layout-trend{
        width: 100%;
        height: calc(100% - 65px);
        padding-top: 20px;
        margin-bottom: 15px;
        overflow: hidden;
    }
    .contents-trend .layout-trend .trend-info{
        display: flex;
        flex-direction: column;
        margin-bottom: 15px;
    }
    .contents-trend .layout-trend .trend-info .topic{
        font-weight: lighter;
    }
    .contents-trend .layout-trend .trend-info .nameTrend{
        font-weight: 600;
    }
    .contents-trend .layout-trend .trend-info .viewTrend{
        font-weight: lighter;
    }
    .contents-trend .options {
        position: absolute;
        right: -10px;
        top: -.2rem;
        transform: scale(1.2);
        display: flex;
        padding: 10px;
        border-radius: 50%;
        transition: var(--tran-05);
    }
    .contents-trend .options:hover {
        background: var(--btn-option);
        box-shadow: var(--boxshadow-01);
    }
    .contents-trend .options img{
        width: 12px; 
        filter: var(--icon-white);
    }
    .contents-trend .show-more {
        color: var(--color-secondary);
        transition: var(--tran-02);
        cursor: pointer;
    }   
    .contents-trend .show-more::after {
        content: "\1F862";
        font-size: 12.8px;
        opacity: 0;
        transition: var(--tran-02);
    }
    .contents-trend .show-more:hover::after {
        opacity: 1;
        padding-left: 5px;
    }
    .contents-trend .show-more:hover {
        opacity: .8;
        color:  #52a0fe;
    }
</style>