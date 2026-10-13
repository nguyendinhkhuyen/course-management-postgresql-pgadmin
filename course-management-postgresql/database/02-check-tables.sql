-- Chay trong Query Tool cua database course_management SAU khi Spring Boot khoi dong.
SELECT table_name FROM information_schema.tables
WHERE table_schema = 'public' AND table_type = 'BASE TABLE'
ORDER BY table_name;
SELECT * FROM public.coursecategories;
SELECT * FROM public.courses;
SELECT * FROM public.courseimages;
-- Kiem tra quan he khoa ngoai:
SELECT tc.table_name, kcu.column_name, ccu.table_name AS referenced_table
FROM information_schema.table_constraints tc
JOIN information_schema.key_column_usage kcu ON tc.constraint_name = kcu.constraint_name AND tc.table_schema=kcu.table_schema
JOIN information_schema.constraint_column_usage ccu ON tc.constraint_name=ccu.constraint_name AND tc.table_schema=ccu.table_schema
WHERE tc.constraint_type='FOREIGN KEY' AND tc.table_schema='public';
