-- ═══════════════════════════════════════════════════════════
-- Student Record Management System - PostgreSQL Setup Script
-- Run this in pgAdmin or psql BEFORE starting the application.
-- ═══════════════════════════════════════════════════════════

-- Step 1: Create the database
-- (Run this line separately if studentdb does not exist yet)
CREATE DATABASE studentdb;

-- Step 2: Connect to the database, then run the rest:
-- \c studentdb    ← in psql
-- or switch to studentdb in pgAdmin Query Tool

-- Step 3: Create the students table
CREATE TABLE IF NOT EXISTS students (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    course     VARCHAR(50)  NOT NULL,
    year_level VARCHAR(20)  NOT NULL
);

-- Step 4: Insert sample data (optional)
INSERT INTO students (name, course, year_level) VALUES
('Juan dela Cruz',    'BSIT',   '1st Year'),
('Maria Santos',      'BSCS',   '2nd Year'),
('Jose Reyes',        'BSIT',   '3rd Year'),
('Ana Gonzales',      'BSECE',  '1st Year'),
('Carlos Mendoza',    'BSCS',   '4th Year');
