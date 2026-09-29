
import type { Product } from '~/data/products'

export interface CartItem {
  product: Product
  quantity: number
}

const CART_STORAGE_KEY = 'merchant-commerce-cart'

export const useCart = () => {
  const cart = useState<CartItem[]>('cart', () => [])

  // Restore the cart from localStorage when the app mounts in the browser.
  onMounted(() => {
    const savedCart = localStorage.getItem(CART_STORAGE_KEY)

    if (!savedCart) {
      return
    }

    try {
      const parsedCart = JSON.parse(savedCart) as CartItem[]

      if (Array.isArray(parsedCart)) {
        cart.value = parsedCart
      }
    } catch {
      // Ignore invalid saved data and start with an empty cart.
      localStorage.removeItem(CART_STORAGE_KEY)
    }
  })

  // Save the cart whenever its contents or quantities change.
  watch(
    cart,
    (newCart) => {
      if (import.meta.client) {
        localStorage.setItem(
          CART_STORAGE_KEY,
          JSON.stringify(newCart)
        )
      }
    },
    { deep: true }
  )

  const addToCart = (product: Product) => {
    const existingItem = cart.value.find(
      item => item.product.id === product.id
    )

    if (existingItem) {
      if (existingItem.quantity < product.stock) {
        existingItem.quantity++
      }
    } else if (product.stock > 0) {
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

  const clearCart = () => {
    cart.value = []
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

  return {
    cart,
    addToCart,
    updateQuantity,
    clearCart,
    cartCount,
    totalPrice
  }
}
