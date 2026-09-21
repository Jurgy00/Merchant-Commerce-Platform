<script setup lang="ts">
const { cartCount } = useCart()

const mobileMenuOpen = ref(false)

const closeMobileMenu = () => {
  mobileMenuOpen.value = false
}
</script>

<template>
  <header class="sticky top-0 z-50 border-b border-default bg-default/90 backdrop-blur">
    <div
      class="mx-auto flex h-16 max-w-7xl items-center justify-between px-4 sm:px-6 lg:px-8"
    >
      <!-- Brand -->
      <NuxtLink
        to="/"
        class="group flex items-center gap-3"
        @click="closeMobileMenu"
      >
        <div
          class="flex size-10 items-center justify-center rounded-xl bg-primary text-white transition-transform duration-200 group-hover:scale-105"
        >
          <UIcon
            name="i-lucide-book-open"
            class="size-5"
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
      <div class="flex items-center gap-2">
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
  </header>

  <!-- Mobile navigation -->
  <USlideover
    v-model:open="mobileMenuOpen"
    title="Menu"
    description="Navigate around BookStore"
  >
    <template #body>
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
    </template>
  </USlideover>
</template>
