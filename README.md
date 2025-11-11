
# 🧑‍💼 Employee Management System (Full Stack Project)

A complete **Employee Management System** built using **Spring Boot**, **React.js**, and **MySQL**.  
This system allows **Admins** to manage employees (CRUD operations), with **secure login (JWT Authentication)** and a clean **React UI** using Tailwind CSS.

---

## 🚀 Features

### 👑 Admin Module
- Admin Login & Authentication (JWT)
- Role-based Access Control
- Manage All Employees

### 👷 Employee Module
- Add, Edit, Delete, View Employees
- Employee List Table with Search & Sort
- Department and Role Management

### 💻 Technical Features
- RESTful APIs using Spring Boot
- React frontend with TailwindCSS
- Axios for API communication
- Spring Security (JWT)
- MySQL database integration

---

## 🧱 Tech Stack

| Layer | Technology |
|--------|-------------|
| **Frontend** | React.js, Tailwind CSS, Axios |
| **Backend** | Spring Boot, Spring Security, JPA, Lombok |
| **Database** | MySQL |
| **Build Tools** | Maven (backend), NPM (frontend) |
| **Authentication** | JWT (JSON Web Token) |

---

## 📁 Folder Structure 




🏗️ Project Overview
Tech Stack:

Backend: Spring Boot (Java 17+)

Frontend: React + Tailwind CSS

Database: MySQL

Build Tool: Maven

REST Communication: Axios / Fetch API

IDE: IntelliJ / VS Code

Server Port: 8080

📁 Folder Structure
🔹 Root Directory
Employee-Management-System/
│
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/aftab/employeemanagement/
│   │   │   │   ├── controller/
│   │   │   │   │   └── EmployeeController.java
│   │   │   │   ├── model/
│   │   │   │   │   └── Employee.java
│   │   │   │   ├── repository/
│   │   │   │   │   └── EmployeeRepository.java
│   │   │   │   ├── service/
│   │   │   │   │   └── EmployeeService.java
│   │   │   │   ├── exception/
│   │   │   │   │   └── ResourceNotFoundException.java
│   │   │   │   └── EmployeeManagementApplication.java
│   │   │   │
│   │   │   ├── resources/
│   │   │   │   ├── application.properties
│   │   │   │   └── data.sql (optional for initial data)
│   │   │
│   │   └── test/
│   │       └── (test classes)
│   │
│   ├── pom.xml
│   └── README.md
│
│
├── frontend/
│   ├── public/
│   │   ├── index.html
│   │   ├── favicon.ico
│   │   └── manifest.json
│   │
│   ├── src/
│   │   ├── assets/
│   │   │   └── logo.png
│   │   │
│   │   ├── components/
│   │   │   ├── Navbar.jsx
│   │   │   ├── EmployeeCard.jsx
│   │   │   └── Loader.jsx
│   │   │
│   │   ├── pages/
│   │   │   ├── Home.jsx
│   │   │   ├── AddEmployee.jsx
│   │   │   ├── EditEmployee.jsx
│   │   │   └── EmployeeList.jsx
│   │   │
│   │   ├── services/
│   │   │   └── employeeService.js
│   │   │
│   │   ├── App.jsx
│   │   ├── index.js
│   │   └── tailwind.config.js
│   │
│   ├── package.json
│   └── README.md
│
│
├── README.md
└── .gitignore
⚙️ Important Files Explained
🧩 Backend – application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/ems_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
server.port=8080
📦 React Service File – employeeService.js
import axios from "axios";

const BASE_URL = "http://localhost:8080/api/employees";

