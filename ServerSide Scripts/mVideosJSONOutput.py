#!/usr/bin/env python
# -*- coding: UTF-8 -*-

# Import modules for CGI handling 
import cgi
import sqlite3
import json
import unicodedata

# enable debugging
import cgitb
cgitb.enable()

# Create instance of FieldStorage 
form = cgi.FieldStorage() 

# Get data from fields
qword = form.getvalue('q')

# Connect to the database
conn = sqlite3.connect('videos.sqlite3')
cursor = conn.cursor()

print "Content-Type: text/plain;charset=utf-8"
print

arrayList = []
storeFrames = []
output = []
isPresent = []

print "{\n\t"
print '"%s"' % "matching_videos" + ": [\n"
tempStr = ""

Id = '"%s"' % "ID"
VideoTitle = '"%s"' % "Title"
VideoID = '"%s"' % "VideoID"
UploadTime = '"%s"' % "Uploadtime"
Description = '"%s"' % "Description"
PlayerUrl = '"%s"' % "PlayerUrl"
Length = '"%s"' % "Length"
Author = '"%s"' % "Author"
ViewCount = '"%s"' % "ViewCount"
Thumbnail0 = '"%s"' % "Thumbnail0"
Topics = '"%s"' % "Topics"

for getFrames in cursor.execute('SELECT imageName FROM imageFrames WHERE videoRef = ?', (qword,)):	
	storeFrames.append(getFrames[0])
	#print len(storeFrames)

for i in range(len(storeFrames)):
	referenceImage = storeFrames[i]
	referenceImageInput = "shot1Test/" + referenceImage[:1] + "/" + referenceImage[1:2] + "/" + referenceImage[:11] + "/" + referenceImage

	for cg in cursor.execute('SELECT matchingFrame FROM matchingFrames WHERE referenceFrameID = ?', (referenceImageInput,)):
		getVideoIds = cg[0]
		getVideoIds = unicodedata.normalize('NFKD', getVideoIds[14:25]).encode('ascii','ignore')
		arrayList.append(getVideoIds)
	
	for i in range(len(arrayList)):
		cursor.execute('SELECT * FROM videosMetaData WHERE videoID = ?', (arrayList[i],))
		test = cursor.fetchall()
		#for k in range(len(isPresent)):
			#if isPresent[k] == arrayList[i]
		if arrayList[i] not in isPresent:
			isPresent.append(arrayList[i]) 
			if (len(str(test)) > 2):
				id = Id + ": " + '"%s"' % str(test[0][0]) + ", " + "\n"
				videoTitle = VideoTitle + ": " + '"%s"' % test[0][1] + ", " + "\n"		
				videoUniqueID = VideoID + ": " + '"%s"' % test[0][2] + ", " + "\n"
				uploadTime = UploadTime + ": " + '"%s"' % test[0][5] + ", " + "\n"
				description = Description + ": " + '"%s"' % test[0][6] + ", " + "\n"
				playerurl = test[0][9]
				playerurl = PlayerUrl + ": " + '"%s"' % playerurl[:-33] + ", " + "\n"
				length = Length + ": " + '"%s"' % str(test[0][11]) + ", " + "\n"
				author = Author + ": " + '"%s"' % test[0][12] + ", " + "\n"
				viewCount = ViewCount + ": " + '"%s"' % test[0][15] + ", " + "\n"			
				thumbnail0 = Thumbnail0 + ": " + '"%s"' % test[0][17] + ", " + "\n"
				topics = Topics + ": " + '"%s"' % str(test[0][21]) + "\n"
				tempStr = tempStr + "\n\t\t" + "{" + "\n\t\t\t" + id + "\t\t\t" + videoTitle + "\t\t\t" + videoUniqueID + "\t\t\t" + uploadTime + "\t\t\t" + description + "\t\t\t" +   playerurl + "\t\t\t" +  length + "\t\t\t" + author + "\t\t\t" + viewCount + "\t\t\t" + thumbnail0 + "\t\t\t" +  topics + "\t\t}, "

if (len(tempStr) > 2):
	tempStr = tempStr[:-2]
print tempStr

print "\n\t]\n}"


# Close connection to Database
conn.close()

