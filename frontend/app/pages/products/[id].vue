<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import type { Product } from '~/types/product'

const route = useRoute()
const { getProduct } = useProductApi()
const { addToCart, cart } = useCart()
const toast = useToast()

const product = ref<Product | null>(null)
const quantity = ref(1)
const isLoading = ref(true)
const errorMessage = ref('')

const loadProduct = async () => {
  const productId = Number(route.params.id)

  product.value = null
  quantity.value = 1
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

  const newQuantity = quantity.value + amount

  if (newQuantity < 1 || newQuantity > remainingStock.value) {
    return
  }

  quantity.value = newQuantity
}

const handleAddToCart = () => {
  if (!product.value || quantity.value > remainingStock.value) {
    return
  }

  for (let i = 0; i < quantity.value; i++) {
    addToCart(product.value)
  }

  toast.add({
    title: 'Added to cart',
    description: `${quantity.value} × ${product.value.name} added to your cart.`,
    icon: 'i-lucide-check-circle'
  })

  quantity.value = 1
}

const handleBuyNow = async () => {
  if (!product.value || quantity.value > remainingStock.value) {
    return
  }

  for (let i = 0; i < quantity.value; i++) {
    addToCart(product.value)
  }

  await navigateTo('/checkout')
}
</script>

<template>
  <main>

    <!-- =================================
         PRODUCT
    ================================== -->
    <section
      v-if="product"
      class="mx-auto max-w-7xl px-4 py-10 sm:px-6 sm:py-14 lg:px-8"
    >

      <!-- Back navigation -->
      <div class="mb-8">
        <UButton
          to="/#products"
          color="neutral"
          variant="ghost"
          icon="i-lucide-arrow-left"
        >
          Back to collection
        </UButton>
      </div>


      <!-- =================================
           MAIN PRODUCT AREA
      ================================== -->
      <div
        class="grid gap-10 lg:grid-cols-[minmax(0,0.9fr)_minmax(0,1.1fr)] lg:items-start lg:gap-16"
      >

        <!-- =================================
             BOOK COVER
        ================================== -->
        <div>
          <div class="flex min-h-[360px] items-center justify-center py-4 sm:min-h-[520px]">
            <img
              :src="product.image"
              :alt="`${product.name} book cover`"
              class="block max-h-[520px] w-auto max-w-[90%] rounded-r-lg rounded-l-sm object-contain shadow-xl"
            />
          </div>


          <!-- Availability beneath cover -->
          <div
            class="mt-4 flex items-center justify-center gap-2 text-sm text-muted"
          >
            <span
              class="size-2 rounded-full"
              :class="
                remainingStock > 0
                  ? 'bg-success'
                  : 'bg-error'
              "
            />

            <span>
              {{
                remainingStock > 0
                  ? `${remainingStock} available`
                  : 'Currently unavailable'
              }}
            </span>
          </div>
        </div>


        <!-- =================================
             PRODUCT INFORMATION
        ================================== -->
        <div>

          <!-- Category -->
          <div
            class="inline-flex items-center rounded-full bg-primary/10 px-3 py-1.5"
          >
            <span
              class="text-xs font-semibold uppercase tracking-widest text-primary"
            >
              {{ product.category }}
            </span>
          </div>


          <!-- Title -->
          <h1
            class="mt-5 text-4xl font-black tracking-tight text-highlighted sm:text-5xl"
          >
            {{ product.name }}
          </h1>


          <!-- Price -->
          <div
            class="mt-6 flex flex-wrap items-end gap-x-4 gap-y-2"
          >
            <p
              class="text-3xl font-bold text-highlighted sm:text-4xl"
            >
              KES {{ product.price }}
            </p>

            <span
              v-if="product.stock > 0"
              class="mb-1 text-sm text-success"
            >
              In stock
            </span>

            <span
              v-else
              class="mb-1 text-sm text-error"
            >
              Sold out
            </span>
          </div>


          <!-- Divider -->
          <div class="my-8 border-t border-default" />


          <!-- Short description -->
          <div>
            <h2
              class="text-sm font-semibold uppercase tracking-widest text-muted"
            >
              About this book
            </h2>

            <p
              class="mt-4 text-base leading-8 text-muted sm:text-lg"
            >
              {{ product.description }}
            </p>
          </div>


          <!-- =================================
               PURCHASE AREA
          ================================== -->
          <div
            class="mt-8 rounded-2xl border border-default bg-elevated/40 p-5 sm:p-6"
          >

            <div
              class="flex flex-col gap-5"
            >

              <!-- Quantity -->
              <div>
                <p
                  class="mb-3 text-sm font-medium"
                >
                  Quantity
                </p>

                <div
                  class="flex w-fit items-center rounded-xl border border-default bg-default"
                >

                  <UButton
                    icon="i-lucide-minus"
                    color="neutral"
                    variant="ghost"
                    aria-label="Decrease quantity"
                    :disabled="quantity <= 1"
                    @click="handleQuantityChange(-1)"
                  />

                  <span
                    class="min-w-12 text-center font-semibold"
                  >
                    {{ quantity }}
                  </span>

                  <UButton
                    icon="i-lucide-plus"
                    color="neutral"
                    variant="ghost"
                    aria-label="Increase quantity"
                    :disabled="
                      remainingStock <= 0 ||
                      quantity >= remainingStock
                    "
                    @click="handleQuantityChange(1)"
                  />

                </div>

                <p
                  v-if="remainingStock > 0"
                  class="mt-2 text-xs text-muted"
                >
                  {{ remainingStock }}
                  available
                </p>

              </div>


              <!-- Buttons -->
              <div
                class="grid gap-3 sm:grid-cols-2"
              >

                <UButton
                  size="lg"
                  block
                  icon="i-lucide-shopping-cart"
                  :disabled="remainingStock <= 0"
                  @click="handleAddToCart"
                >
                  Add to Cart
                </UButton>

                <UButton
                  size="lg"
                  block
                  color="neutral"
                  variant="outline"
                  trailing-icon="i-lucide-arrow-right"
                  :disabled="remainingStock <= 0"
                  @click="handleBuyNow"
                >
                  Buy Now
                </UButton>

              </div>

            </div>
          </div>


          <!-- Small reassurance -->
          <div
            class="mt-6 flex flex-col gap-3 text-sm text-muted sm:flex-row sm:gap-6"
          >
            <div class="flex items-center gap-2">
              <UIcon
                name="i-lucide-shield-check"
                class="size-4 text-primary"
              />
              Secure checkout
            </div>

            <div class="flex items-center gap-2">
              <UIcon
                name="i-lucide-smartphone"
                class="size-4 text-primary"
              />
              Mobile friendly
            </div>
          </div>

        </div>

      </div>
    </section>


    <!-- =================================
         PRODUCT DETAILS
    ================================== -->
    <section
      v-if="product"
      class="border-y border-default bg-elevated/30"
    >
      <div
        class="mx-auto max-w-7xl px-4 py-14 sm:px-6 lg:px-8"
      >

        <div
          class="mx-auto max-w-2xl"
        >

          <!-- Description -->
          <div>
            <p
              class="text-sm font-semibold uppercase tracking-widest text-primary"
            >
              The details
            </p>

            <h2
              class="mt-2 text-3xl font-bold tracking-tight text-highlighted"
            >
              About this book
            </h2>

            <p
              class="mt-6 max-w-3xl text-base leading-8 text-muted"
            >
              {{ product.description }}
            </p>
          </div>


          <!-- Product information -->
          <div>
            <p
              class="text-sm font-semibold uppercase tracking-widest text-primary"
            >
              Product information
            </p>

            <div
              class="mt-5 divide-y divide-default rounded-2xl border border-default bg-default"
            >

              <div
                class="flex items-center justify-between gap-4 px-5 py-4"
              >
                <span class="text-sm text-muted">
                  Category
                </span>

                <span class="text-sm font-semibold text-highlighted">
                  {{ product.category }}
                </span>
              </div>

              <div
                class="flex items-center justify-between gap-4 px-5 py-4"
              >
                <span class="text-sm text-muted">
                  Price
                </span>

                <span class="text-sm font-semibold text-highlighted">
                  KES {{ product.price }}
                </span>
              </div>

             <div
              class="flex items-center justify-between gap-4 px-5 py-4"
            >
              <span class="text-sm text-muted">
                Availability
              </span>

              <span
                class="text-sm font-semibold"
                :class="remainingStock > 0 ? 'text-success' : 'text-error'"
              >
                {{
                  remainingStock > 0
                    ? `${remainingStock} available`
                    : 'Currently unavailable'
                }}
              </span>
            </div>

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
          Back to Store
        </UButton>
      </div>
    </section>

  </main>
</template>
