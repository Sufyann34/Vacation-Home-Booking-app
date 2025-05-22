from fastapi import APIRouter, HTTPException, Path
from app.models import Review
from app.crud import add_review, get_reviews, delete_review
from typing import List

router = APIRouter(prefix="/listings/{listing_id}/reviews", tags=["reviews"])

@router.post("/", status_code=201)
async def create_review(
    listing_id: str = Path(..., description="The ID of the listing to add review to"),
    review: Review = ...
):
    success = await add_review(listing_id, review)
    if not success:
        raise HTTPException(
            status_code=404,
            detail="Listing not found or review could not be added"
        )
    return {"message": "Review added successfully"}

@router.get("/", response_model=List[Review])
async def read_reviews(
    listing_id: str = Path(..., description="The ID of the listing to get reviews for")
):
    reviews = await get_reviews(listing_id)
    return reviews

@router.delete("/{review_id}", response_model=dict)
async def delete_review_endpoint(
    listing_id: str = Path(..., description="The ID of the listing to get reviews for"),
    review_id: str = Path(..., description="The ID of the review to delete review for")
):
    reviews = await delete_review(listing_id, review_id)
    return reviews