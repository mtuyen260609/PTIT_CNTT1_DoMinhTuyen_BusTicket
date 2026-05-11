CREATE DATABASE IF NOT EXISTS busticket CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE busticket;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('PASSENGER','STAFF','ADMIN') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_profiles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT UNIQUE NOT NULL,
    full_name VARCHAR(100),
    phone VARCHAR(15),
    email VARCHAR(100),
    address TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS locations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS routes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    departure_id BIGINT NOT NULL,
    arrival_id BIGINT NOT NULL,
    distance INT,
    FOREIGN KEY (departure_id) REFERENCES locations(id),
    FOREIGN KEY (arrival_id) REFERENCES locations(id),
    UNIQUE (departure_id, arrival_id),
    CHECK (departure_id <> arrival_id)
);

CREATE TABLE IF NOT EXISTS buses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    license_plate VARCHAR(20) UNIQUE NOT NULL,
    bus_type VARCHAR(50),
    total_seats INT NOT NULL,
    company VARCHAR(100),
    driver_name VARCHAR(100),
    CHECK (total_seats > 0)
);

CREATE TABLE IF NOT EXISTS trips (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    route_id BIGINT NOT NULL,
    bus_id BIGINT NOT NULL,
    departure_time DATETIME NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (route_id) REFERENCES routes(id),
    FOREIGN KEY (bus_id) REFERENCES buses(id),
    INDEX idx_trips_route_time (route_id, departure_time),
    INDEX idx_trips_bus_time (bus_id, departure_time)
);

CREATE TABLE IF NOT EXISTS seats (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    trip_id BIGINT NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    seat_row INT,
    seat_column INT,
    status ENUM('AVAILABLE','PENDING','BOOKED') DEFAULT 'AVAILABLE',
    version INT DEFAULT 0,
    FOREIGN KEY (trip_id) REFERENCES trips(id),
    UNIQUE (trip_id, seat_number),
    INDEX idx_seats_trip_status (trip_id, status)
);

CREATE TABLE IF NOT EXISTS tickets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ticket_code VARCHAR(20) UNIQUE NOT NULL,
    user_id BIGINT,
    seat_id BIGINT NOT NULL,
    passenger_name VARCHAR(100) NOT NULL,
    passenger_phone VARCHAR(15) NOT NULL,
    passenger_email VARCHAR(100),
    total_price DECIMAL(10,2),
    status ENUM('PENDING','PAID','CANCELLED') DEFAULT 'PENDING',
    booking_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (seat_id) REFERENCES seats(id),
    INDEX idx_tickets_lookup (ticket_code, passenger_phone),
    INDEX idx_tickets_status (status)
);

INSERT IGNORE INTO locations (id, name) VALUES
(1, 'Ha Noi'),
(2, 'Hai Phong'),
(3, 'Nam Dinh'),
(4, 'Ninh Binh'),
(5, 'Thanh Hoa'),
(6, 'Nghe An');

INSERT IGNORE INTO routes (id, departure_id, arrival_id, distance) VALUES
(1, 1, 2, 120),
(2, 1, 3, 95),
(3, 1, 4, 95),
(4, 1, 5, 160),
(5, 1, 6, 290),
(6, 2, 1, 120),
(7, 3, 1, 95);
