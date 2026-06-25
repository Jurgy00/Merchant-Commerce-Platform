<template>
  <!--
    ProductCard.vue
    ───────────────
    Displays a single product in the catalogue grid.

    Why a separate component?
    • Reusability — used on the homepage, search results, and category pages.
    • Single responsibility — knows how to display one product, nothing else.
    • Testability — easy to test in isolation.
    • Readability — keeps index.vue clean.
  -->
  <div
    class="bg-white dark:bg-gray-900 rounded-2xl border border-gray-100 dark:border-gray-800
           flex flex-col overflow-hidden hover:shadow-md transition-shadow duration-200"
  >
    <!-- Book cover / image -->
    <NuxtLink :to="`/products/${product.id}`" class="block">
      <div
  class="aspect-[3/4] w-full relative overflow-hidden"
  :class="coverBg"
>

  <img
    v-if="product.imageUrl"
    :src="product.imageUrl"
    :alt="product.name"
    class="absolute inset-0 w-full h-full object-cover"
  />

  <!-- fallback (ONLY when image missing) -->
  <div
    v-else
    class="w-full h-full flex items-center justify-center text-4xl"
  >
    {{ coverEmoji }}
  </div>

</div>

    </NuxtLink>

    <!-- Card body -->
    <div class="flex flex-col flex-1 p-4 gap-2">

      <!-- Category pill -->
      <span class="text-[11px] font-medium text-emerald-700 dark:text-emerald-400 uppercase tracking-wide">
        {{ product.category.name }}
      </span>

      <!-- Title -->
      <NuxtLink :to="`/products/${product.id}`">
        <h3 class="font-semibold text-gray-900 dark:text-white text-sm leading-snug line-clamp-2 hover:text-emerald-700 dark:hover:text-emerald-400 transition-colors">
          {{ product.name }}
        </h3>
      </NuxtLink>

      <!-- Description excerpt -->
      <p class="text-xs text-gray-500 dark:text-gray-400 line-clamp-2 leading-relaxed flex-1">
        {{ product.description }}
      </p>

      <!-- Price + Add to cart -->
      <div class="flex items-center justify-between mt-auto pt-3 border-t border-gray-100 dark:border-gray-800">
        <div>
          <p class="text-base font-bold text-gray-900 dark:text-white">
            KES {{ product.price.toLocaleString('en-KE') }}
          </p>
          <p class="text-[11px] text-gray-400">
            {{ product.stockQuantity > 0 ? `${product.stockQuantity} in stock` : 'Out of stock' }}
          </p>
        </div>

        <UButton
          icon="i-heroicons-shopping-bag"
          size="sm"
          color="emerald"
          :disabled="product.stockQuantity === 0"
          :loading="adding"
          @click="handleAddToCart"
        >
          Add
        </UButton>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import type { Product } from '~/types'
const props = defineProps<{
  product: Product
}>()

const { addItem } = useCart()
const toast = useToast()

const adding = ref(false)

// Each category gets a consistent pastel colour for the cover background
const CATEGORY_COLOURS: Record<string, string> = {
  'Fiction':     'bg-amber-50   dark:bg-amber-950',
  'Non-Fiction': 'bg-blue-50    dark:bg-blue-950',
  'Children':    'bg-pink-50    dark:bg-pink-950',
  'Textbooks':   'bg-violet-50  dark:bg-violet-950',
  'Biography':   'bg-teal-50    dark:bg-teal-950',
}

const CATEGORY_EMOJI: Record<string, string> = {
  'Fiction':     '📖',
  'Non-Fiction': '🔍',
  'Children':    '🌈',
  'Textbooks':   '🎓',
  'Biography':   '👤',
}

const coverBg = computed(
  () => CATEGORY_COLOURS[props.product.category.name] ?? 'bg-gray-50 dark:bg-gray-800'
)

const coverEmoji = computed(
  () => CATEGORY_EMOJI[props.product.category.name] ?? '📚'
)

const handleAddToCart = async () => {
  adding.value = true
  // Small artificial delay so the loading state is visible
  await new Promise(resolve => setTimeout(resolve, 300))
  addItem(props.product)
  toast.add({
    title: 'Added to cart',
    description: props.product.name,
    icon: 'i-heroicons-check-circle',
    color: 'emerald',
    timeout: 2500,
  })
  adding.value = false
}
</script>
