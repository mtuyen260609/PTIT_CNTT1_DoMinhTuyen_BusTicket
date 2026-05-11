-- Clean expired trips and their dependent rows.
DELETE t
FROM tickets t
JOIN seats s ON s.id = t.seat_id
JOIN trips tr ON tr.id = s.trip_id
WHERE tr.departure_time < NOW();

DELETE s
FROM seats s
JOIN trips tr ON tr.id = s.trip_id
WHERE tr.departure_time < NOW();

DELETE FROM trips
WHERE departure_time < NOW();

-- Fixed locations.
INSERT INTO locations (name) VALUES
('Ha Noi'),
('Hai Phong'),
('Nam Dinh'),
('Ninh Binh'),
('Thanh Hoa'),
('Nghe An'),
('Da Nang'),
('Hue')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Fixed route catalog.
INSERT INTO routes (departure_id, arrival_id, distance)
SELECT d.id, a.id, x.distance
FROM (
    SELECT 'Ha Noi' departure_name, 'Hai Phong' arrival_name, 120 distance UNION ALL
    SELECT 'Hai Phong', 'Ha Noi', 120 UNION ALL
    SELECT 'Ha Noi', 'Nam Dinh', 95 UNION ALL
    SELECT 'Nam Dinh', 'Ha Noi', 95 UNION ALL
    SELECT 'Ha Noi', 'Ninh Binh', 95 UNION ALL
    SELECT 'Ninh Binh', 'Ha Noi', 95 UNION ALL
    SELECT 'Ha Noi', 'Thanh Hoa', 160 UNION ALL
    SELECT 'Thanh Hoa', 'Ha Noi', 160 UNION ALL
    SELECT 'Ha Noi', 'Nghe An', 290 UNION ALL
    SELECT 'Nghe An', 'Ha Noi', 290 UNION ALL
    SELECT 'Da Nang', 'Hue', 100 UNION ALL
    SELECT 'Hue', 'Da Nang', 100
) x
JOIN locations d ON d.name = x.departure_name
JOIN locations a ON a.name = x.arrival_name
ON DUPLICATE KEY UPDATE distance = VALUES(distance);

-- Fixed bus fleet.
INSERT INTO buses (license_plate, bus_type, total_seats, company, driver_name) VALUES
('29B-12345', 'Limousine 20 chỗ', 20, 'Bus Ticket Pro', 'Nguyen Van A'),
('30F-67890', 'Giường nằm 40 chỗ', 40, 'Bus Ticket Pro', 'Tran Van B'),
('30A-10001', 'Xe 45 chỗ', 45, 'Bus Ticket Pro', 'Le Van C'),
('30A-10002', 'Xe 29 chỗ', 29, 'Bus Ticket Pro', 'Pham Van D'),
('30A-10003', 'Xe 16 chỗ', 16, 'Bus Ticket Pro', 'Hoang Van E'),
('30A-10004', 'Limousine 20 chỗ', 20, 'Bus Ticket Pro', 'Do Van F'),
('30A-10005', 'Giường nằm 40 chỗ', 40, 'Bus Ticket Pro', 'Bui Van G'),
('30A-10006', 'Xe 45 chỗ', 45, 'Bus Ticket Pro', 'Dang Van H'),
('30A-10007', 'Xe 29 chỗ', 29, 'Bus Ticket Pro', 'Vo Van I'),
('30A-10008', 'Xe 16 chỗ', 16, 'Bus Ticket Pro', 'Ngo Van K'),
('30A-10009', 'Limousine 20 chỗ', 20, 'Bus Ticket Pro', 'Ly Van L'),
('30A-10010', 'Giường nằm 40 chỗ', 40, 'Bus Ticket Pro', 'Mai Van M'),
('30A-10011', 'Xe 45 chỗ', 45, 'Bus Ticket Pro', 'Trinh Van N'),
('30A-10012', 'Xe 29 chỗ', 29, 'Bus Ticket Pro', 'Cao Van P'),
('30A-10013', 'Limousine 20 chỗ', 20, 'Bus Ticket Pro', 'Ta Van Q')
ON DUPLICATE KEY UPDATE
    bus_type = VALUES(bus_type),
    total_seats = VALUES(total_seats),
    company = VALUES(company),
    driver_name = VALUES(driver_name);

-- Remove vehicles outside the fixed fleet and their dependent operational data.
DELETE t
FROM tickets t
JOIN seats s ON s.id = t.seat_id
JOIN trips tr ON tr.id = s.trip_id
JOIN buses b ON b.id = tr.bus_id
WHERE b.license_plate NOT IN (
    '29B-12345','30F-67890','30A-10001','30A-10002','30A-10003',
    '30A-10004','30A-10005','30A-10006','30A-10007','30A-10008',
    '30A-10009','30A-10010','30A-10011','30A-10012','30A-10013'
);

DELETE s
FROM seats s
JOIN trips tr ON tr.id = s.trip_id
JOIN buses b ON b.id = tr.bus_id
WHERE b.license_plate NOT IN (
    '29B-12345','30F-67890','30A-10001','30A-10002','30A-10003',
    '30A-10004','30A-10005','30A-10006','30A-10007','30A-10008',
    '30A-10009','30A-10010','30A-10011','30A-10012','30A-10013'
);

DELETE tr
FROM trips tr
JOIN buses b ON b.id = tr.bus_id
WHERE b.license_plate NOT IN (
    '29B-12345','30F-67890','30A-10001','30A-10002','30A-10003',
    '30A-10004','30A-10005','30A-10006','30A-10007','30A-10008',
    '30A-10009','30A-10010','30A-10011','30A-10012','30A-10013'
);

