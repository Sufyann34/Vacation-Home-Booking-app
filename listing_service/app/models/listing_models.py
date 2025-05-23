from pydantic import BaseModel, validator, Field
from bson.decimal128 import Decimal128
from typing import List, Optional
from .review_models import Review
# from datetime import datetime

class Image(BaseModel):
    thumbnail_url: Optional[str] = None
    medium_url: Optional[str] = None
    picture_url: Optional[str] = None
    xl_picture_url: Optional[str] = None

class Location(BaseModel):
    type: str
    coordinates: List[float]
    is_location_exact: bool

class Address(BaseModel):
    street: Optional[str] = None
    suburb: Optional[str] = None
    government_area: Optional[str] = None
    market: str
    country: str
    country_code: str
    location: Location

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
    images: Image
    description: Optional[str] = None
    summary: Optional[str] = None
    amenities: Optional[List[str]] = None
    accommodates: int
    reviews: Optional[List[Review]] = None
    address: Address


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

class AddressForListing(BaseModel):
    market: str
    country: str
