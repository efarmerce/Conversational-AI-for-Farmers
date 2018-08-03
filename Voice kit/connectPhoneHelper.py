
from firebase import Firebase
import random
import sys
def _getRandomNumber():
    return random.randint(1000,5000)
def phoneStatus(val):
     base_url='https://<DATABASEURL>.firebaseio.com/'
     phoneStatus= Firebase(base_url+'phone_status')
     phoneStatus.set(val)
def secretKey():
    base_url='https://<DATABASEURL>.firebaseio.com/'
    key = Firebase(base_url+'secret_key')
    secret_key=_getRandomNumber()
    key.set(secret_key)
    return secret_key
def updateQuestion(question):
    base_url='https://<DATABASEURL>.firebaseio.com/'
    tag= Firebase(base_url+'unanswered')
    tag.push(question)
def setIntent(num):
    base_url='https://<DATABASEURL>.firebaseio.com/'
    tag=Firebase(base_url+'answered')
    tag.set(num)
if sys.argv[1]==1:
    phoneStatus(int(sys.argv[2])
elif sys.argv[1]==2:
    return secretKey()
elif sys.argv[1]==3:
    updateQuestion(sys.argv[2])
elif sys.argv[1]==4:
    setIntent(int(sys.argv[2]))
