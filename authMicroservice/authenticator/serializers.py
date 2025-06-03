from rest_framework import serializers
from django.contrib.auth.models import User
from django.contrib.auth import get_user_model

# Serializer for the custom user model
class UserSerializer(serializers.ModelSerializer):
    class Meta(object):
        model = get_user_model() # Uses the custom user model
        fields = ['id', 'username', 'password', 'email']
        extra_kwargs = {
            'password' : {'write_only' : True} # Hides password from responses
        }