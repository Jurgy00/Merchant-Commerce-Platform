<script setup lang="ts">
const {
  cart,
  cartReady,
  cartCount,
  totalPrice,
  clearCart
} = useCart()

const {
  createOrder,
  initiatePayment,
  getPaymentsForOrder
} = useOrderApi()

const customerName = ref('')
const phoneNumber = ref('')
const deliveryAddress = ref('')
const optionalNotes = ref('')

const isSubmitting = ref(false)
const isCheckingPayment = ref(false)

const orderId = ref<number | null>(null)
const orderNumber = ref('')
const paymentStatus = ref<'PENDING' | 'SUCCESS' | 'FAILED' | null>(null)
const statusMessage = ref('')
const statusIsError = ref(false)

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

  if (cart.value.length === 0) {
    statusMessage.value = 'Your cart is empty. Add a book before checking out.'
    statusIsError.value = true
    return false
  }

  return !Object.values(errors.value).some(error => error)
}

const handleCheckout = async () => {
  if (isSubmitting.value || paymentStatus.value === 'SUCCESS') {
    return
  }

  if (!validateCheckout()) {
    return
  }

  isSubmitting.value = true
  statusMessage.value = ''
  statusIsError.value = false

  const phone = phoneNumber.value.replace(/\s+/g, '')

  try {
    // Create the order only if we do not already have its ID.
    if (orderId.value === null) {
      const order = await createOrder({
        customerName: customerName.value.trim(),
        phoneNumber: phone,
        deliveryLocation: deliveryAddress.value.trim(),
        notes: optionalNotes.value.trim(),
        merchantId: 1,
        items: cart.value.map(item => ({
          productId: item.product.id,
          quantity: item.quantity
        }))
      })

      orderId.value = order.id
      orderNumber.value = order.orderNumber
    }

    // A retry uses the existing order rather than creating another one.
    const payment = await initiatePayment({
      orderId: orderId.value,
      phoneNumber: phone
    })

    paymentStatus.value = payment.status

    if (payment.status === 'SUCCESS') {
      clearCart()
      statusMessage.value =
        `Payment confirmed for order ${orderNumber.value}. Thank you for your purchase!`
    } else if (payment.status === 'FAILED') {
      statusMessage.value =
        payment.resultDesc || 'The payment failed. Please check the details and try again.'
      statusIsError.value = true
    } else {
      statusMessage.value =
        `Order ${orderNumber.value} has been created. Check your phone for the M-Pesa prompt, then enter your PIN if you initiated this payment. Use "Check payment status" before retrying.`
    }
  } catch (error: unknown) {
    statusIsError.value = true

    const detail = error instanceof Error ? error.message : ''
    statusMessage.value = orderId.value === null
      ? `We couldn't confirm that your order was created. ${detail} Check your orders before trying again if the request may have reached the server.`
      : `Your order ${orderNumber.value} exists, but the payment request could not be confirmed. Check payment status before retrying. ${detail}`
  } finally {
    isSubmitting.value = false
  }
}

const checkPaymentStatus = async () => {
  if (orderId.value === null || isCheckingPayment.value) {
    return
  }

  isCheckingPayment.value = true
  statusIsError.value = false

  try {
    const payments = await getPaymentsForOrder(orderId.value)
    const latestPayment = payments[0]

    if (!latestPayment) {
      statusMessage.value =
        'No payment record was found yet. If you just requested payment, wait a moment and check again.'
      return
    }

    paymentStatus.value = latestPayment.status

    if (latestPayment.status === 'SUCCESS') {
      clearCart()
      statusMessage.value =
        `Payment confirmed for order ${orderNumber.value}. Thank you for your purchase!`
    } else if (latestPayment.status === 'FAILED') {
      statusMessage.value =
        latestPayment.resultDesc || 'The payment failed. You can retry payment for this order.'
      statusIsError.value = true
    } else {
      statusMessage.value =
        'Your payment is still pending. Complete the M-Pesa prompt on your phone, then check again.'
    }
  } catch (error: unknown) {
    const detail = error instanceof Error ? error.message : ''
    statusMessage.value =
      `We couldn't retrieve the payment status. Please try again. ${detail}`
    statusIsError.value = true
  } finally {
    isCheckingPayment.value = false
  }
}
</script>

<template>
  <main>
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

    <!-- Order and payment feedback -->
    <section
      v-if="statusMessage"
      class="mx-auto max-w-7xl px-4 pb-6 sm:px-6 lg:px-8"
      aria-live="polite"
    >
      <div
        class="rounded-2xl border px-5 py-4"
        :class="statusIsError
          ? 'border-error/30 bg-error/5 text-error'
          : 'border-primary/30 bg-primary/5 text-highlighted'"
      >
        <p class="font-medium">
          {{ statusMessage }}
        </p>

        <UButton
          v-if="orderId !== null && paymentStatus !== 'SUCCESS'"
          class="mt-3"
          color="neutral"
          variant="outline"
          :loading="isCheckingPayment"
          :disabled="isSubmitting || isCheckingPayment"
          @click="checkPaymentStatus"
        >
          Check payment status
        </UButton>
      </div>
    </section>

    <!-- Empty cart -->
    <section
      v-if="cartReady && cart.length === 0"
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
      v-else-if="cartReady"
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
              <div class="size-16 shrink-0 overflow-hidden rounded-xl bg-elevated/40">
                  <img
                    v-if="item.product.image"
                    :src="item.product.image"
                    :alt="`${item.product.name} book cover`"
                    loading="lazy"
                    class="size-full object-cover"
                  />
                  <div
                    v-else
                    class="flex size-full items-center justify-center"
                  >
                    <UIcon
                      name="i-lucide-book-open"
                      class="size-6 text-primary"
                    />
                  </div>
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
                <UButton
                  block
                  size="lg"
                  trailing-icon="i-lucide-smartphone"
                  :loading="isSubmitting"
                  :disabled="isSubmitting"
                  @click="handleCheckout"
                >
                  Pay with M-Pesa
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
        <!-- Cart initialization -->
    <section
      v-if="!cartReady"
      class="mx-auto flex min-h-[300px] max-w-7xl items-center justify-center gap-3 px-4 py-16 text-sm text-muted sm:px-6 lg:px-8"
    >
      <UIcon
        name="i-lucide-loader-circle"
        class="size-5 animate-spin"
      />
      Restoring your cart...
    </section>
  </main>
</template>
