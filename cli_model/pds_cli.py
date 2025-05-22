import requests
import getpass
from tabulate import tabulate
import shlex

AUTH_SERVICE_URL = "http://authMicroservice:8000"
LISTINGS_SERVICE_URL = "http://listing_service:80"
auth_token = None

# Login
def login(username, password):
    response = requests.post(
        f"{AUTH_SERVICE_URL}/login",
        json={"username": username, "password": password}
    )
    return response.json()

# Signup
def signup(username, password, email):
    response = requests.post(
        f"{AUTH_SERVICE_URL}/signup",
        json={"username": username, "password": password, "email": email}
    )
    return response.json()

# Verify token
def verify(token):
    response = requests.get(
        f"{AUTH_SERVICE_URL}/verify",
        headers={"Authorization": f"Token {token}"}
    )
    return response

# Authentication
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

# List all users
def list_users():
    if not check_auth(): return
    response = requests.get(f"{AUTH_SERVICE_URL}/users/", headers={"Authorization": f"Bearer {auth_token}"})
    if response.ok:
        print(tabulate(response.json(), headers="keys", tablefmt="grid"))
    else:
        print("Failed to fetch users:", response.text)

# Deactivate user
def deactivate_user(user_id):
    if not check_auth(): return
    response = requests.put(f"{AUTH_SERVICE_URL}/users/{user_id}/deactivate", headers={"Authorization": f"Bearer {auth_token}"})
    print("Response:", response.status_code, response.text)

# List listings with pagination
def list_listings(page=1, per_page=10):
    if not check_auth(): return
    try:
        page = int(page)
        if page < 1:
            raise ValueError
    except ValueError:
        print("Invalid page number. Page must be a positive integer.")
        return

    start_index = (page - 1) * per_page
    response = requests.get(f"{LISTINGS_SERVICE_URL}/listings/?index={start_index}&limit={per_page}")

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


# Delete a listing 
def delete_listing(listing_id):
    if not check_auth(): return
    confirm = input(f"Are you sure you want to delete listing {listing_id}? [y/N]: ").strip().lower()
    if confirm != 'y':
        print("Deletion canceled.")
        return

    response = requests.delete(
        f"{LISTINGS_SERVICE_URL}/listings/{listing_id}",
        #headers={"Authorization": f"Bearer {auth_token}"}
    )

    if response.status_code == 200:
        print(f"Listing {listing_id} successfully deleted.")
    elif response.status_code == 404:
        print(f"Listing {listing_id} not found.")
    else:
        print("Listing deletion:", response.status_code, response.text)

# Delete a review
def delete_review(review_id):
    if not check_auth(): return
    response = requests.delete(
        f"{LISTINGS_SERVICE_URL}/reviews/delete",
        json={"review_id": review_id},
        headers={"Authorization": f"Bearer {auth_token}"}
    )
    print("Review deletion:", response.status_code, response.text)

# Help menu
def help_menu():
    print("""
Available commands:
  list-users
  deactivate-user <user_id>
  list-listings [page_number]
  delete-listing <listing_id>
  delete-review <review_id>
  help
  exit
""")

# Main CLI loop
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
            elif command == "deactivate-user" and len(args) == 2:
                deactivate_user(args[1])
            elif command == "list-listings":
                if len(args) == 2:
                    list_listings(page=args[1])
                else:
                    list_listings()
            elif command == "delete-review" and len(args) == 2:
                delete_review(args[1])
            elif command == "delete-listing" and len(args) == 2:
                delete_listing(args[1])
            else:
                print("Unknown command or wrong usage. Type 'help' for options.")
        except KeyboardInterrupt:
            print("\nExiting.")
            break
        except Exception as e:
            print("Error:", str(e))

if __name__ == "__main__":
    main_loop()
