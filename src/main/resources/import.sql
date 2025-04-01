-- ROOMS
INSERT INTO rooms (id, number) VALUES ('10000000-0000-0000-0000-000000000101', '101');
INSERT INTO rooms (id, number) VALUES ('10000000-0000-0000-0000-000000000102', '102');
INSERT INTO rooms (id, number) VALUES ('10000000-0000-0000-0000-000000000103', '103');
INSERT INTO rooms (id, number) VALUES ('10000000-0000-0000-0000-000000000104', '104');
INSERT INTO rooms (id, number) VALUES ('10000000-0000-0000-0000-000000000105', '105');
INSERT INTO rooms (id, number) VALUES ('10000000-0000-0000-0000-000000000106', '106');
INSERT INTO rooms (id, number) VALUES ('10000000-0000-0000-0000-000000000201', '201');
INSERT INTO rooms (id, number) VALUES ('10000000-0000-0000-0000-000000000202', '202');
INSERT INTO rooms (id, number) VALUES ('10000000-0000-0000-0000-000000000203', '203');
INSERT INTO rooms (id, number) VALUES ('10000000-0000-0000-0000-000000000204', '204');
INSERT INTO rooms (id, number) VALUES ('10000000-0000-0000-0000-000000000205', '205');
INSERT INTO rooms (id, number) VALUES ('10000000-0000-0000-0000-000000000206', '206');

-- BOOKINGS CONFIRMADOS
INSERT INTO bookings (id, roomId, customerName, checkIn, checkOut) VALUES
('20000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000101', 'Juan Perez', '2025-03-05', '2025-03-10'),
('20000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000102', 'Maria Lopez', '2025-03-12', '2025-03-15'),
('20000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000103', 'Carlos Ruiz', '2025-03-18', '2025-03-22'),
('20000000-0000-0000-0000-000000000004', '10000000-0000-0000-0000-000000000104', 'Luisa Fernandez', '2025-03-25', '2025-03-30'),
('20000000-0000-0000-0000-000000000005', '10000000-0000-0000-0000-000000000105', 'Ana Torres', '2025-04-01', '2025-04-05'),
('20000000-0000-0000-0000-000000000006', '10000000-0000-0000-0000-000000000106', 'Pedro Gomez', '2025-04-10', '2025-04-15'),
('20000000-0000-0000-0000-000000000007', '10000000-0000-0000-0000-000000000201', 'Lucia Morales', '2025-04-17', '2025-04-20'),
('20000000-0000-0000-0000-000000000008', '10000000-0000-0000-0000-000000000202', 'Jorge Nunez', '2025-04-21', '2025-04-25');

-- BOOKING ORDERS CONFIRMADAS
INSERT INTO booking_orders (id, roomId, customerName, checkIn, checkOut, status) VALUES
('30000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000101', 'Juan Perez', '2025-03-05', '2025-03-10', 'CONFIRMED'),
('30000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000102', 'Maria Lopez', '2025-03-12', '2025-03-15', 'CONFIRMED'),
('30000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000103', 'Carlos Ruiz', '2025-03-18', '2025-03-22', 'CONFIRMED'),
('30000000-0000-0000-0000-000000000004', '10000000-0000-0000-0000-000000000104', 'Luisa Fernandez', '2025-03-25', '2025-03-30', 'CONFIRMED'),
('30000000-0000-0000-0000-000000000005', '10000000-0000-0000-0000-000000000105', 'Ana Torres', '2025-04-01', '2025-04-05', 'CONFIRMED'),
('30000000-0000-0000-0000-000000000006', '10000000-0000-0000-0000-000000000106', 'Pedro Gomez', '2025-04-10', '2025-04-15', 'CONFIRMED'),
('30000000-0000-0000-0000-000000000007', '10000000-0000-0000-0000-000000000201', 'Lucia Morales', '2025-04-17', '2025-04-20', 'CONFIRMED'),
('30000000-0000-0000-0000-000000000008', '10000000-0000-0000-0000-000000000202', 'Jorge Nunez', '2025-04-21', '2025-04-25', 'CONFIRMED');

-- BOOKING ORDERS RECHAZADAS POR CONFLICTO
INSERT INTO booking_orders (id, roomId, customerName, checkIn, checkOut, status) VALUES
('40000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000101', 'Martin Diaz', '2025-03-08', '2025-03-12', 'REJECTED'),
('40000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000102', 'Gabriela Santos', '2025-03-14', '2025-03-16', 'REJECTED'),
('40000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000103', 'Rafael Castillo', '2025-03-20', '2025-03-23', 'REJECTED'),
('40000000-0000-0000-0000-000000000004', '10000000-0000-0000-0000-000000000104', 'Sofia Romero', '2025-03-28', '2025-04-02', 'REJECTED');
