<script setup lang="ts">
import { products } from '~/data/products'

const route = useRoute()

const { addToCart } = useCart()

const toast = useToast()

const handleAddToCart = () => {
  if (!product) {
    return
  }

  addToCart(product)

  toast.add({
    title: 'Added to cart',
    description: `${product.name} was added to your cart.`
  })
}

const productId = Number(route.params.id)

const product = products.find(
  product => product.id === productId
)
</script>

<template>
  <UPageSection>
    <div
      v-if="product"
      class="grid gap-8 lg:grid-cols-2"
    >
      <!-- Product information -->
      <div>
        <p class="text-sm text-muted">
          {{ product.category }}
        </p>

        <h1 class="mt-2 text-3xl font-bold">
          {{ product.name }}
        </h1>

        <p class="mt-4 text-lg text-muted">
          {{ product.description }}
        </p>

        <p class="mt-6 text-3xl font-bold">
          KES {{ product.price }}
        </p>

        <p class="mt-2 text-sm text-muted">
          {{ product.stock }} in stock
        </p>

        <div class="mt-8 flex gap-3">
          <UButton
            to="/"
            color="neutral"
            variant="outline"
          >
            Back to Store
          </UButton>

          <UButton
            :disabled="product.stock === 0"
            @click="handleAddToCart"
          >
            Add to Cart
          </UButton>
        </div>
      </div>
    </div>

    <!-- Product not found -->
    <UAlert
      v-else
      title="Product not found"
      description="The product you're looking for does not exist."
      icon="i-lucide-circle-alert"
    />
  </UPageSection>
</template>
