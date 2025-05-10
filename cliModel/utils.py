import requests
from rich.console import Console
from rich.table import Table
from config import Config

console = Console()

AUTH_TOKEN = None   

def make_request(method, endpoint, data=None, service="auth"):
    base_url = Config.AUTH_SERVICE_URL if service == "auth" else Config.LISTINGS_SERVICE_URL
    headers = {}
    if AUTH_TOKEN and service == "auth":  # Only add token for auth service
        headers["Authorization"] = f"Bearer {AUTH_TOKEN}"
    
    try:
        response = requests.request(
            method, 
            f"{base_url}{endpoint}", 
            json=data,
            headers=headers,
            timeout=Config.TIMEOUT
        )
        
        if response.status_code == 401:
            console.print("[red]Error: Unauthorized. Please login first.[/red]")
            return None
        response.raise_for_status()
        return response.json()
    except requests.exceptions.RequestException as e:
        console.print(f"[red]Error: {str(e)}[/red]")
        return None

def display_table(data, columns):
    """
    Display data in a formatted table using Rich
    
    Args:
        data (list): List of dictionaries containing the data
        columns (list): List of column names to display
    """
    table = Table(*columns)
    for item in data:
        table.add_row(*[str(item.get(col, "")) for col in columns])
    console.print(table)