from app.db import client


database =  client["sample_airbnb"]
collection = database["listingsAndReviews"]


async def get_listings(index):
    cursor = collection.find(limit = 10, skip = 1 * index)
    return list(cursor)


async def create_listing(listing_data: dict) -> dict:
    pass
#     listing = await listing_collection.insert_one(listing_data)
#     new_listing = await listing_collection.find_one({"_id": listing.inserted_id})
#     return new_listing