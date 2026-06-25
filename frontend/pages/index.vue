<template>
  <div>
    <section class="bg-emerald-700 text-white">
      <div class="max-w-6xl mx-auto px-4 sm:px-6 py-12 sm:py-20 flex flex-col sm:flex-row items-center gap-8">
        <div class="flex-1 text-center sm:text-left">
          <p class="text-emerald-200 text-sm font-medium mb-3 uppercase tracking-widest">
            {{ merchant?.name }}
          </p>
          <h1 class="text-3xl sm:text-5xl font-bold leading-tight mb-4">
            Stories that<br class="hidden sm:inline" /> stay with you
          </h1>
          <p class="text-emerald-100 text-base sm:text-lg max-w-md mx-auto sm:mx-0 leading-relaxed">
            {{ merchant?.description }}
          </p>
          <div class="mt-8 flex flex-col sm:flex-row gap-3 justify-center sm:justify-start">
            <UButton
              size="lg"
              color="white"
              variant="solid"
              icon="i-heroicons-book-open"
              @click="scrollToProducts"
            >
              Browse books
            </UButton>
            <UButton
              size="lg"
              color="white"
              variant="outline"
              icon="i-heroicons-phone"
              :to="`tel:${merchant?.phone}`"
            >
              {{ merchant?.phone }}
            </UButton>
          </div>
        </div>
        
        <div class="hidden sm:flex gap-3 items-end flex-shrink-0">
          <div v-for="book in HERO_BOOKS" :key="book.colour"
            class="rounded-lg flex items-end justify-center pb-2 text-xl"
            :class="book.colour"
            :style="{ width: '64px', height: `${book.height}px` }"
          >{{ book.emoji }}</div>
        </div>
      </div>
    </section>

  
    <div class="sm:hidden bg-white dark:bg-gray-900 border-b border-gray-200 dark:border-gray-800 px-4 py-3">
      <UInput
        v-model="searchQuery"
        icon="i-heroicons-magnifying-glass"
        placeholder="Search books…"
        class="w-full"
        @input="onSearch"
      />
    </div>

    <div class="max-w-6xl mx-auto px-4 sm:px-6 py-8" ref="productsSection">

      
      <div class="flex gap-2 overflow-x-auto pb-2 mb-6 scrollbar-hide">
        <button
          v-for="cat in [{ id: 0, name: 'All', slug: 'all' }, ...categories]"
          :key="cat.id"
          class="flex-shrink-0 px-4 py-2 rounded-full text-sm font-medium border transition-colors"
          :class="selectedCategoryId === cat.id
            ? 'bg-emerald-600 text-white border-emerald-600'
            : 'bg-white dark:bg-gray-900 text-gray-600 dark:text-gray-300 border-gray-200 dark:border-gray-700 hover:border-emerald-400'"
          @click="selectedCategoryId = cat.id"
        >
          {{ cat.name }}
        </button>
      </div>

      <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 mb-6">
        <p class="text-sm text-gray-500 dark:text-gray-400">
          <span class="font-medium text-gray-900 dark:text-white">{{ filteredProducts.length }}</span>
          {{ filteredProducts.length === 1 ? 'book' : 'books' }} found
        </p>
        <div class="flex items-center gap-2">
          <span class="text-sm text-gray-500">Sort:</span>
          <USelect
            v-model="sortBy"
            :options="SORT_OPTIONS"
            size="sm"
            class="w-44"
          />
        </div>
      </div>

      
      <div v-if="pending" class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-4 sm:gap-6">
        <div
          v-for="i in 8" :key="i"
          class="bg-white dark:bg-gray-900 rounded-2xl border border-gray-100 dark:border-gray-800 animate-pulse"
          style="height: 340px"
        />
      </div>

      <div v-else-if="filteredProducts.length === 0" class="text-center py-20">
        <div class="text-5xl mb-4">🔍</div>
        <h3 class="text-lg font-semibold text-gray-900 dark:text-white mb-2">No books found</h3>
        <p class="text-gray-500">Try a different search term or category.</p>
        <UButton class="mt-4" variant="ghost" @click="resetFilters">Clear filters</UButton>
      </div>

      <div v-else class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-4 sm:gap-6">
        <ProductCard
          v-for="product in filteredProducts"
          :key="product.id"
          :product="product"
        />
      </div>

    </div>

  </div>
</template>

<script setup lang="ts">
import type { Product, Category, Merchant } from '~/types'

useHead({ title: 'Bookshelf — Browse Books' })

const { fetchProducts, fetchCategories, fetchMerchant } = useProducts()

const merchant  = ref<Merchant | null>(null)
const allProducts = ref<Product[]>([])
const categories = ref<Category[]>([])
const pending  = ref(true)

onMounted(async () => {
  ;[merchant.value, allProducts.value, categories.value] = await Promise.all([
    fetchMerchant(),
    fetchProducts(),
    fetchCategories(),
  ])
  pending.value = false

  
  const route = useRoute()
  if (route.query.search) {
    searchQuery.value = String(route.query.search)
  }
})

const searchQuery = ref('')
const selectedCategoryId = ref(0) 
const sortBy = ref('default')

const SORT_OPTIONS = [
  { label: 'Default',      value: 'default' },
  { label: 'Price: low → high', value: 'price_asc' },
  { label: 'Price: high → low', value: 'price_desc' },
  { label: 'Name A–Z',     value: 'name_asc' },
]

const filteredProducts = computed(() => {
  let results = [...allProducts.value]

  if (searchQuery.value.trim()) {
    const term = searchQuery.value.toLowerCase()
    results = results.filter(
      p => p.name.toLowerCase().includes(term) || p.description.toLowerCase().includes(term)
    )
  }

  if (selectedCategoryId.value !== 0) {
    results = results.filter(p => p.category.id === selectedCategoryId.value)
  }

  if (sortBy.value === 'price_asc')  results.sort((a, b) => a.price - b.price)
  if (sortBy.value === 'price_desc') results.sort((a, b) => b.price - a.price)
  if (sortBy.value === 'name_asc')   results.sort((a, b) => a.name.localeCompare(b.name))

  return results
})

const onSearch = () => { /* reactivity handles the filtering */ }

const resetFilters = () => {
  searchQuery.value = ''
  selectedCategoryId.value = 0
  sortBy.value = 'default'
}
const productsSection = ref<HTMLElement | null>(null)
const scrollToProducts = () => {
  productsSection.value?.scrollIntoView({ behavior: 'smooth' })
}

const HERO_BOOKS = [
  { colour: 'bg-amber-300',  height: 160, emoji: '📚' },
  { colour: 'bg-teal-300',   height: 200, emoji: '📖' },
  { colour: 'bg-pink-300',   height: 140, emoji: '📗' },
  { colour: 'bg-violet-300', height: 180, emoji: '📘' },
]
</script>
