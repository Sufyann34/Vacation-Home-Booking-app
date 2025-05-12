from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pymongo.errors import DuplicateKeyError
from app.crud import get_listings, create_listing, get_listing_by_id, update_listing, delete_listing
from app.models import Listing, Create, MongoCreate
from typing import List

app = FastAPI()

# Allow CORS for alll origins
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/")
async def health_check():
    return {"message": "Listing Service Running"}

@app.post("/listings/", response_model=Create)
async def create_listing_endpoint(house: Create):
    try:
        listings = await create_listing(MongoCreate(house))
        return listings
    except DuplicateKeyError:
        # Handle gracefully if same name used twice
        raise HTTPException(status_code=400, detail="Listing with same name already exists.")

@app.get("/listings/", response_model=List[Listing])
async def get_all_listings_endpoint(index: int):
    listings = await get_listings(index)
    return listings

@app.get("/listings/{item_id}", response_model=Listing)
async def get_listing_by_id_endpoint(item_id: str):
    listings = await get_listing_by_id(item_id)
    return listings

@app.put("/listings/{item_id}", response_model=Create)
async def update_listing_endpoint(item_id: str, updated_listing: Create):
    updated = await update_listing_in_db(item_id, updated_listing)
    if updated:
        return updated
    raise HTTPException(status_code=404, detail="Listing not found")

@app.delete("/listings/{item_id}")
async def delete_listing_endpoint(item_id: str):
    deleted = await delete_listing_from_db(item_id)
    if deleted:
        return {"message": "Listing deleted"}
    raise HTTPException(status_code=404, detail="Listing not found")

