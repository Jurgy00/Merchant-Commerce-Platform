<script setup lang="ts">
import { computed, ref } from 'vue'
import { products } from '~/data/products'

type Product = (typeof products)[number]

const categories = [
  'All',
  'Fiction',
  'Business',
  'Technology',
  'Self Development',
  'Children'
]

const selectedCategory = ref('All')
const maxPrice = ref(4000)

const {
  addToCart,
  cartCount
} = useCart()

const toast = useToast()

const filteredProducts = computed(() => {
  return products.filter((product) => {
    const matchesCategory =
      selectedCategory.value === 'All' ||
      product.category === selectedCategory.value

    const matchesPrice =
      product.price <= maxPrice.value

    return matchesCategory && matchesPrice
  })
})

const handleAddToCart = (product: Product) => {
  addToCart(product)

  toast.add({
    title: 'Added to cart',
    description: `${product.name} was added to your cart.`,
    icon: 'i-lucide-check-circle'
  })
}

const resetFilters = () => {
  selectedCategory.value = 'All'
  maxPrice.value = 4000
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
        class="mx-auto flex min-h-[42vh] max-w-5xl items-center justify-center px-4 py-20 text-center sm:px-6 lg:px-8"
      >
        <div class="max-w-3xl">

          <div
            class="mb-6 inline-flex items-center gap-2 rounded-full border border-default bg-elevated px-4 py-2 text-sm text-muted"
          >
            <UIcon
              name="i-lucide-sparkles"
              class="size-4 text-primary"
            />

            <span>Curated books for curious minds</span>
          </div>

          <h1
            class="text-5xl font-black tracking-tight text-highlighted sm:text-6xl lg:text-7xl"
          >
            Find your next
            <span class="text-primary">
              great read.
            </span>
          </h1>

          <p
            class="mx-auto mt-6 max-w-2xl text-lg leading-8 text-muted sm:text-xl"
          >
            Discover books across fiction, business, technology,
            self-development and more. Whether you're looking to learn,
            grow or simply escape into a good story, there's something
            waiting for you.
          </p>

          <div
            class="mt-8 flex flex-col justify-center gap-3 sm:flex-row"
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
      class="mx-auto max-w-7xl px-4 py-16 sm:px-6 lg:px-8"
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
      <div
        class="mt-8 rounded-2xl border border-default bg-elevated/50 p-4"
      >
        <div
          class="flex flex-col gap-4 lg:flex-row lg:items-end"
        >

          <!-- Category -->
          <div class="flex-1">
            <label
              class="mb-2 block text-sm font-medium"
            >
              Category
            </label>

            <USelect
              v-model="selectedCategory"
              :items="categories"
              class="w-full"
            />
          </div>


          <!-- Price -->
          <div class="flex-1">
            <label
              class="mb-2 block text-sm font-medium"
            >
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


          <!-- Reset -->
          <UButton
            color="neutral"
            variant="outline"
            icon="i-lucide-rotate-ccw"
            class="lg:mb-0"
            @click="resetFilters"
          >
            Reset
          </UButton>

        </div>
      </div>


      <!-- ================================
           PRODUCT GRID
      ================================= -->
     <div
  v-if="filteredProducts.length > 0"
  class="mt-10 grid gap-6 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4"
>
  <UCard
    v-for="product in filteredProducts"
    :key="product.id"
    :ui="{
      root: 'group overflow-hidden transition-all duration-300 hover:-translate-y-1 hover:shadow-xl',
      body: 'p-0 sm:p-0',
      footer: 'p-5'
    }"
  >
    <!-- Book cover -->
    <NuxtLink
      :to="`/products/${product.id}`"
      class="block"
    >
      <div
        class="relative flex aspect-[3/4] items-center justify-center overflow-hidden bg-gradient-to-br from-primary/20 via-primary/5 to-default"
      >
        <!-- Decorative background -->
        <div
          class="absolute -right-16 -top-16 size-40 rounded-full bg-primary/10 blur-2xl"
        />

        <div
          class="absolute -bottom-20 -left-20 size-48 rounded-full bg-primary/10 blur-3xl"
        />

        <!-- Cover -->
        <div
          class="relative mx-8 flex h-[78%] w-[72%] flex-col justify-between overflow-hidden rounded-r-xl rounded-l-md border border-white/10 bg-default p-6 text-center shadow-2xl transition-transform duration-300 group-hover:scale-[1.03]"
        >
          <div>
            <p
              class="text-[10px] font-bold uppercase tracking-[0.25em] text-primary"
            >
              {{ product.category }}
            </p>
          </div>

          <div>
            <UIcon
              name="i-lucide-book-open"
              class="mx-auto mb-4 size-9 text-primary/70"
            />

            <p
              class="text-lg font-bold leading-tight text-highlighted"
            >
              {{ product.name }}
            </p>
          </div>

          <div>
            <div
              class="mx-auto mb-3 h-px w-10 bg-primary/40"
            />

            <p
              class="text-[9px] uppercase tracking-[0.2em] text-muted"
            >
              BookStore
            </p>
          </div>
        </div>

        <!-- View overlay -->
        <div
          class="absolute inset-0 flex items-center justify-center bg-black/0 transition-colors duration-300 group-hover:bg-black/10"
        >
          <span
            class="translate-y-2 rounded-full bg-default/90 px-4 py-2 text-sm font-medium opacity-0 shadow-lg backdrop-blur transition-all duration-300 group-hover:translate-y-0 group-hover:opacity-100"
          >
            View book
          </span>
        </div>
      </div>
    </NuxtLink>

    <!-- Product information -->
    <div class="p-5">

      <!-- Category -->
      <p
        class="text-xs font-semibold uppercase tracking-widest text-primary"
      >
        {{ product.category }}
      </p>

      <!-- Title -->
      <NuxtLink
        :to="`/products/${product.id}`"
        class="mt-2 block"
      >
        <h3
          class="line-clamp-2 text-lg font-bold leading-tight text-highlighted transition-colors hover:text-primary"
        >
          {{ product.name }}
        </h3>
      </NuxtLink>

      <!-- Description -->
      <p
        class="mt-3 line-clamp-2 text-sm leading-6 text-muted"
      >
        {{ product.description }}
      </p>

      <!-- Price / stock -->
      <div
        class="mt-5 flex items-end justify-between gap-3"
      >
        <div>
          <p class="text-xl font-bold text-highlighted">
            KES {{ product.price }}
          </p>

          <p class="mt-1 text-xs text-muted">
            In stock
          </p>
        </div>

        <div
          class="rounded-full px-2.5 py-1 text-xs font-medium"
          :class="
            product.stock <= 3
              ? 'bg-error/10 text-error'
              : 'bg-success/10 text-success'
          "
        >
          {{
            product.stock === 0
              ? 'Sold out'
              : product.stock <= 3
                ? `${product.stock} left`
                : `${product.stock} available`
          }}
        </div>
      </div>

    </div>

    <!-- Actions -->
    <template #footer>
      <div class="grid grid-cols-2 gap-2">

        <UButton
          :to="`/products/${product.id}`"
          color="neutral"
          variant="outline"
          block
        >
          View Details
        </UButton>

        <UButton
          block
          icon="i-lucide-shopping-cart"
          :disabled="product.stock === 0"
          @click="handleAddToCart(product)"
        >
          Add to Cart
        </UButton>

      </div>
    </template>
  </UCard>
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
        class="mx-auto max-w-7xl px-4 py-14 sm:px-6 lg:px-8"
      >

        <div
          class="grid gap-8 md:grid-cols-3"
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


    <!-- ================================
         FINAL CTA
    ================================= -->
    <section
      class="mx-auto max-w-5xl px-4 py-20 text-center sm:px-6 lg:px-8"
    >
      <div
        class="rounded-3xl border border-default bg-elevated px-6 py-14 sm:px-12"
      >

        <UIcon
          name="i-lucide-book-marked"
          class="mx-auto size-10 text-primary"
        />

        <h2
          class="mt-5 text-3xl font-bold tracking-tight text-highlighted sm:text-4xl"
        >
          Your next book is waiting.
        </h2>

        <p
          class="mx-auto mt-4 max-w-xl text-muted"
        >
          Take another look through the collection and find
          something worth adding to your shelf.
        </p>

        <div
          class="mt-7 flex flex-col justify-center gap-3 sm:flex-row"
        >
          <UButton
            to="#products"
            size="lg"
            icon="i-lucide-book-open"
            class="rounded-full px-7"
          >
            Browse Collection
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
    </section>

  </div>
</template>
