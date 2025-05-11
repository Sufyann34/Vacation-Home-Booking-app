from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.crud import get_listings, create_listing, get_listing
from app.models import Listing, Create_listing, Mongo_create_listing
from typing import List

app = FastAPI()

# Allow CORS for these origins
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/")
async def root():
    return {"message": "Listing Service Running"}

@app.post("/listings/", response_model=Create_listing)
async def create_one_listing(house: Create_listing):
    listings = await create_listing(Mongo_create_listing(house))
    return listings

@app.get("/listings/", response_model=List[Listing])
async def get_all_listings(index: int):
    listings = await get_listings(index)
    return listings


# @app.get("/listings/get_specific_listing", response_model=Listing)
# async def get_specific_listing(name: str):
#     listings = await get_listing(name)
#     return listings
