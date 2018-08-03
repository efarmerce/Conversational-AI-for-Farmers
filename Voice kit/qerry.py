import csv
import owm as live
def valueOfHistoricalRainfall(fileName,year,month):
    with open(fileName, 'r') as csvfile:
        csvreader = csv.reader(csvfile)
        next(csvreader)
        for row in csvreader:
            if int(row[0])== year:
                return row[month+1]

def valueOfPredictedRainfall(fileName,month):
    f = open(fileName,'r')
    months = [
          "January",
          "February",
          "March",
          "April",
          "May",
          "June",
          "July",
          "August",
          "September",
          "October",
          "November",
          "December"]
    for line in f.readlines():
        if(str(line.split()[0])==str(months[month])):
            return line.split()[2]
    
def predictStartYear(path):
    f = open(path,'r')
    firstLine=f.readline()
    data= firstLine.split()
    return int(data[1])

# flag=[0-stored data,1-current]
def getResult(station,flag,year,month,default_path):
    if(flag==0):
        if(year<predictStartYear(default_path+"/"+station+"predict.txt")):
            return valueOfHistoricalRainfall(default_path+"/"+station+".csv",year,month)
        else:
            return valueOfPredictedRainfall(default_path+"/"+station+"predict.txt",month)
    else:
        # live data staion Cumbum Uttamapalaiyam Chinnamanur Teni Periyakulam Devadanappatti Vattalkundu
        return live.request(station)
