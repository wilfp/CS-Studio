import json

def test_mapping():

    code = "print(\"Test123\")"

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
        code_mod = code_mod.replace("\r\n", "\r\n;")

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

        #print(data["name"])

        #for level in data["levels"]:
        #    print(level["name"])
        #    print(level["main-context"])
        #    print(level["semi-colons"])

        # TODO further implement data processing

    return null  # TODO implement processing

if __name__ == "__main__":
    test_mapping()
