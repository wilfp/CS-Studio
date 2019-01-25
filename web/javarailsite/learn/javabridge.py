import subprocess

class CommandExecution:

    def __init__(self):

        self.result_buffer = null
        self.read_interval = 100
        self.read_time = 0

        self.process = subprocess.Popen(["java", "JavaBridge.jar"], stdin=subprocess.PIPE, stdout=subprocess.PIPE)

        return

    def poll_result(self, code_id):

        # if current_time-read_time > read_interval:
        # read results

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
