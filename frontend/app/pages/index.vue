<script setup lang="ts">
import { products } from '~/data/products'

type Product = (typeof products)[number]

interface HeroLink {
  label: string
  to: string
  trailingIcon?: string
  size: 'xs' | 'sm' | 'md' | 'lg' | 'xl'
}

const categories = [
  'All',
  'Fiction',
  'Business',
  'Technology',
  'Self Development',
  'Children'
]

const maxPrice = ref(4000)
const selectedCategory = ref('All')

const {
  cartCount,
  addToCart
} = useCart()

const toast = useToast()

const cheapProducts = computed(() => {
  return products.filter(product => {
    const matchesPrice = product.price <= maxPrice.value

    const matchesCategory =
      selectedCategory.value === 'All' ||
      product.category === selectedCategory.value

    return matchesPrice && matchesCategory
  })
})

const handleAddToCart = (product: Product) => {
  addToCart(product)

  toast.add({
    title: 'Added to cart',
    description: `${product.name} was added to your cart.`
  })
}

const heroTitle = 'Find your next great read'

const heroDescription =
  'Discover books for every reader. Explore our collection and find your next favorite book.'

const heroLinks: HeroLink[] = [
  {
    label: 'Explore collection',
    to: '#products',
    trailingIcon: 'i-lucide-arrow-down',
    size: 'xl'
  }
]
</script>

<template>
  <!-- Store navigation -->
  <div class="flex justify-end px-4 pt-4">
    <UButton
      to="/cart"
      color="neutral"
      variant="outline"
      icon="i-lucide-shopping-cart"
    >
      Cart

      <UBadge
        color="primary"
        variant="solid"
        size="sm"
      >
        {{ cartCount }}
      </UBadge>
    </UButton>
  </div>

  <!-- Hero -->
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
        :color="selectedCategory === category ? 'primary' : 'neutral'"
        :variant="selectedCategory === category ? 'solid' : 'outline'"
        @click="selectedCategory = category"
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
    <!-- Filters -->
    <UCard class="mb-8">
      <div class="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
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
          :min="0"
        />
      </div>
    </UCard>

    <!-- Product grid -->
    <div class="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
      <UCard
        v-for="product in cheapProducts"
        :key="product.id"
        class="flex flex-col"
      >
        <template #header>
          <div class="flex items-start justify-between gap-4">
            <div>
              <p class="text-lg font-semibold">
                {{ product.name }}
              </p>

              <p class="mt-1 text-sm text-muted">
                {{ product.category }}
              </p>
            </div>

            <UBadge
              :color="product.stock === 0 ? 'error' : 'success'"
              variant="subtle"
            >
              {{
                product.stock === 0
                  ? 'Out of stock'
                  : `${product.stock} in stock`
              }}
            </UBadge>
          </div>
        </template>

        <div class="flex flex-1 flex-col">
          <p class="text-sm leading-6 text-muted">
            {{ product.description }}
          </p>

          <div class="mt-auto pt-6">
            <p class="text-2xl font-bold">
              KES {{ product.price }}
            </p>
          </div>
        </div>

        <template #footer>
          <div class="flex flex-col gap-2">
            <UButton
              block
              :to="`/products/${product.id}`"
              color="neutral"
              variant="outline"
            >
              View Details
            </UButton>

            <UButton
              block
              :disabled="product.stock === 0"
              @click="handleAddToCart(product)"
            >
              {{
                product.stock === 0
                  ? 'Out of Stock'
                  : 'Add to Cart'
              }}
            </UButton>
          </div>
        </template>
      </UCard>
    </div>

    <!-- No results -->
    <UAlert
      v-if="cheapProducts.length === 0"
      class="mt-6"
      title="No books found"
      description="Try increasing the maximum price or choosing another category."
      icon="i-lucide-search-x"
    />
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
          trailingIcon: 'i-lucide-arrow-down'
        },
        {
          label: 'View Cart',
          to: '/cart',
          icon: 'i-lucide-shopping-cart',
          color: 'neutral',
          variant: 'outline'
        }
      ]"
    />
  </UPageSection>
</template>
