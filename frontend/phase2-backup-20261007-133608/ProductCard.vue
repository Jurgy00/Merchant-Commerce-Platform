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
      root: 'group overflow-hidden rounded-xl border-default/80 bg-default shadow-sm transition-all duration-300 hover:-translate-y-0.5 hover:border-primary/20 hover:shadow-lg',
      body: 'p-0',
      footer: 'p-3'
    }"
  >
    <!-- Book cover -->
    <NuxtLink
      :to="`/products/${product.id}`"
      class="block"
    >
      <div
        class="relative flex aspect-[4/3] items-center justify-center overflow-hidden bg-gradient-to-br from-primary/15 via-primary/5 to-default"
      >
        <div
          class="absolute -right-16 -top-16 size-40 rounded-full bg-primary/10 blur-2xl"
        />

        <div
          class="absolute -bottom-20 -left-20 size-48 rounded-full bg-primary/10 blur-3xl"
        />

        <!-- Cover -->
        <div
          class="relative flex h-[78%] w-[58%] flex-col justify-between overflow-hidden rounded-r-lg rounded-l-md border border-white/10 bg-default p-4 text-center shadow-xl transition-transform duration-300 group-hover:scale-[1.025]"
        >
          <div>
            <p
              class="text-[9px] font-bold uppercase tracking-[0.2em] text-primary"
            >
              {{ product.category }}
            </p>
          </div>

          <div>
            <UIcon
              name="i-lucide-book-open"
              class="mx-auto mb-2 size-7 text-primary/70"
            />

            <p
              class="text-base font-bold leading-tight text-highlighted"
            >
              {{ product.name }}
            </p>
          </div>

          <div>
            <div
              class="mx-auto mb-2 h-px w-8 bg-primary/40"
            />

            <p
              class="text-[8px] uppercase tracking-[0.2em] text-muted"
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
            class="translate-y-2 rounded-full bg-default/90 px-3 py-1.5 text-xs font-medium opacity-0 shadow-lg backdrop-blur transition-all duration-300 group-hover:translate-y-0 group-hover:opacity-100"
          >
            View book
          </span>
        </div>
      </div>
    </NuxtLink>

    <!-- Product information -->
    <div class="p-3.5">
      <p
        class="text-[10px] font-semibold uppercase tracking-widest text-primary"
      >
        {{ product.category }}
      </p>

      <NuxtLink
        :to="`/products/${product.id}`"
        class="mt-1.5 block"
      >
        <h3
          class="line-clamp-2 text-[15px] font-bold leading-tight text-highlighted transition-colors hover:text-primary"
        >
          {{ product.name }}
        </h3>
      </NuxtLink>

      <p
        class="mt-2 line-clamp-2 text-xs leading-5 text-muted"
      >
        {{ product.description }}
      </p>

      <!-- Price / stock -->
      <div
        class="mt-3 flex items-end justify-between gap-2"
      >
        <div>
          <p class="text-[17px] font-bold tracking-tight text-highlighted">
            KES {{ product.price }}
          </p>

          <p class="mt-0.5 text-[10px] text-muted">
            {{ product.stock === 0 ? 'Out of stock' : 'In stock' }}
          </p>
        </div>

        <div
          class="rounded-full px-2 py-0.5 text-[10px] font-medium"
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
