<script setup lang="ts">
interface Product {
  id: number
  name: string
  description: string
  price: number
  category: string
  stock: number
}

interface CartItem {
  product: Product
  quantity: number
}

interface HeroLink {
  label: string
  to: string
  trailingIcon?: string
  size: 'xs' | 'sm' | 'md' | 'lg' | 'xl'
}

const categories = [
  'Fiction',
  'Business',
  'Technology',
  'Self Development',
  'Children'
]

const sampleProducts: Product[] = [
  {
    id: 1,
    name: 'Atomic Habits',
    description:
      'A practical guide to building good habits and breaking bad ones.',
    price: 1850,
    category: 'Self Development',
    stock: 12
  },
  {
    id: 2,
    name: 'Clean Code',
    description:
      'A guide to writing readable, maintainable and professional software.',
    price: 3200,
    category: 'Technology',
    stock: 5
  },
  {
    id: 3,
    name: 'Deep Work',
    description:
      'Learn how to focus without distraction and produce better work.',
    price: 1950,
    category: 'Business',
    stock: 8
  }
]

const cart = ref<CartItem[]>([])

const maxPrice = ref(4000)

const addToCart = (product: Product) => {
  const existingItem = cart.value.find(
    item => item.product.id === product.id
  )

  if (existingItem) {
    if (existingItem.quantity < product.stock) {
      existingItem.quantity++
    }
  } else {
    cart.value.push({
      product,
      quantity: 1
    })
  }
}

const updateQuantity = (productId: number, change: number) => {
  const item = cart.value.find(
    item => item.product.id === productId
  )

  if (!item) {
    return
  }

  const newQuantity = item.quantity + change

  if (newQuantity <= 0) {
    cart.value = cart.value.filter(
      item => item.product.id !== productId
    )

    return
  }

  if (newQuantity > item.product.stock) {
    return
  }

  item.quantity = newQuantity
}

const cartCount = computed(() => {
  return cart.value.reduce((total, item) => {
    return total + item.quantity
  }, 0)
})

const totalPrice = computed(() => {
  return cart.value.reduce((total, item) => {
    return total + item.product.price * item.quantity
  }, 0)
})

const cheapProducts = computed(() => {
  return sampleProducts.filter(
    product => product.price <= maxPrice.value
  )
})

const heroTitle = 'Find your next great read'

const heroDescription =
  'Discover books for every reader. Explore our collection and find your next favorite book.'

const heroLinks: HeroLink[] = [
  {
    label: 'Explore collection',
    to: '#products',
    trailingIcon: 'i-lucide-arrow-right',
    size: 'xl'
  },
  {
    label: `Cart (${cartCount.value})`,
    to: '#cart',
    icon: 'i-lucide-shopping-cart',
    size: 'xl'
  }
]
</script>

