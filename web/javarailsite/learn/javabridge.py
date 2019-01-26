import subprocess
import json
import base64

class CommandExecution:

    def __init__(self):

        self.result_buffer = []
        self.read_interval = 100
        self.read_time = 0
        self.counter = 0
        self.alphabet = "ABCDEFGHIJKLMNOP"

        self.process = subprocess.Popen(["java", "JavaBridge.jar"], stdin=subprocess.PIPE, stdout=subprocess.PIPE)

        return

    def poll_result(self, code_id):

        if current_time - self.read_time > self.read_interval:

            for line in iter(self.process.stdout.readline, ''):

                # Process line as json

                json_data = json.loads(line.rstrip())

                self.result_buffer.append(Result(json_data['name'], json_data['state'], json_data['output'],
                                                 json_data['error'], json_data['lines']))

            self.read_time = current_time

        # if result_buffer contains code_id
        # return result

        if result

        return None

    def submit(self, code):

        code_id = self.get_code_id()

        # create new file in temp folder called code_id.java
        # write code to file

        self.process.stdin.write(code_id)

        return code_id

    def get_code_id(self):

        self.counter += 1

        code_id = ""
        temp_counter = self.counter

        while temp_counter > 16:
            code_id += self.alphabet[temp_counter % 16]
            temp_counter /= 16

        code_id += self.alphabet[temp_counter]

        return code_id

    def close(self):

        self.process.stdin.close()

        return


class Result:

    def __init__(self, name, state, output, error, lines):
        self.name = name
        self.state = state
        self.output = output
        self.error = error
        self.lines = lines
        return
