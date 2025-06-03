# FastAPI application entry point - configures CORS and registers API routers
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.api import listings, reviews

app = FastAPI()

# CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Include routers
app.include_router(listings.router)
app.include_router(reviews.router)

@app.get("/")
async def health_check():
    return {"message": "Listing Service Running"}