from django.contrib import admin
from .models import Challenge
from .models import FileUpload

# Register your models here.

admin.site.register(Challenge)
admin.site.register(FileUpload)
