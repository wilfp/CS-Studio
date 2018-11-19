

if __name__ == "__main__":
    test_mapping()


def test_mapping():

    mappings = get_mappings()

    for m in mappings:
        print(m)

    return


def get_mappings():

    with open('mappings.json') as f:
        data = json.load(f)

        print(data["name"])

        for level in data["levels"]:
            print(level["name"])
            print(level["main-context"])
            print(level["semi-colons"])

        # TODO further implement data processing

    return null # TODO implement processing

