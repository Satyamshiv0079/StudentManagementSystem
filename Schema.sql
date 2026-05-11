-- ================================================
--  Student Management System - Database Schema
--  Your University Name
-- ================================================

CREATE DATABASE IF NOT EXISTS student_management;
USE student_management;

CREATE TABLE IF NOT EXISTS students (
                                        id          INT AUTO_INCREMENT PRIMARY KEY,
                                        name        VARCHAR(100)  NOT NULL,
    roll_no     VARCHAR(20)   NOT NULL UNIQUE,
    course      VARCHAR(100)  NOT NULL,
    branch      VARCHAR(100)  NOT NULL,
    semester    INT           NOT NULL CHECK (semester BETWEEN 1 AND 8),
    email       VARCHAR(150)  NOT NULL UNIQUE,
    phone       VARCHAR(15),
    created_at  TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

INSERT INTO students (name, roll_no, course, branch, semester, email, phone) VALUES
                                                                                 ('Satyam Kumar',  'GU2024001', 'B.Tech', 'Computer Science',        3, 'satyam@galgotias.edu',  '9876543210'),
                                                                                 ('Priya Sharma',  'GU2024002', 'B.Tech', 'Electronics Engineering', 3, 'priya@galgotias.edu',   '9876543211'),
                                                                                 ('Rohit Verma',   'GU2024003', 'BCA',    'Computer Applications',   5, 'rohit@galgotias.edu',   '9876543212'),
                                                                                 ('Anjali Singh',  'GU2024004', 'MCA',    'Computer Applications',   1, 'anjali@galgotias.edu',  '9876543213'),
                                                                                 ('Vikram Patel',  'GU2024005', 'B.Tech', 'Mechanical Engineering',  7, 'vikram@galgotias.edu',  '9876543214');