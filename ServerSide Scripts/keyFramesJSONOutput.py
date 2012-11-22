#!/usr/bin/env python
# -*- coding: UTF-8 -*-

# Import modules for CGI handling 
import cgi
import sqlite3
import json
import binascii
import base64

# enable debugging
import cgitb
cgitb.enable()

# Create instance of FieldStorage 
form = cgi.FieldStorage() 

# Get data from fields
qword = form.getvalue('q')

# connect to Database
conn = sqlite3.connect('videos.sqlite3')
cursor = conn.cursor()

print "Content-Type: text/plain;charset=utf-8"
print

# variables
Id = '"%s"' % "ID"
ImageSource = '"%s"' % "ImageSource"
Name = '"%s"' % "ImageName"
VideoRef = '"%s"' % "VideoRef"
tempStr = "["

# perform SQL query and print JSON
cursor.execute ("SELECT MAX (imageName) FROM imageFrames WHERE videoRef = ?",(qword,))
lastEntryInEachCategory = cursor.fetchone()[0]
print "{\n\t"
print '"%s"' % "key_frame" + ": [\n"
for cg in cursor.execute('SELECT * FROM imageFrames WHERE videoRef = ? ORDER BY imageName', (qword,)):
	print json.dumps({'ID':"%s" % cg[0],'ImageSource':"%s" % cg[1],'Name':cg[2],'VideoRef':cg[3]})
	if cg[2] != lastEntryInEachCategory:
		print ","
print "\n\t]\n}"

# close Database connection
conn.close()
