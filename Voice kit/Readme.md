<p> As we have alread mentioned this folder contains all the souce code which help make an interaction between Nasa world wind app and Google AIY voice KIt</p> 
<h1> How to utilize these files</h1>
<p> We used firebase in our app<p>
<p> The final.zip file created using AI Tool should be uploaded to the firebase storage and that storage bucket url should be linked with the download.py file</p>
<p> Download.py file download and extract all the necessary file for every 12 hours in order to update the content. Please use python 2 to download the content from fireabse</p>
<p> In owm.py file use your open weather map api key</p>
<p> In connectPhoneHelper.py put your firebase database url </p>
<p> For this sample you need to make both firebase storage and firebase database security rules to be open to public</p>
<p> refer firebase.com to do so</p>
<p> These scripts needs a lot of setup and please refer https://aiyprojects.withgoogle.com/voice to make the necesarry setup ready</p>
<p> Besides please follow the instruction as given in https://github.com/mikexstudios/python-firebase to install firebase dependences <p>
<p> execute main.py in python3 <p>
<h1> Hidden Features</h1>
<p>For those question which the google AIY voice kit is not trained will automatically uploaded in the firebase databse.</p>
<p>Developers can make decision on top of it</p>
<h2> Known Bug</h2>
<p> Everyone who open the app will automatically update with the activity</p>
<h3>Bug fix</h3>
<p> USe bluetooth scanning in the app to solve it</p>
