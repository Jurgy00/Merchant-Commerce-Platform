<script setup lang="ts">
const {
  cart,
  cartCount,
  totalPrice
} = useCart()

const customerName = ref('')
const phoneNumber = ref('')
const deliveryAddress = ref('')
const optionalNotes = ref('')

const isSubmitting = ref(false)

const errors = ref({
  customerName: '',
  phoneNumber: '',
  deliveryAddress: ''
})

const validateCheckout = () => {
  errors.value = {
    customerName: '',
    phoneNumber: '',
    deliveryAddress: ''
  }

  if (!customerName.value.trim()) {
    errors.value.customerName = 'Please enter your full name.'
  }

  const phone = phoneNumber.value.replace(/\s+/g, '')

  if (!/^(?:\+254|0)(?:7|1)\d{8}$/.test(phone)) {
    errors.value.phoneNumber =
      'Enter a valid Kenyan M-Pesa phone number.'
  }

  if (!deliveryAddress.value.trim()) {
    errors.value.deliveryAddress =
      'Please enter your delivery address.'
  }

  return !Object.values(errors.value).some(error => error)
}

const handleCheckout = async () => {
  const isValid = validateCheckout()

  if (!isValid) {
    return
  }

  if (cart.value.length === 0) {
    return
  }

  isSubmitting.value = true

  try {
    const config = useRuntimeConfig()

    const orderData = {
      customerName: customerName.value.trim(),
      phoneNumber: phoneNumber.value.replace(/\s+/g, ''),
      deliveryAddress: deliveryAddress.value.trim(),
      optionalNotes: optionalNotes.value.trim(),
      items: cart.value.map(item => ({
        productId: item.product.id,
        quantity: item.quantity
      }))
    }

    const response = await $fetch<OrderResponse>(
      '/api/storefront/orders',
      {
        baseURL: config.public.apiBase,
        method: 'POST',
        body: orderData
      }
    )

    orderConfirmation.value = response
    clearCart()
  } catch (error: unknown) {
    console.error('Order creation failed:', error)

    const fetchError = error as {
      data?: { message?: string; detail?: string; title?: string }
      message?: string
    }

    orderError.value =
      fetchError.data?.detail
      || fetchError.data?.message
      || fetchError.data?.title
      || fetchError.message
      || 'We could not create your order. Please try again.'
  } finally {
    isSubmitting.value = false
  }
}

interface OrderResponse {
  id: number
  orderReference: string
  customerName: string
  phoneNumber: string
  deliveryAddress: string
  optionalNotes: string | null
  totalAmount: number
  orderStatus: string
  paymentStatus: string
  createdAt: string
  items: {
    productId: number
    productName: string
    quantity: number
    unitPrice: number
    lineTotal: number
  }[]
}

const { clearCart } = useCart()
const orderConfirmation = ref<OrderResponse | null>(null)
const orderError = ref('')

</script>

