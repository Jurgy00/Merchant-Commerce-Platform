export interface Product {
  id: number
  name: string
  description: string
  price: number
  category: string
  stock: number
}

export const products: Product[] = [
  {
    id: 1,
    name: 'Atomic Habits',
    description:
      'A practical guide to building good habits and breaking bad ones.',
    price: 1850,
    category: 'Self Development',
    stock: 12
  },
  {
    id: 2,
    name: 'Clean Code',
    description:
      'A guide to writing readable, maintainable and professional software.',
    price: 3200,
    category: 'Technology',
    stock: 5
  },
  {
    id: 3,
    name: 'Deep Work',
    description:
      'Learn how to focus without distraction and produce better work.',
    price: 1950,
    category: 'Business',
    stock: 8
  }
]
