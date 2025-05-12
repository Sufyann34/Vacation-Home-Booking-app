import hashlib

def generate_custom_id(name: str) -> str:
    # Hash the name string
    hash_object = hashlib.sha256(name.encode())
    hex_digest = hash_object.hexdigest()

    # Take a slice of the hash and convert to an int
    numeric_id = int(hex_digest[:6], 16)  # 6 hex chars = up to ~16 million

    # Ensure it's in your desired format (e.g., 8-digit number starting with "10")
    custom_id = 10000000 + (numeric_id % 8999999)  # keeps it 8-digit
    return str(custom_id)

