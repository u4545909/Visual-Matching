#!/usr/bin/env python
# -*- coding: UTF-8 -*-

# Import modules for CGI handling 
import cgi
import sqlite3
import json

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

# variables
Id = '"%s"' % "ID"
ReferenceFrameID = '"%s"' % "ReferenceFrameID"
MatchingFrame = '"%s"' % "MatchingFrame"
MatchingFrameURL = '"%s"' % "MatchingFrameURL"
print "{\n\t" + '"%s"' % "matching_key_frames" + ": [\n" 
tempStr = ""

# Query the database
for cg in cursor.execute('SELECT * FROM matchingFrames WHERE referenceFrameID = ?', (qword,)):
	id = Id + ": " + '"%s"' % str(cg[0]) + ", " + "\n"
	referenceFrameID = ReferenceFrameID + ": " + '"%s"' % cg[1] + ", " + "\n"
	matchingFrame = MatchingFrame + ": " + '"%s"' % cg[2] + ", " + "\n"
	matchingFrameURL = MatchingFrameURL + ": " + '"%s"' % cg[3] + "\n"
	tempStr = tempStr + "\n" + "\t" + "\t" + "{" + "\n" + "\t" + "\t" + "\t" + id + "\t" + "\t" + "\t" + referenceFrameID + "\t" + "\t" + "\t" + matchingFrame + "\t" + "\t" + "\t" + matchingFrameURL + "\t" + "\t" + "}, " 
if (len(tempStr)>0):
	tempStr = tempStr[:-2]

#output JSON
print tempStr
print  "\n\t]\n}"


# Close connection to Database
conn.close()
