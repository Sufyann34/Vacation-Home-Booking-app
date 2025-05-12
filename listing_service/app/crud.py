from app.db import client
from app.models import Create
from app.hash import generate_custom_id

database =  client["sample_airbnb"]
collection = database["listingsAndReviews"]

async def get_listings(index):
    cursor = collection.find(limit = 10, skip = 1 * index)
    return list(cursor)

async def get_listing_by_id(id):
    cursor = collection.find_one({"_id": id})
    return cursor

async def create_listing(listing_data: Create) -> dict:
    custom_id = generate_custom_id(listing_data['name'])
    document = listing_data
    document["_id"] = custom_id
    collection.insert_one(document)
    return document

async def update_listing(item_id: str, new_data: Create):
    result = await collection.find_one_and_update(
        {"_id": item_id},
        {"$set": new_data.dict()},
        return_document=True
    )
    return result

async def delete_listing(item_id: str):
    result = await collection.delete_one({"_id": item_id})
    return result.deleted_count > 0
