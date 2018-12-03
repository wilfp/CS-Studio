import json
import re

def test_mapping():

    with open('testcase1.txt') as f:

        code = f.read()

        mappings = get_mappings()
        challenge = get_challenge()

        print(get_code(code, mappings, challenge))

    return


def get_code(code, mappings, challenge):

    code_mod = code

    level = mappings["levels"][challenge["level"]]

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

    with open('MainContext.java') as f:
        return f.read().replace("%CODEPOINT%", code)

    return null


def get_challenge():

    with open('starter.json') as f:
        data = json.load(f)
        return data

    return null


def get_mappings():

    with open('mappings.json') as f:
        data = json.load(f)
        return data

    return null


if __name__ == "__main__":
    test_mapping()
