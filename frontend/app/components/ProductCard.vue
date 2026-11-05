<script setup lang="ts">
import type { Product } from '~/types/product'

defineProps<{
  product: Product
}>()

const { addToCart } = useCart()
const toast = useToast()

const handleAddToCart = (product: Product) => {
  addToCart(product)

  toast.add({
    title: 'Added to bag',
    description: `${product.name} was added to your bag.`,
    icon: 'i-lucide-check'
  })
}
</script>

<template>
  <article
    class="group flex min-w-0 flex-col overflow-hidden border border-default bg-default transition-all duration-300 hover:-translate-y-1 hover:shadow-lg"
  >
    <!-- Book cover -->
    <NuxtLink
      :to="`/products/${product.id}`"
      class="relative block overflow-hidden bg-[#f3f1ed] dark:bg-[#252522]"
    >
      <div class="flex aspect-[2/3] items-center justify-center p-3 sm:p-4">
        <img
          :src="product.image"
          :alt="`${product.name} book cover`"
          loading="lazy"
          class="size-full object-contain transition-transform duration-500 group-hover:scale-[1.04]"
        />
      </div>

      <span
        v-if="product.badge"
        class="absolute left-2 top-2 bg-white px-2.5 py-1 text-[9px] font-bold uppercase tracking-wider text-gray-900 shadow-sm"
      >
        {{ product.badge }}
      </span>

      <span
        v-if="product.stock <= 3"
        class="absolute right-2 top-2 bg-white px-2.5 py-1 text-[9px] font-bold uppercase tracking-wider shadow-sm"
        :class="product.stock === 0 ? 'text-red-600' : 'text-orange-600'"
      >
        {{ product.stock === 0 ? 'Sold out' : `${product.stock} left` }}
      </span>

      <div
        class="absolute inset-x-0 bottom-0 bg-black/75 px-3 py-2 text-center opacity-0 backdrop-blur-sm transition-opacity duration-300 group-hover:opacity-100"
      >
        <span class="text-[10px] font-semibold uppercase tracking-[0.15em] text-white">
          View details
        </span>
      </div>
    </NuxtLink>

    <!-- Book information -->
    <div class="flex flex-1 flex-col p-3 sm:p-4">
      <p class="text-[9px] font-semibold uppercase tracking-[0.16em] text-muted">
        {{ product.category }}
      </p>

      <NuxtLink
        :to="`/products/${product.id}`"
        class="mt-1.5 block"
      >
        <h3 class="line-clamp-2 min-h-10 text-sm font-bold leading-5 text-highlighted transition-colors group-hover:text-primary">
          {{ product.name }}
        </h3>
      </NuxtLink>

      <p class="mt-1 truncate text-xs text-muted">
        {{ product.author }}
      </p>

      <div class="mt-auto pt-4">
        <div class="mb-3 border-t border-default pt-3">
          <p class="text-[9px] font-medium uppercase tracking-wider text-muted">
            Price
          </p>
          <p class="mt-0.5 text-base font-bold tracking-tight text-highlighted">
            KES {{ product.price.toLocaleString() }}
          </p>
        </div>

        <UButton
          block
          color="neutral"
          variant="solid"
          size="sm"
          icon="i-lucide-shopping-bag"
          :disabled="product.stock === 0"
          class="w-full cursor-pointer rounded-none font-semibold transition-all duration-200 hover:brightness-110 active:scale-[0.98]"
          @click="handleAddToCart(product)"
        >
          {{ product.stock === 0 ? 'Out of stock' : 'Add to bag' }}
        </UButton>
      </div>
    </div>
  </article>
</template>
