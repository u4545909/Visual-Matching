#!/usr/bin/env python
# -*- coding: UTF-8 -*-

# Import modules for CGI handling 
import cgi
import sqlite3
import json
import string

# enable debugging
import cgitb
cgitb.enable()

# Create instance of FieldStorage 
form = cgi.FieldStorage() 

# Get data from fields
qword = form.getvalue('q')
#last_name  = form.getvalue('last_name')

# connect to Database
conn = sqlite3.connect('videos.sqlite3')
cursor = conn.cursor()

print "Content-Type: text/plain;charset=utf-8"
print

# variables
Id = '"%s"' % "ID"
VideoTitle = '"%s"' % "Title"
VideoID = '"%s"' % "VideoID"
Query = '"%s"' % "Query"
Rank = '"%s"' % "Rank"
UploadTime = '"%s"' % "Uploadtime"
Description = '"%s"' % "Description"
Category = '"%s"' % "Category"
Tags = '"%s"' % "Tags"
PlayerUrl = '"%s"' % "PlayerUrl"
FlashPlayerUrl = '"%s"' % "FlashPlayerUrl"
Length = '"%s"' % "Length"
Author = '"%s"' % "Author"
CommentCount = '"%s"' % "CommentCount"
FavoriteCount = '"%s"' % "FavoriteCount"
ViewCount = '"%s"' % "ViewCount"
RatingAvg = '"%s"' % "RatingAvg"
Thumbnail0 = '"%s"' % "Thumbnail0"
Thumbnail1 = '"%s"' % "Thumbnail1"
Thumbnail2 = '"%s"' % "Thumbnail2"
Thumbnail3 = '"%s"' % "Thumbnail3"
Topics = '"%s"' % "Topics"

print "{"

# iterative query for creating JSON
def iterateSqlQuery (parameters,test,idd):
	for names in cursor.execute("SELECT * FROM topics WHERE id = ?", (parameters,)):
		tempStr = "\t" + '"%s"' % names[1] + ": ["
		cursor.execute ("SELECT MAX (id) FROM videosMetaData WHERE topics =?", (names[0],))
		lastEntryInEachCategory = cursor.fetchone()[0]
		for cg in cursor.execute("SELECT * FROM videosMetaData WHERE topics =?", (names[0],)): 
			id = Id + ": " + '"%s"' % str(cg[0]) + ", " + "\n"
			videoTitle = VideoTitle + ": " + '"%s"' % cg[1] + ", " + "\n"		
			videoUniqueID = VideoID + ": " + '"%s"' % cg[2] + ", " + "\n"
			uploadTime = UploadTime + ": " + '"%s"' % cg[5] + ", " + "\n"
			description = Description + ": " + '"%s"' % cg[6] + ", " + "\n"
			playerurl = cg[9] 
			playerurl = PlayerUrl + ": " + '"%s"' % playerurl[:-33] + ", " + "\n"
			length = Length + ": " + '"%s"' % str(cg[11]) + ", " + "\n"
			author = Author + ": " + '"%s"' % cg[12] + ", " + "\n"
			viewCount = ViewCount + ": " + '"%s"' % cg[15] + ", " + "\n"			
			thumbnail0 = Thumbnail0 + ": " + '"%s"' % cg[17] + ", " + "\n"
			topics = Topics + ": " + '"%s"' % str(cg[21]) + "\n"
			tempStr = tempStr + "\n\t\t" + "{" + "\n\t\t\t" + id + "\t\t\t" + videoTitle + "\t\t\t" + videoUniqueID + "\t\t\t" + uploadTime + "\t\t\t" + description + "\t\t\t" +   playerurl + "\t\t\t" +  length + "\t\t\t" + author + "\t\t\t" + viewCount + "\t\t\t" + thumbnail0 + "\t\t\t" +  topics + "\t\t}, "
			if len(id) > 0 and cg[0] == lastEntryInEachCategory:
				tempStr = tempStr[:-2]
		if (test == idd):
			tempStr = tempStr + "\n\t]"
		else:	
			tempStr = tempStr + "\n\t],"
	print filter(lambda x: x in string.printable, tempStr)	# filters out non-printable characters
	test = test + 1
	if test <= idd:
		iterateSqlQuery(parameters + 1,test,idd)
	return

cursor.execute ("SELECT MAX (id) FROM topics")
lastID = cursor.fetchone()[0]
iterateSqlQuery(1,1,lastID)

print "}"

# close database connection
conn.close()
