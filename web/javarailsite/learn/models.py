from django.db import models


# Create your models here.

class Profile(models.Model):
    name = models.CharField(max_length=200)
    level = models.IntegerField(default=0)


class Challenge(models.Model):
    id = models.IntegerField(default=0, primary_key=True)
    name = models.CharField(max_length=200)
    level = models.CharField(max_length=100)
    desc = models.CharField(max_length=500)
    has_input = models.BooleanField()
    has_output = models.BooleanField()
    validate_text = models.BooleanField()
    validation_format = models.CharField(max_length=200)
