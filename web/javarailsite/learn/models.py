from django.db import models

# Create your models here.

class Profile(models.Model):
	name = models.CharField(max_length=200)
	level = models.IntegerField(default=0)

	