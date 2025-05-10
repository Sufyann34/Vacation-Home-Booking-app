import click
from utils import make_request, console

@click.group()
def auth_commands():
    """Authentication commands"""
    pass

def __init__():
    pass


@auth_commands.command()
@click.option("--username", prompt=True)
@click.option("--password", prompt=True, hide_input=True)
@click.option("--email", prompt=True)
@click.option("--is-admin", is_flag=True)
def create_user(username, password, email, is_admin):
    """Create a new user"""
    response = make_request("POST", "/auth/register", {
        "username": username,
        "password": password,
        "email": email,
        "is_admin": is_admin
    })
    if response:
        console.print("[green]User created![/green]")


        




      