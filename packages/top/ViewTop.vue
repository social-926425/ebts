<template>
        <div id="content">
            <Top></Top>
        </div>
</template>

<script>
    import Top from './topology'
    export default {
        name: 'ViewTop',
        components: {
            Top
        },
    }
</script>

<style scoped>
    #content{
        width: 100%;
        margin: 0 auto;
        height: 100%;
    }
</style>
