import click
from utils import make_request, console

@click.group()
def test_commands():
    """Test API endpoints"""
    pass

@test_commands.command()
def test_all():
    """Test all API endpoints"""
    # Test authentication
    console.print("\n[bold]Testing Auth Service:[/bold]")
    response = make_request("GET", "/auth/health")
    console.print(f"Auth Health: {'✅' if response else '❌'}")
    
    # Test listings
    console.print("\n[bold]Testing Listings Service:[/bold]")
    response = make_request("GET", "/listings/health", service="listings")
    console.print(f"Listings Health: {'✅' if response else '❌'}")