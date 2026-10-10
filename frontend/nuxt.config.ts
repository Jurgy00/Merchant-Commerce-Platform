// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  modules: [
    '@nuxt/eslint',
    '@nuxt/ui'
  ],
colorMode: {
    preference: 'dark',
    fallback: 'dark',
    classSuffix: ''
  },
  devtools: {
    enabled: true
  },

  css: ['~/assets/css/main.css'],

  routeRules: {
    '/': { prerender: true }
  },

  compatibilityDate: '2026-06-30',

  icon: {
    clientBundle: {
      scan: true,
      icons: [
        'lucide:book-open',
        'lucide:shopping-bag',
        'lucide:tag',
        'lucide:search',
        'lucide:menu',
        'lucide:check',
        'lucide:plus',
        'lucide:minus',
        'lucide:arrow-up',
        'lucide:arrow-left',
        'lucide:arrow-right',
        'lucide:shopping-cart',
        'lucide:map-pin',
        'lucide:user',
        'lucide:smartphone',
        'lucide:shield-check',
        'lucide:search-x',
        'lucide:trash-2',
        'lucide:circle-alert',
        'lucide:check-circle',
        'lucide:sun',
        'lucide:moon',
        'lucide:chevron-down'
      ]
    }
  },

  eslint: {
    config: {
      stylistic: {
        commaDangle: 'never',
        braceStyle: '1tbs'
      }
    }
  }
})
