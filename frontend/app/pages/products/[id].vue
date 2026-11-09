
<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import type { Product } from '~/types/product'

const route = useRoute()
const { getProduct } = useProductApi()
const { addToCart, updateQuantity, cart } = useCart()
const toast = useToast()

const product = ref<Product | null>(null)
const isLoading = ref(true)
const errorMessage = ref('')

const loadProduct = async () => {
  const productId = Number(route.params.id)

  product.value = null
  isLoading.value = true
  errorMessage.value = ''

  if (!Number.isInteger(productId) || productId <= 0) {
    isLoading.value = false
    return
  }

  try {
    product.value = await getProduct(productId)
  } catch (error) {
    console.error('Failed to load product:', error)

    const fetchError = error as {
      status?: number
      statusCode?: number
    }

    if ((fetchError.statusCode ?? fetchError.status) !== 404) {
      errorMessage.value =
        'We could not load this book. Check your connection and try again.'
    }
  } finally {
    isLoading.value = false
  }
}

onMounted(loadProduct)

watch(
  () => route.params.id,
  () => {
    void loadProduct()
  }
)

const cartQuantity = computed(() => {
  if (!product.value) {
    return 0
  }

  return cart.value.find(
    item => item.product.id === product.value?.id
  )?.quantity ?? 0
})

const remainingStock = computed(() => {
  if (!product.value) {
    return 0
  }

  return Math.max(0, product.value.stock - cartQuantity.value)
})

const handleQuantityChange = (amount: number) => {
  if (!product.value) {
    return
  }

  const currentQuantity = cartQuantity.value
  const newQuantity = currentQuantity + amount

  if (newQuantity < 0 || newQuantity > product.value.stock) {
    return
  }

  if (currentQuantity === 0 && newQuantity > 0) {
    addToCart(product.value)
    return
  }

  if (currentQuantity > 0) {
    updateQuantity(product.value.id, amount)
  }
}

const handleAddToCart = () => {
  if (!product.value || remainingStock.value <= 0) {
    return
  }

  addToCart(product.value)

  toast.add({
    title: 'Added to cart',
    description: `${product.value.name} added to your bag.`,
    icon: 'i-lucide-check-circle'
  })
}

const handleBuyNow = async () => {
  if (!product.value) {
    return
  }

  if (cartQuantity.value === 0) {
    if (product.value.stock <= 0) {
      return
    }

    addToCart(product.value)
  }

  await navigateTo('/checkout')
}
</script>

