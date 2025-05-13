from typing import List, Optional
from app.models import Review
from app.database import collection  # Assuming you have a database connection setup

async def add_review(listing_id: str, review: Review) -> bool:
    """
    Add a review to a listing
    Returns True if successful, False if listing not found
    """
    result = await collection.update_one(
        {"_id": listing_id},
        {"$push": {"reviews": review.dict()}}
    )
    return result.modified_count > 0

async def get_reviews(listing_id: str) -> List[Review]:
    """
    Get all reviews for a listing
    Returns empty list if no reviews or listing not found
    """
    listing = await collection.find_one({"_id": listing_id})
    if listing and "reviews" in listing:
        return listing["reviews"]
    return []