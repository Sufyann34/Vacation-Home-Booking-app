import requests
import getpass
from tabulate import tabulate
import shlex
from datetime import datetime
from textwrap import shorten

AUTH_SERVICE_URL = "http://authMicroservice:8000"
LISTINGS_SERVICE_URL = "http://listing_service:80"
auth_token = None

def verify(token):
    response = requests.get(
        f"{AUTH_SERVICE_URL}/verify",
        headers={"Authorization": f"Token {token}"}
    )
    return response

# AUTHENTICATION FUNCTIONS
def authenticate():
    global auth_token
    print("Welcome to the PDS Admin CLI")
    username = input("Login: ")
    password = getpass.getpass("Password: ")

    response = requests.post(f"{AUTH_SERVICE_URL}/login", json={"username": username, "password": password})

    if response.status_code == 200:
        auth_token = response.json().get("token")
        print("Authenticated successfully.\n")
    else:
        print("Authentication failed:", response.text)
        exit(1)

def check_auth():
    if not auth_token:
        print("ERROR: Not authenticated.")
        return False
    response = verify(auth_token)
    if response.status_code == 200:
        return True
    print("ERROR: Token invalid.")
    return False

# USER MANAGEMENT
def list_users():
    if not check_auth(): return
    response = requests.get(f"{AUTH_SERVICE_URL}/users/", headers={"Authorization": f"Bearer {auth_token}"})
    if response.ok:
        print(tabulate(response.json(), headers="keys", tablefmt="grid"))
    else:
        print("Failed to fetch users:", response.text)

def delete_user(username):
    if not check_auth(): return
    confirm = input(f"Are you sure you want to delete user '{username}'? [Y/N]: ").strip().lower()
    if confirm != 'y':
        print("User deletion canceled.")
        return
    response = requests.delete(f"{AUTH_SERVICE_URL}/delete/{username}", headers={"Authorization": f"Bearer {auth_token}"})
    if response.status_code == 200:
        print(f"User '{username}' deleted successfully.")
    elif response.status_code == 404:
        print(f"User '{username}' not found.")
    else:
        print("User deletion failed:", response.status_code, response.text)


# LISTING MANAGEMENT
def list_listings(page=1, per_page=10):
    if not check_auth(): return
    try:
        page = int(page)
        if page < 1:
            raise ValueError
    except ValueError:
        print("Invalid page number. Page must be a positive integer.")
        return

    response = requests.get(f"{LISTINGS_SERVICE_URL}/listings/?page={page}&limit={per_page}")

    if response.ok:
        listings = response.json()
        if not listings:
            print("No listings found on this page.")
            return
        simplified = []
        for listing in listings:
            simplified.append({
                "ID": listing.get("_id", ""),  
                "Name": listing.get("name", ""),
                "Price": f"${listing.get('price', '')}"
            })
        print(f"\nPage {page}")
        print(tabulate(simplified, headers="keys", tablefmt="grid"))
    else:
        print("Error fetching listings:", response.text)

def delete_listing(listing_id):
    if not check_auth(): return
    confirm = input(f"Are you sure you want to delete listing {listing_id}? [Y/N]: ").strip().lower()
    if confirm != 'y':
        print("Deletion canceled.")
        return

    response = requests.delete(
        f"{LISTINGS_SERVICE_URL}/listings/{listing_id}",
    )

    if response.status_code == 200:
        print(f"Listing {listing_id} successfully deleted.")
    elif response.status_code == 404:
        print(f"Listing {listing_id} not found.")
    else:
        print("Listing deletion:", response.status_code, response.text)

# REVIEW MANAGEMENT
def list_reviews(listing_id):
    if not check_auth():
        return
    response = requests.get(
        f"{LISTINGS_SERVICE_URL}/listings/{listing_id}/reviews",
        headers={"Authorization": f"Bearer {auth_token}"}
    )
    if response.status_code != 200:
        print("Error fetching reviews:", response.status_code, response.text)
        return

    reviews = response.json()
    if not reviews:
        print("No reviews found for this listing.")
        return

    table = []
    for review in reviews:
        table.append({
            "ID": review.get("_id", ""),
            "Reviewer": review.get("reviewer_name", ""),
            "Reviewer ID": review.get("reviewer_id", ""),
            "Date": datetime.fromisoformat(review.get("date")).strftime("%Y-%m-%d"),
            "Comment": shorten(review.get("comments", ""), width=50, placeholder="...")
        })

    print(tabulate(table, headers="keys", tablefmt="fancy_grid"))

def delete_review(listing_id, review_id):
    if not check_auth():
        return

    url = f"{LISTINGS_SERVICE_URL}/listings/{listing_id}/reviews/{review_id}"
    headers = {"Authorization": f"Bearer {auth_token}"}

    response = requests.delete(url, headers=headers)

    if response.status_code == 200:
        print(f"Review {review_id} successfully deleted.")
    elif response.status_code == 404:
        print("Review not found.")
    elif response.status_code == 403:
        print("You don't have permission to delete this review.")
    else:
        print(f"Error deleting review: {response.status_code} - {response.text}")

