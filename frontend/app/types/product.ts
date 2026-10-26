export interface Product {
  id: number
  name: string
  description: string
  price: number
  category: string
  stock: number
  author: string
  image: string
  badge?: string
}

/**
 * Product fields returned by the Spring Boot API.
 * The API uses stockQuantity and imageUrl instead of
 * the frontend's stock and image properties.
 */
export interface ProductApiResponse {
  id: number
  name: string
  description?: string | null
  price: number | string
  stockQuantity?: number | null
  imageUrl?: string | null
  author?: string | null
  badge?: string | null
  categoryName?: string | null
  category?: string | { id?: number; name?: string } | null
}

/**
 * Category returned by the Spring Boot API.
 */
export interface Category {
  id: number
  name: string
  description?: string | null
  merchantId: number
}

/**
 * Category response used by the frontend API composable.
 */
export interface CategoryApiResponse {
  id: number
  name: string
  description?: string | null
  merchantId: number
}
