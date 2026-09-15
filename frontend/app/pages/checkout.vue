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

  isSubmitting.value = true

  try {
    const orderData = {
      customerName: customerName.value,
      phoneNumber: phoneNumber.value,
      deliveryAddress: deliveryAddress.value,
      optionalNotes: optionalNotes.value,
      items: cart.value.map(item => ({
        productId: item.product.id,
        quantity: item.quantity
      }))
    }

    console.log(orderData)
  } finally {
    isSubmitting.value = false
  }
}
</script>

<template>
  <UPageSection
    title="Checkout"
    description="Review your order and provide your delivery details."
  >
    <UAlert
      v-if="cart.length === 0"
      title="Your cart is empty"
      description="Add a product before proceeding to checkout."
      icon="i-lucide-shopping-cart"
    >
      <template #actions>
        <UButton to="/">
          Browse Books
        </UButton>
      </template>
    </UAlert>

    <div
      v-else
      class="grid gap-8 lg:grid-cols-2"
    >
      <!-- Customer details -->
      <UCard>
        <template #header>
          <div>
            <p class="text-lg font-semibold">
              Customer Details
            </p>

            <p class="text-sm text-muted">
              Enter the information needed to complete your order.
            </p>
          </div>
        </template>

        <div class="space-y-5">
          <UFormField
              label="Full Name"
              name="name"
              :error="errors.customerName"
            >
            <UInput
              v-model="customerName"
              placeholder="John Doe"
            />
          </UFormField>

                    <UFormField
              label="M-Pesa Phone Number"
              name="phone"
              :error="errors.phoneNumber"
            >
            <UInput
              v-model="phoneNumber"
              type="tel"
              placeholder="0712345678"
            />
          </UFormField>
              <UFormField
                label="Delivery Address"
                name="address"
                :error="errors.deliveryAddress"
              >
            <UTextarea
              v-model="deliveryAddress"
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
              placeholder="e.g. Please call when you arrive..."
            />
          </UFormField>
        </div>
      </UCard>

      <!-- Order summary -->
      <UCard>
        <template #header>
          <div>
            <p class="text-lg font-semibold">
              Order Summary
            </p>

            <p class="text-sm text-muted">
              {{ cartCount }} item(s)
            </p>
          </div>
        </template>

        <div class="space-y-4">
          <div
            v-for="item in cart"
            :key="item.product.id"
            class="flex items-start justify-between gap-4"
          >
            <div>
              <p class="font-medium">
                {{ item.product.name }}
              </p>

              <p class="text-sm text-muted">
                {{ item.quantity }} × KES {{ item.product.price }}
              </p>
            </div>

            <p class="font-semibold">
              KES {{ item.product.price * item.quantity }}
            </p>
          </div>

          <div class="border-t pt-4">
            <div class="flex items-center justify-between">
              <span class="font-semibold">
                Total
              </span>

              <span class="text-xl font-bold">
                KES {{ totalPrice }}
              </span>
            </div>
          </div>
        </div>

        <template #footer>
              <div class="flex flex-col gap-3 sm:flex-row">
                <UButton
                  to="/cart"
                  color="neutral"
                  variant="outline"
                  leading-icon="i-lucide-arrow-left"
                >
                  Back to Cart
                </UButton>

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
              </div>
            </template>
      </UCard>
    </div>
  </UPageSection>
</template>
