from pydantic import BaseModel, Field
from datetime import datetime


class ReviewScores(BaseModel):
    review_scores_accuracy: int
    review_scores_cleanliness: int
    review_scores_checkin: int
    review_scores_communication: int
    review_scores_location: int
    review_scores_value: int
    review_scores_rating: int

class Review(BaseModel):
    id: str = Field(..., alias="_id")
    date: datetime
    listing_id: str
    reviewer_id: str
    reviewer_name: str
    comments: str
