import subprocess
import json
import base64
import time
import os

class CommandExecution:

    def __init__(self, java_bridge_jar, directory):

        # handle file system

        self.java_bridge_jar = java_bridge_jar
        self.temp_path = directory + "/javabridge/"

        if not os.path.exists(self.temp_path):
            os.mkdir(self.temp_path)

        # init starting data

        self.result_buffer = []
        self.read_interval = 2
        self.read_time = 0
        self.counter = 0
        self.alphabet = "ABCDEFGHIJKLMNOP"
        self.timeout = 15

        # call JavaBridge subprocess

        self.process = subprocess.Popen(["java", "-cp", self.java_bridge_jar.path, "studio.csuk.javabridge.JavaBridgeTest", self.temp_path], stdin=subprocess.PIPE, stdout=subprocess.PIPE)

        return

    def poll_result(self, code_id):

        result_pointer = 0
        start_time = time.time()

        # check for result till timeout

        while time.time()-start_time < self.timeout:

            self.update_result_list()

            while result_pointer < len(self.result_buffer)-1:

                result = self.result_buffer[result_pointer]

                if result.name == code_id:
                    return result

                result_pointer += 1

        # if no result was found

        return None

    def update_result_list(self):

        if time.time()-self.read_time > self.read_interval:

            for line in iter(self.process.stdout.readline, ''):

                # Process line as json

                line = line.rstrip()

                if len(line) < 2:
                    continue

                line = line.decode("UTF-8")
                
                print("line: " + line)

                json_data = json.loads("line: " + line)

                # Put json into data structure

                self.result_buffer.append(
                    Result(json_data['name'], json_data['state'], base64.decode(json_data['output']),
                           base64.decode(json_data['error']), json_data['lines']))

            self.read_time = time.time()

        return

    def submit(self, code):

        # get id for code

        code_id = self.get_code_id()

        # write code to file

        f = open(self.temp_path + code_id + '.java', 'w+')
        f.write(code)
        f.close()

        # call process

        self.process.stdin.write(code_id.encode("UTF-8"))

        # return the code id for later

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

        self.process.stdin.write(".exit".encode("UTF-8"))
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
