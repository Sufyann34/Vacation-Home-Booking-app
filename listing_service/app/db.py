from motor.motor_asyncio import AsyncIOMotorClient

import os

uri = os.getenv("MONGO_URI")

# Create a new client and connect to the server
client = AsyncIOMotorClient(uri)

# Send a ping to confirm a successful connection
try:
    client.admin.command('ping')
    print("Pinged your deployment. You successfully connected to MongoDB!")
except Exception as e:
    print(e)