<template>
  <UPageHero
    :title="heroTitle"
    :description="heroDescription"
    :links="heroLinks"
  />

  <!-- Categories -->
  <UPageSection
    title="Shop by Category"
    description="Explore books across different interests."
  >
    <div class="flex flex-wrap gap-3">
      <UButton
        v-for="category in categories"
        :key="category"
        color="neutral"
        variant="outline"
      >
        {{ category }}
      </UButton>
    </div>
  </UPageSection>

  <!-- Products -->
  <UPageSection
    id="products"
    title="Our Books"
    description="Find something you'll love."
  >
    <div class="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
      <div>
        <p class="font-semibold">
          Maximum price
        </p>

        <p class="text-sm text-muted">
          Show books costing up to KES {{ maxPrice }}
        </p>
      </div>

      <UInput
        v-model="maxPrice"
        type="number"
        class="w-full sm:w-48"
      />
    </div>

    <div class="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
      <UCard
        v-for="product in cheapProducts"
        :key="product.id"
      >
        <template #header>
          <div>
            <p class="text-lg font-semibold">
              {{ product.name }}
            </p>

            <p class="text-sm text-muted">
              {{ product.category }}
            </p>
          </div>
        </template>

        <div class="space-y-4">
          <p class="text-sm text-muted">
            {{ product.description }}
          </p>

          <div>
            <p class="text-xl font-bold">
              KES {{ product.price }}
            </p>

            <p class="text-sm text-muted">
              {{ product.stock }} in stock
            </p>
          </div>
        </div>

        <template #footer>
          <UButton
            block
            :disabled="product.stock === 0"
            @click="addToCart(product)"
          >
            Add to Cart
          </UButton>
        </template>
      </UCard>
    </div>

    <UAlert
      v-if="cheapProducts.length === 0"
      class="mt-6"
      title="No books found"
      description="Try increasing the maximum price."
      icon="i-lucide-search-x"
    />
  </UPageSection>

  <!-- Cart -->
  <UPageSection
    id="cart"
    title="Your Cart"
    :description="cartCount === 0
      ? 'Your selected products will appear here.'
      : `${cartCount} item(s) in your cart.`"
  >
    <UAlert
      v-if="cart.length === 0"
      title="Your cart is empty"
      description="Add a book from our collection to get started."
      icon="i-lucide-shopping-cart"
    />

    <div
      v-else
      class="space-y-4"
    >
      <UCard
        v-for="item in cart"
        :key="item.product.id"
      >
        <div class="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
          <!-- Product information -->
          <div class="min-w-0">
            <p class="font-semibold">
              {{ item.product.name }}
            </p>

            <p class="text-sm text-muted">
              {{ item.product.category }}
            </p>

            <p class="mt-1 text-sm text-muted">
              KES {{ item.product.price }} each
            </p>
          </div>

          <!-- Quantity controls -->
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
              @click="updateQuantity(item.product.id, 1)"
            />
          </div>

          <!-- Line total -->
          <p class="font-semibold">
            KES {{ item.product.price * item.quantity }}
          </p>
        </div>
      </UCard>

      <!-- Cart summary -->
      <UCard>
        <div class="space-y-3">
          <div class="flex items-center justify-between">
            <span class="text-muted">
              Items
            </span>

            <span>
              {{ cartCount }}
            </span>
          </div>

          <div class="flex items-center justify-between">
            <span class="font-semibold">
              Subtotal
            </span>

            <span class="text-xl font-bold">
              KES {{ totalPrice }}
            </span>
          </div>
        </div>

        <template #footer>
          <div class="flex flex-col gap-3 sm:flex-row">
            <UButton
              to="#products"
              color="neutral"
              variant="outline"
            >
              Continue Shopping
            </UButton>

            <UButton
              block
              trailing-icon="i-lucide-arrow-right"
            >
              Checkout
            </UButton>
          </div>
        </template>
      </UCard>
    </div>
  </UPageSection>

  <!-- Features -->
  <UPageSection
    title="Why shop with us?"
    description="A simple shopping experience built with modern web technology."
    :features="[
      {
        icon: 'i-lucide-shield-check',
        title: 'Secure checkout',
        description:
          'Your order and payment information will be handled securely.'
      },
      {
        icon: 'i-lucide-smartphone',
        title: 'Mobile friendly',
        description:
          'Shop comfortably from your phone, tablet or desktop.'
      },
      {
        icon: 'i-lucide-book-open',
        title: 'Curated collection',
        description:
          'Discover books across technology, business, fiction and more.'
      },
      {
        icon: 'i-lucide-zap',
        title: 'Fast experience',
        description:
          'Enjoy a responsive storefront designed for a smooth shopping experience.'
      }
    ]"
  />

  <!-- Final CTA -->
  <UPageSection>
    <UPageCTA
      title="Ready to find your next book?"
      description="Explore our collection and add your favorites to the cart."
      variant="subtle"
      :links="[
        {
          label: 'Browse Books',
          to: '#products',
          trailingIcon: 'i-lucide-arrow-right'
        },
        {
          label: 'View Cart',
          to: '#cart',
          icon: 'i-lucide-shopping-cart',
          color: 'neutral',
          variant: 'outline'
        }
      ]"
    />
  </UPageSection>
</template>
