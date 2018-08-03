def getStationName(text):
    stationName=[["bodi","bodi","bohi","bhodi","bode","boordy","body"],
    ["cumbum","cumbum","Kambam","kumbum","Aarambam","Khammam"],
    ["gandamanur","gandamanur","ghandhamannur"],
    ["gudalore","gudalore","kodalore","godalore","google"],
    ["manjalardam","manjalardam"],
    ["mayiladumparai","mayiladumparai"],
    ["periakualm","periakualm","periyakulam","pariyakulam"],
    ["utamapalaym","utamapalaym","othamapalaym"],
    ["vaigaidam","vaigaidam"],
    ["veerapandi","veerapandi"]]
    i=0
    for station in stationName:
        for alternatives in station:
            if alternatives.lower() in text.lower():
                return station[0],i
        i+=1
    return None
def getYear(text):
    from qerry import predictStartYear
    predictYear=predictStartYear(getDeafultPath()+"/"+"bodi"+"predict.txt")
    for year in range(2000,predictYear+1):
        if str(year) in text:
            return year
    return None
def getMonth(text):
    months=[[0,"january","jan","1st","genral"],
            [1,"february","feb","2nd","fidelity","debility","severity"],
            [2,"march","mar","3rd","watch","mark"],
            [3,"april","apr","4th","appel","apple"],
            [4,"may","may","5th"],
            [5,"june","jun","6th","room","tour"],
            [6,"july","jul","7th","julie"],
            [7,"august","aug","8th"],
            [8,"september","sep","9th"],
            [9,"october","oct","10th"],
            [10,"november","nov","11th"],
            [11,"december","dec","12th"]]
    check=0
    for month in months:
        check+=1
        for alternatives in month:
            if type(alternatives) is not int:
                if alternatives.lower() in text.lower():
                    return check
    return None
def getDeafultPath():
    return "/home/pi/AIY-projects-python/src/examples/voice/files"
