# app/crud/__init__.py

# Expose CRUD operations at package level

from .listings import (
    get_listings,
    get_listing_by_id,
    create_listing,
    update_listing,
    delete_listing,
    search_listings,
    get_listings_paginated,
    get_listings_for_user
)

from .reviews import (
    add_review,
    delete_review,
    get_reviews,
    get_reviews_paginated
)


__all__ = [
    # Listing operations
    "get_listings",
    "get_listing_by_id",
    "create_listing",
    "update_listing",
    "delete_listing",
    "search_listings",
    "get_listings_paginated",
    "get_listings_for_user",
    
    # Review operations
    "add_review",
    "delete_review",
    "get_reviews_paginated",
    "get_reviews"
]