from pydantic import BaseModel, Field
from datetime import datetime
from typing import List, Optional



class ReviewScores(BaseModel):
    review_scores_accuracy: Optional[int] = None
    review_scores_cleanliness: Optional[int] = None
    review_scores_checkin: Optional[int] = None
    review_scores_communication: Optional[int] = None
    review_scores_location: Optional[int] = None
    review_scores_value: Optional[int] = None
    review_scores_rating: Optional[int] = None

class Review(BaseModel):
    id: str = Field(..., alias="_id")
    date: datetime
    listing_id: str 
    reviewer_id: str
    reviewer_name: str
    comments: str
