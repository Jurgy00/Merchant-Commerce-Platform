<template>
  <div class="min-h-screen bg-default">
    <AppHeader />

    <main>
      <NuxtPage />
    </main>

    <footer class="border-t border-default">
      <div class="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
        <div class="flex flex-col gap-2 sm:flex-row sm:items-center sm:justify-between">
          <div>
            <p class="font-semibold">
              BookStore
            </p>

            <p class="text-sm text-muted">
              Read. Learn. Grow.
            </p>
          </div>

          <p class="text-sm text-muted">
            © 2026 BookStore. All rights reserved.
          </p>
        </div>
      </div>
    </footer>
  </div>
</template>
