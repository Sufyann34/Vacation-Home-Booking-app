import click
from utils import make_request, console

@click.group()
def reviews_commands():
    """Manage reviews"""
    pass

@reviews_commands.command()
@click.argument("review_id")
def delete(review_id):
    """Delete a review"""
    if click.confirm(f"Are you sure you want to delete review {review_id}?"):
        response = make_request("DELETE", f"/reviews/{review_id}", service="listings")
        if response:
            console.print("[green]Review deleted![/green]")



        