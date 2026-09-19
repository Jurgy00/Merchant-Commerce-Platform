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
  <main>
    <!-- Page heading -->
    <section class="mx-auto max-w-7xl px-4 pb-8 pt-10 sm:px-6 sm:pt-14 lg:px-8">
      <div class="max-w-2xl">
        <p class="text-sm font-semibold uppercase tracking-widest text-primary">
          Shopping Cart
        </p>

        <h1 class="mt-2 text-4xl font-black tracking-tight text-highlighted sm:text-5xl">
          Your cart
        </h1>

        <p class="mt-4 text-base leading-7 text-muted">
          Review your selected books before continuing to checkout.
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
          You haven't added any books yet. Explore our collection and find your next great read.
        </p>

        <div class="mt-7">
          <UButton
            to="/"
            size="lg"
            leading-icon="i-lucide-arrow-left"
          >
            Continue Shopping
          </UButton>
        </div>
      </div>
    </section>

    <!-- Cart with items -->
    <section
      v-else
      class="mx-auto max-w-7xl px-4 pb-16 sm:px-6 lg:px-8"
    >
      <div class="grid gap-8 lg:grid-cols-[minmax(0,1fr)_360px] lg:items-start">
        <!-- Cart items -->
        <div>
          <div class="mb-4 flex items-center justify-between">
            <div>
              <h2 class="text-lg font-semibold text-highlighted">
                Your items
              </h2>

              <p class="mt-1 text-sm text-muted">
                {{ cartCount }} item(s) selected
              </p>
            </div>
          </div>

          <div class="space-y-4">
            <UCard
              v-for="item in cart"
              :key="item.product.id"
              class="overflow-hidden"
            >
              <div class="flex flex-col gap-5 sm:flex-row sm:items-center">
                <!-- Book visual -->
                <div
                  class="flex size-20 shrink-0 items-center justify-center rounded-2xl bg-primary/10"
                >
                  <UIcon
                    name="i-lucide-book-open"
                    class="size-8 text-primary"
                  />
                </div>

                <!-- Product -->
                <div class="min-w-0 flex-1">
                  <p class="text-lg font-semibold text-highlighted">
                    {{ item.product.name }}
                  </p>

                  <p class="mt-1 text-sm font-medium text-primary">
                    {{ item.product.category }}
                  </p>

                  <p class="mt-2 text-sm text-muted">
                    KES {{ item.product.price }} each
                  </p>
                </div>

                <!-- Quantity -->
                <div class="sm:text-center">
                  <p class="mb-2 text-xs font-semibold uppercase tracking-wider text-muted">
                    Quantity
                  </p>

                  <div class="flex items-center rounded-xl border border-default bg-elevated/40">
                    <UButton
                      icon="i-lucide-minus"
                      color="neutral"
                      variant="ghost"
                      aria-label="Decrease quantity"
                      @click="updateQuantity(item.product.id, -1)"
                    />

                    <span class="min-w-10 text-center font-semibold">
                      {{ item.quantity }}
                    </span>

                    <UButton
                      icon="i-lucide-plus"
                      color="neutral"
                      variant="ghost"
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
                <div class="sm:min-w-28 sm:text-right">
                  <p class="text-xs font-semibold uppercase tracking-wider text-muted">
                    Total
                  </p>

                  <p class="mt-1 text-xl font-bold text-highlighted">
                    KES {{ item.product.price * item.quantity }}
                  </p>
                </div>
              </div>
            </UCard>
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
                  Review your order before checkout.
                </p>
              </div>
            </template>

            <div class="space-y-4">
              <div class="flex items-center justify-between text-sm">
                <span class="text-muted">
                  Items
                </span>

                <span class="font-medium">
                  {{ cartCount }}
                </span>
              </div>

              <div class="border-t border-default pt-4">
                <div class="flex items-end justify-between gap-4">
                  <span class="font-semibold">
                    Subtotal
                  </span>

                  <span class="text-2xl font-bold text-highlighted">
                    KES {{ totalPrice }}
                  </span>
                </div>
              </div>

              <p class="text-xs leading-5 text-muted">
                Taxes, delivery, and payment details will be handled during checkout.
              </p>
            </div>

            <template #footer>
              <div class="space-y-3">
                <UButton
                  to="/checkout"
                  size="lg"
                  block
                  trailing-icon="i-lucide-arrow-right"
                >
                  Continue to Checkout
                </UButton>

                <UButton
                  to="/"
                  color="neutral"
                  variant="outline"
                  block
                  leading-icon="i-lucide-arrow-left"
                >
                  Continue Shopping
                </UButton>

                <div class="pt-2">
                  <UButton
                    color="error"
                    variant="ghost"
                    block
                    icon="i-lucide-trash-2"
                    @click="clearCart"
                  >
                    Clear Cart
                  </UButton>
                </div>
              </div>
            </template>
          </UCard>
        </aside>
      </div>
    </section>
  </main>
</template>
