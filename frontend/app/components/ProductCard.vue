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
    title: 'Added to cart',
    description: `${product.name} was added to your cart.`,
    icon: 'i-lucide-check-circle'
  })
}
</script>

<template>
  <UCard
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
    <div class="p-4">
      <p
        class="text-xs font-semibold uppercase tracking-widest text-primary"
      >
        {{ product.category }}
      </p>

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
              {{ product.stock === 0 ? 'Out of stock' : 'In stock' }}
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
      size="sm"
      class="min-w-0 whitespace-nowrap px-2 text-xs"
    >
      Details
    </UButton>

    <UButton
      block
      size="sm"
      icon="i-lucide-shopping-cart"
      :disabled="product.stock === 0"
      class="min-w-0 whitespace-nowrap px-2 text-xs"
      @click="handleAddToCart(product)"
    >
      Add to Cart
    </UButton>
  </div>
</template>
  </UCard>
</template>
