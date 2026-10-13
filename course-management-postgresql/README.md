# Quản lý khóa học – Java Spring Boot + PostgreSQL (pgAdmin 4)

## Công nghệ
Java 17, Spring Boot 3.3.5, Maven, Spring MVC, Thymeleaf, Spring Data JPA/Hibernate, PostgreSQL. **Không dùng H2**.

## Hướng dẫn chạy trên Windows / IntelliJ IDEA

1. Cài **PostgreSQL** và **pgAdmin 4**, ghi nhớ mật khẩu tài khoản `postgres` khi cài.
2. Mở pgAdmin, kết nối server PostgreSQL, nhấp chuột phải **Databases → Create → Database...**, đặt tên **`course_management`**, Save. Hoặc mở Query Tool của database `postgres` rồi chạy `database/01-create-database.sql` (chỉ chạy 1 lần, không chạy nếu đã tạo DB).
3. Mở `src/main/resources/application.properties`. Sửa `spring.datasource.password` thành mật khẩu PostgreSQL trên máy. Mặc định username `postgres`, port `5432`; thay đổi nếu máy bạn khác. **Không chia sẻ mật khẩu khi nộp bài**.
4. IntelliJ: mở **đúng thư mục chứa `pom.xml`**, nhấn **Load Maven**, chọn **JDK 17+**.
5. Chạy `CourseApplication.java` hoặc `mvn spring-boot:run`.
6. Truy cập **http://localhost:8080**. Lần đầu Hibernate sẽ tự tạo 3 bảng và `SeedData` tạo **3 loại, 5 khóa học**.
7. Trong pgAdmin: **Databases → course_management → Schemas → public → Tables**, nhấn Refresh để thấy `courses`, `coursecategories`, `courseimages` (PostgreSQL lưu tên bảng không đặt dấu nháy ở dạng chữ thường). Mở Query Tool của database `course_management` để chạy `database/02-check-tables.sql`.

### Lưu ý
- **pgAdmin là công cụ quản trị**, dữ liệu nằm trên PostgreSQL server.
- `spring.jpa.hibernate.ddl-auto=update`: Hibernate tự tạo/cập nhật bảng từ Entity; **database `course_management` phải được tạo trước**.
- Dữ liệu được lưu trong PostgreSQL và tồn tại sau khi dừng ứng dụng.
- Nếu PostgreSQL báo `password authentication failed`: sửa username/password trong `application.properties`.
- Nếu báo `Connection refused`: kiểm tra PostgreSQL service và port 5432.
- Nếu báo `database "course_management" does not exist`: tạo database theo bước 2.
- Nếu `CREATE DATABASE` báo already exists: database đã có, bỏ qua bước tạo.
- Không chạy lại script tạo DB khi đã có dữ liệu.

## Các chức năng
- Quản lý loại khóa học, không xóa loại còn khóa học.
- Thêm/sửa/xóa/xem chi tiết khóa học; validation tên, học phí, thời lượng, loại; kiểm tra tên trùng trong cùng loại.
- Tìm kiếm, lọc theo loại/trạng thái/khoảng học phí, sắp xếp; phân loại ngắn/dài hạn khi hiển thị.
- Thêm ảnh URL, hiển thị ảnh, chọn/đổi ảnh đại diện.
- Xử lý lỗi thân thiện.

## Các bảng
- `coursecategories`: `id`, `name`, `description`
- `courses`: `id`, `name`, `tuition_fee`, `duration`, `is_active`, `description`, `course_category_id`
- `courseimages`: `id`, `image_url`, `is_thumbnail`, `course_id`

Quan hệ: CourseCategory 1–N Course; Course 1–N CourseImage.
