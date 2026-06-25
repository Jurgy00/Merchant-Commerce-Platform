<template>
  <div class="max-w-6xl mx-auto px-4 sm:px-6 py-8">

    <!-- Loading skeleton -->
    <div v-if="pending" class="animate-pulse">
      <div class="h-4 w-32 bg-gray-200 dark:bg-gray-700 rounded mb-6" />
      <div class="grid grid-cols-1 sm:grid-cols-2 gap-8">
        <div class="aspect-[3/4] bg-gray-200 dark:bg-gray-700 rounded-2xl" />
        <div class="space-y-4">
          <div class="h-8 bg-gray-200 dark:bg-gray-700 rounded w-3/4" />
          <div class="h-4 bg-gray-100 dark:bg-gray-800 rounded w-1/2" />
          <div class="h-24 bg-gray-100 dark:bg-gray-800 rounded" />
        </div>
      </div>
    </div>

    <!-- Error state -->
    <div v-else-if="error" class="text-center py-20">
      <div class="text-5xl mb-4">😔</div>
      <h2 class="text-xl font-semibold text-gray-900 dark:text-white mb-2">Book not found</h2>
      <p class="text-gray-500 mb-6">That page doesn't exist or the book was removed.</p>
      <UButton to="/" icon="i-heroicons-arrow-left">Back to store</UButton>
    </div>

    <!-- Product detail -->
    <template v-else-if="product">

      <!-- Breadcrumb -->
      <nav class="flex items-center gap-1.5 text-sm text-gray-500 mb-6">
        <NuxtLink to="/" class="hover:text-emerald-700 transition-colors">Home</NuxtLink>
        <span>/</span>
        <span>{{ product.category.name }}</span>
        <span>/</span>
        <span class="text-gray-900 dark:text-white font-medium truncate max-w-[200px]">{{ product.name }}</span>
      </nav>

      <div class="grid grid-cols-1 sm:grid-cols-2 gap-8 lg:gap-12">

        <!-- Book cover -->
        <div
          class="aspect-[3/4] rounded-2xl flex items-center justify-center text-7xl select-none"
          :class="coverBg"
        >
          <img
            v-if="product.imageUrl"
            :src="product.imageUrl"
            :alt="product.name"
            class="w-full h-full object-cover rounded-2xl"
          />
          <span v-else>{{ coverEmoji }}</span>
        </div>

        <!-- Info panel -->
        <div class="flex flex-col gap-4">

          <span class="text-sm font-medium text-emerald-700 dark:text-emerald-400 uppercase tracking-wide">
            {{ product.category.name }}
          </span>

          <h1 class="text-2xl sm:text-3xl font-bold text-gray-900 dark:text-white leading-tight">
            {{ product.name }}
          </h1>

          <p class="text-gray-600 dark:text-gray-300 leading-relaxed">
            {{ product.description }}
          </p>

          <!-- Price -->
          <div class="py-4 border-y border-gray-100 dark:border-gray-800">
            <p class="text-3xl font-bold text-gray-900 dark:text-white">
              KES {{ product.price.toLocaleString('en-KE') }}
            </p>
            <p class="text-sm mt-1" :class="product.stockQuantity > 0 ? 'text-emerald-600' : 'text-red-500'">
              {{ product.stockQuantity > 0 ? `✓ In stock (${product.stockQuantity} available)` : '✗ Out of stock' }}
            </p>
          </div>

          <!-- Quantity + Add to cart -->
          <div class="flex items-center gap-3">
            <div class="flex items-center border border-gray-200 dark:border-gray-700 rounded-lg overflow-hidden">
              <button
                class="px-3 py-2 text-gray-600 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-800 transition-colors"
                @click="qty = Math.max(1, qty - 1)"
              >−</button>
              <span class="px-4 py-2 text-sm font-semibold text-gray-900 dark:text-white min-w-[40px] text-center">{{ qty }}</span>
              <button
                class="px-3 py-2 text-gray-600 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-800 transition-colors"
                @click="qty = Math.min(product.stockQuantity, qty + 1)"
              >+</button>
            </div>

            <UButton
              class="flex-1"
              size="lg"
              color="emerald"
              icon="i-heroicons-shopping-bag"
              :disabled="product.stockQuantity === 0"
              :loading="adding"
              @click="handleAddToCart"
            >
              Add to cart · KES {{ (product.price * qty).toLocaleString('en-KE') }}
            </UButton>
          </div>

          <!-- Cart link -->
          <NuxtLink
            v-if="alreadyInCart"
            to="/cart"
            class="text-sm text-emerald-700 dark:text-emerald-400 hover:underline flex items-center gap-1"
          >
            <UIcon name="i-heroicons-shopping-bag" class="w-4 h-4" />
            Already in your cart — view cart
          </NuxtLink>

        </div>
      </div>

    </template>
  </div>
</template>

<script setup lang="ts">
import type { Product } from '~/types'

const route = useRoute()
const { fetchProduct } = useProducts()
const { addItem, items } = useCart()
const toast = useToast()

const product = ref<Product | null>(null)
const pending = ref(true)
const error   = ref(false)
const qty     = ref(1)
const adding  = ref(false)

onMounted(async () => {
  try {
    product.value = await fetchProduct(Number(route.params.id))
    useHead({ title: `${product.value.name} — Bookshelf` })
  } catch {
    error.value = true
  } finally {
    pending.value = false
  }
})

const alreadyInCart = computed(() =>
  items.value.some(i => i.product.id === product.value?.id)
)

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

const coverBg    = computed(() => product.value ? (CATEGORY_COLOURS[product.value.category.name] ?? 'bg-gray-50') : '')
const coverEmoji = computed(() => product.value ? (CATEGORY_EMOJI[product.value.category.name] ?? '📚') : '')

const handleAddToCart = async () => {
  if (!product.value) return
  adding.value = true
  await new Promise(r => setTimeout(r, 300))
  addItem(product.value, qty.value)
  toast.add({
    title: 'Added to cart',
    description: `${product.value.name} ×${qty.value}`,
    icon: 'i-heroicons-check-circle',
    color: 'emerald',
    timeout: 2500,
  })
  adding.value = false
}
</script>
