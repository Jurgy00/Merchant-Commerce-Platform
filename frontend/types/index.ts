

export interface Merchant {
  id: number
  name: string
  description: string
  logoUrl: string | null
  bannerUrl: string | null
  email: string
  phone: string
}


export interface Category {
  id: number
  name: string
  slug: string
}


export interface Product {
  id: number
  name: string
  description: string
  price: number
  stockQuantity: number
  imageUrl: string | null
  category: Category
}


export interface CartItem {
  product: Product
  quantity: number
}


export interface ProductQuery {
  search?: string
  categoryId?: number
  page?: number
  size?: number
}