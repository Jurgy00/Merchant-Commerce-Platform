<template>
  <div class="min-h-screen bg-default">
    <AppHeader />

    <main>
      <NuxtPage />
    </main>

    <footer class="border-t border-default bg-elevated/20">
      <div class="mx-auto max-w-[1800px] px-4 sm:px-6 lg:px-8">
        <div class="grid gap-10 py-12 sm:grid-cols-2 lg:grid-cols-[2fr_1fr_1fr_1fr]">
          <!-- Brand -->
          <div>
            <NuxtLink
              to="/"
              class="inline-flex items-center gap-2.5"
            >
              <div
                class="flex size-9 items-center justify-center rounded-full bg-primary text-white"
              >
                <UIcon
                  name="i-lucide-book-open"
                  class="size-4.5"
                />
              </div>

              <div>
                <p class="text-sm font-black text-highlighted">
                  BookStore
                </p>

                <p class="text-[9px] text-muted">
                  Read. Learn. Grow.
                </p>
              </div>
            </NuxtLink>

            <p class="mt-5 max-w-md text-sm leading-6 text-muted">
              A thoughtfully curated collection of books for curious minds,
              ambitious builders, lifelong learners, and readers who still
              love getting lost in a good story.
            </p>
          </div>

          <!-- Explore -->
          <div>
            <p class="text-[10px] font-black uppercase tracking-[0.16em] text-highlighted">
              Explore
            </p>

            <nav class="mt-4 flex flex-col items-start gap-3">
              <NuxtLink
                to="/"
                class="text-sm text-muted transition-colors hover:text-primary"
              >
                All books
              </NuxtLink>

              <NuxtLink
                to="/#featured"
                class="text-sm text-muted transition-colors hover:text-primary"
              >
                Featured
              </NuxtLink>

              <NuxtLink
                to="/#products"
                class="text-sm text-muted transition-colors hover:text-primary"
              >
                Collection
              </NuxtLink>
            </nav>
          </div>

          <!-- Categories -->
          <div>
            <p class="text-[10px] font-black uppercase tracking-[0.16em] text-highlighted">
              Categories
            </p>

            <nav class="mt-4 flex flex-col items-start gap-3">
              <NuxtLink
                to="/"
                class="text-sm text-muted transition-colors hover:text-primary"
              >
                Fiction
              </NuxtLink>

              <NuxtLink
                to="/"
                class="text-sm text-muted transition-colors hover:text-primary"
              >
                Business
              </NuxtLink>

              <NuxtLink
                to="/"
                class="text-sm text-muted transition-colors hover:text-primary"
              >
                Technology
              </NuxtLink>

              <NuxtLink
                to="/"
                class="text-sm text-muted transition-colors hover:text-primary"
              >
                Self Development
              </NuxtLink>
            </nav>
          </div>

          <!-- Your order -->
          <div>
            <p class="text-[10px] font-black uppercase tracking-[0.16em] text-highlighted">
              Your order
            </p>

            <nav class="mt-4 flex flex-col items-start gap-3">
              <NuxtLink
                to="/cart"
                class="text-sm text-muted transition-colors hover:text-primary"
              >
                Shopping bag
              </NuxtLink>

              <NuxtLink
                to="/checkout"
                class="text-sm text-muted transition-colors hover:text-primary"
              >
                Checkout
              </NuxtLink>
            </nav>
          </div>
        </div>

        <div class="flex flex-col gap-3 border-t border-default py-6 sm:flex-row sm:items-center sm:justify-between">
          <p class="text-xs text-muted">
            © 2026 BookStore. All rights reserved.
          </p>

          <NuxtLink
            to="/"
            class="inline-flex items-center gap-1 text-xs text-muted transition-colors hover:text-primary"
          >
            Back to top
            <UIcon
              name="i-lucide-arrow-up"
              class="size-3"
            />
          </NuxtLink>
        </div>
      </div>
    </footer>
  </div>
</template>
