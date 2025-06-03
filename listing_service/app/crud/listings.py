# Database operations for listing management (CRUD and search)
from app.database import collection
from ..models.listing_models import Create
from app.hash import generate_custom_id
from typing import Optional


async def get_listing_by_id(id):
    cursor = collection.find({"_id": id})
    listings = await cursor.to_list(length = 1)
    return listings

async def create_listing(listing_data: dict) -> dict:
    custom_id = generate_custom_id(listing_data['name'])
    document = listing_data
    document["_id"] = custom_id
    collection.insert_one(document)
    return document

async def update_listing(item_id: str, new_data: Create):
    result = collection.find_one_and_update(
        {"_id": item_id},
        {"$set": new_data.dict(exclude_unset=True)},
        return_document=True
    )
    return await result

async def delete_listing(item_id: str):
    result = await collection.delete_one({"_id": item_id})
    return result.deleted_count > 0

async def search_listings(
        filters: dict, 
        page: int, 
        limit: int, 
        sort_by: Optional[str] = None,
        sort_order: Optional[int] = 1
    ):
    skip = (page - 1) * limit
    listings = collection.find(filters).skip(skip).limit(limit)
    if(sort_by):
        listings = listings.sort(sort_by, sort_order)
    listing = await listings.to_list(length = limit)
    return listing

async def get_listings_paginated(page: int, limit: int):
    listing = collection.find(limit = limit, skip = (page - 1) * limit)
    listings = await listing.to_list(length = limit)
    return listings

async def get_listings_for_user(user_id: str):
    results = []
    async for listing in collection.find({"user_id": user_id}):
        results.append(listing)
    return results
