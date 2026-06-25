import type { Product, Category, Merchant, ProductQuery } from '~/types'

export const useProducts = () => {
  const config = useRuntimeConfig()
  const BASE = config.public.apiBaseUrl

  const fetchProducts = async (query: ProductQuery = {}): Promise<Product[]> => {
    const params = new URLSearchParams()

    if (query.search) {
      params.set('search', query.search)
    }

    if (query.categoryId) {
      params.set('categoryId', String(query.categoryId))
    }

    const response = await $fetch<any[]>(`${BASE}/products?${params}`)

    return response.map((p: any) => ({
      id: p.id,
      name: p.name,
      description: p.description,
      price: p.price,
      stockQuantity: p.stockQuantity,
      imageUrl: p.imageUrl,
      category: {
        id: p.category.id,
        name: p.category.name,
        slug: p.category.slug,
      }
    })) as Product[]
  }

  const fetchProduct = async (id: number): Promise<Product> => {
    const p = await $fetch<any>(`${BASE}/products/${id}`)

    return {
      id: p.id,
      name: p.name,
      description: p.description,
      price: p.price,
      stockQuantity: p.stockQuantity,
      imageUrl: p.imageUrl,
      category: {
        id: p.category.id,
        name: p.category.name,
        slug: p.category.slug,
      }
    } as Product
  }

  const fetchCategories = async (): Promise<Category[]> => {
    const response = await $fetch<any[]>(`${BASE}/categories`)

    return response.map((c: any) => ({
      id: c.id,
      name: c.name,
      slug: c.slug,
    })) as Category[]
  }

  const fetchMerchant = async (): Promise<Merchant> => {
    const response = await $fetch<any>(`${BASE}/merchants/1`)

    return {
      id: response.id,
      name: response.name,
      description: response.description,
      email: response.email,
      phone: response.phone,
      logoUrl: response.logoUrl,
      bannerUrl: null,
    } as Merchant
  }

  return {
    fetchProducts,
    fetchProduct,
    fetchCategories,
    fetchMerchant,
  }
}