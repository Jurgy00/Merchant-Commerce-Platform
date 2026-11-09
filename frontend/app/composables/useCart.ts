import type { Product } from '~/types/product'

export interface CartItem {
  product: Product
  quantity: number
}

const CART_STORAGE_KEY = 'merchant-commerce-cart'

export const useCart = () => {
  const cart = useState<CartItem[]>('cart', () => [])
  const cartReady = useState<boolean>('cart-ready', () => false)

  // Restore saved cart data before enabling persistence.
  onMounted(() => {
    if (!cartReady.value) {
      try {
        const savedCart = localStorage.getItem(CART_STORAGE_KEY)

        if (savedCart) {
          const parsedCart = JSON.parse(savedCart) as unknown

          if (Array.isArray(parsedCart)) {
            cart.value = parsedCart as CartItem[]
          }
        }
      } catch {
        // Ignore malformed or inaccessible saved data.
      } finally {
        cartReady.value = true
      }
    }
  })

  // Never overwrite saved data before restoration completes.
  watch(
    cart,
    (newCart) => {
      if (import.meta.client && cartReady.value) {
        try {
          localStorage.setItem(
            CART_STORAGE_KEY,
            JSON.stringify(newCart)
          )
        } catch (error) {
          console.error('Could not save cart:', error)
        }
      }
    },
    { deep: true }
  )

  const addToCart = (product: Product) => {
    if (!cartReady.value) return

    const existingItem = cart.value.find(
      item => item.product.id === product.id
    )

    if (existingItem) {
      if (existingItem.quantity < product.stock) {
        existingItem.quantity++
      }
    } else if (product.stock > 0) {
      cart.value.push({ product, quantity: 1 })
    }
  }

  const updateQuantity = (productId: number, change: number) => {
    if (!cartReady.value) return

    const item = cart.value.find(
      item => item.product.id === productId
    )

    if (!item) return

    const newQuantity = item.quantity + change

    if (newQuantity <= 0) {
      cart.value = cart.value.filter(
        item => item.product.id !== productId
      )
      return
    }

    if (newQuantity > item.product.stock) return

    item.quantity = newQuantity
  }

  const clearCart = () => {
    if (!cartReady.value) return
    cart.value = []
  }

  const cartCount = computed(() =>
    cart.value.reduce((total, item) => total + item.quantity, 0)
  )

  const totalPrice = computed(() =>
    cart.value.reduce(
      (total, item) => total + item.product.price * item.quantity,
      0
    )
  )

  return {
    cart,
    cartReady,
    addToCart,
    updateQuantity,
    clearCart,
    cartCount,
    totalPrice
  }
}
