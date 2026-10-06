export const storeCategories = [
  'All',
  'Fiction',
  'Business',
  'Technology',
  'Self Development',
  'Children'
]

export const useStoreFilters = () => {
  const searchQuery = useState('store-search-query', () => '')
  const selectedCategory = useState('store-selected-category', () => 'All')
  const maxPrice = useState('store-max-price', () => 4000)
  const sortBy = useState('store-sort-by', () => 'default')

  const resetFilters = () => {
    searchQuery.value = ''
    selectedCategory.value = 'All'
    maxPrice.value = 4000
    sortBy.value = 'default'
  }

  return {
    categories: storeCategories,
    searchQuery,
    selectedCategory,
    maxPrice,
    sortBy,
    resetFilters
  }
}
