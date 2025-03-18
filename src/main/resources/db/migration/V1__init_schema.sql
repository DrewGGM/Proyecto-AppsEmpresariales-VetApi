-- Creación de la base de datos
CREATE DATABASE IF NOT EXISTS vetapi_db;
USE vetapi_db;

-- Creación de tablas para el sistema veterinario

-- Tabla de clientes
CREATE TABLE customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(200),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla de usuarios (veterinarios, admin, recepcionistas)
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    last_access TIMESTAMP NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla de mascotas
CREATE TABLE pets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    birth_date DATE NULL,
    species VARCHAR(50) NOT NULL,
    breed VARCHAR(50) NULL,
    gender VARCHAR(10) NULL,
    weight FLOAT NULL,
    photo_url VARCHAR(255) NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

-- Tabla de consultas
CREATE TABLE consultations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pet_id BIGINT NOT NULL,
    veterinarian_id BIGINT NOT NULL,
    date TIMESTAMP NOT NULL,
    reason VARCHAR(200) NOT NULL,
    diagnosis VARCHAR(500) NULL,
    observations TEXT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (pet_id) REFERENCES pets(id),
    FOREIGN KEY (veterinarian_id) REFERENCES users(id)
);

-- Tabla de vacunaciones
CREATE TABLE vaccinations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pet_id BIGINT NOT NULL,
    veterinarian_id BIGINT NOT NULL,
    vaccine_type VARCHAR(100) NOT NULL,
    application_date DATE NOT NULL,
    next_application_date DATE NULL,
    lot_number VARCHAR(50) NULL,
    observations TEXT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (pet_id) REFERENCES pets(id),
    FOREIGN KEY (veterinarian_id) REFERENCES users(id)
);

-- Tabla de tratamientos
CREATE TABLE treatments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pet_id BIGINT NOT NULL,
    consultation_id BIGINT NOT NULL,
    medication VARCHAR(100) NOT NULL,
    dosage VARCHAR(50) NOT NULL,
    frequency VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    observations TEXT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (pet_id) REFERENCES pets(id),
    FOREIGN KEY (consultation_id) REFERENCES consultations(id)
);

-- Tabla de citas
CREATE TABLE appointments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pet_id BIGINT NOT NULL,
    veterinarian_id BIGINT NOT NULL,
    date_time TIMESTAMP NOT NULL,
    reason VARCHAR(200) NOT NULL,
    status VARCHAR(20) NOT NULL, -- SCHEDULED, COMPLETED, CANCELLED
    confirmed BOOLEAN NOT NULL DEFAULT FALSE,
    observations TEXT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (pet_id) REFERENCES pets(id),
    FOREIGN KEY (veterinarian_id) REFERENCES users(id)
);

-- Tabla de documentos
CREATE TABLE documents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    consultation_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    url VARCHAR(255) NOT NULL,
    size BIGINT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (consultation_id) REFERENCES consultations(id)
);

-- Creación de índices para optimizar consultas
CREATE INDEX idx_customers_email ON customers (email);
CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_pets_customer ON pets (customer_id);
CREATE INDEX idx_consultations_pet ON consultations (pet_id);
CREATE INDEX idx_consultations_veterinarian ON consultations (veterinarian_id);
CREATE INDEX idx_vaccinations_pet ON vaccinations (pet_id);
CREATE INDEX idx_treatments_pet ON treatments (pet_id);
CREATE INDEX idx_treatments_consultation ON treatments (consultation_id);
CREATE INDEX idx_appointments_pet ON appointments (pet_id);
CREATE INDEX idx_appointments_veterinarian ON appointments (veterinarian_id);
CREATE INDEX idx_appointments_datetime ON appointments (date_time);
CREATE INDEX idx_documents_consultation ON documents (consultation_id);

