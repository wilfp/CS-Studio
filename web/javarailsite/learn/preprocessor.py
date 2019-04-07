import json
import re


def get_code(code, challenge, mappings, class_context, method_context):

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
    
    if level["class"]:
        code_mod = class_context.replace("%CODEPOINT%", code_mod)
    elif level["method"]:
        code_mod = method_context.replace("%CODEPOINT%", code_mod)
    elif level["infer"]:
        
        class_search = re.search(codematcher.class_name, code)
        main_search = re.search(codematcher.main_method, code)
        
        if main_search == None:
            code_mod = class_context.replace("%CODEPOINT%", code_mod)
        if class_search == None:
            code_mod = method_context.replace("%CODEPOINT%", code_mod)
        else:
            pass
        
    elif level["none"]:
        pass
    else:
        print("Error level not found")
        pass
        
    return code_mod
