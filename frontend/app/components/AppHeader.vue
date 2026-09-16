<script setup lang="ts">
const { cartCount } = useCart()
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

      <!-- Navigation -->
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

      <!-- Cart -->
      <UButton
        to="/cart"
        color="neutral"
        variant="outline"
        icon="i-lucide-shopping-cart"
        class="rounded-full"
      >
        <span class="hidden sm:inline">
          Cart
        </span>

        <UBadge
          color="primary"
          variant="solid"
          size="sm"
        >
          {{ cartCount }}
        </UBadge>
      </UButton>
    </div>
  </header>
</template>
