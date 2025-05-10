import click
from utils import make_request, display_table
from utils import make_request, display_table, console  

@click.group()
def listings_commands():
    """Manage vacation home listings"""
    pass

def __init__():
    pass


@listings_commands.command()
@click.argument("listing_id")
def delete(listing_id):
    """Delete a listing"""
    if click.confirm(f"Are you sure you want to delete listing {listing_id}?"):
        response = make_request("DELETE", f"/listings/{listing_id}", service="listings")
        if response:
            console.print("[green]Listing deleted![/green]")

@listings_commands.command()
@click.option("--page", default=1, help="Page number")
@click.option("--per-page", default=10, help="Items per page")
def list(page, per_page):
    """List all properties"""
    response = make_request("GET", f"/listings?page={page}&per_page={per_page}", service="listings")
    if response:
        display_table(response.get("items", []), ["id", "title", "price"])
        console.print(f"Page {page} of {response.get('total_pages', 1)}")






      