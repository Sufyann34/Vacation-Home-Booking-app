from rest_framework.decorators import api_view
from rest_framework.response import Response
from .serializers import UserSerializer
from rest_framework.authtoken.models import Token
# from django.contrib.auth.models import User
from rest_framework import status
from django.shortcuts import get_object_or_404
from rest_framework.decorators import authentication_classes, permission_classes
from rest_framework.authentication import SessionAuthentication, TokenAuthentication
from rest_framework.permissions import IsAuthenticated
from django.contrib.auth import get_user_model

User = get_user_model()

@api_view(['POST'])
def login(request):
    # Authenticates user and returns token
    user = get_object_or_404(User, username = request.data['username'])
    if not user.check_password(request.data['password']):
        return Response({"detail": "Not found."}, status = status.HTTP_404_BAD_REQUEST)
    token, created = Token.objects.get_or_create(user = user)
    serializer = UserSerializer(instance = user)
    return Response({"token": token.key, "user": serializer.data})

@api_view(['POST'])
def signup(request):
    # Registers a new user and returns token
    if User.objects.filter(email=request.data.get('email')).exists():
        return Response({"detail": "Email address is already registered."}, status=status.HTTP_400_BAD_REQUEST)
     
    serializer = UserSerializer(data=request.data)
    if serializer.is_valid():
        serializer.save()
        user = User.objects.get(username = request.data['username'])
        user.set_password(request.data['password'])
        user.save()
        token = Token.objects.create(user = user)
        return Response({"token": token.key, "user": serializer.data})
    return Response(serializer.errors, status = status.HTTP_400_BAD_REQUEST)

@api_view(['GET'])
@authentication_classes([SessionAuthentication, TokenAuthentication])
@permission_classes([IsAuthenticated])
def verify(request):
    # Verifies if user is authenticated
    return Response("passed for {}".format(request.user.username))

@api_view(['GET'])
@authentication_classes([SessionAuthentication, TokenAuthentication])
def list_users(request):
    # Lists all users (authentication required)
    users = User.objects.all()
    serializer = UserSerializer(users, many=True)
    return Response(serializer.data)

@api_view(['DELETE'])
@authentication_classes([SessionAuthentication, TokenAuthentication])
def delete_user(request, username):
    # Deletes a user by username
    try:
        user_to_delete = User.objects.get(username=username)
        user_to_delete.delete()
        return Response({"detail": f"User '{username}' deleted."}, status=status.HTTP_200_OK)
    except User.DoesNotExist:
        return Response({"detail": "User not found."}, status=status.HTTP_404_NOT_FOUND)