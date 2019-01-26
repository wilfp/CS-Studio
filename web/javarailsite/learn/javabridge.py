import subprocess
import json
import base64
import time


class CommandExecution:

    def __init__(self):

        # init starting data

        self.result_buffer = []
        self.read_interval = 10000
        self.read_time = 0
        self.counter = 0
        self.alphabet = "ABCDEFGHIJKLMNOP"

        # call JavaBridge subprocess

        self.process = subprocess.Popen(["java", "JavaBridge.jar"], stdin=subprocess.PIPE, stdout=subprocess.PIPE)

        return

    def poll_result(self, code_id):

        # Update list of incoming results

        if time.time() - self.read_time > self.read_interval:

            for line in iter(self.process.stdout.readline, ''):

                # Process line as json

                json_data = json.loads(line.rstrip())

                # Put json into data structure

                self.result_buffer.append(Result(json_data['name'], json_data['state'], base64.decode(json_data['output']),
                                                 base64.decode(json_data['error']), json_data['lines']))

            self.read_time = time.time()

        # check incoming results

        for result in self.result_buffer:
            if result.name == code_id:
                return result

        # if no result was found

        return None

    def submit(self, code):

        code_id = self.get_code_id()

        # TODO create new file in temp folder called code_id.java
        # TODO write code to file

        # call process
        self.process.stdin.write(code_id)

        return code_id

    def get_code_id(self):

        # get next id number
        self.counter += 1

        # convert number to letters

        code_id = ""
        temp_counter = self.counter

        while temp_counter > 16:
            code_id += self.alphabet[temp_counter % 16]
            temp_counter /= 16

        code_id += self.alphabet[temp_counter]

        # return generated id

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
