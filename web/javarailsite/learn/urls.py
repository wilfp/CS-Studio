from django.urls import path
from django.template import loader

from . import views

urlpatterns = [
    path('', views.index, name='index'),
    path('profile', views.profile, name='profile'),
    path('challenge/<int:challenge_id>/', views.challenge, name='challenge'),
]
