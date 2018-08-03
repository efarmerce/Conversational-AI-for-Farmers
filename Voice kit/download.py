import requests, zipfile,datetime
import StringIO 
def download():
    zip_file_url='https://firebasestorage.googleapis.com/v0/b/<STORAGE-BUCKET-URL>.appspot.com'
    r = requests.get(zip_file_url, stream=True)
    z = zipfile.ZipFile(StringIO.StringIO(r.content))
    # print sys.argv[0]
    z.extractall("/home/pi/AIY-projects-python/src/examples/voice/files")
    f = open('info','w')
    f.write(str(datetime.datetime.now()))
    f.close()
download()
