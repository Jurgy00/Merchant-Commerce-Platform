<script setup lang="ts">
const { cartCount } = useCart()
const colorMode = useColorMode()

const isDark = computed(() => colorMode.value === 'dark')

const toggleColorMode = () => {
  colorMode.preference = isDark.value ? 'light' : 'dark'
}

const mobileMenuOpen = ref(false)
const searchQuery = useState('store-search', () => '')
const selectedCategory = useState<string>('store-category', () => 'All')

const categories = [
  'All',
  'Fiction',
  'Technology',
  'Business',
  'Self Development',
  'Children'
]

const selectCategory = async (category: string) => {
  selectedCategory.value = category
  mobileMenuOpen.value = false

  await navigateTo('/#products')
}

const closeMobileMenu = () => {
  mobileMenuOpen.value = false
}
</script>

<template>
  <header class="sticky top-0 z-50 border-b border-default bg-default">
    <!-- Announcement bar -->
    <div class="bg-neutral-950 text-white">
      <div class="mx-auto flex h-8 max-w-[1800px] items-center justify-center px-4 text-[9px] font-semibold uppercase tracking-[0.18em] sm:px-6">
        Discover your next favourite read · Books delivered across Kenya
      </div>
    </div>

    <!-- Main header -->
    <div class="mx-auto flex h-[76px] max-w-[1800px] items-center gap-5 px-4 sm:px-6 lg:px-8">
      <NuxtLink
        to="/"
        class="shrink-0 text-xl font-black tracking-[-0.08em] text-neutral-950 dark:text-white sm:text-2xl"
        @click="closeMobileMenu"
      >
        BOOKSTORE
      </NuxtLink>

      <!-- Desktop search -->
      <div class="mx-auto hidden w-full max-w-xl md:block">
        <UInput
          v-model="searchQuery"
          icon="i-lucide-search"
          placeholder="Search books, authors..."
          size="lg"
          color="neutral"
          variant="outline"
          class="w-full"
        />
      </div>

      <!-- Actions -->
      <div class="ml-auto flex shrink-0 items-center gap-1">
        <UButton
          color="neutral"
          variant="ghost"
          icon="i-lucide-search"
          class="md:hidden"
          aria-label="Search books"
          @click="mobileMenuOpen = true"
        />

        <UButton
          to="/cart"
          color="neutral"
          variant="ghost"
          class="gap-2 text-neutral-700 hover:bg-neutral-100 hover:text-neutral-950 dark:text-neutral-200 dark:hover:bg-neutral-800 dark:hover:text-white"
          aria-label="Shopping bag"
        >
          <UIcon
            name="i-lucide-shopping-bag"
            class="size-5"
          />

          <span class="hidden text-xs font-semibold sm:inline">
            Bag
          </span>

          <UBadge
            v-if="cartCount > 0"
            color="neutral"
            variant="solid"
            size="sm"
          >
            {{ cartCount }}
          </UBadge>
        </UButton>

        <UButton
          color="neutral"
          variant="ghost"
          :icon="isDark ? 'i-lucide-sun' : 'i-lucide-moon'"
          :aria-label="isDark ? 'Switch to light mode' : 'Switch to dark mode'"
          @click="toggleColorMode"
        />

        <UButton
          color="neutral"
          variant="ghost"
          icon="i-lucide-menu"
          class="lg:hidden"
          aria-label="Open menu"
          @click="mobileMenuOpen = true"
        />
      </div>
    </div>

    <!-- Desktop category navigation -->
    <nav class="hidden border-t border-default lg:block">
      <div class="mx-auto flex h-12 max-w-[1800px] items-center justify-center gap-8 px-8">
        <button
          v-for="category in categories"
          :key="category"
          type="button"
          class="h-full border-b-2 px-1 text-[11px] font-semibold uppercase tracking-[0.12em] transition-colors"
          :class="selectedCategory === category
            ? 'border-neutral-950 text-neutral-950 dark:border-white dark:text-white'
            : 'border-transparent text-neutral-500 hover:border-neutral-400 hover:text-neutral-950 dark:text-neutral-400 dark:hover:border-neutral-600 dark:hover:text-white'"
          @click="selectCategory(category)"
        >
          {{ category === 'All' ? 'All Books' : category }}
        </button>
      </div>
    </nav>
  </header>

  <!-- Mobile navigation -->
  <USlideover
    v-model:open="mobileMenuOpen"
    title="BOOKSTORE"
    description="Browse our collection."
  >
    <template #body>
      <div class="space-y-7">
        <UInput
          v-model="searchQuery"
          icon="i-lucide-search"
          placeholder="Search books, authors..."
          size="lg"
          class="w-full"
        />

        <div>
          <p class="mb-4 text-[10px] font-bold uppercase tracking-[0.16em] text-neutral-400 dark:text-neutral-500">
            Browse categories
          </p>

          <nav class="flex flex-col">
            <button
              v-for="category in categories"
              :key="category"
              type="button"
              class="flex items-center justify-between border-b border-neutral-100 py-4 text-left text-sm font-medium dark:border-neutral-800"
              :class="selectedCategory === category
                ? 'text-neutral-950 dark:text-white'
                : 'text-neutral-500 dark:text-neutral-400'"
              @click="selectCategory(category)"
            >
              {{ category === 'All' ? 'All Books' : category }}

              <UIcon
                v-if="selectedCategory === category"
                name="i-lucide-check"
                class="size-4"
              />
            </button>
          </nav>
        </div>

        <UButton
          to="/cart"
          block
          color="neutral"
          variant="outline"
          @click="closeMobileMenu"
        >
          <UIcon name="i-lucide-shopping-bag" />
          Shopping Bag
          <span v-if="cartCount > 0">({{ cartCount }})</span>
        </UButton>
      </div>
    </template>
  </USlideover>
</template>
