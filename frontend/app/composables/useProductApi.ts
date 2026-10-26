import type {
  Category,
  CategoryApiResponse,
  Product,
  ProductApiResponse
} from '~/types/product'

const normalizeProduct = (item: ProductApiResponse): Product => {
  const category = typeof item.category === 'string'
    ? item.category
    : item.category?.name

  return {
    id: Number(item.id),
    name: item.name,
    description: item.description ?? '',
    price: Number(item.price),
    category: item.categoryName ?? category ?? 'Uncategorized',
    stock: Number(item.stockQuantity ?? 0),
    author: item.author ?? '',
    image: item.imageUrl ?? '',
    ...(item.badge ? { badge: item.badge } : {})
  }
}

export const useProductApi = () => {
  const config = useRuntimeConfig()
  const baseURL = config.public.apiBase as string

  const getProducts = async (): Promise<Product[]> => {
    const response = await $fetch<ProductApiResponse[]>('/api/products', {
      baseURL
    })

    return response.map(normalizeProduct)
  }

  const getProduct = async (id: number): Promise<Product> => {
    const response = await $fetch<ProductApiResponse>(
      `/api/products/${id}`,
      { baseURL }
    )

    return normalizeProduct(response)
  }

  const getCategories = async (merchantId: number): Promise<Category[]> => {
    const response = await $fetch<CategoryApiResponse[]>(
      `/api/merchants/${merchantId}/categories`,
      { baseURL }
    )

    return response.map((category) => ({
      id: Number(category.id),
      name: category.name,
      description: category.description ?? '',
      merchantId: Number(category.merchantId)
    }))
  }

  return {
    getProducts,
    getProduct,
    getCategories
  }
}
