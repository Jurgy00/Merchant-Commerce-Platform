<script setup lang="ts">
const { cartCount } = useCart()

const mobileMenuOpen = ref(false)

const searchQuery = useState('store-search', () => '')

const closeMobileMenu = () => {
  mobileMenuOpen.value = false
}
</script>

<template>
  <header class="sticky top-0 z-50 bg-default">
    <!-- Utility bar -->
    <div class="border-b border-default bg-elevated/30">
      <div class="mx-auto flex h-8 max-w-[1800px] items-center justify-between px-4 text-[9px] font-semibold uppercase tracking-[0.16em] text-muted sm:px-6 lg:px-8">
        <span>Curated books for curious minds</span>
        <span class="hidden sm:block">Kenya · Books delivered to you</span>
      </div>
    </div>

    <!-- Main header -->
    <div class="border-b border-default">
      <div class="mx-auto flex h-16 max-w-[1800px] items-center gap-5 px-4 sm:px-6 lg:px-8">
        <NuxtLink
          to="/"
          class="flex shrink-0 items-center gap-2.5"
          @click="closeMobileMenu"
        >
          <div class="flex size-9 items-center justify-center rounded-full bg-primary text-white">
            <UIcon
              name="i-lucide-book-open"
              class="size-4.5"
            />
          </div>

          <div class="hidden sm:block">
            <p class="text-sm font-black tracking-tight text-highlighted">
              BookStore
            </p>

            <p class="text-[9px] text-muted">
              Read. Learn. Grow.
            </p>
          </div>
        </NuxtLink>

        <nav class="hidden items-center gap-6 lg:flex">
          <NuxtLink
            to="/"
            class="text-xs font-bold uppercase tracking-wide text-highlighted hover:text-primary"
          >
            Books
          </NuxtLink>

          <NuxtLink
            to="/#products"
            class="text-xs font-bold uppercase tracking-wide text-muted hover:text-primary"
          >
            Collection
          </NuxtLink>
        </nav>

        <div class="ml-auto hidden max-w-xl flex-1 md:block">
          <UInput
            v-model="searchQuery"
            icon="i-lucide-search"
            placeholder="Search books, authors..."
            size="sm"
            class="w-full"
          />
        </div>

        <UButton
          color="neutral"
          variant="ghost"
          icon="i-lucide-search"
          class="md:hidden"
          aria-label="Search"
          @click="mobileMenuOpen = true"
        />

        <UButton
          to="/cart"
          color="neutral"
          variant="ghost"
          icon="i-lucide-shopping-bag"
          class="rounded-full"
          aria-label="Shopping bag"
        >
          <span class="hidden text-xs font-semibold sm:inline">
            Cart
          </span>

          <UBadge
            v-if="cartCount > 0"
            color="primary"
            variant="solid"
            size="sm"
          >
            {{ cartCount }}
          </UBadge>
        </UButton>

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
  </header>

  <USlideover
    v-model:open="mobileMenuOpen"
    title="BookStore"
    description="Browse the collection."
  >
    <template #body>
      <div class="space-y-6">
        <UInput
          v-model="searchQuery"
          icon="i-lucide-search"
          placeholder="Search books, authors..."
          class="w-full"
        />

        <nav class="flex flex-col gap-2">
          <UButton
            to="/"
            block
            color="neutral"
            variant="ghost"
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
            class="justify-start"
            @click="closeMobileMenu"
          >
            Shopping Bag
          </UButton>
        </nav>
      </div>
    </template>
  </USlideover>
</template>