<template>
  <main>
    <!-- Order confirmation -->
    <section
      v-if="orderConfirmation"
      class="mx-auto max-w-3xl px-4 py-16 sm:px-6 lg:px-8"
    >
      <UCard>
        <div class="py-6 text-center">
          <div class="mx-auto flex size-16 items-center justify-center rounded-full bg-primary/10">
            <UIcon
              name="i-lucide-check-circle"
              class="size-8 text-primary"
            />
          </div>

          <p class="mt-6 text-sm font-semibold uppercase tracking-widest text-primary">
            Order Created
          </p>

          <h1 class="mt-2 text-3xl font-black tracking-tight text-highlighted sm:text-4xl">
            Thank you for your order
          </h1>

          <p class="mt-4 text-muted">
            Your order has been saved. Payment has not been completed yet.
          </p>

          <div class="mx-auto mt-8 max-w-md rounded-2xl border border-default bg-elevated/30 p-5 text-left">
            <p class="text-sm text-muted">Order reference</p>
            <p class="mt-1 break-all text-xl font-bold text-highlighted">
              {{ orderConfirmation.orderReference }}
            </p>

            <div class="mt-5 flex items-center justify-between gap-4">
              <span class="text-sm text-muted">Order total</span>
              <span class="text-xl font-bold text-highlighted">
                KES {{ Number(orderConfirmation.totalAmount).toLocaleString('en-KE') }}
              </span>
            </div>

            <div class="mt-4 flex items-center justify-between gap-4">
              <span class="text-sm text-muted">Order status</span>
              <UBadge color="warning">
                {{ orderConfirmation.orderStatus }}
              </UBadge>
            </div>

            <div class="mt-3 flex items-center justify-between gap-4">
              <span class="text-sm text-muted">Payment status</span>
              <UBadge color="warning">
                {{ orderConfirmation.paymentStatus }}
              </UBadge>
            </div>
          </div>

          <div class="mt-8 flex flex-col justify-center gap-3 sm:flex-row">
            <UButton to="/" size="lg" leading-icon="i-lucide-book-open">
              Continue Browsing
            </UButton>
          </div>
        </div>
      </UCard>
    </section>

    <!-- Checkout -->
    <template v-else>
    <!-- Page heading -->
    <section class="mx-auto max-w-7xl px-4 pb-8 pt-10 sm:px-6 sm:pt-14 lg:px-8">
      <div class="max-w-2xl">
        <p class="text-sm font-semibold uppercase tracking-widest text-primary">
          Secure Checkout
        </p>

        <h1 class="mt-2 text-4xl font-black tracking-tight text-highlighted sm:text-5xl">
          Complete your order
        </h1>

        <p class="mt-4 text-base leading-7 text-muted">
          Enter your delivery details and review your order before payment.
        </p>
      </div>
    </section>

    <!-- Empty cart -->
    <section
      v-if="cart.length === 0"
      class="mx-auto max-w-7xl px-4 pb-16 sm:px-6 lg:px-8"
    >
      <div class="rounded-3xl border border-default bg-elevated/30 px-6 py-16 text-center">
        <div class="mx-auto flex size-16 items-center justify-center rounded-2xl bg-primary/10">
          <UIcon
            name="i-lucide-shopping-cart"
            class="size-8 text-primary"
          />
        </div>

        <h2 class="mt-6 text-2xl font-bold text-highlighted">
          Your cart is empty
        </h2>

        <p class="mx-auto mt-3 max-w-md text-muted">
          Add a book to your cart before proceeding to checkout.
        </p>

        <div class="mt-7">
          <UButton
            to="/"
            size="lg"
            leading-icon="i-lucide-arrow-left"
          >
            Browse Books
          </UButton>
        </div>
      </div>
    </section>

    <!-- Checkout -->
    <section
      v-else
      class="mx-auto max-w-7xl px-4 pb-16 sm:px-6 lg:px-8"
    >
      <div class="grid gap-8 lg:grid-cols-[minmax(0,1fr)_380px] lg:items-start">
        <!-- Customer details -->
        <div>
          <UCard>
            <template #header>
              <div class="flex items-start gap-4">
                <div class="flex size-10 shrink-0 items-center justify-center rounded-xl bg-primary/10">
                  <UIcon
                    name="i-lucide-map-pin"
                    class="size-5 text-primary"
                  />
                </div>

                <div>
                  <p class="text-lg font-semibold text-highlighted">
                    Delivery Details
                  </p>

                  <p class="mt-1 text-sm text-muted">
                    Enter the information needed to deliver your order.
                  </p>
                </div>
              </div>
            </template>

            <div class="space-y-6">
              <UFormField
                label="Full Name"
                name="name"
                :error="errors.customerName"
              >
                <UInput
                  v-model="customerName"
                  size="lg"
                  placeholder="John Doe"
                  icon="i-lucide-user"
                />
              </UFormField>

              <UFormField
                label="M-Pesa Phone Number"
                name="phone"
                :error="errors.phoneNumber"
                description="Use the Kenyan number registered with M-Pesa."
              >
                <UInput
                  v-model="phoneNumber"
                  type="tel"
                  size="lg"
                  placeholder="0712345678"
                  icon="i-lucide-smartphone"
                />
              </UFormField>

              <UFormField
                label="Delivery Address"
                name="address"
                :error="errors.deliveryAddress"
              >
                <UTextarea
                  v-model="deliveryAddress"
                  size="lg"
                  :rows="4"
                  placeholder="Enter your delivery address"
                />
              </UFormField>

              <UFormField
                label="Optional Notes"
                name="notes"
                description="Any additional instructions for your order."
              >
                <UTextarea
                  v-model="optionalNotes"
                  size="lg"
                  :rows="3"
                  placeholder="e.g. Please call when you arrive..."
                />
              </UFormField>
            </div>
          </UCard>

          <div class="mt-5 flex items-center gap-3 rounded-2xl border border-default bg-elevated/30 p-4">
            <UIcon
              name="i-lucide-shield-check"
              class="size-5 shrink-0 text-primary"
            />

            <p class="text-sm text-muted">
              Your delivery information is used to process and deliver your order.
            </p>
          </div>
        </div>

        <!-- Order summary -->
        <aside class="lg:sticky lg:top-24">
          <UCard>
            <template #header>
              <div>
                <p class="text-lg font-semibold text-highlighted">
                  Order Summary
                </p>

                <p class="mt-1 text-sm text-muted">
                  {{ cartCount }} item(s) in your order
                </p>
              </div>
            </template>

            <div class="space-y-5">
              <div
                v-for="item in cart"
                :key="item.product.id"
                class="flex gap-4"
              >
                <div
                  class="size-14 shrink-0 overflow-hidden rounded-lg bg-elevated"
                >
                  <img
                    :src="item.product.image"
                    :alt="`${item.product.name} book cover`"
                    class="size-full object-cover"
                  />
                </div>

                <div class="min-w-0 flex-1">
                  <p class="font-medium text-highlighted">
                    {{ item.product.name }}
                  </p>

                  <p class="mt-1 text-sm text-muted">
                    {{ item.quantity }} × KES {{ item.product.price }}
                  </p>
                </div>

                <p class="shrink-0 font-semibold text-highlighted">
                  KES {{ item.product.price * item.quantity }}
                </p>
              </div>

              <div class="border-t border-default pt-5">
                <div class="flex items-center justify-between">
                  <span class="text-sm text-muted">
                    Items
                  </span>

                  <span class="text-sm font-medium">
                    {{ cartCount }}
                  </span>
                </div>

                <div class="mt-4 flex items-end justify-between gap-4">
                  <span class="font-semibold">
                    Total
                  </span>

                  <span class="text-2xl font-bold text-highlighted">
                    KES {{ totalPrice }}
                  </span>
                </div>
              </div>
            </div>

            <template #footer>
              <div class="space-y-3">
                <UAlert
                  v-if="orderError"
                  color="error"
                  variant="soft"
                  title="Order could not be created"
                  :description="orderError"
                  icon="i-lucide-circle-alert"
                  class="mb-3"
                />

                <UButton
                  block
                  size="lg"
                  trailing-icon="i-lucide-arrow-right"
                  :loading="isSubmitting"
                  :disabled="isSubmitting || cart.length === 0"
                  @click="handleCheckout"
                >
                  {{ isSubmitting ? 'Creating Order...' : 'Place Order' }}
                </UButton>

                <UButton
                  to="/cart"
                  color="neutral"
                  variant="outline"
                  block
                  leading-icon="i-lucide-arrow-left"
                >
                  Back to Cart
                </UButton>
              </div>
            </template>
          </UCard>
        </aside>
      </div>
    </section>
    </template>
  </main>
</template>
