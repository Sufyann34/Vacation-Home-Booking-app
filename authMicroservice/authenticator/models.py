import uuid
from django.contrib.auth.models import AbstractUser
from django.db import models

# Custom user model with UUID as primary key
class CustomUser(AbstractUser):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)