# CREATE LISTING
def create_listing():
    if not check_auth():
        return
    print("Creating a new listing:")
    name = input("Name: ").strip()
    description = input("Description: ").strip()
    summary = input("Enter summary: ").strip()
    street = input("Enter street Name: ").strip()
    country = input("Enter country: ").strip()

    while True:
        try:
            price = float(input("Price: ").strip())
            break
        except ValueError:
            print("Invalid price. Please enter a number.")

    property_type = input("Property Type (e.g., Cottage, Apartment): ").strip()

    while True:
        try:
            accommodates = int(input("Accommodates (number of guests): ").strip())
            break
        except ValueError:
            print("Invalid number. Please enter an integer.")

    print("\nPlease enter URLs for the image versions:")
    picture_url = input("Main image URL (optional): ").strip()
    thumbnail_url = input("Thumbnail URL (optional): ").strip()
    medium_url = input("Medium URL (optional): ").strip()
    xl_picture_url = input("XL Picture URL (optional): ").strip()

    images = {
        "picture_url": picture_url or "",
        "thumbnail_url": thumbnail_url or "",
        "medium_url": medium_url or "",
        "xl_picture_url": xl_picture_url or ""
    }
    
    review_score = {
        "review_scores_accuracy": 0,
        "review_scores_cleanliness": 0,
        "review_scores_checkin": 0,
        "review_scores_communication": 0,
        "review_scores_location": 0,
        "review_scores_value": 0,
        "review_scores_rating": 0
    }

    location = {
        "type": "Point",
        "coordinates": [0.0, 0.0],
        "is_location_exact": False
    }
    
    address = {
        "street": street or "",
        "suburb": "",
        "government_area":"",
        "market":"",
        "country": country or "",
        "country_code": "",
        "location": location
    }

    listing_data = {
        "name": name or "",
        "price": price or 0.0,
        "property_type": property_type or "",
        "images": images,
        "description": description or "",
        "summary": summary or "",
        "amenities": [],
        "accommodates": accommodates or 0,
        "reviews": [],
        "review_scores": review_score,
        "address": address
    }

    headers = {"Authorization": f"Bearer {auth_token}"}
    response = requests.post(f"{LISTINGS_SERVICE_URL}/listings/", json=listing_data, headers=headers)

    if response.status_code in [200, 201]:
        print("Listing created successfully.")
        # print(response.json())
    else:
        print(f"Failed to create listing: {response.status_code} - {response.text}")


#Test APIS
def test_api():
    if not check_auth():
        return
    print("Starting API tests...")
    
    try:
        response = requests.get(f"{LISTINGS_SERVICE_URL}/")
        if response.status_code==200:
            temp = response.json().get('message')
            print(temp)
    except:
        print("listing service not found")
    

# HELP COMMANDS

def help_menu():
    commands = [
        ["list-users", "List all registered users"],
        ["delete_user <username>", "Delete a specific user"],
        ["list-listings [page]", "List listings with pagination"],
        ["delete-listing <listing_id>", "Delete a specific listing"],
        ["create-listing", "Create a new property listing"],
        ["list-reviews <listing_id>", "List reviews for a listing"],
        ["delete-review <listing_id> <review_id>", "Delete a specific review"],
        ["test-api", "Run API test suite"],
        ["help", "Show this help message"],
        ["exit", "Exit the CLI"],
    ]
    
    print("\n Available Commands:\n")
    print(tabulate(commands, headers=["Command", "Description"], tablefmt="fancy_grid"))


def main_loop():
    authenticate()
    while True:
        try:
            raw = input("> ")
            args = shlex.split(raw)
            if not args:
                continue

            command = args[0]

            if command in ["exit", "quit"]:
                print("Goodbye!")
                break
            elif command == "help":
                help_menu()
            elif command == "list-users":
                list_users()
            elif command == "delete_user" and len(args) == 2:
                delete_user(args[1])
            elif command == "list-listings":
                if len(args) == 2:
                    list_listings(page=args[1])
                else:
                    list_listings()
            elif command == "delete-review" and len(args) == 3:
                delete_review(args[1], args[2])  
            elif command == "test-api":
                test_api()
            elif command == "list-reviews" and len(args) == 2:
                list_reviews(args[1])
            elif command == "delete-listing" and len(args) == 2:
                delete_listing(args[1])
            elif command == "create-listing":
                create_listing()
            else:
                print("Unknown command or wrong usage. Type 'help' for options.")
        except KeyboardInterrupt:
            print("\nExiting.")
            break
        except Exception as e:
            print("Error:", str(e))

if __name__ == "__main__":
    main_loop()
