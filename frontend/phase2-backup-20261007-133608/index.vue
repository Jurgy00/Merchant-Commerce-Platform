<script setup lang="ts">
import { computed } from 'vue'
import { products } from '~/data/products'

const {
  searchQuery,
  selectedCategory,
  maxPrice,
  sortBy,
  resetFilters
} = useStoreFilters()

const filteredProducts = computed(() => {
  const query = searchQuery.value.trim().toLowerCase()

  const filtered = products.filter((product) => {
    const matchesSearch =
      !query ||
      product.name.toLowerCase().includes(query) ||
      product.description.toLowerCase().includes(query) ||
      product.category.toLowerCase().includes(query)

    const matchesCategory =
      selectedCategory.value === 'All' ||
      product.category === selectedCategory.value

    const matchesPrice =
      product.price <= maxPrice.value

    return matchesSearch && matchesCategory && matchesPrice
  })

  if (sortBy.value === 'price-low') {
    return [...filtered].sort((a, b) => a.price - b.price)
  }

  if (sortBy.value === 'price-high') {
    return [...filtered].sort((a, b) => b.price - a.price)
  }

  return filtered
})
</script>
<template>
  <div class="min-h-screen">

    <!-- =================================
         STORE INTRO
    ================================== -->
    <section class="border-b border-default bg-elevated/10">
      <div
        class="mx-auto max-w-[1800px] px-4 py-5 sm:px-6 lg:px-8 lg:py-6"
      >
        <p
          class="text-[11px] font-bold uppercase tracking-[0.22em] text-primary"
        >
          BookStore
        </p>

        <div
          class="mt-1 flex flex-col gap-1 sm:flex-row sm:items-baseline sm:gap-4"
        >
          <h1
            class="text-2xl font-black tracking-tight text-highlighted sm:text-3xl"
          >
            Browse books.
          </h1>

          <p class="text-sm text-muted">
            Discover your next great read across fiction, business,
            technology, self-development and more.
          </p>
        </div>
      </div>
    </section>


    <!-- =================================
         PRODUCT COLLECTION
    ================================== -->
    <main
      id="products"
      class="mx-auto max-w-[1800px] px-4 py-6 sm:px-6 lg:px-8 lg:py-7"
    >

      <!-- Collection heading -->
      <div
        class="flex items-center justify-between gap-4"
      >
        <div class="flex items-baseline gap-3">
          <p
            class="hidden text-[11px] font-bold uppercase tracking-[0.2em] text-primary sm:block"
          >
            Collection
          </p>

          <h2
            class="text-xl font-bold tracking-tight text-highlighted sm:text-2xl"
          >
            All books
          </h2>
        </div>

        <div
          class="shrink-0 rounded-full border border-default bg-elevated/30 px-3 py-1 text-xs text-muted"
        >
          <span class="font-semibold text-highlighted">
            {{ filteredProducts.length }}
          </span>
          {{ filteredProducts.length === 1 ? 'book' : 'books' }}
        </div>
      </div>


      <!-- =================================
           PRODUCT GRID
      ================================== -->
      <div
        v-if="filteredProducts.length > 0"
        class="mt-5 grid grid-cols-2 gap-3 md:grid-cols-3 lg:grid-cols-4 lg:gap-4 xl:grid-cols-5"
      >
        <ProductCard
          v-for="product in filteredProducts"
          :key="product.id"
          :product="product"
        />
      </div>


      <!-- =================================
           EMPTY STATE
      ================================== -->
      <UCard
        v-else
        class="mt-6"
      >
        <div
          class="flex flex-col items-center justify-center py-16 text-center"
        >
          <div
            class="flex size-16 items-center justify-center rounded-full bg-muted"
          >
            <UIcon
              name="i-lucide-search-x"
              class="size-7 text-muted"
            />
          </div>

          <h3
            class="mt-5 text-xl font-semibold text-highlighted"
          >
            No books found
          </h3>

          <p class="mt-2 max-w-md text-sm text-muted">
            Try adjusting your search, category, or price filter.
          </p>

          <UButton
            class="mt-6"
            color="primary"
            @click="resetFilters"
          >
            Reset filters
          </UButton>
        </div>
      </UCard>

    </main>
  </div>
</template>
