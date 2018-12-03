import json
import re

def get_code(code, challenge_name):

    challenge = get_challenge(challenge_name)

    code_mod = code

    level = loaded_mappings["levels"][challenge["level"]]

    for replacement in level["direct-replacements"]:
        code_mod = code_mod.replace(replacement["old"], replacement["new"])

    if not level["semi-colons"]:

        code_mod_replace = ""

        for line in code_mod.split("\n"):

            if line != "" and not line.endswith("}") and not line.endswith("{"):
                line += ";"

            code_mod_replace += line + "\r\n"

        code_mod = code_mod_replace

    if not level["main-context"]:
        code_mod = get_main_context(code_mod)

    return code_mod


def get_main_context(code):

    with open('PreProcessorContext.java') as f:
        return f.read().replace("%CODEPOINT%", code)

    return null


def get_challenge(name):

    with open("/challenges/"+name+".json") as f:
        data = json.load(f)
        return data

    return null


def get_mappings():

    with open('preprocessormappings.json') as f:
        data = json.load(f)
        return data

    return null

loaded_mappings = get_mappings()
