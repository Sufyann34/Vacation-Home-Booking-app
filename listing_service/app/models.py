from pydantic import BaseModel, validator
from bson.decimal128 import Decimal128
from datetime import datetime
from typing import List, TypedDict


class Image(BaseModel):
    thumbnail_url: str | None
    medium_url: str | None
    picture_url: str | None
    xl_picture_url: str | None

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

class Location(BaseModel):
    type: str
    coordinates: List[float]
    is_location_exact: bool

class Address(BaseModel):
    street: str
    suburb: str
    government_area: str
    market: str
    country: str
    country_code: str
    location: Location
    
class Availability(BaseModel):
    availability_30: int
    availability_60: int
    availability_90: int
    availability_365: int

class Review_scores(BaseModel):
    review_scores_accuracy: int
    review_scores_cleanliness: int
    review_scores_checkin: int
    review_scores_communication: int
    review_scores_location: int
    review_scores_value: int
    review_scores_rating: int

class Review(BaseModel):
    _id: str
    date: datetime
    listing_id: str
    reviewer_id: str
    reviewer_name: str
    comments: str


class AddressForListing(BaseModel):
    market: str
    country: str

class Listing(BaseModel):
    # _id: str
    name: str
    price: float
    property_type: str
    # address: AddressForListing

    # summary: str
    # property_type: str
    # description: str | None
    # last_scraped: datetime
    # reviews: list[Review] | None

    @validator('price', pre=True)
    def convert_decimal128(cls, v):
        if isinstance(v, Decimal128):
            return float(str(v))
        return v

class Mongo_create_listing(TypedDict):
    name: str
    price: float
    property_type: str
    

class Create_listing(BaseModel):
    name: str
    price: float
    property_type: str
    

class Detailed_list(BaseModel):
    _id: str
    name: str
    price: float
    description: str