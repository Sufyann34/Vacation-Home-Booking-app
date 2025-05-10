import click
from auth import auth_commands
from listings import listings_commands
from reviews import reviews_commands
from test import test_commands 

@click.group()
def cli():
    """Admin CLI for Vacation Home Booking System"""
    pass
def __init__():
    pass

# Add command groups
cli.add_command(auth_commands, name="auth")
cli.add_command(listings_commands, name="listings")
cli.add_command(reviews_commands, name="reviews")
cli.add_command(test_commands, name="test")

if __name__ == '__main__':
    cli()




 