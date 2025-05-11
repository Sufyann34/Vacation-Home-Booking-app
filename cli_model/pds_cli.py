import requests
import getpass
from tabulate import tabulate
import shlex

AUTH_SERVICE_URL = "http://authMicroservice:8000"
LISTINGS_SERVICE_URL = "http://listing_service:80"
# LISTINGS_SERVICE_URL = "http://127.0.0.1:9090"

# Store session token globally
auth_token = None

def authenticate():
    global auth_token
    print("Welcome to the PDS Admin CLI")
    username = input("Login: ")
    password = getpass.getpass("Password: ")
    
    response = requests.post(f"{AUTH_SERVICE_URL}/login", {"username": username, "password": password})
    
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
    return True

def list_users():
    if not check_auth(): return
    response = requests.get(f"{AUTH_SERVICE_URL}/users/", headers={"Authorization": f"Bearer {auth_token}"})
    if response.ok:
        print(tabulate(response.json(), headers="keys", tablefmt="grid"))
    else:
        print("Failed to fetch users:", response.text)

def deactivate_user(user_id):
    if not check_auth(): return
    response = requests.put(f"{AUTH_SERVICE_URL}/users/{user_id}/deactivate", headers={"Authorization": f"Bearer {auth_token}"})
    print("Response:", response.status_code, response.text)

def list_listings():
    index = 0
    if not check_auth(): return
    response = requests.get(
        f"{LISTINGS_SERVICE_URL}/listings/?index=" + str(index), 
        # headers={"Authorization": f"Bearer {auth_token}"}
    )
    if response.ok:
        print(tabulate(response.json(), headers="keys", tablefmt="grid"))
    else:
        print("Error fetching listings:", response.text)

def approve_listing(listing_id):
    if not check_auth(): return
    response = requests.put(f"{LISTINGS_SERVICE_URL}/listings/{listing_id}/approve", headers={"Authorization": f"Bearer {auth_token}"})
    print("Response:", response.status_code, response.text)

def delete_review(review_id):
    if not check_auth(): return
    response = requests.delete(f"{LISTINGS_SERVICE_URL}/reviews/delete", 
                               json={"review_id": review_id},
                               headers={"Authorization": f"Bearer {auth_token}"})
    print("Review deletion:", response.status_code, response.text)

def help_menu():
    print("""
Available commands:
  list-users
  deactivate-user <user_id>
  list-listings
  approve-listing <listing_id>
  delete-review <review_id>
  help
  exit
""")

def main_loop():
    authenticate()
    while True:
        try:
            raw = input("> ")
            args = shlex.split(raw)
            if not args:
                continue

            command = args[0]
            
            if command == "exit" or command == "quit":
                print("Goodbye!")
                break
            elif command == "help":
                help_menu()
            elif command == "list-users":
                list_users()
            elif command == "deactivate-user" and len(args) == 2:
                deactivate_user(args[1])
            elif command == "list-listings":
                list_listings()
            elif command == "approve-listing" and len(args) == 2:
                approve_listing(args[1])
            elif command == "delete-review" and len(args) == 2:
                delete_review(args[1])
            else:
                print("Unknown command or wrong usage. Type 'help' for options.")
        except KeyboardInterrupt:
            print("\nExiting.")
            break
        except Exception as e:
            print("Error:", str(e))

if __name__ == "__main__":
    # authenticate()
    main_loop()
