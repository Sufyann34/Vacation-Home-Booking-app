from typing import List
from app.models import Review
from app.database import collection  # Assuming you have a database connection setup
from app.hash import generate_custom_id

async def add_review(listing_id: str, review: Review) -> bool:
    """
    Add a review to a listing
    Returns True if successful, False if listing not found
    """
    review_dict = review.dict()
    review_dict["_id"] = generate_custom_id(review.reviewer_name + review.reviewer_id)
    
    result = await collection.update_one(
        {"_id": listing_id},
        {"$push": {"reviews": review_dict}}
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


async def delete_review(listing_id: str, review_id: str) -> dict:
    """
    Deletes a specific review from a listing based on review_id.
    Returns a message indicating success or failure.
    """
    result = await collection.update_one(
        {"_id": listing_id},
        {"$pull": {"reviews": {"_id": review_id}}}
    )

    if result.modified_count > 0:
        return {"message": "Review deleted successfully."}
    else:
        return {"message": "Review not found or already deleted."}