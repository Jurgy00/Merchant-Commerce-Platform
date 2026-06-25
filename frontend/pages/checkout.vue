<template>
  <div class="max-w-4xl mx-auto px-4 sm:px-6 py-8">

    <div class="flex items-center gap-3 mb-8">
      <NuxtLink to="/cart" class="text-gray-400 hover:text-gray-600 transition-colors">
        <UIcon name="i-heroicons-arrow-left" class="w-5 h-5" />
      </NuxtLink>
      <h1 class="text-2xl font-bold text-gray-900 dark:text-white">Checkout</h1>
    </div>

    
    <div v-if="isEmpty" class="text-center py-20">
      <p class="text-gray-500 mb-4">Your cart is empty.</p>
      <UButton to="/" color="emerald">Browse books</UButton>
    </div>

    <div v-else class="grid grid-cols-1 lg:grid-cols-3 gap-8">

      
      <div class="lg:col-span-2">
        <div class="bg-white dark:bg-gray-900 rounded-2xl border border-gray-100 dark:border-gray-800 p-6">
          <h2 class="font-semibold text-gray-900 dark:text-white mb-6">Delivery details</h2>

          <div class="space-y-4">

            <UFormGroup label="Full name" required>
              <UInput
                v-model="form.customerName"
                placeholder="e.g. Griffins onsongo"
                icon="i-heroicons-user"
                :error="errors.customerName"
              />
              <template v-if="errors.customerName" #error>
                {{ errors.customerName }}
              </template>
            </UFormGroup>

            <UFormGroup label="M-Pesa phone number" required>
              <UInput
                v-model="form.customerPhone"
                placeholder="07XX XXX XXX"
                icon="i-heroicons-phone"
                :error="errors.customerPhone"
              />
              <template #hint>
                <span class="text-xs text-gray-400">We'll send the M-Pesa payment prompt to this number.</span>
              </template>
              <template v-if="errors.customerPhone" #error>
                {{ errors.customerPhone }}
              </template>
            </UFormGroup>

            <UFormGroup label="Delivery location" required>
              <UInput
                v-model="form.deliveryLocation"
                placeholder="e.g. Amara apartments Imara Daima, Nairobi "
                icon="i-heroicons-map-pin"
                :error="errors.deliveryLocation"
              />
              <template #hint>
                <span class="text-xs text-gray-400">Be as specific as possible so we can reach you.</span>
              </template>
              <template v-if="errors.deliveryLocation" #error>
                {{ errors.deliveryLocation }}
              </template>
            </UFormGroup>

            <UFormGroup label="Notes (optional)">
              <UTextarea
                v-model="form.notes"
                placeholder="Any special instructions for delivery?"
                :rows="3"
              />
            </UFormGroup>

          </div>
        </div>

        <!-- M-Pesa notice -->
        <div class="mt-4 bg-green-50 dark:bg-green-950 border border-green-200 dark:border-green-800 rounded-2xl p-4 flex gap-3">
          <span class="text-2xl flex-shrink-0">📱</span>
          <div>
            <p class="text-sm font-medium text-green-800 dark:text-green-200">Payment via M-Pesa</p>
            <p class="text-xs text-green-700 dark:text-green-300 mt-0.5 leading-relaxed">
              After placing your order, you'll receive an M-Pesa STK Push prompt on your phone.
              Enter your PIN to complete payment. Your order is confirmed once payment is received.
            </p>
          </div>
        </div>
      </div>

      <div class="lg:col-span-1">
        <div class="bg-white dark:bg-gray-900 rounded-2xl border border-gray-100 dark:border-gray-800 p-6 sticky top-24">
          <h2 class="font-semibold text-gray-900 dark:text-white mb-4">Order summary</h2>

          <div class="space-y-1.5 text-sm mb-4">
            <div
              v-for="item in items"
              :key="item.product.id"
              class="flex justify-between text-gray-600 dark:text-gray-400"
            >
              <span class="truncate max-w-[150px]">{{ item.product.name }} ×{{ item.quantity }}</span>
              <span class="flex-shrink-0 ml-2">KES {{ (item.product.price * item.quantity).toLocaleString('en-KE') }}</span>
            </div>
          </div>

          <div class="border-t border-gray-100 dark:border-gray-800 pt-4 flex justify-between font-bold text-gray-900 dark:text-white mb-1">
            <span>Total (estimate)</span>
            <span>{{ formattedTotal }}</span>
          </div>
          <p class="text-[11px] text-gray-400 mb-6">
            Backend calculates the confirmed total before payment is initiated.
          </p>

          <UButton
            color="emerald"
            block
            size="lg"
            icon="i-heroicons-device-phone-mobile"
            :loading="submitting"
            @click="placeOrder"
          >
            Place order and pay via M-Pesa
          </UButton>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import type { CheckoutForm } from '~/types'

useHead({ title: 'Checkout — Bookshelf' })

const { items, isEmpty, formattedTotal, clearCart } = useCart()
const router = useRouter()
const toast  = useToast()

const form = reactive<CheckoutForm>({
  customerName: '',
  customerPhone: '',
  deliveryLocation: '',
  notes: '',
})

const errors = reactive({
  customerName: '',
  customerPhone: '',
  deliveryLocation: '',
})

const submitting = ref(false)


const validate = (): boolean => {
  errors.customerName     = ''
  errors.customerPhone    = ''
  errors.deliveryLocation = ''
  let valid = true

  if (!form.customerName.trim() || form.customerName.trim().length < 3) {
    errors.customerName = 'Please enter your full name (at least 3 characters).'
    valid = false
  }

  const phoneRegex = /^(07|01)\d{8}$|^2547\d{8}$|^2541\d{8}$/
  const cleanPhone = form.customerPhone.replace(/\s+/g, '')
  if (!phoneRegex.test(cleanPhone)) {
    errors.customerPhone = 'Enter a valid Safaricom number, e.g. 0712345678.'
    valid = false
  }

  if (!form.deliveryLocation.trim() || form.deliveryLocation.trim().length < 5) {
    errors.deliveryLocation = 'Please provide a more specific delivery location.'
    valid = false
  }

  return valid
}


const placeOrder = async () => {
  if (!validate()) return

  submitting.value = true
  try {
    
    await new Promise(r => setTimeout(r, 1000))


    toast.add({
      title: 'Milestone 1 — UI complete!',
      description: 'Order placement will be wired to the backend in Milestone 6.',
      icon: 'i-heroicons-information-circle',
      color: 'blue',
      timeout: 4000,
    })
  } finally {
    submitting.value = false
  }
}
</script>
