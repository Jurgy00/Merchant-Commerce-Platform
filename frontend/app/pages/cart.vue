<script setup lang="ts">
const {
  cart,
  updateQuantity,
  clearCart,
  cartCount,
  totalPrice
} = useCart()
</script>

<template>
  <UPageSection
    title="Your Cart"
    :description="
      cartCount === 0
        ? 'Your selected products will appear here.'
        : `${cartCount} item(s) in your cart.`
    "
  >
    <!-- Empty cart -->
    <UAlert
      v-if="cart.length === 0"
      title="Your cart is empty"
      description="Add a book from our collection to get started."
      icon="i-lucide-shopping-cart"
    >
      <template #actions>
        <UButton
          to="/"
          icon="i-lucide-arrow-left"
        >
          Continue Shopping
        </UButton>
      </template>
    </UAlert>

    <!-- Cart with items -->
    <div
      v-else
      class="space-y-6"
    >
      <!-- Cart items -->
      <div class="space-y-4">
        <UCard
          v-for="item in cart"
          :key="item.product.id"
        >
          <div
            class="flex flex-col gap-5 lg:flex-row lg:items-center lg:justify-between"
          >
            <!-- Product -->
            <div class="min-w-0 flex-1">
              <p class="text-lg font-semibold">
                {{ item.product.name }}
              </p>

              <p class="mt-1 text-sm text-muted">
                {{ item.product.category }}
              </p>

              <p class="mt-2 text-sm text-muted">
                KES {{ item.product.price }} each
              </p>
            </div>

            <!-- Quantity -->
            <div>
              <p class="mb-2 text-sm font-medium">
                Quantity
              </p>

              <div class="flex items-center gap-3">
                <UButton
                  icon="i-lucide-minus"
                  color="neutral"
                  variant="outline"
                  aria-label="Decrease quantity"
                  @click="updateQuantity(item.product.id, -1)"
                />

                <span class="min-w-8 text-center font-semibold">
                  {{ item.quantity }}
                </span>

                <UButton
                  icon="i-lucide-plus"
                  color="neutral"
                  variant="outline"
                  aria-label="Increase quantity"
                  :disabled="item.quantity >= item.product.stock"
                  @click="updateQuantity(item.product.id, 1)"
                />
              </div>

              <p class="mt-2 text-xs text-muted">
                {{ item.quantity }} of {{ item.product.stock }} available
              </p>
            </div>

            <!-- Line total -->
            <div class="lg:min-w-32 lg:text-right">
              <p class="text-sm text-muted">
                Item total
              </p>

              <p class="text-xl font-bold">
                KES {{ item.product.price * item.quantity }}
              </p>
            </div>
          </div>
        </UCard>
      </div>

      <!-- Summary -->
      <UCard>
        <template #header>
          <div>
            <p class="text-lg font-semibold">
              Order Summary
            </p>

            <p class="text-sm text-muted">
              Review your order before checkout.
            </p>
          </div>
        </template>

        <div class="space-y-4">
          <div class="flex items-center justify-between">
            <span class="text-muted">
              Items
            </span>

            <span>
              {{ cartCount }}
            </span>
          </div>

          <div class="border-t pt-4">
            <div class="flex items-center justify-between">
              <span class="text-lg font-semibold">
                Subtotal
              </span>

              <span class="text-2xl font-bold">
                KES {{ totalPrice }}
              </span>
            </div>
          </div>
        </div>

        <template #footer>
          <div class="space-y-4">
            <!-- Cart management -->
            <UButton
              color="error"
              variant="outline"
              icon="i-lucide-trash-2"
              @click="clearCart"
            >
              Clear Cart
            </UButton>

            <!-- Navigation -->
            <div class="flex flex-col gap-3 sm:flex-row">
              <UButton
                to="/"
                color="neutral"
                variant="outline"
                leading-icon="i-lucide-arrow-left"
              >
                Continue Shopping
              </UButton>

              <UButton
                to="/checkout"
                block
                trailing-icon="i-lucide-arrow-right"
              >
                Continue to Checkout
              </UButton>
            </div>
          </div>
        </template>
      </UCard>
    </div>
  </UPageSection>
</template>
