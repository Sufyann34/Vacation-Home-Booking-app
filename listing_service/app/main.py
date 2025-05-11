from fastapi import FastAPI
from app.crud import get_listings, create_listing, get_listing
from app.models import Listing, Create_listing, Mongo_create_listing
from typing import List

app = FastAPI()

@app.get("/")
async def root():
    return {"message": "Vacation Home Service Running"}

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
