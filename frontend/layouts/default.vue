<template>
  <div class="min-h-screen bg-gray-50 dark:bg-gray-950 flex flex-col">
    <header class="bg-white dark:bg-gray-900 border-b border-gray-200 dark:border-gray-800 sticky top-0 z-50">
      <div class="max-w-6xl mx-auto px-4 sm:px-6 h-16 flex items-center justify-between gap-4">
        
        <NuxtLink to="/" class="flex items-center gap-2 font-semibold text-gray-900 dark:text-white text-lg">
          <div class="w-8 h-8 rounded-lg bg-emerald-600 flex items-center justify-center text-white text-sm font-bold">
            JB
          </div>
          <span class="hidden sm:inline">Jurgens Bookshelf</span>
        </NuxtLink>

        
        <div class="hidden sm:flex flex-1 max-w-md">
          <UInput
            v-model="searchQuery"
            icon="i-heroicons-magnifying-glass"
            placeholder="Search books…"
            class="w-full"
            @keyup.enter="goToSearch"
          />
        </div>

        
        <div class="flex items-center gap-2">
        
          <NuxtLink to="/?search=1" class="sm:hidden p-2 rounded-lg text-gray-500 hover:bg-gray-100">
            <UIcon name="i-heroicons-magnifying-glass" class="w-5 h-5" />
          </NuxtLink>

        
          <NuxtLink to="/cart" class="relative p-2 rounded-lg text-gray-500 hover:bg-gray-100 dark:hover:bg-gray-800">
            <UIcon name="i-heroicons-shopping-bag" class="w-6 h-6" />
            <span
              v-if="itemCount > 0"
              class="absolute -top-0.5 -right-0.5 min-w-[18px] h-[18px] bg-emerald-600 text-white text-[10px] font-bold rounded-full flex items-center justify-center px-1"
            >
              {{ itemCount }}
            </span>
          </NuxtLink>
        </div>

      </div>
    </header>

    <main class="flex-1">
      <slot />
    </main>

    <!-- ── Footer ─────────────────────────────────────────────────────── -->
    <footer class="bg-white dark:bg-gray-900 border-t border-gray-200 dark:border-gray-800 mt-auto">
      <div class="max-w-6xl mx-auto px-4 sm:px-6 py-8">
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <div>
            <p class="font-semibold text-gray-900 dark:text-white">JurgensBookshelf</p>
            <p class="text-sm text-gray-500 mt-0.5">jurgenskips@gmail.com· 0742 098841</p>
          </div>
          <p class="text-xs text-gray-400">
            © {{ new Date().getFullYear() }} Bookshelf. Secure payments via M-Pesa.
          </p>
        </div>
      </div>
    </footer>

  </div>
</template>

<script setup lang="ts">
const { itemCount } = useCart()

const searchQuery = ref('')
const router = useRouter()

const goToSearch = () => {
  if (searchQuery.value.trim()) {
    router.push({ path: '/', query: { search: searchQuery.value.trim() } })
  }
}
</script>
