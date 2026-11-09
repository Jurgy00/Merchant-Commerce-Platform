
<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import type { Product } from '~/types/product'

const { getProducts, getCategories } = useProductApi()

const products = ref<Product[]>([])
const categories = ref<string[]>([])
const isLoading = ref(true)
const errorMessage = ref('')

const categoryOptions = computed(() => ['All', ...categories.value])

const searchQuery = useState('store-search', () => '')
const selectedCategory = useState('store-category', () => 'All')
const maxPrice = useState<number | ''>('store-max-price', () => '')
const sortBy = useState('store-sort', () => 'default')

// Fetch from the backend after the page mounts.
// This avoids requiring the backend during Nuxt's homepage prerender.
const loadProducts = async () => {
  isLoading.value = true
  errorMessage.value = ''

  try {
    products.value = await getProducts()
  } catch (error) {
    console.error('Failed to load bookstore products:', error)
    errorMessage.value = 'We could not load the books. Please try again.'
  } finally {
    isLoading.value = false
  }
}

onMounted(async () => {
  const categoryRequest = getCategories(2)
    .then((result) => {
      categories.value = result.map(category => category.name)
    })
    .catch((error) => {
      console.error('Failed to load bookstore categories:', error)
    })

  await loadProducts()
  await categoryRequest
})

const filteredProducts = computed(() => {
  const query = searchQuery.value.trim().toLowerCase()

  const result = products.value.filter((product) => {
    const matchesSearch =
      !query ||
      product.name.toLowerCase().includes(query) ||
      product.author.toLowerCase().includes(query) ||
      product.category.toLowerCase().includes(query) ||
      product.description.toLowerCase().includes(query)

    const matchesCategory =
      selectedCategory.value === 'All' ||
      product.category === selectedCategory.value

    const matchesPrice =
      maxPrice.value === '' ||
      product.price <= Number(maxPrice.value)

    return matchesSearch && matchesCategory && matchesPrice
  })

  if (sortBy.value === 'price-low') {
    return [...result].sort((a, b) => a.price - b.price)
  }

  if (sortBy.value === 'price-high') {
    return [...result].sort((a, b) => b.price - a.price)
  }

  return result
})

const resetFilters = () => {
  searchQuery.value = ''
  selectedCategory.value = 'All'
  maxPrice.value = ''
  sortBy.value = 'default'
}
</script>

<template>
  <div>
    <!-- Editorial introduction -->
    <section class="border-b border-default">
      <div class="mx-auto max-w-[1800px] px-4 py-3 sm:px-6 lg:px-8 lg:py-4">
        <div class="flex flex-col gap-2 lg:flex-row lg:items-center lg:justify-between">
          <div class="max-w-4xl">
            <p class="text-[10px] font-black uppercase tracking-[0.28em] text-primary">
              The BookStore Collection
            </p>

            <h1 class="mt-1 max-w-4xl text-2xl font-black leading-tight tracking-tight text-highlighted sm:text-3xl lg:text-4xl">
              Books worth making
              <span class="text-primary">time for.</span>
            </h1>

            <p class="mt-2 max-w-2xl text-sm leading-5 text-muted">
              Discover thoughtful books for focused work, personal growth,
              technology, business and stories worth getting lost in.
            </p>
          </div>

          <div class="flex gap-8 border-t border-default pt-4 lg:border-l lg:border-t-0 lg:pl-8 lg:pt-0">
            <div>
              <p class="text-2xl font-black text-highlighted">
                {{ products.length }}
              </p>
              <p class="text-[9px] font-bold uppercase tracking-[0.14em] text-muted">
                Titles
              </p>
            </div>

            <div>
              <p class="text-2xl font-black text-highlighted">
                {{ categories.length }}
              </p>
              <p class="text-[9px] font-bold uppercase tracking-[0.14em] text-muted">
                Categories
              </p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Catalogue -->
    <main
      id="products"
      class="mx-auto max-w-[1800px] scroll-mt-32 px-4 pb-12 pt-5 sm:px-6 lg:px-8 lg:pt-6"
    >
      <div class="flex flex-col gap-4 border-b border-default pb-5 sm:flex-row sm:items-end sm:justify-between">
        <div>
          <p class="text-[9px] font-black uppercase tracking-[0.2em] text-primary">
            Explore the collection
          </p>

          <div class="mt-1 flex items-baseline gap-3">
            <h2 class="text-2xl font-black tracking-tight text-highlighted sm:text-3xl">
              {{ selectedCategory === 'All' ? 'All books' : selectedCategory }}
            </h2>

            <span class="text-xs text-muted">
              {{ filteredProducts.length }}
              {{ filteredProducts.length === 1 ? 'title' : 'titles' }}
            </span>
          </div>
        </div>

        <div class="flex flex-wrap items-center gap-2">
          <UInput
            v-model="searchQuery"
            icon="i-lucide-search"
            size="sm"
            class="w-full sm:w-52"
            placeholder="Search books or authors..."
            aria-label="Search books or authors"
          />

        <UInput
            :model-value="maxPrice === '' ? '' : String(maxPrice)"
            type="number"
            min="0"
            size="sm"
            icon="i-lucide-tag"
            class="w-32"
            placeholder="Max price"
            aria-label="Maximum book price"
            @update:model-value="(value) => {
              maxPrice = value === '' ? '' : Number(value)
            }"
          />
          <USelect
            v-model="sortBy"
            :items="[
              { label: 'Featured', value: 'default' },
              { label: 'Price: Low to High', value: 'price-low' },
              { label: 'Price: High to Low', value: 'price-high' }
            ]"
            size="sm"
            class="w-44"
          />

          <UButton
            v-if="searchQuery || selectedCategory !== 'All' || maxPrice !== '' || sortBy !== 'default'"
            color="neutral"
            variant="ghost"
            size="sm"
            @click="resetFilters"
          >
            Clear
          </UButton>
        </div>
      </div>

      <!-- Loading state -->
      <div
        v-if="isLoading"
        class="flex min-h-[240px] items-center justify-center gap-3 text-sm text-muted"
      >
        <UIcon name="i-lucide-loader-circle" class="size-5 animate-spin" />
        Loading books...
      </div>

      <!-- API error state -->
      <div
        v-else-if="errorMessage"
        class="flex min-h-[240px] flex-col items-center justify-center text-center"
      >
        <UIcon name="i-lucide-wifi-off" class="size-8 text-muted" />

        <p class="mt-3 text-sm text-muted">
          {{ errorMessage }}
        </p>

        <UButton
          class="mt-4"
          @click="loadProducts"
        >
          Try again
        </UButton>
      </div>

      <!-- Product grid -->
      <div
        v-else-if="filteredProducts.length"
        class="mt-5 grid grid-cols-2 gap-x-4 gap-y-7 sm:grid-cols-3 sm:gap-x-5 md:grid-cols-4 md:gap-y-8 xl:grid-cols-5"
      >
        <ProductCard
          v-for="product in filteredProducts"
          :key="product.id"
          :product="product"
        />
      </div>

      <!-- Empty state -->
      <div
        v-else
        class="flex min-h-[360px] flex-col items-center justify-center text-center"
      >
        <div class="flex size-14 items-center justify-center rounded-full bg-elevated">
          <UIcon name="i-lucide-search-x" class="size-6 text-muted" />
        </div>

        <h3 class="mt-4 text-lg font-bold text-highlighted">
          No books found
        </h3>

        <p class="mt-1 max-w-sm text-sm text-muted">
          Try another search, category or price range.
        </p>

        <UButton class="mt-5" @click="resetFilters">
          Reset filters
        </UButton>
      </div>
    </main>
  </div>
</template>
