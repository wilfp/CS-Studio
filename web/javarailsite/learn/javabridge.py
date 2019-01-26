import subprocess

class CommandExecution:

    def __init__(self):

        self.result_buffer = []
        self.read_interval = 100
        self.read_time = 0

        self.process = subprocess.Popen(["java", "JavaBridge.jar"], stdin=subprocess.PIPE, stdout=subprocess.PIPE)

        return

    def poll_result(self, code_id):

        if current_time - self.read_time > self.read_interval:

            for line in iter(self.process.stdout.readline, ''):

                # Process line as json

                self.result_buffer.append(line.rstrip())

            self.read_time = current_time

        # if result_buffer contains code_id
        # return result

        return None

    def submit(self, code):

        # TODO: run code here
        msg = None

        # Process code into readable format

        self.process.stdin.write(msg)

        code_id = "0"  # Assign id

        return code_id

    def close(self):

        self.process.stdin.close()

        return


class Result:

    def __init__(self, ):

        return
