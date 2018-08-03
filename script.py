import csv
import numpy  as np
from sklearn.svm import SVR
import pandas as py
import matplotlib.pyplot as plt
import time
import zipfile
import os
month=[]
rainfall_val=[]
filesToRead=[]
def predict_rainfall(dates,rainfall,x):
    dates=np.reshape(dates,(len(dates),1))
    
    # svr_lin=SVR(kernel='linear',C=0.1)
    # svr_poly=SVR(kernel='poly',C=0.1,degree=25)
    svr_rbf=SVR(kernel='rbf',C=50.7,gamma=19.9)
    
    # svr_lin.fit(dates,rainfall)
    # svr_poly.fit(dates,rainfall)
    svr_rbf.fit(dates,rainfall)

    # plt.scatter(dates,rainfall,color='black',label='Data')
    # plt.plot(dates,svr_rbf.predict(dates),color='red',label="RBF model")
    # plt.plot(dates,svr_lin.predict(dates),color='green',label="lin model")
    # plt.plot(dates,svr_poly.predict(dates),color='blue',label="poly model")
    # plt.xlabel('Date')
    # plt.ylabel('Rainfall')
    # plt.title('SVR')
    # plt.legend()
    # plt.show()
    return svr_rbf.predict(x)
def get_data(filename,row_num):
    global month,rainfall_val
    with open(filename,'r') as csvfile:
        csvFileReader=csv.reader(csvfile)
        next(csvFileReader)

        for row in csvFileReader:
            month.append(int(row[0]))
            rainfall_val.append(float(row[row_num]))
    return
def readIndex(fileName):
    global filesToRead
    name=fileName.split('/')[:-1]
    name='/'.join(name)
    with open(fileName,'r') as csvfile:
        csvFileReader=csv.reader(csvfile)
        next(csvFileReader)
        for row in csvFileReader:
            filesToRead.append(name+'/'+row[0]+'.csv')
    return
predictMonth=-1
predictVal=0
import sys
readIndex(sys.argv[1])
zipFIleName="final.zip"
zip_archive=zipfile.ZipFile(zipFIleName,"w")
fileNameCreated=[]
for files in filesToRead:
    fileName=files.split('/')[-1]
    fileName=fileName.split('.')[0]
    fileName=fileName+'predict'+'.txt'
    fileNameCreated.append(fileName)
    f = open(fileName,'w')
    print files
    rainfall_val=[]
    month=[]
    for i in range(1,13):
        get_data(files,i)
        print time.strftime('%B', time.struct_time((0, i, 0,)+(0,)*6)),
        print month[len(month)-1]+(month[1]-month[0]),
        print predict_rainfall(month,rainfall_val,2018)
        f.write(str(time.strftime('%B', time.struct_time((0, i, 0,)+(0,)*6)))+" "+str(month[len(month)-1]+(month[1]-month[0]))+" "+str(predict_rainfall(month,rainfall_val,2018)[0]))
        f.write('\n')
    f.close()
zip_archive.write('index.csv')
for files in filesToRead:
    temp=files.split('/')[-1]
    zip_archive.write(temp)
for files in fileNameCreated:
    zip_archive.write(files)
    os.unlink(files)
zip_archive.close()