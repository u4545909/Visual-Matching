import sqlite3
import sqlite3 as lite
import os
import glob
import ConfigParser
import re
import sys
import json
import unicodedata
from xml.dom.minidom import parseString

global xmlperkeyword

try:
	cf = ConfigParser.ConfigParser()
    	cf.read('./config.ini')
    	xmlperkeyword = int(cf.get('NUMBERS', 'xml_per_keyword'))
except Exception,e:
    	print e
    	print 'Error: Invalid config file.'
    	exit(1)

#print '----------------------------------------------------------------------------'
#print 'START CRAWLXML.PY'
#print '----------------------------------------------------------------------------'
#os.chdir(os.path.expanduser("~/data/crawling/"))
#os.system('python2.7 ./crawlxml.py -i')

#print '---------------------------------------------------------------------------'
#print 'START CRAWLVIDEO.PY'
#print '---------------------------------------------------------------------------'
#os.system('python2.7 ./crawlvideo.py -i')

#print '---------------------------------------------------------------------------'
#print 'START BATCHRUN.PY'
#print '---------------------------------------------------------------------------'
#os.chdir(os.path.expanduser("~/data/videosplitting/"))
#os.system('python2.7 ./batchrun.py -i')

# connect to database
os.chdir(os.path.expanduser("~/data/www/cgi-bin/"))
conn = sqlite3.connect('videos.sqlite3') 
c = conn.cursor()


#c.execute ('''DROP TABLE IF EXISTS topics''') 
#c.execute ('''DROP TABLE IF EXISTS videosMetaData''')
#c.execute ('''DROP TABLE IF EXISTS imageFrames''')
#c.execute ('''DROP TABLE IF EXISTS matchingFrames''')

c.execute('''CREATE TABLE topics (id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ,  name TEXT NOT NULL )''')

c.execute('''CREATE TABLE videosMetaData 
(id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , title TEXT NOT NULL , videoID TEXT ,
query TEXT, rank INTEGER, uploadtime DATETIME, description TEXT, category TEXT, 
tags TEXT, playerurl TEXT, flashplayerurl TEXT, length DOUBLE, author TEXT, commentcount INTEGER, favoritecount INTEGER,
viewcount INTEGER, ratingavg TEXT,thumbnail0 TEXT, thumbnail1 TEXT, thumbnail2 TEXT, thumbnail3 TEXT, topics INTEGER,
FOREIGN KEY(topics) REFERENCES topics(id))''')

c.execute('''CREATE TABLE imageFrames
(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, imageUrl TEXT, imageName TEXT, videoRef TEXT,
FOREIGN KEY (videoRef) REFERENCES videoMetaData (videoID))''')

c.execute('''CREATE TABLE matchingFrames
(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, referenceFrameID TEXT, matchingFrame TEXT, matchingFrameURL TEXT)''')


# inserts the topics into the topics table (excludes trash directory)

print '---------------------------------------------------------------------------'
print 'ADDING ROWS TO TOPICS (CATEGORY) TABLE '
print '---------------------------------------------------------------------------'
os.chdir(os.path.expanduser("~/data/crawling/topics/"))
for files in glob.glob("*"):
	if not files.startswith('trash') and not len(files) == 5 and not files.endswith('/'):
		c.execute ("INSERT INTO topics (name) VALUES(?)", (files,))

def count_letters(word):
	count = 0
	for c in word:
		count = count + 1
 	return count

def listSubDir(filename, subdirectory=''):
    if subdirectory:
        path = subdirectory
    else:
        path = os.getcwd()
    for root, dirs, names in os.walk(path):
	if os.path.exists (os.path.join(root, filename)):
		return os.path.join(root, filename)
		

# inserts matching keyframes into the matchingFrame table

