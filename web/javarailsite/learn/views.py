from django.http import HttpResponse
from django.template import loader
from learn.models import Challenge


def index(request):
    return HttpResponse("Hello, world. This is the learn app index.")


def profile(request):

    context = {
        "challenge_list": Challenge.objects.all(),
    }

    template = loader.get_template("learn/profile.html")

    return HttpResponse(template.render(context, request))


def challenge(request, challenge_id):

    context = {"challenge_id": challenge_id}
    template = loader.get_template("learn/challenge.html")

    return HttpResponse(template.render(context, request))
