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

export const products: Product[] = [
  {
    id: 1,
    name: 'Atomic Habits',
    description: 'A practical guide to building better habits through small, consistent changes.',
    price: 1850,
    category: 'Self Development',
    stock: 12,
    author: 'James Clear',
    image: '/books/atomic-habits.svg',
    badge: 'Bestseller'
  },
  {
    id: 2,
    name: 'Clean Code',
    description: 'A guide to writing readable, maintainable and professional software.',
    price: 3200,
    category: 'Technology',
    stock: 5,
    author: 'Robert C. Martin',
    image: '/books/clean-code.svg',
    badge: 'Developer Pick'
  },
  {
    id: 3,
    name: 'Deep Work',
    description: 'Rules for focused success in a distracted world.',
    price: 1950,
    category: 'Business',
    stock: 8,
    author: 'Cal Newport',
    image: '/books/deep-work.svg',
    badge: 'Popular'
  },
  {
    id: 4,
    name: 'The Psychology of Money',
    description: 'Timeless lessons on wealth, greed, happiness and financial decisions.',
    price: 2100,
    category: 'Business',
    stock: 10,
    author: 'Morgan Housel',
    image: '/books/psychology-money.svg',
    badge: 'Popular'
  },

  {
    id: 7,
    name: '1984',
    description: 'An enduring dystopian novel about surveillance, power and truth.',
    price: 1250,
    category: 'Fiction',
    stock: 18,
    author: 'George Orwell',
    image: '/books/1984.svg'
  },
  {
    id: 8,
    name: 'Sapiens',
    description: 'A sweeping exploration of the history of humankind.',
    price: 2400,
    category: 'Fiction',
    stock: 9,
    author: 'Yuval Noah Harari',
    image: '/books/sapiens.svg',
    badge: 'Bestseller'
  },
  {
    id: 9,
    name: 'The Alchemist',
    description: 'A philosophical story about dreams, destiny and following your path.',
    price: 1350,
    category: 'Fiction',
    stock: 15,
    author: 'Paulo Coelho',
    image: '/books/alchemist.svg'
  },
  {
    id: 10,
    name: 'Rich Dad Poor Dad',
    description: 'A personal-finance classic about financial education and building wealth.',
    price: 1600,
    category: 'Business',
    stock: 11,
    author: 'Robert T. Kiyosaki',
    image: '/books/rich-dad-poor-dad.svg'
  },
  {
    id: 11,
    name: 'Start with Why',
    description: 'Understanding purpose, leadership and what inspires people to act.',
    price: 1850,
    category: 'Business',
    stock: 7,
    author: 'Simon Sinek',
    image: '/books/start-with-why.svg'
  },
  {
    id: 12,
    name: "Don't Make Me Think",
    description: 'A practical guide to intuitive navigation and usable web experiences.',
    price: 2900,
    category: 'Technology',
    stock: 4,
    author: 'Steve Krug',
    image: '/books/dont-make-me-think.svg',
    badge: 'UX Classic'
  }
]