export const getAllEmployees = () => axios.get(BASE_URL);
export const getEmployeeById = (id) => axios.get(`${BASE_URL}/${id}`);
export const addEmployee = (employee) => axios.post(BASE_URL, employee);
export const updateEmployee = (id, employee) => axios.put(`${BASE_URL}/${id}`, employee);
export const deleteEmployee = (id) => axios.delete(`${BASE_URL}/${id}`);
🚀 Backend Endpoints
HTTP Method	Endpoint	Description
GET	/api/employees	Get all employees
GET	/api/employees/{id}	Get employee by ID
POST	/api/employees	Add new employee
PUT	/api/employees/{id}	Update employee
DELETE	/api/employees/{id}	Delete employee
💡 Next Steps
Initialize Spring Boot:
spring init --dependencies=web,jpa,mysql,lombok EmployeeManagement

Setup React App:
npx create-react-app frontend
cd frontend
npm install axios react-router-dom tailwindcss

Run Backend:
mvn spring-boot:run

Run Frontend:
npm run dev



🧩 Backend → Spring Boot
🎨 Frontend → React + TailwindCSS
🗄️ Database → MySQL
🔐 Security → Spring Security (JWT-based Authentication)

🧱 Updated Folder Structure (with Admin Module)
Employee-Management-System/
│
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/aftab/employeemanagement/
│   │   │   │   ├── controller/
│   │   │   │   │   ├── EmployeeController.java
│   │   │   │   │   └── AdminController.java
│   │   │   │   │
│   │   │   │   ├── model/
│   │   │   │   │   ├── Employee.java
│   │   │   │   │   ├── Admin.java
│   │   │   │   │   └── Role.java
│   │   │   │   │
│   │   │   │   ├── repository/
│   │   │   │   │   ├── EmployeeRepository.java
│   │   │   │   │   ├── AdminRepository.java
│   │   │   │   │   └── RoleRepository.java
│   │   │   │   │
│   │   │   │   ├── service/
│   │   │   │   │   ├── EmployeeService.java
│   │   │   │   │   ├── AdminService.java
│   │   │   │   │   └── JwtService.java
│   │   │   │   │
│   │   │   │   ├── security/
│   │   │   │   │   ├── JwtAuthFilter.java
│   │   │   │   │   ├── SecurityConfig.java
│   │   │   │   │   └── CustomUserDetailsService.java
│   │   │   │   │
│   │   │   │   ├── exception/
│   │   │   │   │   └── ResourceNotFoundException.java
│   │   │   │   │
│   │   │   │   └── EmployeeManagementApplication.java
│   │   │   │
│   │   │   ├── resources/
│   │   │   │   └── application.properties
│   │   │
│   │   └── test/
│   │       └── (test files)
│   │
│   ├── pom.xml
│   └── README.md
│
│
├── frontend/
│   ├── public/
│   │   └── index.html
│   │
│   ├── src/
│   │   ├── assets/
│   │   │   └── logo.png
│   │   │
│   │   ├── components/
│   │   │   ├── Navbar.jsx
│   │   │   ├── Sidebar.jsx
│   │   │   └── ProtectedRoute.jsx
│   │   │
│   │   ├── pages/
│   │   │   ├── Home.jsx
│   │   │   ├── EmployeeList.jsx
│   │   │   ├── AddEmployee.jsx
│   │   │   ├── EditEmployee.jsx
│   │   │   ├── AdminDashboard.jsx
│   │   │   ├── Login.jsx
│   │   │   └── RegisterAdmin.jsx
│   │   │
│   │   ├── services/
│   │   │   ├── employeeService.js
│   │   │   ├── adminService.js
│   │   │   └── authService.js
│   │   │
│   │   ├── App.jsx
│   │   ├── index.js
│   │   └── tailwind.config.js
│   │
│   ├── package.json
│   └── README.md
│
└── README.md
⚙️ Important Backend Code (Highlights)
🧩 Admin.java
@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();
}
🔐 SecurityConfig.java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .userDetailsService(customUserDetailsService)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
⚙️ AdminController.java
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/add")
    public ResponseEntity<Admin> addAdmin(@RequestBody Admin admin) {
        return ResponseEntity.ok(adminService.addAdmin(admin));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }
}
🔑 AuthController.java
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        Authentication authentication = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtService.generateToken(request.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
🧩 React Frontend (Auth & Admin Dashboard)
🔑 authService.js
import axios from "axios";
const BASE_URL = "http://localhost:8080/api/auth";

export const login = async (credentials) => {
  const response = await axios.post(`${BASE_URL}/login`, credentials);
  localStorage.setItem("token", response.data.token);
  return response.data;
};
🧠 ProtectedRoute.jsx
import { Navigate } from "react-router-dom";

const ProtectedRoute = ({ children }) => {
  const token = localStorage.getItem("token");
  return token ? children : <Navigate to="/login" />;
};

export default ProtectedRoute;
🖥️ AdminDashboard.jsx
import React, { useEffect, useState } from "react";
import { getAllEmployees } from "../services/employeeService";

export default function AdminDashboard() {
  const [employees, setEmployees] = useState([]);

  useEffect(() => {
    getAllEmployees().then(res => setEmployees(res.data));
  }, []);

  return (
    <div className="p-6">
      <h1 className="text-2xl font-semibold mb-4">Admin Dashboard</h1>
      <table className="w-full border">
        <thead>
          <tr className="bg-gray-200">
            <th>ID</th><th>Name</th><th>Email</th><th>Department</th>
          </tr>
        </thead>
        <tbody>
          {employees.map(e => (
            <tr key={e.id} className="border-b">
              <td>{e.id}</td>
              <td>{e.name}</td>
              <td>{e.email}</td>
              <td>{e.department}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
🗝️ Admin Login Workflow
Admin registers or is created manually in DB.

Admin logs in (/api/auth/login) → gets JWT token.

Token stored in localStorage.

All employee CRUD APIs require valid token.

Admin dashboard displays employee data (authorized access only).

⚡ Summary
✅ Employee CRUD
✅ Admin Authentication (JWT)
✅ Role-based Access (Admin only)
✅ React Protected Routes
✅ MySQL integration
✅ Clean UI using TailwindCSS

Agar chaho to mai poora ready-to-run project ZIP code (backend + frontend + SQL + setup guide) bana du —
sirf ek command se run ho jaye (Spring Boot + React setup).

👉 Kya chahte ho mai ready project code generate karke folder-wise likh du (copy-paste or download use ke liye)?

You said:
Github ke liye readme ke liye define karo 
ChatGPT said:
Perfect 👍 — ab main tumhare Employee Management System (EMS) project ke liye ek professional GitHub-ready README.md bana raha hoon,
jo tum seedha apne repo me daal sakte ho.
Isme overview, tech stack, setup instructions, screenshots placeholder, and API details sab included hain 👇

🧩 README.md (for GitHub)
# 🧑‍💼 Employee Management System (Full Stack Project)

A complete **Employee Management System** built using **Spring Boot**, **React.js**, and **MySQL**.  
This system allows **Admins** to manage employees (CRUD operations), with **secure login (JWT Authentication)** and a clean **React UI** using Tailwind CSS.

---

## 🚀 Features

### 👑 Admin Module
- Admin Login & Authentication (JWT)
- Role-based Access Control
- Manage All Employees

### 👷 Employee Module
- Add, Edit, Delete, View Employees
- Employee List Table with Search & Sort
- Department and Role Management

### 💻 Technical Features
- RESTful APIs using Spring Boot
- React frontend with TailwindCSS
- Axios for API communication
- Spring Security (JWT)
- MySQL database integration

---

## 🧱 Tech Stack

| Layer | Technology |
|--------|-------------|
| **Frontend** | React.js, Tailwind CSS, Axios |
| **Backend** | Spring Boot, Spring Security, JPA, Lombok |
| **Database** | MySQL |
| **Build Tools** | Maven (backend), NPM (frontend) |
| **Authentication** | JWT (JSON Web Token) |

