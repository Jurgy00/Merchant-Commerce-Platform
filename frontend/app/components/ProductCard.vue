<script setup lang="ts">
import { products } from '~/data/products'

type Product = (typeof products)[number]

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
  <article class="group min-w-0">
    <NuxtLink
      :to="`/products/${product.id}`"
      class="relative block overflow-hidden bg-elevated/40"
    >
      <div class="aspect-[2/3] w-full">
        <img
          :src="product.image"
          :alt="`${product.name} book cover`"
          loading="lazy"
          class="size-full object-cover transition-transform duration-500 group-hover:scale-[1.025]"
        />
      </div>

      <span
        v-if="product.badge"
        class="absolute left-2 top-2 bg-white/95 px-2 py-1 text-[8px] font-bold uppercase tracking-[0.12em] text-gray-900 shadow-sm"
      >
        {{ product.badge }}
      </span>

      <span
        v-if="product.stock <= 3"
        class="absolute right-2 top-2 bg-white/95 px-2 py-1 text-[8px] font-bold uppercase tracking-[0.12em] text-red-600 shadow-sm"
      >
        {{ product.stock === 0 ? 'Sold out' : `${product.stock} left` }}
      </span>

      <div class="absolute inset-x-0 bottom-0 translate-y-full bg-black/75 px-3 py-2.5 text-center backdrop-blur-sm transition-transform duration-300 group-hover:translate-y-0">
        <span class="text-[9px] font-bold uppercase tracking-[0.15em] text-white">
          View book
        </span>
      </div>
    </NuxtLink>

    <div class="pt-3">
      <p class="text-[9px] font-bold uppercase tracking-[0.16em] text-primary">
        {{ product.category }}
      </p>

      <NuxtLink
        :to="`/products/${product.id}`"
        class="mt-1 block"
      >
        <h3 class="line-clamp-2 text-sm font-semibold leading-5 text-highlighted group-hover:text-primary">
          {{ product.name }}
        </h3>
      </NuxtLink>

      <p class="mt-1 text-xs italic text-muted">
        {{ product.author }}
      </p>

      <div class="mt-2 flex items-center justify-between">
        <span class="text-sm font-bold text-highlighted">
          KES {{ product.price.toLocaleString() }}
        </span>

        <UButton
          icon="i-lucide-plus"
          size="xs"
          color="primary"
          :disabled="product.stock === 0"
          class="rounded-full"
          aria-label="Add to bag"
          @click="handleAddToCart(product)"
        />
      </div>
    </div>
  </article>
</template>
