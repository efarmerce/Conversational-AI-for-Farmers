from firebase import Firebase
import aiy.audio
import aiy.voicehat
import random
import subprocess
import time
import connectPhoneHelper
def init(button):
    python3_command = "connectPhoneHelper.py 1 0"
    process = subprocess.Popen(python3_command.split(), stdout=subprocess.PIPE)
    python3_command = "connectPhoneHelper.py 2"
    process = subprocess.Popen(python3_command.split(), stdout=subprocess.PIPE)
    secret_key= process.communicate() 
    aiy.audio.say('If '+secret_key+' number is shown. ')
    time.sleep(50)
    aiy.audio.say(' Press the connect button in the phone')
    time.sleep(50)
    aiy.audio.say('Then press connect button in the voice kit')
    button.wait_for_press()
    python3_command = "connectPhoneHelper.py 1 2"
    process = subprocess.Popen(python3_command.split(), stdout=subprocess.PIPE)
    return True
def updateQuestion(question):
    python3_command = "connectPhoneHelper.py 3 "+question
    process = subprocess.Popen(python3_command.split(), stdout=subprocess.PIPE)
def setIntent(num):
    python3_command = "connectPhoneHelper.py 4 "+str(num)
    process = subprocess.Popen(python3_command.split(), stdout=subprocess.PIPE)
