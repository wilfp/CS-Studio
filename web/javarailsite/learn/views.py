from django.http import HttpResponse
from django.template import loader
from learn.models import Challenge


def index(request):
    return HttpResponse("Hello, world. This is the learn app index.")


def profile(request):

    c = Challenge(name="Hello world", level=1, path="path")
    c.save()

    c1 = Challenge(name="Create a number", level=2, path="path1")
    c1.save()

    c2 = Challenge(name="Maths wizardry", level=3, path="path1")
    c2.save()

    context = {
        "challenge_list": Challenge.objects.all(),
    }

    template = loader.get_template("learn/profile.html")

    return HttpResponse(template.render(context, request))