DELETE FROM buses
WHERE license_plate NOT IN (
    '29B-12345','30F-67890','30A-10001','30A-10002','30A-10003',
    '30A-10004','30A-10005','30A-10006','30A-10007','30A-10008',
    '30A-10009','30A-10010','30A-10011','30A-10012','30A-10013'
);

CREATE TEMPORARY TABLE fixed_trip_seed (
    departure_name VARCHAR(100) NOT NULL,
    arrival_name VARCHAR(100) NOT NULL,
    license_plate VARCHAR(20) NOT NULL,
    day_offset INT NOT NULL,
    departure_clock TIME NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);

INSERT INTO fixed_trip_seed (departure_name, arrival_name, license_plate, day_offset, departure_clock, price) VALUES
('Ha Noi', 'Hai Phong', '29B-12345', 0, '08:00:00', 120000),
('Ha Noi', 'Hai Phong', '30A-10004', 0, '14:30:00', 120000),
('Ha Noi', 'Nam Dinh', '30F-67890', 0, '09:15:00', 100000),
('Hai Phong', 'Ha Noi', '30A-10002', 0, '17:30:00', 120000),
('Ha Noi', 'Ninh Binh', '30A-10001', 0, '10:00:00', 110000),
('Ha Noi', 'Thanh Hoa', '30A-10005', 0, '13:00:00', 160000),
('Ha Noi', 'Nghe An', '30A-10006', 0, '20:00:00', 260000),
('Da Nang', 'Hue', '30A-10009', 0, '07:30:00', 90000),

('Ha Noi', 'Hai Phong', '30A-10007', 1, '08:00:00', 120000),
('Ha Noi', 'Hai Phong', '30A-10011', 1, '14:30:00', 120000),
('Ha Noi', 'Nam Dinh', '30A-10010', 1, '09:15:00', 100000),
('Hai Phong', 'Ha Noi', '30A-10008', 1, '17:30:00', 120000),
('Ha Noi', 'Ninh Binh', '30A-10012', 1, '10:00:00', 110000),
('Ha Noi', 'Thanh Hoa', '30A-10013', 1, '13:00:00', 160000),
('Ha Noi', 'Nghe An', '30A-10003', 1, '20:00:00', 260000),
('Hue', 'Da Nang', '30A-10009', 1, '16:00:00', 90000),

('Ha Noi', 'Hai Phong', '29B-12345', 2, '08:00:00', 120000),
('Ha Noi', 'Hai Phong', '30A-10004', 2, '14:30:00', 120000),
('Ha Noi', 'Nam Dinh', '30F-67890', 2, '09:15:00', 100000),
('Hai Phong', 'Ha Noi', '30A-10002', 2, '17:30:00', 120000),
('Ha Noi', 'Ninh Binh', '30A-10001', 2, '10:00:00', 110000),
('Ha Noi', 'Thanh Hoa', '30A-10005', 2, '13:00:00', 160000),
('Ha Noi', 'Nghe An', '30A-10006', 2, '20:00:00', 260000),
('Da Nang', 'Hue', '30A-10009', 2, '07:30:00', 90000),

('Ha Noi', 'Hai Phong', '30A-10007', 3, '08:00:00', 120000),
('Ha Noi', 'Hai Phong', '30A-10011', 3, '14:30:00', 120000),
('Ha Noi', 'Nam Dinh', '30A-10010', 3, '09:15:00', 100000),
('Hai Phong', 'Ha Noi', '30A-10008', 3, '17:30:00', 120000),
('Ha Noi', 'Ninh Binh', '30A-10012', 3, '10:00:00', 110000),
('Ha Noi', 'Thanh Hoa', '30A-10013', 3, '13:00:00', 160000),
('Ha Noi', 'Nghe An', '30A-10003', 3, '20:00:00', 260000),
('Hue', 'Da Nang', '30A-10009', 3, '16:00:00', 90000);

INSERT INTO trips (route_id, bus_id, departure_time, price)
SELECT r.id,
       b.id,
       TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL s.day_offset DAY), s.departure_clock),
       s.price
FROM fixed_trip_seed s
JOIN locations d ON d.name = s.departure_name
JOIN locations a ON a.name = s.arrival_name
JOIN routes r ON r.departure_id = d.id AND r.arrival_id = a.id
JOIN buses b ON b.license_plate = s.license_plate
WHERE TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL s.day_offset DAY), s.departure_clock) > NOW()
  AND NOT EXISTS (
      SELECT 1
      FROM trips existing
      WHERE existing.bus_id = b.id
        AND existing.departure_time = TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL s.day_offset DAY), s.departure_clock)
  );

INSERT INTO seats (trip_id, seat_number, seat_row, seat_column, status, version)
WITH RECURSIVE seq(n) AS (
    SELECT 1
    UNION ALL
    SELECT n + 1 FROM seq WHERE n < 80
)
SELECT tr.id,
       CONCAT('A', seq.n),
       FLOOR((seq.n - 1) / IF(b.total_seats > 20, 4, 2)) + 1,
       MOD(seq.n - 1, IF(b.total_seats > 20, 4, 2)) + 1,
       'AVAILABLE',
       0
FROM trips tr
JOIN buses b ON b.id = tr.bus_id
JOIN seq ON seq.n <= b.total_seats
WHERE NOT EXISTS (
    SELECT 1
    FROM seats existing
    WHERE existing.trip_id = tr.id
      AND existing.seat_number = CONCAT('A', seq.n)
);

DROP TEMPORARY TABLE fixed_trip_seed;
