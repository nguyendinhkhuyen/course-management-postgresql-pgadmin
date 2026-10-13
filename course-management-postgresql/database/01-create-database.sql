-- Chay trong pgAdmin Query Tool khi dang ket noi database postgres.
-- PostgreSQL khong cho phep CREATE DATABASE trong transaction block.
CREATE DATABASE course_management
    WITH ENCODING = 'UTF8'
    TEMPLATE = template0;
-- Sau do chay ung dung Spring Boot: Hibernate tao 3 bang va du lieu mau.
