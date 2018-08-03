
import aiy.audio
import aiy.cloudspeech
import aiy.voicehat
import owm
import helper
import qerry
import aiy.assistant.grpc
import aiy.audio
import aiy.voicehat
import logging
import time
import connectPhone



logging.basicConfig(
    level=logging.INFO,
    format="[%(asctime)s] %(levelname)s:%(name)s:%(message)s"
)
def speechToTextConverter(button,assistant):
    print('Press the button and speak')
    button.wait_for_press()
    print('Listening...')
    text, audio = assistant.recognize()
    if text is None:
        text=speechToTextConverter(button,assistant)
    return text
def askForStationName(recognizer,button):
    stationName = None
    while stationName is None:
        print('What is the city name?')
        aiy.audio.say('What is the city name?')
        text=speechToTextConverter(button,recognizer)
        stationName,stationID=helper.getStationName(text)
        connectPhone.setIntent(stationID)
    return stationName
def askForMonth(recognizer,button):
    month=None
    while month is None:
        print('Which month are you intrested in?')
        aiy.audio.say('Which month are you intrested in?')
        text=speechToTextConverter(button,recognizer)
        month=helper.getMonth(text)
        print (month)
    return month
def askForYear(recognizer,button):
    year=None
    while year is None:
        print('Which Year are you intrested in?')
        aiy.audio.say('Which Year are you intrested in?')
        text=speechToTextConverter(button,recognizer)
        year=helper.getYear(text)
    return year
def main():
    button = aiy.voicehat.get_button()
    led = aiy.voicehat.get_led()
    aiy.audio.get_recorder().start()
    connectPhone.init()
    assistant = aiy.assistant.grpc.get_assistant()
    while True:
        text=speechToTextConverter(button,assistant)
        print (text)
        stationName=""
        typeOfData=""
        year=""
        month=""
        answered=False
        temperature=['temperature','temp','hot','cold']
        for temp in temperature:
            if not answered:
                if temp in text:
                    typeOfData=temp
                    stationName,stationID=helper.getStationName(text)
                    connectPhone.setIntent(stationID)
                    if stationName == None:
                        stationName = askForStationName(assistant,button)
                    result=owm.request(stationName)-273.15
                    aiy.audio.say("The temperature at "+stationName+" is "+str(round(result,2))+'celsius')
                    answered=True
                    print ("The temperature at "+stationName+" is "+str(result))
        rainfall=['rain','rainfall','rainy']
        for rain in rainfall:
            if not answered:
                if rain in text:
                    stationName,stationID=helper.getStationName(text)
                    connectPhone.setIntent(stationID)
                    if stationName == None:
                        stationName = askForStationName(assistant,button)
                    year=helper.getYear(text)
                    if year == None:
                        year=askForYear(assistant,button)
                    month = helper.getMonth(text)
                    if month == None:
                        month = askForMonth(assistant,button)
                    result=qerry.getResult(stationName,0,year,month,helper.getDeafultPath())
                    if year==2018:
                        result="The rainfall at "+stationName+" is predicted to be "+str(result)+'mm'
                    else:
                        result="The rainfall was "+stationName+" is "+str(result)+'mm'
                    aiy.audio.say(result)
                    answered=True
                    print ("The rainfall at "+stationName+" is "+str(result))
        if 'today' in text or 'clouds' in text or 'cloud' in text:
            if not answered:
                stationName = helper.getStationName(text)
                if stationName == None:
                    stationName = askForStationName(assistant,button)
                result=owm.cloudsToday(stationName)
                aiy.audio.say("The weather is "+str(result))
                print ("The weather is "+result)
                answered=True


        rainfall=['Thank you','Thanks','Thank','Bye']
        for rain in rainfall:
            if not answered:
                if rain.lower() in text.lower():
                    result="My developers are keen at teaching me farming and I'm consistently learning"
                    aiy.audio.say(result)
                    time.sleep(0.3)
                    result="Thank you"
                    aiy.audio.say(result)
                    answered=True
                    exit(0)
        if(not answered):
            aiy.audio.say("Sorry the question is not properly audible!")
            connectPhone.updateQuestion(text)
