-- Insertar usuarios de prueba
-- Contraseña: securepassword123 (bcrypt hash)
INSERT INTO users (name, last_name, email, password, role, created_at, active) VALUES
('John', 'Doe', 'john.doe@example.com', '$2a$10$/7HbiDcEI3gqQVZNYmvyT./D3XXKB.q3zm56Xb9SQC08XupfbIReS', 'ADMIN', GETDATE(), 1),
('Jane', 'Smith', 'jane.smith@example.com', '$2a$10$/7HbiDcEI3gqQVZNYmvyT./D3XXKB.q3zm56Xb9SQC08XupfbIReS', 'VETERINARIAN', GETDATE(), 1),
('Maria', 'Garcia', 'maria.garcia@example.com', '$2a$10$/7HbiDcEI3gqQVZNYmvyT./D3XXKB.q3zm56Xb9SQC08XupfbIReS', 'RECEPTIONIST', GETDATE(), 1);

-- Insertar clientes de prueba
INSERT INTO customers (name, last_name, email, phone, address, created_at, active) VALUES
('Carlos', 'Rodriguez', 'carlos.rodriguez@example.com', '1234567890', 'Calle Principal 123', GETDATE(), 1),
('Ana', 'Martinez', 'ana.martinez@example.com', '0987654321', 'Avenida Central 456', GETDATE(), 1),
('Pedro', 'Sanchez', 'pedro.sanchez@example.com', '5551234567', 'Calle Secundaria 789', GETDATE(), 1);

-- Insertar mascotas de prueba
INSERT INTO pets (customer_id, name, birth_date, species, breed, gender, weight, photo_url, created_at, active) VALUES
(1, 'Firulais', '2019-05-15', 'Canino', 'Labrador', 'Macho', 27.5, 'https://example.com/photos/firulais.jpg', GETDATE(), 1),
(1, 'Luna', '2021-03-10', 'Canino', 'Golden Retriever', 'Hembra', 22.0, 'https://example.com/photos/luna.jpg', GETDATE(), 1),
(2, 'Michi', '2020-07-20', 'Felino', 'Siamés', 'Hembra', 4.5, 'https://example.com/photos/michi.jpg', GETDATE(), 1),
(3, 'Rocky', '2018-11-05', 'Canino', 'Bulldog', 'Macho', 15.0, 'https://example.com/photos/rocky.jpg', GETDATE(), 1);

-- Insertar consultas de prueba (fechas históricas para evitar validaciones de fecha futura)
INSERT INTO consultations (pet_id, veterinarian_id, date, reason, diagnosis, observations, created_at, active) VALUES
(1, 2, '2023-06-15 10:30:00', 'Control rutinario', 'Excelente estado de salud', 'Continuar con alimentación actual', GETDATE(), 1),
(1, 2, '2023-07-10 14:00:00', 'Vacunación anual', 'Vacunación completada', 'Próxima vacuna en un año', GETDATE(), 1),
(3, 2, '2023-06-20 09:15:00', 'Problemas digestivos', 'Gastritis leve', 'Dieta blanda por 3 días', GETDATE(), 1),
(4, 2, '2023-07-05 16:30:00', 'Revisión de piel', 'Dermatitis alérgica', 'Tratamiento con crema tópica', GETDATE(), 1);

-- Insertar vacunaciones de prueba
INSERT INTO vaccinations (pet_id, veterinarian_id, vaccine_type, application_date, next_application_date, lot_number, observations, created_at, active) VALUES
(1, 2, 'Rabia', '2023-06-15', '2024-06-15', 'RAB-2023-001', 'Aplicación sin complicaciones', GETDATE(), 1),
(2, 2, 'Parvovirus', '2023-05-20', '2024-05-20', 'PAR-2023-002', 'Primera dosis', GETDATE(), 1),
(3, 2, 'Triple Felina', '2023-06-22', '2024-06-22', 'TF-2023-003', 'Aplicación sin complicaciones', GETDATE(), 1),
(4, 2, 'Rabia', '2023-07-05', '2024-07-05', 'RAB-2023-004', 'Aplicación sin complicaciones', GETDATE(), 1);

-- Insertar tratamientos de prueba
INSERT INTO treatments (pet_id, consultation_id, medication, dosage, frequency, start_date, end_date, completed, observations, created_at, active) VALUES
(1, 1, 'Vitaminas', '5ml', 'Una vez al día', '2023-06-15', '2023-07-15', 1, 'Administrar con alimentos', GETDATE(), 1),
(3, 3, 'Omeprazol', '2ml', 'Dos veces al día', '2023-06-20', '2023-06-27', 1, 'Administrar antes de las comidas', GETDATE(), 1),
(4, 4, 'Crema dermatológica', 'Aplicación tópica', 'Tres veces al día', '2023-07-05', '2023-07-19', 0, 'Aplicar en zonas afectadas', GETDATE(), 1);

-- Insertar citas de prueba (fechas futuras)
-- En SQL Server usamos DATEADD para calcular fechas futuras
INSERT INTO appointments (pet_id, veterinarian_id, date_time, reason, status, confirmed, created_at, active) VALUES
(1, 2, DATEADD(DAY, 7, GETDATE()), 'Control de seguimiento', 'PENDING', 1, GETDATE(), 1),
(2, 2, DATEADD(DAY, 10, GETDATE()), 'Vacunación programada', 'PENDING', 0, GETDATE(), 1),
(3, 2, DATEADD(DAY, 5, GETDATE()), 'Control digestivo', 'PENDING', 1, GETDATE(), 1);

-- Insertar documentos de prueba (URLs ficticias)
INSERT INTO documents (consultation_id, name, type, url, size, created_at, active) VALUES
(1, 'radiografia_firulais.jpg', 'image/jpeg', 'https://vetapi-storage.example.com/consultations/1/radiografia_firulais.jpg', 250000, GETDATE(), 1),
(3, 'analisis_sangre_michi.pdf', 'application/pdf', 'https://vetapi-storage.example.com/consultations/3/analisis_sangre_michi.pdf', 420000, GETDATE(), 1),
(4, 'dermatitis_rocky.jpg', 'image/jpeg', 'https://vetapi-storage.example.com/consultations/4/dermatitis_rocky.jpg', 310000, GETDATE(), 1);