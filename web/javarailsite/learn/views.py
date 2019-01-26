from django.http import HttpResponse
from django.template import loader
from learn.models import Challenge
from learn.models import FileUpload
from learn import preprocessor
from learn import javabridge
import base64
import json
import time

cmd = javabridge.CommandExecution()


def index(request):

    template = loader.get_template("learn/index.html")

    return HttpResponse(template.render(None, request))


def profile(request):

    context = {
        "challenge_list": Challenge.objects.all(),
    }

    template = loader.get_template("learn/profile.html")

    return HttpResponse(template.render(context, request))


def challenge(request, challenge_id):

    challenge_obj = Challenge.objects.get(id=challenge_id)

    context = {"challenge": challenge_obj}
    template = loader.get_template("learn/challenge.html")

    return HttpResponse(template.render(context, request))


def run_java_code(request):

    if request.method != "POST":
        return HttpResponse("ERROR")

    json_data = json.loads(request.body.decode())

    challenge_id = json_data['challenge_id']
    challenge_obj = Challenge.objects.get(id=challenge_id)

    # get the code which was submitted

    code_submitted = base64.b64decode(json_data['code']).decode()

    mappings_file = FileUpload.objects.get(name="mappings.json").file
    mappings_text = mappings_file.read()
    mappings = json.loads(mappings_text)

    context_file = FileUpload.objects.get(name="MainContext.java").file
    main_context = context_file.read().decode()

    # pre-process the submitted code

    ready_code = preprocessor.get_code(code_submitted, challenge_obj, mappings, main_context)

    # run the code with JavaBridge

    code_id = cmd.submit(ready_code)
    result = cmd.poll_result(code_id)

    # return the result as a HttpResponse

    text = result.output
    status = result.state
    lines = result.lines

    response = "{ \"text\": \"" + text + "\", \"status\": \"" + status + "\", \"lines\": " + lines + " }"

    return HttpResponse(response)
