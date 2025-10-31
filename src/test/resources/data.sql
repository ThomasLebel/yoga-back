INSERT INTO TEACHERS (first_name, last_name)
VALUES ('Margot', 'DELAHAYE'),
       ('Hélène', 'THIERCELIN');

INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES ('Admin', 'Admin', true, 'yoga@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq'),
       ('John', 'Doe', false, 'john@gmail.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq');

INSERT INTO SESSIONS (name, description, date, teacher_id)
VALUES ('Hatha Yoga', 'Seance relaxante de Hatha Yoga pour debutants', '2025-10-24 08:30:00', 1),
       ('Yin Yoga', 'Séance douce de Yin Yoga pour étirer et relâcher les tensions', '2025-10-25 18:00:00', 1),
       ('Power Yoga', 'Séance intense de Power Yoga pour brûler des calories', '2025-10-26 20:00:00', 2);