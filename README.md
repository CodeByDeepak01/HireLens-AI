# HireLens AI 🤖

### AI-Powered Resume Analyzer & Job Matching Platform

HireLens AI is an intelligent resume analysis platform that helps job seekers evaluate their resumes against job descriptions, identify skill gaps, and improve their chances of passing Applicant Tracking Systems (ATS).

## 🚀 Features

- Resume Upload and Analysis
- ATS Score Generation
- Resume and Job Description Matching
- Skills Gap Identification
- AI-Powered Recommendations
- Semantic Similarity Analysis

## 🛠️ Tech Stack

| Component | Technologies |
|---|---|
| Frontend | React.js, Vite, JavaScript, CSS |
| Backend | Java, Spring Boot, Spring Security |
| Database | PostgreSQL |
| AI / ML Service | Python, FastAPI |
| AI Model | Ollama, Qwen 2.5 |
| Build Tools | Maven, npm |
| Version Control | Git, GitHub |

## 📁 Project Structure

```text
HireLens-AI/
├── hirelens-backend/
├── hirelens-frontend/
├── ml-service/
└── .gitignore
```

## ⚙️ Prerequisites

- Java JDK
- Maven
- Node.js and npm
- Python
- PostgreSQL
- Ollama

## 💻 Installation and Setup

### 1. Clone Repository

```bash
git clone https://github.com/CodeByDeepak01/HireLens-AI.git
cd HireLens-AI
```

### 2. Backend Setup

```bash
cd hirelens-backend
```

Configure PostgreSQL credentials using environment variables:

- `POSTGRES_DB_USERNAME`
- `POSTGRES_DB_PASSWORD`

Run the Spring Boot application using Maven.

### 3. Frontend Setup

```bash
cd hirelens-frontend
npm install
npm run dev
```

### 4. ML Service Setup

```bash
cd ml-service
python -m venv .venv
```

Activate the virtual environment on Windows:

```powershell
.\.venv\Scripts\Activate.ps1
```

Install dependencies and start FastAPI:

```bash
pip install -r requirements.txt
python -m uvicorn main:app --reload
```

## 👨‍💻 Author

**Deepak**

GitHub: [CodeByDeepak01](https://github.com/CodeByDeepak01)

---

⭐ If you find this project interesting, consider giving it a star!
