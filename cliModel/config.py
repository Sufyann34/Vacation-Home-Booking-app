import os
from dotenv import load_dotenv

load_dotenv()  # Load environment variables

class Config:
    AUTH_SERVICE_URL = os.getenv("AUTH_SERVICE_URL", "http://localhost:8000")
    LISTINGS_SERVICE_URL = os.getenv("LISTINGS_SERVICE_URL", "http://localhost:8001")
    TIMEOUT = int(os.getenv("REQUEST_TIMEOUT", "10"))  # seconds
    VERBOSE = os.getenv("VERBOSE", "false").lower() == "true"



    