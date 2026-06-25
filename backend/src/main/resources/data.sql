INSERT INTO merchants (id, name, description, email, phone, logo_url) VALUES
(1, 'Bookstore', 'Your neighbourhood bookstore, now online. We stock fiction, non-fiction, children''s books and textbooks delivered to your door.', 'hello@bookstore.co.ke', '0742098841', null);

INSERT INTO categories (id, name, slug, merchant_id) VALUES
(1, 'Fiction', 'fiction', 1),
(2, 'Non-Fiction', 'non-fiction', 1),
(3, 'Children', 'children', 1),
(4, 'Textbooks', 'textbooks', 1),
(5, 'Biography', 'biography', 1);

INSERT INTO products (id, name, description, price, stock_quantity, image_url, category_id) VALUES
(1, 'Things Fall Apart', 'Chinua Achebe''s landmark novel about pre-colonial Igbo society.', 850.00, 12, 'https://covers.openlibrary.org/b/isbn/9780385474542-L.jpg', 1),
(2, 'Petals of Blood', 'Ngugi wa Thiong''o''s story of four villagers in post-independence Kenya.', 950.00, 8, 'https://covers.openlibrary.org/b/isbn/9780143104827-L.jpg', 1),
(3, 'Half of a Yellow Sun', 'Chimamanda Ngozi Adichie brings the Nigerian Civil War to life.', 1100.00, 5, 'https://covers.openlibrary.org/b/isbn/9781400095209-L.jpg', 1),
(4, 'Americanah', 'A razor-sharp look at race, identity, and love.', 1050.00, 7, 'https://covers.openlibrary.org/b/isbn/9780307455925-L.jpg', 1),
(5, 'Long Walk to Freedom', 'Nelson Mandela''s autobiography.', 1200.00, 6, 'https://covers.openlibrary.org/b/isbn/9780316548182-L.jpg', 5),
(6, 'I Am Malala', 'The story of the girl who stood up for education.', 950.00, 9, 'https://covers.openlibrary.org/b/isbn/9780316322423-L.jpg', 5),
(7, 'Sapiens', 'Yuval Noah Harari''s sweeping account of humankind''s history.', 1350.00, 4, 'https://covers.openlibrary.org/b/isbn/9780062316097-L.jpg', 2),
(8, 'Atomic Habits', 'James Clear''s proven framework for building good habits.', 1150.00, 15, 'https://covers.openlibrary.org/b/isbn/9780735211292-L.jpg', 2),
(9, 'The Alchemist', 'Paulo Coelho''s beloved fable about following your dreams.', 800.00, 18, 'https://covers.openlibrary.org/b/isbn/9780062315007-L.jpg', 1),
(10, 'Business Accounting 101', 'A practical introduction to bookkeeping for Kenyan businesses.', 1800.00, 3, 'https://covers.openlibrary.org/b/isbn/9780470533475-L.jpg', 4),
(11, 'Diary of a Wimpy Kid', 'Greg Heffley''s hilarious diary of middle-school survival.', 650.00, 22, 'https://covers.openlibrary.org/b/isbn/9780810993136-L.jpg', 3);