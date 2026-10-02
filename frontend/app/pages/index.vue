<script setup lang="ts">
import { computed, ref } from 'vue'
import { products } from '~/data/products'

const categories = [
  'All',
  'Fiction',
  'Business',
  'Technology',
  'Self Development',
  'Children'
]

const searchQuery = ref('')
const selectedCategory = ref('All')
const maxPrice = ref(4000)
const sortBy = ref('default')

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

const resetFilters = () => {
  searchQuery.value = ''
  selectedCategory.value = 'All'
  maxPrice.value = 4000
  sortBy.value = 'default'
}
</script>

<template>
  <div>

    <!-- ================================
         INTRO
    ================================= -->
    <section
      class="relative overflow-hidden border-b border-default"
    >
      <div
        class="mx-auto flex min-h-[28vh] max-w-5xl items-center justify-center px-4 py-10 text-center sm:px-6 sm:py-12 lg:px-8"
      >
        <div class="max-w-3xl">

          <h1
            class="text-4xl font-black tracking-tight text-highlighted sm:text-5xl lg:text-6xl"
          >
            Find your next
            <span class="text-primary">
              great read.
            </span>
          </h1>

          <p
            class="mx-auto mt-4 max-w-2xl text-base leading-7 text-muted sm:text-lg"
          >
            Discover books across fiction, business, technology,
            self-development and more. Whether you're looking to learn,
            grow or simply escape into a good story, there's something
            waiting for you.
          </p>

          <div
            class="mt-6 flex flex-col justify-center gap-3 sm:flex-row"
          >
            <UButton
              to="#products"
              size="lg"
              trailing-icon="i-lucide-arrow-down"
              class="rounded-full px-7"
            >
              Browse Books
            </UButton>

            <UButton
              to="/cart"
              size="lg"
              color="neutral"
              variant="outline"
              icon="i-lucide-shopping-cart"
              class="rounded-full px-7"
            >
              View Cart
            </UButton>
          </div>

        </div>
      </div>
    </section>


    <!-- ================================
         PRODUCTS
    ================================= -->
          <main
        id="products"
        class="mx-auto max-w-7xl px-4 py-10 sm:px-6 lg:px-8"
      >

      <!-- Section heading -->
      <div
        class="flex flex-col gap-6 lg:flex-row lg:items-end lg:justify-between"
      >
        <div>
          <p
            class="text-sm font-semibold uppercase tracking-widest text-primary"
          >
            Our collection
          </p>

          <h2
            class="mt-2 text-3xl font-bold tracking-tight text-highlighted sm:text-4xl"
          >
            Our Books
          </h2>

          <p class="mt-3 max-w-xl text-muted">
            Explore our collection and find something you'll
            want to keep coming back to.
          </p>
        </div>

        <p class="text-sm text-muted">
          Showing
          <span class="font-semibold text-highlighted">
            {{ filteredProducts.length }}
          </span>
          {{ filteredProducts.length === 1 ? 'book' : 'books' }}
        </p>
      </div>

<!-- ================================
     FILTER BAR
================================= -->
<div class="mt-6 rounded-2xl border border-default bg-elevated/50 p-4">
  <div class="grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-6 xl:items-end">

    <!-- Search -->
    <div class="min-w-0 sm:col-span-2 xl:col-span-2">
      <label class="mb-2 block text-sm font-medium">
        Search books
      </label>

      <UInput
        v-model="searchQuery"
        placeholder="Search by title, category..."
        icon="i-lucide-search"
        class="w-full"
      />
    </div>

    <!-- Category -->
    <div class="min-w-0">
      <label class="mb-2 block text-sm font-medium">
        Category
      </label>

      <USelect
        v-model="selectedCategory"
        :items="categories"
        class="w-full"
      />
    </div>

    <!-- Maximum price -->
    <div class="min-w-0">
      <label class="mb-2 block text-sm font-medium">
        Maximum price
      </label>

      <UInput
        v-model="maxPrice"
        type="number"
        min="0"
        placeholder="4000"
        icon="i-lucide-tag"
        class="w-full"
      />
    </div>

    <!-- Sort -->
    <div class="min-w-0">
      <label class="mb-2 block text-sm font-medium">
        Sort by
      </label>

      <USelect
        v-model="sortBy"
        :items="[
          { label: 'Default', value: 'default' },
          { label: 'Price: Low to High', value: 'price-low' },
          { label: 'Price: High to Low', value: 'price-high' }
        ]"
        class="w-full"
      />
    </div>

    <!-- Reset -->
    <div class="flex items-end">
      <UButton
        color="neutral"
        variant="outline"
        icon="i-lucide-rotate-ccw"
        class="w-full justify-center sm:w-auto"
        @click="resetFilters"
      >
        Reset
      </UButton>
    </div>

  </div>
</div>

      <!-- ================================
           PRODUCT GRID
      ================================= -->
     <div
  v-if="filteredProducts.length > 0"
  class="mt-8 grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4"
>
      <ProductCard
        v-for="product in filteredProducts"
        :key="product.id"
        :product="product"
      />
</div>
      <!-- Empty state -->
      <UCard
        v-else
        class="mt-10"
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

          <p
            class="mt-2 max-w-md text-sm text-muted"
          >
            Try changing the category or increasing the maximum
            price to see more books.
          </p>

          <UButton
            class="mt-6"
            color="neutral"
            variant="outline"
            @click="resetFilters"
          >
            Reset Filters
          </UButton>
        </div>
      </UCard>

    </main>


    <!-- ================================
         BENEFITS
    ================================= -->
    <section
      class="border-y border-default bg-elevated/30"
    >
      <div
        class="mx-auto max-w-7xl px-4 py-10 sm:px-6 lg:px-8"
      >

        <div
          class="grid gap-6 md:grid-cols-3"
        >

          <div
            class="flex gap-4"
          >
            <div
              class="flex size-11 shrink-0 items-center justify-center rounded-xl bg-primary/10 text-primary"
            >
              <UIcon
                name="i-lucide-shield-check"
                class="size-5"
              />
            </div>

            <div>
              <h3 class="font-semibold text-highlighted">
                Secure checkout
              </h3>

              <p class="mt-1 text-sm leading-6 text-muted">
                A simple and secure way to complete your order.
              </p>
            </div>
          </div>


          <div
            class="flex gap-4"
          >
            <div
              class="flex size-11 shrink-0 items-center justify-center rounded-xl bg-primary/10 text-primary"
            >
              <UIcon
                name="i-lucide-smartphone"
                class="size-5"
              />
            </div>

            <div>
              <h3 class="font-semibold text-highlighted">
                Shop anywhere
              </h3>

              <p class="mt-1 text-sm leading-6 text-muted">
                Designed to work beautifully across phones,
                tablets and desktops.
              </p>
            </div>
          </div>


          <div
            class="flex gap-4"
          >
            <div
              class="flex size-11 shrink-0 items-center justify-center rounded-xl bg-primary/10 text-primary"
            >
              <UIcon
                name="i-lucide-book-open"
                class="size-5"
              />
            </div>

            <div>
              <h3 class="font-semibold text-highlighted">
                Something for everyone
              </h3>

              <p class="mt-1 text-sm leading-6 text-muted">
                Explore different genres, interests and ideas.
              </p>
            </div>
          </div>

        </div>

      </div>
    </section>
  </div>
</template>
