import type { Product, CartItem } from '~/types'

const STORAGE_KEY = 'mcp_cart'

export const useCart = () => {
  const items = useState<CartItem[]>('cart', () => [])

  const loadFromStorage = () => {
    if (import.meta.client) {
      try {
        const raw = localStorage.getItem(STORAGE_KEY)
        if (raw) items.value = JSON.parse(raw) as CartItem[]
      } catch {
        items.value = []
      }
    }
  }

  const saveToStorage = () => {
    if (import.meta.client) {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(items.value))
    }
  }

  const addItem = (product: Product, quantity = 1) => {
    const existing = items.value.find(i => i.product.id === product.id)
    if (existing) {
      existing.quantity += quantity
    } else {
      items.value.push({ product, quantity })
    }
    saveToStorage()
  }

  const removeItem = (productId: number) => {
    items.value = items.value.filter(i => i.product.id !== productId)
    saveToStorage()
  }

  const updateQuantity = (productId: number, quantity: number) => {
    if (quantity < 1) {
      removeItem(productId)
      return
    }
    const item = items.value.find(i => i.product.id === productId)
    if (item) {
      item.quantity = quantity
      saveToStorage()
    }
  }

  const clearCart = () => {
    items.value = []
    saveToStorage()
  }

  const itemCount = computed(() => items.value.reduce((sum, i) => sum + i.quantity, 0))
  const total = computed(() => items.value.reduce((sum, i) => sum + i.product.price * i.quantity, 0))
  const formattedTotal = computed(() => `KES ${total.value.toLocaleString('en-KE')}`)
  const isEmpty = computed(() => items.value.length === 0)

  return {
    items,
    itemCount,
    total,
    formattedTotal,
    isEmpty,
    addItem,
    removeItem,
    updateQuantity,
    clearCart,
    loadFromStorage,
  }
}