import json
import re


def get_code(code, challenge, mappings, context):

    code_mod = code

    level = mappings["levels"][challenge.level]

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
        code_mod = context.replace("%CODEPOINT%", code_mod)

    return code_mod
