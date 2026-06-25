<template>
  <div class="max-w-4xl mx-auto px-4 sm:px-6 py-8">

    <h1 class="text-2xl font-bold text-gray-900 dark:text-white mb-8">Your cart</h1>

    
    <div v-if="isEmpty" class="text-center py-20">
      <div class="text-6xl mb-4">🛍️</div>
      <h2 class="text-xl font-semibold text-gray-900 dark:text-white mb-2">Your cart is empty</h2>
      <p class="text-gray-500 mb-6">Find a book you love and add it here.</p>
      <UButton to="/" color="emerald" icon="i-heroicons-book-open">Browse books</UButton>
    </div>

    
    <div v-else class="grid grid-cols-1 lg:grid-cols-3 gap-8">

      
      <div class="lg:col-span-2 space-y-4">
        <div
          v-for="item in items"
          :key="item.product.id"
          class="bg-white dark:bg-gray-900 rounded-2xl border border-gray-100 dark:border-gray-800 p-4 flex gap-4"
        >
          
          <div
            class="w-16 h-20 flex-shrink-0 rounded-lg flex items-center justify-center text-2xl select-none"
            :class="getCoverBg(item.product.category.name)"
          >
            <img
              v-if="item.product.imageUrl"
              :src="item.product.imageUrl"
              :alt="item.product.name"
              class="w-full h-full object-cover rounded-lg"
            />
            <span v-else>{{ getCoverEmoji(item.product.category.name) }}</span>
          </div>

          
          <div class="flex-1 min-w-0">
            <NuxtLink :to="`/products/${item.product.id}`">
              <h3 class="font-semibold text-gray-900 dark:text-white text-sm hover:text-emerald-700 transition-colors line-clamp-1">
                {{ item.product.name }}
              </h3>
            </NuxtLink>
            <p class="text-xs text-gray-500 mt-0.5">{{ item.product.category.name }}</p>
            <p class="text-sm font-bold text-gray-900 dark:text-white mt-2">
              KES {{ item.product.price.toLocaleString('en-KE') }}
            </p>
          </div>

          
          <div class="flex flex-col items-end justify-between gap-2">
            <button
              class="text-xs text-red-400 hover:text-red-600 transition-colors"
              @click="removeItem(item.product.id)"
            >
              Remove
            </button>
            <div class="flex items-center border border-gray-200 dark:border-gray-700 rounded-lg overflow-hidden">
              <button
                class="px-2 py-1 text-gray-600 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-800 transition-colors text-sm"
                @click="updateQuantity(item.product.id, item.quantity - 1)"
              >−</button>
              <span class="px-2.5 py-1 text-sm font-semibold text-gray-900 dark:text-white min-w-[28px] text-center">
                {{ item.quantity }}
              </span>
              <button
                class="px-2 py-1 text-gray-600 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-800 transition-colors text-sm"
                @click="updateQuantity(item.product.id, item.quantity + 1)"
              >+</button>
            </div>
            <p class="text-sm font-bold text-gray-900 dark:text-white">
              KES {{ (item.product.price * item.quantity).toLocaleString('en-KE') }}
            </p>
          </div>

        </div>
      </div>

      
      <div class="lg:col-span-1">
        <div class="bg-white dark:bg-gray-900 rounded-2xl border border-gray-100 dark:border-gray-800 p-6 sticky top-24">
          <h2 class="font-semibold text-gray-900 dark:text-white mb-4">Order summary</h2>

          <div class="space-y-2 text-sm">
            <div v-for="item in items" :key="item.product.id" class="flex justify-between text-gray-600 dark:text-gray-400">
              <span class="truncate max-w-[160px]">{{ item.product.name }} ×{{ item.quantity }}</span>
              <span>KES {{ (item.product.price * item.quantity).toLocaleString('en-KE') }}</span>
            </div>
          </div>

          <div class="border-t border-gray-100 dark:border-gray-800 mt-4 pt-4 flex justify-between font-bold text-gray-900 dark:text-white">
            <span>Total</span>
            <span>{{ formattedTotal }}</span>
          </div>

          <p class="text-[11px] text-gray-400 mt-2 mb-4">
            Final total is confirmed at checkout. Delivery fees may apply.
          </p>

          <UButton
            to="/checkout"
            color="emerald"
            block
            size="lg"
            icon="i-heroicons-credit-card"
          >
            Proceed to checkout
          </UButton>

          <UButton
            to="/"
            variant="ghost"
            block
            class="mt-2"
            icon="i-heroicons-arrow-left"
          >
            Continue shopping
          </UButton>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
useHead({ title: 'Cart — Bookshelf' })

const { items, isEmpty, formattedTotal, removeItem, updateQuantity } = useCart()

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

const getCoverBg    = (cat: string) => CATEGORY_COLOURS[cat] ?? 'bg-gray-50 dark:bg-gray-800'
const getCoverEmoji = (cat: string) => CATEGORY_EMOJI[cat] ?? '📚'
</script>
