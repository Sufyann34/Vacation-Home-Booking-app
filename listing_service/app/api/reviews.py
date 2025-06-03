# API endpoints for review management (create, read, delete operations)
from fastapi import APIRouter, HTTPException, Path, Query
from app.models import Review
from app.crud import add_review, get_reviews, delete_review, get_reviews_paginated
from typing import List, Optional

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
    listing_id: str = Path(..., description="The ID of the listing to get reviews for"),
    page: Optional[int] = Query(None, ge=1, description="Page number for pagination"),
    limit: Optional[int] = Query(None, ge=1, le=100, description="Number of reviews per page")
):
    if page and limit:
        reviews = await get_reviews_paginated(listing_id, page, limit)
    else:
        reviews = await get_reviews(listing_id)
    return reviews


@router.delete("/{review_id}", response_model=dict)
async def delete_review_endpoint(
    listing_id: str = Path(..., description="The ID of the listing to get reviews for"),
    review_id: str = Path(..., description="The ID of the review to delete review for")
):
    reviews = await delete_review(listing_id, review_id)
    return reviews