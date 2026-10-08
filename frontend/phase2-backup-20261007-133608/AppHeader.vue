<script setup lang="ts">
const { cartCount } = useCart()

const {
  categories,
  searchQuery,
  selectedCategory,
  maxPrice,
  sortBy,
  resetFilters
} = useStoreFilters()

const mobileMenuOpen = ref(false)

const closeMobileMenu = () => {
  mobileMenuOpen.value = false
}
</script>

<template>
  <header
    class="sticky top-0 z-50 border-b border-default bg-default/95 backdrop-blur"
  >
    <div class="mx-auto max-w-[1800px] px-4 sm:px-6 lg:px-8">

      <!-- =================================
           MAIN HEADER
      ================================== -->
      <div class="flex h-14 items-center justify-between gap-4">
        <!-- Brand -->
        <NuxtLink
          to="/"
          class="group flex shrink-0 items-center gap-3"
          @click="closeMobileMenu"
        >
          <div
            class="flex size-9 items-center justify-center rounded-lg bg-primary text-white transition-transform duration-200 group-hover:scale-105"
          >
            <UIcon
              name="i-lucide-book-open"
              class="size-4.5"
            />
          </div>

          <div>
            <p class="text-lg font-bold tracking-tight text-highlighted">
              BookStore
            </p>

            <p class="hidden text-xs text-muted sm:block">
              Read. Learn. Grow.
            </p>
          </div>
        </NuxtLink>


        <!-- Desktop navigation -->
        <nav class="hidden items-center gap-1 md:flex">
          <UButton
            to="/"
            color="neutral"
            variant="ghost"
          >
            Books
          </UButton>

          <UButton
            to="/#products"
            color="neutral"
            variant="ghost"
          >
            Collection
          </UButton>
        </nav>


        <!-- Actions -->
        <div class="flex shrink-0 items-center gap-2">

          <!-- Desktop cart -->
          <UButton
            to="/cart"
            color="neutral"
            variant="outline"
            icon="i-lucide-shopping-cart"
            class="hidden rounded-full sm:flex"
          >
            Cart

            <UBadge
              color="primary"
              variant="solid"
              size="sm"
            >
              {{ cartCount }}
            </UBadge>
          </UButton>


          <!-- Mobile cart -->
          <UButton
            to="/cart"
            color="neutral"
            variant="outline"
            icon="i-lucide-shopping-cart"
            class="rounded-full sm:hidden"
            aria-label="Shopping cart"
          >
            <UBadge
              color="primary"
              variant="solid"
              size="sm"
            >
              {{ cartCount }}
            </UBadge>
          </UButton>


          <!-- Mobile menu -->
          <UButton
            color="neutral"
            variant="outline"
            icon="i-lucide-menu"
            class="rounded-full md:hidden"
            aria-label="Open navigation menu"
            @click="mobileMenuOpen = true"
          />
        </div>
      </div>


      <!-- =================================
           DESKTOP STORE FILTERS
      ================================== -->
      <div class="hidden border-t border-default py-2.5 md:block">
        <div class="grid grid-cols-12 items-end gap-3">

          <!-- Search -->
          <div class="col-span-5 min-w-0">
            <label class="mb-1 block text-xs font-medium text-muted">
              Search books
            </label>

            <UInput
              v-model="searchQuery"
              placeholder="Search by title, description or category..."
              icon="i-lucide-search"
              class="w-full"
            />
          </div>


          <!-- Category -->
          <div class="col-span-2 min-w-0">
            <label class="mb-1 block text-xs font-medium text-muted">
              Category
            </label>

            <USelect
              v-model="selectedCategory"
              :items="categories"
              class="w-full"
            />
          </div>


          <!-- Max price -->
          <div class="col-span-2 min-w-0">
            <label class="mb-1 block text-xs font-medium text-muted">
              Max price
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
          <div class="col-span-2 min-w-0">
            <label class="mb-1 block text-xs font-medium text-muted">
              Sort
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
          <div class="col-span-1">
            <UButton
              color="neutral"
              variant="outline"
              icon="i-lucide-rotate-ccw"
              class="w-full justify-center"
              @click="resetFilters"
            >
              Reset
            </UButton>
          </div>

        </div>
      </div>
    </div>
  </header>


  <!-- =================================
       MOBILE NAVIGATION + FILTERS
  ================================== -->
  <USlideover
    v-model:open="mobileMenuOpen"
    title="BookStore"
    description="Browse books and refine your search"
  >
    <template #body>
      <div class="space-y-7">

        <!-- Navigation -->
        <div>
          <p
            class="mb-3 text-xs font-semibold uppercase tracking-widest text-muted"
          >
            Navigation
          </p>

          <nav class="space-y-2">
            <UButton
              to="/"
              block
              color="neutral"
              variant="ghost"
              leading-icon="i-lucide-book-open"
              class="justify-start"
              @click="closeMobileMenu"
            >
              Books
            </UButton>

            <UButton
              to="/#products"
              block
              color="neutral"
              variant="ghost"
              leading-icon="i-lucide-library"
              class="justify-start"
              @click="closeMobileMenu"
            >
              Collection
            </UButton>

            <UButton
              to="/cart"
              block
              color="neutral"
              variant="ghost"
              leading-icon="i-lucide-shopping-cart"
              class="justify-start"
              @click="closeMobileMenu"
            >
              <span class="flex w-full items-center justify-between">
                <span>Cart</span>

                <UBadge
                  color="primary"
                  variant="solid"
                  size="sm"
                >
                  {{ cartCount }}
                </UBadge>
              </span>
            </UButton>
          </nav>
        </div>


        <!-- Filters -->
        <div class="border-t border-default pt-6">
          <div class="mb-4 flex items-center justify-between">
            <p
              class="text-xs font-semibold uppercase tracking-widest text-muted"
            >
              Filter books
            </p>

            <UButton
              color="neutral"
              variant="ghost"
              size="xs"
              icon="i-lucide-rotate-ccw"
              @click="resetFilters"
            >
              Reset
            </UButton>
          </div>

          <div class="space-y-4">

            <!-- Search -->
            <div>
              <label class="mb-1.5 block text-sm font-medium">
                Search
              </label>

              <UInput
                v-model="searchQuery"
                placeholder="Search books..."
                icon="i-lucide-search"
                class="w-full"
              />
            </div>


            <!-- Category -->
            <div>
              <label class="mb-1.5 block text-sm font-medium">
                Category
              </label>

              <USelect
                v-model="selectedCategory"
                :items="categories"
                class="w-full"
              />
            </div>


            <!-- Max price -->
            <div>
              <label class="mb-1.5 block text-sm font-medium">
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
            <div>
              <label class="mb-1.5 block text-sm font-medium">
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

          </div>
        </div>

      </div>
    </template>
  </USlideover>
</template>
