INSERT INTO public.users (login, password_hash, user_role, is_blocked) VALUES
                                                                           ('johnDoe', 'hashed_password_john', 'READER', FALSE),
                                                                           ('janeSmith', 'hashed_password_jane', 'LIBRARIAN', FALSE),
                                                                           ('adminUser', 'hashed_password_admin', 'ADMIN', FALSE);
INSERT INTO public.edition (edition_name_uk, edition_name_en) VALUES
                                                                  ('Видавництво "Літопис"', 'Chronicle Publishing'),
                                                                  ('Видавництво "Грані"', 'Edges Publishing'),
                                                                  ('Антична Література', 'Ancient Literature');
INSERT INTO public.book (title_uk, description_uk, edition_id, language_uk, publication_date, price_uan, count, title_en, description_en, language_en) VALUES
                                                                                                                                                           ('Захар Беркут', 'Історичний роман про волелюбні карпатські громади', 1, 'Українська', '2023-01-01', 320.00, 12, 'Zahar Berkut', 'A historical novel about the freedom-loving Carpathian communities', 'Ukrainian'),
                                                                                                                                                           ('Кобзар', 'Збірка віршів та поем', 2, 'Українська', '2023-02-01', 280.00, 15, 'Kobzar', 'A collection of poems and ballads', 'Ukrainian'),
                                                                                                                                                           ('Роздуми', 'Філософський трактат Марка Аврелія', 3, 'Латинь', '2023-03-01', 350.00, 20, 'Meditations', 'Philosophical treatise by Marcus Aurelius', 'Latin');
INSERT INTO public.author (full_name_uk, full_name_en) VALUES
                                                           ('Іван Франко', 'Ivan Franko'),
                                                           ('Тарас Шевченко', 'Taras Shevchenko'),
                                                           ('Марк Аврелій', 'Marcus Aurelius');
INSERT INTO public.authorship (author_id, book_id) VALUES
                                                       (1, 1),
                                                       (2, 2),
                                                       (3, 3);
INSERT INTO public.orders (user_id, book_id, start_date, end_date, status) VALUES
                                                                               (1, 1, '2023-04-01', '2023-04-15', 'APPROVED'),
                                                                               (2, 2, '2023-04-05', '2023-04-20', 'RECEIVED'),
                                                                               (1, 3, '2023-05-01', '2023-05-15', 'APPROVED');