print '---------------------------------------------------------------------------'
print 'ADDING ROWS TO MATCHING FRAME TABLE '
print '---------------------------------------------------------------------------'
mKFramePath = os.path.expanduser("~/data/www/memexplore/matchingKeyFramesJSON/")
baseUrl = "http://cantabile.anu.edu.au/memexplore/"
os.chdir(mKFramePath)
for path,subdirs,files in os.walk(mKFramePath):
	for file in files:
		filePath = listSubDir(file,mKFramePath)
		json_data=open(filePath).read()
		data = json.loads(json_data)
		ok = data["FileMatches"]
		referenceFileName = unicodedata.normalize('NFKD', data["FileName"]).encode('ascii','ignore')
		for i in range(len(ok)):
			c.execute ("INSERT INTO matchingFrames(referenceFrameID, matchingFrame, matchingFrameURL) VALUES (?,?,?)", (referenceFileName, unicodedata.normalize('NFKD', ok[i]).encode('ascii','ignore'), baseUrl + unicodedata.normalize('NFKD', ok[i]).encode('ascii','ignore')))
		#json_data.close();




# inserts images into imageFrames table in binary format

print '---------------------------------------------------------------------------'
print 'ADDING IMAGES TO IMAGEFRAMES TABLE '
print '---------------------------------------------------------------------------'
imageFrameDir = os.path.expanduser("~/data/www/memexplore/shot1Test/")
os.chdir(imageFrameDir)
for path,subdirs, files in os.walk(imageFrameDir):
	for subDir in subdirs:
		if (count_letters(subDir) > 1):
			storeVideoId = subDir		
	for file in files:
		#print file
		filepath = listSubDir(file,imageFrameDir)
		fin = None
		try:
			fin = open (filepath,'rb')
			img = fin.read()
			binary = lite.Binary(img)
			c.execute ("INSERT INTO imageFrames(imageUrl,imageName,videoRef) VALUES (?,?,?)", (baseUrl + "shot1Test/" + storeVideoId[:1] + "/" + storeVideoId[1:2] + "/" + storeVideoId + "/" + file,file,storeVideoId))
			#print (filepath)
		except IOError, e:
			print "Error %d: %s" % (e.args[0],e.args[1])
			ok123 = "error"
			print ok123
		finally:
			if fin:
				fin.close()




# inserts metadata into the metadata table

print '---------------------------------------------------------------------------'
print 'ADDING ROWS TO XML METADATA TABLE '
print '---------------------------------------------------------------------------'
x = 0
previous = 0

