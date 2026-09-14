<script setup lang="ts">
const cart = ref<CartItem[]>([])

const addToCart = (product: Product) => {
  const existingItem = cart.value.find(
    item => item.product.id === product.id
  )

  if (existingItem) {
    existingItem.quantity++
  } else {
    cart.value.push({
      product,
      quantity: 1
    })
  }
}

const cartCount = computed(() => {
  return cart.value.reduce((total, item) => {
    return total + item.quantity
  }, 0)
})
const updateQuantity = (productId: number, change: number) => {
  const item = cart.value.find(
    item => item.product.id === productId
  )

  if (!item) {
    return
  }

  item.quantity += change

  if (item.quantity <= 0) {
    cart.value = cart.value.filter(
      item => item.product.id !== productId
    )
  }
}
const categories = [
  'Fiction',
  'Business',
  'Technology',
  'Self Development',
  'Children'
]

interface Product {
  id: number
  name: string
  price: number
  category: string
}
interface CartItem {
  product: Product
  quantity: number
}
const sampleProducts: Product[] = [
  {
    id: 1,
    name: 'Atomic Habits',
    price: 1850,
    category: 'Self Development'
  },
  {
    id: 2,
    name: 'Clean Code',
    price: 3200,
    category: 'Technology'
  },
  {
    id: 3,
    name: 'Deep Work',
    price: 1950,
    category: 'Business'
  }

]
  const totalPrice = computed(() => {
  return sampleProducts.reduce((total, product) => {
    return total + product.price
  }, 0)
})
const maxPrice = ref(2000)

const cheapProducts = computed(() => {
  return sampleProducts.filter(product => product.price < maxPrice.value)
})
const heroTitle = 'Find your next great read'
const heroDescription = 'Discover books for every reader.'
interface HeroLink {
  label: string
  to: string
  trailingIcon?: string
  size: 'xs' | 'sm' | 'md' | 'lg' | 'xl'
}
const heroLinks: HeroLink[] = [
  {
    label: 'Explore collection',
    to: '#products',
    trailingIcon: 'i-lucide-arrow-right',
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

  <UPageSection title="Vue Practice">
          <UInput
        v-model="maxPrice"
        type="number"
      />
 <p v-if="cartCount === 0">
  Your cart is empty.
</p>

<p v-else>
  You have {{ cartCount }} item(s) in your cart.
</p>

<div class="mt-8">
  <h3 class="text-xl font-semibold">
    Your Cart
  </h3>

  <div
    v-if="cart.length === 0"
    class="mt-4"
  >
    <UAlert
      title="Your cart is empty"
      description="Add a product to get started."
      icon="i-lucide-shopping-cart"
    />
  </div>

  <div
    v-else
    class="mt-4 space-y-4"
  >
    <UCard
      v-for="item in cart"
      :key="item.product.id"
    >
      <div class="flex items-center justify-between gap-4">
        <div>
          <p class="font-semibold">
            {{ item.product.name }}
          </p>

          <p class="text-sm text-muted">
            KES {{ item.product.price }} each
          </p>
        </div>

        <div class="flex items-center gap-2">
          <UButton
            icon="i-lucide-minus"
            color="neutral"
            variant="outline"
            @click="updateQuantity(item.product.id, -1)"
          />

          <span class="min-w-6 text-center">
            {{ item.quantity }}
          </span>

          <UButton
            icon="i-lucide-plus"
            color="neutral"
            variant="outline"
            @click="updateQuantity(item.product.id, 1)"
          />
        </div>
      </div>
    </UCard>
  </div>
</div>
<UCard
  v-for="product in cheapProducts"
  :key="product.id"
>
  <template #header>
    <div>
      <p class="font-semibold">
        {{ product.name }}
      </p>

      <p class="text-sm text-muted">
        {{ product.category }}
      </p>
    </div>
  </template>

  <p class="text-lg font-semibold">
    KES {{ product.price }}
  </p>

  <template #footer>
    <UButton
      block
      @click="addToCart(product)"
    >
      Add to Cart
    </UButton>
  </template>
</UCard>
  </UPageSection>

  <UPageSection
    id="features"
    title="Everything you need to build modern Nuxt apps"
    description="Start with a solid foundation. This template includes all the essentials for building production-ready applications with Nuxt UI's powerful component system."
    :features="[{
      icon: 'i-lucide-rocket',
      title: 'Production-ready from day one',
      description: 'Pre-configured with TypeScript, ESLint, Tailwind CSS, and all the best practices. Focus on building features, not setting up tooling.'
    }, {
      icon: 'i-lucide-palette',
      title: 'Beautiful by default',
      description: 'Leveraging Nuxt UI\'s design system with automatic dark mode, consistent spacing, and polished components that look great out of the box.'
    }, {
      icon: 'i-lucide-zap',
      title: 'Lightning fast',
      description: 'Optimized for performance with SSR/SSG support, automatic code splitting, and edge-ready deployment. Your users will love the speed.'
    }, {
      icon: 'i-lucide-blocks',
      title: '100+ components included',
      description: 'Access Nuxt UI\'s comprehensive component library. From forms to navigation, everything is accessible, responsive, and customizable.'
    }, {
      icon: 'i-lucide-code-2',
      title: 'Developer experience first',
      description: 'Auto-imports, hot module replacement, and TypeScript support. Write less boilerplate and ship more features.'
    }, {
      icon: 'i-lucide-shield-check',
      title: 'Built for scale',
      description: 'Enterprise-ready architecture with proper error handling, SEO optimization, and security best practices built-in.'
    }]"
  />

  <UPageSection>
    <UPageCTA
      title="Ready to build your next Nuxt app?"
      description="Join thousands of developers building with Nuxt and Nuxt UI. Get this template and start shipping today."
      variant="subtle"
      :links="[{
        label: 'Start building',
        to: 'https://ui.nuxt.com/docs/getting-started/installation/nuxt',
        target: '_blank',
        trailingIcon: 'i-lucide-arrow-right',
        color: 'neutral'
      }, {
        label: 'View on GitHub',
        to: 'https://github.com/nuxt-ui-templates/starter',
        target: '_blank',
        icon: 'i-simple-icons-github',
        color: 'neutral',
        variant: 'outline'
      }]"
    />
  </UPageSection>
</template>
