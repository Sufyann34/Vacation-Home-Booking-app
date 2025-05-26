from pydantic import BaseModel, validator, Field
from bson.decimal128 import Decimal128
from typing import List, Optional
from .review_models import Review, ReviewScores
# from datetime import datetime

class Image(BaseModel):
    thumbnail_url: Optional[str] = None
    medium_url: Optional[str] = None
    picture_url: Optional[str] = None
    xl_picture_url: Optional[str] = None

class Location(BaseModel):
    type: Optional[str] = None
    coordinates: Optional[List[float]] = None
    is_location_exact: Optional[bool] = None

class Address(BaseModel):
    street: Optional[str] = None
    suburb: Optional[str] = None
    government_area: Optional[str] = None
    market: Optional[str] = None
    country: Optional[str] = None
    country_code: Optional[str] = None
    location: Optional[Location] = None

class Listing(BaseModel):
    id: str = Field(..., alias="_id")
    name: str
    price: float
    property_type: str
    images: Image
    summary: str
    address: Optional[Address] = None

    @validator('price', pre=True)
    def convert_decimal128(cls, v):
        if isinstance(v, Decimal128):
            return float(str(v))
        return v
    
class Create(BaseModel):
    name: str
    price: float
    property_type: str
    images: Image
    description: Optional[str] = None
    summary: Optional[str] = None
    amenities: Optional[List[str]] = None
    accommodates: int
    reviews: Optional[List[Review]] = None

    def to_mongo_dict(self) -> dict:
        data = self.dict()
        data["images"] = self.images.dict()
        return data
    
class DetailedList(BaseModel):
    id: str = Field(..., alias="_id")
    name: str
    price: float
    property_type: str
    images: Optional[Image] = None
    description: Optional[str] = None
    summary: Optional[str] = None
    amenities: Optional[List[str]] = None
    accommodates: Optional[int] = None
    reviews: Optional[List[Review]] = None
    review_scores: Optional[ReviewScores] = None
    address: Optional[Address] = None

    @validator('price', pre=True)
    def convert_decimal128(cls, v):
        if isinstance(v, Decimal128):
            return float(str(v))
        return v

class Host(BaseModel):
    host_id: str
    host_url: str
    host_name: str
    host_location: str
    host_about: str
    host_response_time: str
    host_thumbnail_url: str
    host_picture_url: str
    host_neighbourhood: str
    host_response_rate: int
    host_is_superhost: bool
    host_has_profile_pic: bool
    host_identity_verified: bool
    host_listings_count: int
    host_total_listings_count: int
    host_verifications: List[str]
    
class Availability(BaseModel):
    availability_30: int
    availability_60: int
    availability_90: int
    availability_365: int

class AddressForListing(BaseModel):
    market: str
    country: str
