import datetime
import time
def requestRedownload():
    import download
    download.download()
try:
    f = open('info','r')
except IOError:
    requestRedownload()
    f=open('info','r')
data=f.read()
if(data==None):
    requestRedownload()
else:
    pattern="%Y-%m-%d %H:%M:%S.%f"
    storedTime=datetime.datetime.strptime(data,pattern )
    current=datetime.datetime.now()
    downloadedEpoches = int(time.mktime(time.strptime(data, pattern)))
    currentEpoches = int(time.mktime(time.strptime(str(current), pattern)))
    if currentEpoches-downloadedEpoches>68352:
        requestRedownload()
import voice
voice.main()
