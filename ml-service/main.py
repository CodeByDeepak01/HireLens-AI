
from fastapi import FastAPI
from pydantic import BaseModel
from sentence_transformers import SentenceTransformer, util

app = FastAPI(title="HireLens ML Service")

# Load the pretrained semantic similarity model
model = SentenceTransformer("sentence-transformers/all-MiniLM-L6-v2")


class SimilarityRequest(BaseModel):
    resume_text: str
    job_description: str


@app.get("/")
def home():
    return {"message": "HireLens ML Service is running"}


@app.post("/similarity")
def calculate_similarity(request: SimilarityRequest):

    # Convert resume and job description into embeddings
    resume_embedding = model.encode(request.resume_text)
    job_embedding = model.encode(request.job_description)

    # Calculate cosine similarity
    similarity = util.cos_sim(
        resume_embedding,
        job_embedding
    ).item()

    # Convert similarity into a percentage
    similarity_score = round(max(0, similarity) * 100, 2)

    return {
        "semantic_similarity_score": similarity_score
    }