<template>
  <main>
    <!-- Main product section -->
    <section
      v-if="product"
      class="mx-auto max-w-7xl px-4 py-8 sm:px-6 sm:py-12 lg:px-8"
    >
      <!-- Back navigation -->
      <div class="mb-8">
        <UButton
          to="/#products"
          color="neutral"
          variant="ghost"
          size="sm"
          icon="i-lucide-arrow-left"
        >
          Back to collection
        </UButton>
      </div>

      <div class="grid gap-7 lg:grid-cols-[0.55fr_1.05fr] lg:gap-16">
        <!-- Book cover -->
        <div>
          <div class="flex min-h-[420px] items-center justify-center border border-default bg-[#f3f1ed] p-8 dark:bg-[#252522] sm:min-h-[560px]">
            <img
              :src="product.image"
              :alt="`${product.name} book cover`"
              class="max-h-[500px] max-w-full object-contain shadow-2xl"
            />
          </div>

          <div class="mt-4 flex items-center justify-center gap-2 text-sm text-muted">
            <span
              class="size-2 rounded-full"
              :class="remainingStock > 0 ? 'bg-success' : 'bg-error'"
            />

            {{ remainingStock > 0
              ? `${remainingStock} available`
              : 'Currently unavailable'
            }}
          </div>
        </div>

        <!-- Product information -->
        <div class="flex flex-col">
          <!-- Category -->
          <p class="text-xs font-bold uppercase tracking-[0.2em] text-primary">
            {{ product.category }}
          </p>

          <!-- Title -->
          <h1 class="mt-4 text-3xl font-black leading-tight tracking-tight text-highlighted sm:text-4xl lg:text-5xl">
            {{ product.name }}
          </h1>

          <!-- Author -->
          <p class="mt-3 text-base text-muted">
            By {{ product.author }}
          </p>

          <!-- Price -->
          <div class="mt-7">
            <p class="text-xs font-semibold uppercase tracking-wider text-muted">
              Price
            </p>

            <p class="mt-1 text-3xl font-bold tracking-tight text-highlighted">
              KES {{ product.price.toLocaleString() }}
            </p>
          </div>

          <div class="my-7 border-t border-default" />

          <!-- Description -->
          <div>
            <h2 class="text-sm font-bold uppercase tracking-wider text-highlighted">
              About this book
            </h2>

            <p class="mt-3 text-sm leading-7 text-muted sm:text-base">
              {{ product.description }}
            </p>
          </div>

          <!-- Purchase area -->
          <div class="mt-8 border border-default bg-default p-5 sm:p-6">
            <div class="flex flex-wrap items-center justify-between gap-3">
              <p class="text-sm font-semibold text-highlighted">
                Availability
              </p>

              <span
                class="text-xs font-semibold"
                :class="remainingStock > 0 ? 'text-success' : 'text-error'"
              >
                {{ remainingStock > 0
                  ? `${remainingStock} in stock`
                  : 'Out of stock'
                }}
              </span>
            </div>

            <div class="mt-5 flex items-center justify-between gap-4">
              <div>
                <p class="text-sm font-semibold text-highlighted">
                  Quantity
                </p>

                <p class="mt-1 text-xs text-muted">
                  {{ cartQuantity > 0
                    ? `${cartQuantity} ${cartQuantity === 1 ? 'copy' : 'copies'} already in your bag`
                    : 'Your bag is empty for this book'
                  }}
                </p>
              </div>

              <div class="flex items-center border border-default">
                <UButton
                  icon="i-lucide-minus"
                  color="neutral"
                  variant="ghost"
                  square
                  aria-label="Decrease quantity"
                  :disabled="cartQuantity <= 0"
                  @click="handleQuantityChange(-1)"
                />

                <span class="min-w-10 text-center text-sm font-semibold text-highlighted">
                  {{ cartQuantity }}
                </span>

                <UButton
                  icon="i-lucide-plus"
                  color="neutral"
                  variant="ghost"
                  square
                  aria-label="Increase quantity"
                  :disabled="remainingStock <= 0"
                  @click="handleQuantityChange(1)"
                />
              </div>
            </div>

            <div class="mt-6 grid gap-3 sm:grid-cols-2">
              <UButton
                block
                size="lg"
                color="neutral"
                variant="solid"
                icon="i-lucide-shopping-bag"
                :disabled="remainingStock <= 0"
                class="w-full cursor-pointer rounded-none font-semibold transition-all duration-200 hover:brightness-110 active:scale-[0.98]"
                @click="handleAddToCart"
              >
                Add to bag
              </UButton>

              <UButton
                block
                size="lg"
                color="neutral"
                variant="outline"
                trailing-icon="i-lucide-arrow-right"
                :disabled="cartQuantity <= 0 && product.stock <= 0"
                class="rounded-none font-semibold"
                @click="handleBuyNow"
              >
                Buy now
              </UButton>
            </div>
          </div>

          <!-- Reassurance -->
          <div class="mt-6 flex flex-wrap gap-x-6 gap-y-3 text-xs text-muted">
            <div class="flex items-center gap-2">
              <UIcon name="i-lucide-shield-check" class="size-4" />
              Secure checkout
            </div>

            <div class="flex items-center gap-2">
              <UIcon name="i-lucide-book-open" class="size-4" />
              Discover your next read
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Additional product information -->
    <section
      v-if="product"
      class="border-t border-default bg-elevated/30"
    >
      <div class="mx-auto max-w-7xl px-4 py-12 sm:px-6 lg:px-8">
        <div class="max-w-3xl">
          <p class="text-xs font-bold uppercase tracking-[0.2em] text-primary">
            More information
          </p>

          <h2 class="mt-2 text-2xl font-bold tracking-tight text-highlighted">
            Book details
          </h2>

          <div class="mt-6 divide-y divide-default border-y border-default">
            <div class="flex items-center justify-between gap-4 py-4">
              <span class="text-sm text-muted">
                Book title
              </span>

              <span class="text-right text-sm font-semibold text-highlighted">
                {{ product.name }}
              </span>
            </div>

            <div class="flex items-center justify-between gap-4 py-4">
              <span class="text-sm text-muted">
                Author
              </span>

              <span class="text-right text-sm font-semibold text-highlighted">
                {{ product.author }}
              </span>
            </div>

            <div class="flex items-center justify-between gap-4 py-4">
              <span class="text-sm text-muted">
                Category
              </span>

              <span class="text-right text-sm font-semibold text-highlighted">
                {{ product.category }}
              </span>
            </div>

            <div class="flex items-center justify-between gap-4 py-4">
              <span class="text-sm text-muted">
                Availability
              </span>

              <span
                class="text-right text-sm font-semibold"
                :class="remainingStock > 0 ? 'text-success' : 'text-error'"
              >
                {{ remainingStock > 0
                  ? `${remainingStock} available`
                  : 'Currently unavailable'
                }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Loading state -->
    <section
      v-else-if="isLoading"
      class="mx-auto flex min-h-[360px] max-w-2xl items-center justify-center gap-3 px-4 py-20 text-sm text-muted sm:px-6 lg:px-8"
    >
      <UIcon
        name="i-lucide-loader-circle"
        class="size-5 animate-spin"
      />
      Loading book details...
    </section>

    <!-- Missing product or API error -->
    <section
      v-else
      class="mx-auto max-w-2xl px-4 py-20 sm:px-6 lg:px-8"
    >
      <UAlert
        :title="errorMessage ? 'Unable to load book' : 'Product not found'"
        :description="errorMessage || 'The product you are looking for does not exist.'"
        :icon="errorMessage ? 'i-lucide-wifi-off' : 'i-lucide-circle-alert'"
      />

      <div class="mt-6 flex justify-center gap-3">
        <UButton
          v-if="errorMessage"
          color="primary"
          @click="loadProduct"
        >
          Try again
        </UButton>

        <UButton
          to="/"
          color="neutral"
          variant="outline"
        >
          Back to store
        </UButton>
      </div>
    </section>
  </main>
</template>
