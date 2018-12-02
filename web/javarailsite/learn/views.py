from django.http import HttpResponse
from django.template import loader
from learn.models import Challenge
import json


def index(request):
    return HttpResponse("Hello, world. This is the learn app index.")


def profile(request):

    context = {
        "challenge_list": Challenge.objects.all(),
    }

    template = loader.get_template("learn/profile.html")

    return HttpResponse(template.render(context, request))


def challenge(request, challenge_id):

    challenge_obj = Challenge.objects.get(id=challenge_id)

    f = challenge_obj.file.open()
    data = json.load(f)

    context = {"challenge": challenge_obj, "challenge_desc": data["desc"]}
    template = loader.get_template("learn/challenge.html")

    return HttpResponse(template.render(context, request))

def runJavaCode(request):

	if(request.method != "POST"):
		return HttpResponse("ERROR")
	
	code = request.body
	
	# TODO: compile and call Java
	
	text = "Code run successfully!"
	status = "success"
	
	response = "{ \"text\": \"" + text + "\", \"status\": \"" + status + "\" }"

	return HttpResponse(response)
