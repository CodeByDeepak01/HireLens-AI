
import { useState } from "react";
import axios from "axios";
import "./App.css";

const API_BASE_URL = "http://localhost:8080/api";

function App() {
  const [file, setFile] = useState(null);
  const [companyName, setCompanyName] = useState("Tech Solutions");
  const [jobRole, setJobRole] = useState("Java Backend Developer Intern");
  const [jobDescription, setJobDescription] = useState("");

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [result, setResult] = useState(null);
  const [darkMode, setDarkMode] = useState(false);

  const handleFileChange = (event) => {
    const selectedFile = event.target.files?.[0];

    setFile(null);
    setError("");
    setResult(null);

    if (!selectedFile) return;

    if (selectedFile.type !== "application/pdf") {
      setError("Please upload a PDF file only.");
      event.target.value = "";
      return;
    }

    setFile(selectedFile);
  };

  const getResumeId = (data) =>
    data?.resumeId ??
    data?.id ??
    data?.resume?.id ??
    data?.data?.resumeId ??
    data?.data?.id ??
    null;

  const getJobId = (data) =>
    data?.jobId ??
    data?.id ??
    data?.job?.id ??
    data?.data?.jobId ??
    data?.data?.id ??
    null;

  const getAnalysisId = (data) =>
    data?.analysisId ??
    data?.id ??
    data?.analysis?.id ??
    data?.data?.analysisId ??
    data?.data?.id ??
    null;

  const handleAnalyze = async (event) => {
    event.preventDefault();

    setError("");
    setResult(null);

    if (!file) {
      setError("Please select a resume PDF.");
      return;
    }

    if (!companyName.trim() || !jobRole.trim() || !jobDescription.trim()) {
      setError("Please fill in all job details.");
      return;
    }

    setLoading(true);

    try {
      // 1. Upload Resume
      const formData = new FormData();
      formData.append("file", file);

      const uploadResponse = await axios.post(
        `${API_BASE_URL}/resumes/upload`,
        formData
      );

      const resumeId = getResumeId(uploadResponse.data);

      if (resumeId == null) {
        throw new Error("Resume ID was not returned by the backend.");
      }

      // 2. Create Job
      const jobResponse = await axios.post(`${API_BASE_URL}/jobs`, {
        company: companyName.trim(),
        role: jobRole.trim(),
        jobDescription: jobDescription.trim(),
      });

      const jobId = getJobId(jobResponse.data);

      if (jobId == null) {
        throw new Error("Job ID was not returned by the backend.");
      }

      // 3. Create Analysis
      const analysisResponse = await axios.post(
        `${API_BASE_URL}/analyses`,
        {
          resumeId: Number(resumeId),
          jobId: Number(jobId),
        }
      );

      let analysisData = analysisResponse.data;
      const analysisId = getAnalysisId(analysisData);

      // 4. Fetch details if score is not included
      if (
        analysisData?.atsScore == null &&
        analysisId != null
      ) {
        const detailsResponse = await axios.get(
          `${API_BASE_URL}/analyses/${analysisId}`
        );

        analysisData = detailsResponse.data;
      }

      if (analysisData?.atsScore == null) {
        throw new Error("ATS Score was not returned by the backend.");
      }

      setResult(analysisData);
    } catch (err) {
      console.error("Resume Analysis Error:", err);

      if (err.response) {
        setError(
          err.response.data?.message ||
          err.response.data?.error ||
          `Backend error: ${err.response.status}`
        );
      } else {
        setError(
          err.message ||
          "Network Error. Please check the backend connection."
        );
      }
    } finally {
      setLoading(false);
    }
  };

  const formatSkills = (skills) => {
    if (!skills) return [];

    if (Array.isArray(skills)) return skills;

    if (typeof skills === "string") {
      return skills
        .split(",")
        .map((skill) => skill.trim())
        .filter(Boolean);
    }

    return [];
  };

  return (
    <div className={`app ${darkMode ? "dark" : "light"}`}>
      <header className="navbar">
        <div className="nav-container">
          <a href="/" className="brand">
            <span className="brand-icon">H</span>
            <span>HireLens<span className="brand-ai"> AI</span></span>
          </a>

          <button
            className="theme-toggle"
            type="button"
            onClick={() => setDarkMode((prev) => !prev)}
            aria-label="Toggle dark mode"
          >
            {darkMode ? "☀️ Light" : "🌙 Dark"}
          </button>
        </div>
      </header>

      <main className="main-container">
        <section className="hero">
          <span className="eyebrow">AI-POWERED RESUME ANALYZER</span>
          <h1>Make your resume <span>job-ready.</span></h1>
          <p>
            Compare your resume with a job description and discover
            your skills match, gaps, and improvement suggestions.
          </p>
        </section>

        <section className="workspace">
          <div className="section-heading">
            <div>
              <h2>Analyze your resume</h2>
              <p>Upload your resume and enter the target job details.</p>
            </div>
          </div>

          <form onSubmit={handleAnalyze} className="analysis-form">
            <div className="form-group">
              <label htmlFor="resume">Resume PDF</label>

              <div className="upload-box">
                <div className="upload-icon">↑</div>
                <p className="upload-title">
                  {file ? file.name : "Choose your resume"}
                </p>
                <p className="upload-subtitle">
                  PDF format only
                </p>

                <input
                  id="resume"
                  type="file"
                  accept=".pdf,application/pdf"
                  onChange={handleFileChange}
                  disabled={loading}
                />
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label htmlFor="companyName">Company Name</label>
                <input
                  id="companyName"
                  type="text"
                  value={companyName}
                  onChange={(e) => setCompanyName(e.target.value)}
                  placeholder="e.g. Google"
                  disabled={loading}
                />
              </div>

              <div className="form-group">
                <label htmlFor="jobRole">Job Role</label>
                <input
                  id="jobRole"
                  type="text"
                  value={jobRole}
                  onChange={(e) => setJobRole(e.target.value)}
                  placeholder="e.g. Java Backend Developer"
                  disabled={loading}
                />
              </div>
            </div>

            <div className="form-group">
              <label htmlFor="jobDescription">Job Description</label>
              <textarea
                id="jobDescription"
                value={jobDescription}
                onChange={(e) => setJobDescription(e.target.value)}
                placeholder="Paste the job description here..."
                rows={7}
                disabled={loading}
              />
            </div>

            {error && (
              <div className="error-message">
                <span>⚠</span>
                {error}
              </div>
            )}

            <button
              type="submit"
              className="analyze-button"
              disabled={loading}
            >
              {loading ? (
                <>
                  <span className="spinner" />
                  Analyzing Resume...
                </>
              ) : (
                "Analyze Resume →"
              )}
            </button>
          </form>
        </section>

        {result && (
          <section className="results-section">
            <div className="section-heading">
              <div>
                <span className="eyebrow">ANALYSIS COMPLETE</span>
                <h2>Your resume insights</h2>
                <p>Here is how your resume matches this job.</p>
              </div>
            </div>

            <div className="score-card">
              <div>
                <span className="score-label">ATS Match Score</span>
                <h3>{Number(result.atsScore).toFixed(1)}%</h3>
                <p>Estimated keyword match</p>
              </div>

              <div className="score-circle">
                <svg viewBox="0 0 120 120">
                  <circle
                    className="score-track"
                    cx="60"
                    cy="60"
                    r="48"
                  />
                  <circle
                    className="score-progress"
                    cx="60"
                    cy="60"
                    r="48"
                    style={{
                      strokeDashoffset:
                        301.59 -
                        (301.59 * Math.min(Number(result.atsScore), 100)) /
                          100,
                    }}
                  />
                </svg>
                <span className="circle-value">
                  {Math.round(Number(result.atsScore))}%
                </span>
              </div>
            </div>

            <div className="skills-grid">
              <div className="result-card">
                <div className="result-card-heading">
                  <span className="result-icon matched-icon">✓</span>
                  <div>
                    <h3>Matched Skills</h3>
                    <p>Skills found in your resume</p>
                  </div>
                </div>

                <div className="skills-list">
                  {formatSkills(result.matchedSkills).length > 0 ? (
                    formatSkills(result.matchedSkills).map((skill, index) => (
                      <span className="skill-tag matched" key={`${skill}-${index}`}>
                        {skill}
                      </span>
                    ))
                  ) : (
                    <p className="empty-text">No matched skills found.</p>
                  )}
                </div>
              </div>

              <div className="result-card">
                <div className="result-card-heading">
                  <span className="result-icon missing-icon">!</span>
                  <div>
                    <h3>Missing Skills</h3>
                    <p>Skills to consider improving</p>
                  </div>
                </div>

                <div className="skills-list">
                  {formatSkills(result.missingSkills).length > 0 ? (
                    formatSkills(result.missingSkills).map((skill, index) => (
                      <span className="skill-tag missing" key={`${skill}-${index}`}>
                        {skill}
                      </span>
                    ))
                  ) : (
                    <p className="empty-text">No missing skills found.</p>
                  )}
                </div>
              </div>
            </div>

            <div className="result-card recommendations-card">
              <div className="result-card-heading">
                <span className="result-icon recommendation-icon">✦</span>
                <div>
                  <h3>Recommendations</h3>
                  <p>Suggestions to improve your resume match</p>
                </div>
              </div>

              <p className="recommendation-text">
                {result.recommendations ||
                  "No recommendations available."}
              </p>
            </div>
          </section>
        )}
      </main>

      <footer className="footer">
        <div className="footer-brand">HireLens AI</div>
        <p>Resume analysis and career improvement</p>
        <small>
          ATS score is an estimate based on resume keywords.
          It is not an official ATS score or a guarantee of hiring.
        </small>
      </footer>
    </div>
  );
}

export default App;
