from fastapi import APIRouter, HTTPException, Path
from app.models import Review
from app.crud import add_review, get_reviews
from typing import List

router = APIRouter(prefix="/listings/{item_id}/reviews", tags=["reviews"])

@router.post("/", status_code=201)
async def create_review(
    item_id: str = Path(..., description="The ID of the listing to add review to"),
    review: Review = ...
):
    success = await add_review(item_id, review)
    if not success:
        raise HTTPException(
            status_code=404,
            detail="Listing not found or review could not be added"
        )
    return {"message": "Review added successfully"}

@router.get("/", response_model=List[Review])
async def read_reviews(
    item_id: str = Path(..., description="The ID of the listing to get reviews for")
):
    reviews = await get_reviews(item_id)
    return reviews