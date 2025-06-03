from motor.motor_asyncio import AsyncIOMotorClient

import os

# MongoDB database connection setup using Motor async driver

uri = os.getenv("MONGO_URI")

# Create a new client and connect to the server
client = AsyncIOMotorClient(uri)

collection = client["sample_airbnb"]["listingsAndReviews"]


# Send a ping to confirm a successful connection
try:
    client.admin.command('ping')
    print("Pinged your deployment. You successfully connected to MongoDB!")
except Exception as e:
    print(e)