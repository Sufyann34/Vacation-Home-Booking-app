from app.db import client


database =  client["sample_airbnb"]
collection = database["listingsAndReviews"]


async def get_listings(index):
    cursor = collection.find(limit = 10, skip = 1 * index)
    return list(cursor)

async def get_listing(name):
    cursor = collection.find_one({"name": name})
    return cursor

async def create_listing(listing_data):
    cursor = collection.find_one({"name": listing_data['name']})
    if(cursor == None):
        collection.insert_one(listing_data)
        cursor = collection.find_one({"name": listing_data['name']})
    return cursor