root = os.path.expanduser("~/data/crawling/xml/raw/")
os.chdir(root)
for path,subdirs,files in os.walk(root):
    for file in files:
	xmlFilePath = listSubDir(file,root)
	if '/relevance/' not in xmlFilePath:
		xmlFilePath = "notValid"
	else:
		new = xmlFilePath.find('/relevance/')
	if previous != new:
		x = x + 1
		previous = new
	try:	
		
		f=open(xmlFilePath, 'r')
		data = f.read()
		dom = parseString(data)
		try:
			xmlTitle = dom.getElementsByTagName('title')[0].toxml()
			titleData = xmlTitle.replace('<title>','').replace('</title>','')
		except IndexError:
			titleData = None

		try:
			xmlQuery = dom.getElementsByTagName('query')[0].toxml()
			queryData = xmlQuery.replace('<query>','').replace('</query>','')
		except IndexError:
			queryData = None

		try:
			xmlRank = dom.getElementsByTagName('rank')[0].toxml()
			rankData = xmlRank.replace('<rank>','').replace('</rank>','')
		except IndexError:
			rankData = None

		try:
			xmlUploadtime = dom.getElementsByTagName('uploadtime')[0].toxml()
			uploadtimeData = xmlUploadtime.replace('<uploadtime>','').replace('</uploadtime>','')
		except IndexError:
			uploadtimeData = None
	
		try:
			xmlDescription = dom.getElementsByTagName('description')[0].toxml()
			descriptionData = xmlDescription.replace('<description>','').replace('</description>','')
		except IndexError:
			descriptionData = None

		try:
			xmlCategory = dom.getElementsByTagName('category')[0].toxml()
			categoryData = xmlCategory.replace('<category>','').replace('</category>','')
		except IndexError:
			categoryData = None
	
		try:
			xmlTags = dom.getElementsByTagName('tags')[0].toxml()
			tagsData = xmlTags.replace('<tags>','').replace('</tags>','')
		except IndexError:
			tagsData = None

		try:
			xmlPlayerurl = dom.getElementsByTagName('palyerurl')[0].toxml()
			playerurlData = xmlPlayerurl.replace('<palyerurl>','').replace('</palyerurl>','')
		except IndexError:
			playerurlData = None

		try:
			xmlFlashplayerurl = dom.getElementsByTagName('flashpalyerurl')[0].toxml()
			flashplayerurlData = xmlFlashplayerurl.replace('<flashpalyerurl>','').replace('</flashpalyerurl>','')
		except IndexError:
			flashplayerurl = None

		try:
			xmlLength = dom.getElementsByTagName('length')[0].toxml()
			lengthData = xmlLength.replace('<length>','').replace('</length>','')
		except IndexError:
			lengthData = None

		try:
			xmlAuthor = dom.getElementsByTagName('author')[0].toxml()
			authorData = xmlAuthor.replace('<author>','').replace('</author>','')
		except IndexError:
			authorData = None

		try:
			xmlComment_count = dom.getElementsByTagName('comment_count')[0].toxml()
			comment_countData = xmlComment_count.replace('<comment_count>','').replace('</comment_count>','')
		except IndexError:
			comment_countData = None

		try:
			xmlFavorite_count = dom.getElementsByTagName('favorite_count')[0].toxml()
			favorite_countData = xmlFavorite_count.replace('<favorite_count>','').replace('</favorite_count>','')
		except IndexError:
			favorite_countData = None

		try:
			xmlViewcount = dom.getElementsByTagName('viewcount')[0].toxml()
			viewcountData = xmlViewcount.replace('<viewcount>','').replace('</viewcount>','')
		except IndexError:
			viewcountData = None

		try:
			xmlRating_avg = dom.getElementsByTagName('rating_avg')[0].toxml()
			rating_avgData = xmlRating_avg.replace('<rating_avg>','').replace('</rating_avg>','')
		except IndexError:
			rating_avgData = None

		try:
			xmlThumbnail0 = dom.getElementsByTagName('thumbnail')[0].toxml()
			thumbnailData0 = xmlThumbnail0.replace('<thumbnail>','').replace('</thumbnail>','')
		except IndexError:
			thumbnailData0 = None

		try:
			xmlThumbnail1 = dom.getElementsByTagName('thumbnail')[1].toxml()
			thumbnailData1 = xmlThumbnail1.replace('<thumbnail>','').replace('</thumbnail>','')
		except IndexError:
			thumbnailData1 = None

		try:
			xmlThumbnail2 = dom.getElementsByTagName('thumbnail')[2].toxml()
			thumbnailData2 = xmlThumbnail2.replace('<thumbnail>','').replace('</thumbnail>','')
		except IndexError:
			thumbnailData2 = None

		try:
			xmlThumbnail3 = dom.getElementsByTagName('thumbnail')[3].toxml()
			thumbnailData3 = xmlThumbnail3.replace('<thumbnail>','').replace('</thumbnail>','')
		except IndexError:
			thumbnailData3 = None

		try:
			videoIDData = playerurlData[31:42]
		except IndexError:
			videoIDData = None

		topic = x
		
		c.execute('''INSERT INTO videosMetaData (title,videoID,query,rank,uploadtime,description,category,tags,playerurl,flashplayerurl,length,author,commentcount, favoritecount, viewcount,ratingavg, thumbnail0,thumbnail1, thumbnail2, thumbnail3, topics) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)''', (titleData, videoIDData, queryData, rankData, uploadtimeData, descriptionData, categoryData, tagsData, playerurlData, flashplayerurlData,lengthData,authorData,comment_countData,favorite_countData, viewcountData,rating_avgData, thumbnailData0, thumbnailData1,thumbnailData2,thumbnailData3, topic))
		f.close()
	except IOError, e:
		ok = "testtt";
	
c.execute('pragma foreign_keys = on')

# save (commit) the changes
conn.commit()

# close connection
conn.close()
