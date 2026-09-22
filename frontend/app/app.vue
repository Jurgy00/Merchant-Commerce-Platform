<template>
  <div class="min-h-screen bg-default">
    <AppHeader />

    <main>
      <NuxtPage />
    </main>

    <footer class="border-t border-default bg-elevated/20">
      <div class="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <!-- Main footer -->
        <div class="grid gap-10 py-12 sm:grid-cols-2 lg:grid-cols-[1.5fr_1fr_1fr]">
          <!-- Brand -->
          <div>
            <NuxtLink
              to="/"
              class="group inline-flex items-center gap-3"
            >
              <div
                class="flex size-10 items-center justify-center rounded-xl bg-primary text-white transition-transform duration-200 group-hover:scale-105"
              >
                <UIcon
                  name="i-lucide-book-open"
                  class="size-5"
                />
              </div>

              <div>
                <p class="font-bold tracking-tight text-highlighted">
                  BookStore
                </p>

                <p class="text-xs text-muted">
                  Read. Learn. Grow.
                </p>
              </div>
            </NuxtLink>

            <p class="mt-5 max-w-sm text-sm leading-6 text-muted">
              A curated collection of books for curious minds, lifelong learners,
              and anyone looking for their next great read.
            </p>
          </div>

          <!-- Shop -->
          <div>
            <p class="text-sm font-semibold text-highlighted">
              Shop
            </p>

            <nav class="mt-4 flex flex-col items-start gap-3">
              <UButton
                to="/"
                color="neutral"
                variant="link"
                class="p-0"
              >
                Books
              </UButton>

              <UButton
                to="/#products"
                color="neutral"
                variant="link"
                class="p-0"
              >
                Collection
              </UButton>

              <UButton
                to="/cart"
                color="neutral"
                variant="link"
                class="p-0"
              >
                Cart
              </UButton>
            </nav>
          </div>

          <!-- Help -->
          <div>
            <p class="text-sm font-semibold text-highlighted">
              Your Order
            </p>

            <nav class="mt-4 flex flex-col items-start gap-3">
              <UButton
                to="/cart"
                color="neutral"
                variant="link"
                class="p-0"
              >
                Review Cart
              </UButton>

              <UButton
                to="/checkout"
                color="neutral"
                variant="link"
                class="p-0"
              >
                Checkout
              </UButton>
            </nav>
          </div>
        </div>

        <!-- Bottom bar -->
        <div class="flex flex-col gap-3 border-t border-default py-6 sm:flex-row sm:items-center sm:justify-between">
          <p class="text-sm text-muted">
            © 2026 BookStore. All rights reserved.
          </p>

          <UButton
            to="/"
            color="neutral"
            variant="ghost"
            size="sm"
            icon="i-lucide-arrow-up"
            aria-label="Back to top"
          >
            Back to top
          </UButton>
        </div>
      </div>
    </footer>
  </div>
</template>
