from django.http import HttpResponse
from django.template import loader

def index(request):
    return HttpResponse("Hello, world. This is the learn app index.")

def profile(request):

	item_list = ["Item 1", "Item 2", "Item 3"];

	context = {
		"item_list" : item_list,
    }
	
	template = loader.get_template("learn/profile.html")
	
	return HttpResponse(template.render(context, request))
