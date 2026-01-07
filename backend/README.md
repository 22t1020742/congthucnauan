# Recipe Backend API

Backend API cho ứng dụng công thức nấu ăn, được xây dựng bằng Spring Boot và MySQL.

## Yêu cầu hệ thống

- Java 17 trở lên
- Maven 3.6+
- MySQL 8.0+
- IntelliJ IDEA hoặc Eclipse (khuyến nghị)

## Cài đặt và chạy

### 1. Cấu hình Database

Tạo database trong MySQL:
```sql
CREATE DATABASE congthucnauan;
```

Sau đó chạy script SQL để tạo các bảng (đã có trong file requirement).

### 2. Cấu hình ứng dụng

Chỉnh sửa file `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/congthucnauan
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

# Spoonacular API
spoonacular.api.key=YOUR_API_KEY
```

### 3. Build và chạy

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Server sẽ chạy tại: `http://localhost:8080`

## API Endpoints

### Authentication

#### Đăng ký
```
POST /api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123",
  "fullName": "Test User"
}
```

#### Đăng nhập
```
POST /api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "testuser",
  "email": "test@example.com",
  "fullName": "Test User"
}
```

#### Lấy thông tin user hiện tại
```
GET /api/auth/me
Authorization: Bearer {token}
```

### Recipes (Công thức nấu ăn - Spoonacular API)

#### Tìm kiếm công thức
```
GET /api/recipes/search?query=pasta&number=10
Optional params: cuisine, diet, type
```

#### Lấy chi tiết công thức
```
GET /api/recipes/{id}
```

#### Lấy công thức ngẫu nhiên
```
GET /api/recipes/random?number=10&tags=vegetarian
```

#### Lấy công thức tương tự
```
GET /api/recipes/{id}/similar?number=4
```

#### Tìm theo nguyên liệu
```
GET /api/recipes/findByIngredients?ingredients=tomato,cheese&number=10
```

#### Autocomplete tìm kiếm
```
GET /api/recipes/autocomplete?query=chick&number=10
```

### Favorites (Yêu thích)

#### Thêm món ăn yêu thích
```
POST /api/favorites
Authorization: Bearer {token}
Content-Type: application/json

{
  "recipeId": "716429",
  "title": "Pasta with Garlic, Scallions",
  "imageUrl": "https://spoonacular.com/recipeImages/716429-312x231.jpg",
  "readyInMinutes": 45,
  "servings": 2,
  "sourceUrl": "http://fullbellysisters.blogspot.com/",
  "summary": "Recipe summary...",
  "instructions": "Recipe instructions..."
}
```

#### Lấy danh sách món ăn yêu thích
```
GET /api/favorites
Authorization: Bearer {token}
```

#### Xóa món ăn yêu thích
```
DELETE /api/favorites/{recipeId}
Authorization: Bearer {token}
```

#### Kiểm tra món ăn đã yêu thích chưa
```
GET /api/favorites/check/{recipeId}
Authorization: Bearer {token}
```

## Cấu trúc Project

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/congthucnauan/
│   │   │   ├── config/           # Security configuration
│   │   │   ├── controller/       # REST Controllers
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   ├── entity/           # JPA Entities
│   │   │   ├── repository/       # Spring Data Repositories
│   │   │   ├── security/         # JWT & Security
│   │   │   ├── service/          # Business Logic
│   │   │   └── RecipeApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

## Công nghệ sử dụng

- Spring Boot 3.2.1
- Spring Security (JWT Authentication)
- Spring Data JPA
- MySQL
- Lombok
- Maven
- Spoonacular Food API

## Lưu ý

- Token JWT có thời hạn 24 giờ (có thể thay đổi trong `application.properties`)
- CORS được cấu hình cho phép tất cả origins (nên giới hạn trong production)
- Password được mã hóa bằng BCrypt
- Spoonacular API có giới hạn request (free tier: 150 requests/day)
- Các endpoint `/api/recipes/**` không cần authentication, còn `/api/favorites/**` cần Bearer token
