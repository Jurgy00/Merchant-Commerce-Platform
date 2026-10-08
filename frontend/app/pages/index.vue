<script setup lang="ts">
import { computed } from 'vue'
import { products } from '~/data/products'

const categories = [
  'All',
  'Fiction',
  'Business',
  'Technology',
  'Self Development',
  'Children'
]

const searchQuery = useState('store-search', () => '')
const selectedCategory = useState('store-category', () => 'All')
const maxPrice = useState('store-max-price', () => 4000)
const sortBy = useState('store-sort', () => 'default')

const filteredProducts = computed(() => {
  const query = searchQuery.value.trim().toLowerCase()

  const result = products.filter((product) => {
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
  maxPrice.value = 4000
  sortBy.value = 'default'
}
</script>

<template>
  <div>
    <!-- Editorial introduction -->
    <section class="border-b border-default">
      <div class="mx-auto max-w-[1800px] px-4 pb-9 pt-10 sm:px-6 lg:px-8 lg:pb-11 lg:pt-14">
        <div class="flex flex-col gap-8 lg:flex-row lg:items-end lg:justify-between">
          <div class="max-w-4xl">
            <p class="text-[10px] font-black uppercase tracking-[0.28em] text-primary">
              The BookStore Collection
            </p>

            <h1 class="mt-3 max-w-4xl text-4xl font-black leading-[0.98] tracking-[-0.045em] text-highlighted sm:text-5xl lg:text-7xl">
              Books worth making
              <span class="text-primary">time for.</span>
            </h1>

            <p class="mt-5 max-w-2xl text-sm leading-6 text-muted sm:text-base">
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
                {{ categories.length - 1 }}
              </p>

              <p class="text-[9px] font-bold uppercase tracking-[0.14em] text-muted">
                Categories
              </p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Categories -->
    <section class="border-b border-default">
      <div class="mx-auto max-w-[1800px] px-4 sm:px-6 lg:px-8">
        <div class="flex items-center gap-5 overflow-x-auto">
          <span class="shrink-0 py-4 text-[9px] font-black uppercase tracking-[0.18em] text-muted">
            Browse
          </span>

          <button
            v-for="category in categories"
            :key="category"
            type="button"
            class="shrink-0 border-b-2 py-4 text-[10px] font-bold uppercase tracking-[0.1em] transition-colors"
            :class="
              selectedCategory === category
                ? 'border-primary text-primary'
                : 'border-transparent text-muted hover:text-highlighted'
            "
            @click="selectedCategory = category"
          >
            {{ category }}
          </button>
        </div>
      </div>
    </section>

    <!-- Catalogue -->
    <main
      id="products"
      class="mx-auto max-w-[1800px] scroll-mt-32 px-4 pb-16 pt-9 sm:px-6 lg:px-8 lg:pt-11"
    >
      <!-- Single toolbar -->
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
            v-model="maxPrice"
            type="number"
            min="0"
            size="sm"
            icon="i-lucide-tag"
            class="w-32"
            placeholder="Max price"
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
            v-if="searchQuery || selectedCategory !== 'All' || maxPrice !== 4000 || sortBy !== 'default'"
            color="neutral"
            variant="ghost"
            size="sm"
            @click="resetFilters"
          >
            Clear
          </UButton>
        </div>
      </div>

      <!-- ONE product grid -->
      <div
        v-if="filteredProducts.length"
        class="mt-7 grid grid-cols-2 gap-x-4 gap-y-10 sm:grid-cols-3 md:gap-x-5 lg:grid-cols-4 xl:grid-cols-5 2xl:grid-cols-6"
      >
        <ProductCard
          v-for="product in filteredProducts"
          :key="product.id"
          :product="product"
        />
      </div>

      <!-- Empty -->
      <div
        v-else
        class="flex min-h-[360px] flex-col items-center justify-center text-center"
      >
        <div class="flex size-14 items-center justify-center rounded-full bg-elevated">
          <UIcon
            name="i-lucide-search-x"
            class="size-6 text-muted"
          />
        </div>

        <h3 class="mt-4 text-lg font-bold text-highlighted">
          No books found
        </h3>

        <p class="mt-1 max-w-sm text-sm text-muted">
          Try another search, category or price range.
        </p>

        <UButton
          class="mt-5"
          @click="resetFilters"
        >
          Reset filters
        </UButton>
      </div>
    </main>
  </div>
</template>
