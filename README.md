#  Library Management System


##  Features

### Core Functionalities

- **Book Management**
    - Add, update, delete, and search books
    - Track ISBN, title, author, publisher, category, and availability
    - Real-time availability tracking
    - Advanced search by title, author, ISBN, or category

- **Member Management**
    - Register and manage library members
    - Track member status (Active, Inactive, Suspended)
    - Store enrollment dates and contact information
    - Search members by name, student ID, or email

- **Issue & Return System**
    - Issue books to members with automatic due date calculation (14-day loan period)
    - Process book returns with validation
    - Track active and returned books
    - View overdue books

- **Fine Management**
    - Automatic fine calculation for overdue books (₹5 per day)
    - Track paid and unpaid fines
    - Generate fine reports
    - Process fine payments

- **Authentication & Security**
    - Admin registration and login
    - BCrypt password encryption
    - Secure session management

### Rules & Conditions

- Maximum 5 books per member
- 14-day loan period
- ₹5 per day late fee
- Prevent duplicate book issuance to same member
- Real-time book availability tracking
- Member status-based access control

## Tech Stack

### Backend
- **Framework:** Spring Boot 3.2.0
- **Language:** Java 17
- **Build Tool:** Maven 3.8+
- **Database:** MySQL 8.0
- **ORM:** Spring Data JPA (Hibernate)
- **Security:** Spring Security with BCrypt

### Key Dependencies
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Spring Boot Starter Validation
- MySQL Connector
- Lombok
- Spring Boot DevTools


##  Database Schema

```sql
┌──────────────┐         ┌──────────────┐         ┌──────────────┐
│    admin     │         │   members    │         │    books     │
├──────────────┤         ├──────────────┤         ├──────────────┤
│ admin_id (PK)│         │ member_id(PK)│         │ book_id (PK) │
│ username     │         │ student_id   │         │ isbn         │
│ password     │         │ full_name    │         │ title        │
│ email        │         │ email        │         │ author       │
│ full_name    │         │ phone        │         │ publisher    │
│ created_at   │         │ address      │         │ category     │
└──────────────┘         │ enrollment   │         │ total_copies │
                         │ status       │         │ available    │
                         │ created_at   │         │ shelf_loc    │
                         └──────────────┘         └──────────────┘
                                │                        │
                                │                        │
                                └────────┬───────────────┘
                                         │
                                  ┌──────────────┐
                                  │ book_issues  │
                                  ├──────────────┤
                                  │ issue_id(PK) │
                                  │ book_id (FK) │
                                  │ member_id(FK)│
                                  │ issue_date   │
                                  │ due_date     │
                                  │ return_date  │
                                  │ fine_amount  │
                                  │ status       │
                                  └──────────────┘
                                         │
                                         │
                                  ┌──────────────┐
                                  │    fines     │
                                  ├──────────────┤
                                  │ fine_id (PK) │
                                  │ issue_id(FK) │
                                  │ fine_amount  │
                                  │ fine_reason  │
                                  │ paid_status  │
                                  │ payment_date │
                                  └──────────────┘
```



### API Endpoints

#### Register Admin
```http
POST /api/auth/register
```

#### Login
```http
POST /api/auth/login
```

#### Book Management APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/books` | Get all books |
| GET | `/api/books/{id}` | Get book by ID |
| GET | `/api/books/isbn/{isbn}` | Get book by ISBN |
| GET | `/api/books/search?query={keyword}` | Search books |
| GET | `/api/books/available` | Get available books |
| POST | `/api/books` | Add new book |
| PUT | `/api/books/{id}` | Update book |
| DELETE | `/api/books/{id}` | Delete book |

#### Member Management APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/members` | Get all members |
| GET | `/api/members/{id}` | Get member by ID |
| GET | `/api/members/student/{studentId}` | Get member by student ID |
| GET | `/api/members/search?query={keyword}` | Search members |
| POST | `/api/members` | Add new member |
| PUT | `/api/members/{id}` | Update member |
| DELETE | `/api/members/{id}` | Delete member |

#### Issue & Return APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/issues` | Get all issues |
| GET | `/api/issues/member/{memberId}` | Get issues by member |
| GET | `/api/issues/overdue` | Get overdue issues |
| POST | `/api/issues/issue?bookId={id}&memberId={id}` | Issue a book |
| PUT | `/api/issues/return/{issueId}` | Return a book |

#### Fine Management APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/fines` | Get all fines |
| GET | `/api/fines/unpaid` | Get unpaid fines |
| GET | `/api/fines/member/{memberId}` | Get fines by member |
| PUT | `/api/fines/{fineId}/pay` | Pay a fine |



## Author

- **Sambita Khuntia**
- Email: somyasambita@gmail.com
