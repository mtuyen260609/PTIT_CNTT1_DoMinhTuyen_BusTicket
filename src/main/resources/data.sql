INSERT INTO locations (id, name)
SELECT 1, 'Ha Noi' WHERE NOT EXISTS (SELECT 1 FROM locations WHERE id = 1);

INSERT INTO locations (id, name)
SELECT 2, 'Hai Phong' WHERE NOT EXISTS (SELECT 1 FROM locations WHERE id = 2);

INSERT INTO locations (id, name)
SELECT 3, 'Nam Dinh' WHERE NOT EXISTS (SELECT 1 FROM locations WHERE id = 3);

INSERT INTO locations (id, name)
SELECT 4, 'Ninh Binh' WHERE NOT EXISTS (SELECT 1 FROM locations WHERE id = 4);

INSERT INTO locations (id, name)
SELECT 5, 'Thanh Hoa' WHERE NOT EXISTS (SELECT 1 FROM locations WHERE id = 5);

INSERT INTO locations (id, name)
SELECT 6, 'Nghe An' WHERE NOT EXISTS (SELECT 1 FROM locations WHERE id = 6);

INSERT INTO routes (id, departure_id, arrival_id, distance)
SELECT 1, 1, 2, 120 WHERE NOT EXISTS (SELECT 1 FROM routes WHERE id = 1);

INSERT INTO routes (id, departure_id, arrival_id, distance)
SELECT 2, 1, 3, 95 WHERE NOT EXISTS (SELECT 1 FROM routes WHERE id = 2);

INSERT INTO routes (id, departure_id, arrival_id, distance)
SELECT 3, 1, 4, 95 WHERE NOT EXISTS (SELECT 1 FROM routes WHERE id = 3);

INSERT INTO routes (id, departure_id, arrival_id, distance)
SELECT 4, 1, 5, 160 WHERE NOT EXISTS (SELECT 1 FROM routes WHERE id = 4);

INSERT INTO routes (id, departure_id, arrival_id, distance)
SELECT 5, 1, 6, 290 WHERE NOT EXISTS (SELECT 1 FROM routes WHERE id = 5);

INSERT INTO routes (id, departure_id, arrival_id, distance)
SELECT 6, 2, 1, 120 WHERE NOT EXISTS (SELECT 1 FROM routes WHERE id = 6);

INSERT INTO routes (id, departure_id, arrival_id, distance)
SELECT 7, 3, 1, 95 WHERE NOT EXISTS (SELECT 1 FROM routes WHERE id = 7);
