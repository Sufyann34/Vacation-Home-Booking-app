# app/api/__init__.py

# Import all routers to make them easily accessible
from .listings import router as listings_router
from .reviews import router as reviews_router

# You can define a list of all routers for easy importing
__all__ = ["listings_router", "reviews_router"]

# Optional: If you want to initialize something when the package is imported
def init_routers(app):
    """Initialize all API routers"""
    app.include_router(listings_router)
    app.include_router(reviews_router)