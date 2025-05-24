-- Primero verificar si el usuario ya existe
SELECT * FROM users WHERE email = 'admin@vetapi.com';

-- Si existe, borrarlo para volver a crearlo
DELETE FROM users WHERE email = 'admin@vetapi.com';

-- Insertar el usuario admin con todos los campos necesarios
INSERT INTO users (name, last_name, email, password, role, active, created_at, updated_at) 
VALUES (
    'Admin', 
    'Sistema', 
    'admin@vetapi.com', 
    '$2a$10$e63Nf1.ean6Plrwqic56mu4aCierYEArmlvL8S2KmVXAqaDIZFxfW',  -- admin123
    'ADMIN', 
    true,
    NOW(),
    NOW()
);

-- Verificar que se insertó correctamente
SELECT id, name, last_name, email, role, active, created_at 
FROM users 
WHERE email = 'admin@vetapi.com'; 