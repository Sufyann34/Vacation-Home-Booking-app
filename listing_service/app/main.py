from fastapi import FastAPI
from app.crud import get_listings, create_listing
from app.models import Listing
from typing import List

app = FastAPI()

@app.get("/")
async def root():
    return {"message": "Vacation Home Service Running"}

@app.get("/listings/", response_model=List[Listing])
async def get_all_listings(index: int):
    listings = await get_listings(index)
    return listings
