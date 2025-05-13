from fastapi import APIRouter, HTTPException, Query
from pymongo.errors import DuplicateKeyError
from app.crud import (  get_listings, create_listing, get_listing_by_id, 
                        update_listing, delete_listing, search_listings, 
                        get_listings_paginated, get_listings_for_user)
from app.models.listing_models import Listing, Create, MongoCreate
from typing import List, Optional


router = APIRouter(prefix="/listings", tags=["listings"])

@router.post("/", response_model=Create)
async def create_listing_endpoint(house: Create):
    try:
        listings = await create_listing(MongoCreate(house))
        return listings
    except DuplicateKeyError:
        # Handle gracefully if same name used twice
        raise HTTPException(status_code=400, detail="Listing with same name already exists.")

@router.get("/", response_model=List[Listing])
async def get_all_listings_endpoint(index: int = 0):
    listings = await get_listings(index)
    return listings

@router.get("/search", response_model=List[Listing])
async def search_listings_endpoint(
    limit: int = 10,
    name: Optional[str] = Query(None),
    property_type: Optional[str] = Query(None),
    min_price: Optional[float] = Query(None),
    max_price: Optional[float] = Query(None)
):

    print('filters being built with:')
    print('name: ', name)
    print('property_type: ', property_type)
    print('min_price: ', min_price)
    print('max_price: ', max_price)
    
    filters = {}
    if name:
        filters["name"] = {"$regex": name, "$options": "i"}
    if property_type:
        filters["property_type"] = property_type
    if min_price is not None or max_price is not None:
        filters["price"] = {}
        if min_price is not None:
            filters["price"]["$gte"] = min_price
        if max_price is not None:
            filters["price"]["$lte"] = max_price

    print('filters are ', filters)

    listings = await search_listings(filters, limit)
    return listings

@router.get("/", response_model=List[Listing])
async def get_listings_paginated_endpoint(skip: int = 0, limit: int = 10):
    listings = await get_listings_paginated(skip, limit)
    return listings

@router.get("/{item_id}", response_model=List[Listing])
async def get_listing_by_id_endpoint(item_id: str):
    listings = await get_listing_by_id(item_id)
    return listings

@router.put("/{item_id}", response_model=Create)
async def update_listing_endpoint(item_id: str, updated_listing: Create):
    updated = await update_listing(item_id, updated_listing)
    return updated

@router.delete("/{item_id}")
async def delete_listing_endpoint(item_id: str):
    deleted = await delete_listing(item_id)
    if deleted:
        return {"message": "Listing deleted"}
    raise HTTPException(status_code=404, detail="Listing not found")

@router.get("/user/{user_id}", response_model=List[Listing])
async def get_listings_for_user_endpoint(user_id: str):
    listings = await get_listings_for_user(user_id)
    return listings