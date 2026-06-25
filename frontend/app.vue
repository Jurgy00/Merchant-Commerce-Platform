<template>
  <div>
    <NuxtLayout>
      <NuxtPage />
    </NuxtLayout>
  </div>
</template>

<script setup lang="ts">
const { loadFromStorage } = useCart()

onMounted(() => {
  loadFromStorage()
})
</script>
